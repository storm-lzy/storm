package com.storm.mq.rabbit;

import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.storm.mq.Constant;
import com.storm.mq.MessageHandler;
import com.storm.mq.MqChannel;
import com.storm.mq.codec.Codec;
import com.storm.mq.concurrent.CallbackFuture;
import com.storm.mq.concurrent.FutureCallback;
import com.storm.mq.concurrent.StateFuture;
import com.storm.mq.config.ConsumerConfig;
import com.storm.mq.enums.Ack;
import com.storm.mq.exception.BrokerConfirmException;
import com.storm.mq.exception.MqException;
import com.storm.mq.exception.NetException;
import com.storm.mq.exception.factory.ExceptionProviderFactory;
import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import com.storm.mq.message.MessageProp;
import com.storm.mq.rabbit.config.RabbitMqClientConfig;
import com.storm.mq.store.MessageRepository;
import javafx.util.Pair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 *
 */
@Data
@Slf4j
public class RabbitMqChannel implements MqChannel {

    private final RabbitMqClientConfig rabbitMqClientConfig;
    private final Channel channel;
    private final Codec codec;
    private final String clientName;
    private final Map<String, Codec> allSupportCodecs;
    private final Set<String> declaredQueues;
    private final Map<Long, Pair<StateFuture<MessageContext>, MessageContext>> waitConfirmMsg;
    private final ThreadPoolExecutor sharedPublishExecutor;
    private final ThreadPoolExecutor sharedConsumerExecutor;
    private final String clusterSeq;
    private final MessageRepository messageRepository;

    public RabbitMqChannel(Connection connection, RabbitMqClientConfig rabbitMqClientConfig, final String clientName, ThreadPoolExecutor sharedPublishExecutor, ThreadPoolExecutor sharedConsumerExecutor, String clusterSeq) {
        this.rabbitMqClientConfig = rabbitMqClientConfig;
        this.codec = rabbitMqClientConfig.getCodec();
        this.clientName = clientName;
        this.sharedPublishExecutor = sharedPublishExecutor;
        this.sharedConsumerExecutor = sharedConsumerExecutor;
        this.clusterSeq = clusterSeq;
        this.waitConfirmMsg = new ConcurrentHashMap<>();
        this.declaredQueues = new HashSet<>();
        this.allSupportCodecs = rabbitMqClientConfig.getAllSupportCodecs().stream().collect(Collectors.toMap(Codec::id, (Codec) -> Codec));
        this.messageRepository = rabbitMqClientConfig.getMessageRepository();
        ConfirmListener confirmListener = new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) {
                log.info("[MQ-{}][发布确认] deliveryTag:{} handleAck", clientName, deliveryTag);
                Pair<StateFuture<MessageContext>, MessageContext> pair = RabbitMqChannel.this.waitConfirmMsg.remove(deliveryTag);
                if (pair == null) {
                    RabbitMqChannel.log.info("[MQ-{}][发布确认] deliveryTag:{} broken ack not found", clientName, deliveryTag);
                } else {
                    pair.getKey().done(pair.getValue());
                }
            }

            public void handleNack(long deliveryTag, boolean multiple) {
                RabbitMqChannel.log.error("[MQ-{}][发布确认] deliveryTag:{} handleNack", clientName, deliveryTag);
                Pair<StateFuture<MessageContext>, MessageContext> pair = RabbitMqChannel.this.waitConfirmMsg.remove(deliveryTag);
                if (pair == null) {
                    RabbitMqChannel.log.error("[MQ-{}][发布确认] deliveryTag:{} broken ack not found", clientName, deliveryTag);
                } else {
                    pair.getKey().exception(new BrokerConfirmException(pair.getValue()));
                }
            }
        };

        try {
            this.channel = connection.createChannel();
            this.channel.basicQos(rabbitMqClientConfig.getConsumer().getChannel().getBasicQos());
            this.channel.confirmSelect();
            this.channel.addConfirmListener(confirmListener);
        } catch (IOException var9) {
            throw new NetException(String.format("[MQ-%s][网络异常] 通道初始化失败", clientName), var9);
        }
    }


    public <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message, MessageProp messageProp) {
        MessageProp availableMessageProp = messageProp == null ? new MessageProp() : messageProp;
        String charsetName = Constant.NET_TRANSFER_CHARSET.name();
        AMQP.BasicProperties.Builder basicPropertiesBuilder = new AMQP.BasicProperties.Builder();
        this.parse(availableMessageProp.getExtContext(), basicPropertiesBuilder);
        byte[] payload = this.codec.encode(message, charsetName);
        Date date = new Date();
        MessageContext messageContext = new MessageContext();
        messageContext.setMessageId(message.getMessageId());
        messageContext.setPayload(payload);
        messageContext.setContentEncoding(charsetName);
        messageContext.setContentType(this.codec.id());
        messageContext.setTimestamp(date.getTime());
        messageContext.setTopic(topic);
        messageContext.setMessageProp(availableMessageProp);
        basicPropertiesBuilder.deliveryMode(availableMessageProp.isPersistent() ? 2 : 1);
        basicPropertiesBuilder.contentEncoding(charsetName);
        basicPropertiesBuilder.contentType(this.codec.id());
        basicPropertiesBuilder.messageId(message.getMessageId());
        basicPropertiesBuilder.timestamp(date);
        basicPropertiesBuilder.type(Message.class.getName());
        final StateFuture<MessageContext> messageStateFuture = new StateFuture<>();
        messageStateFuture.startRun();
        this.sharedPublishExecutor.submit(() -> {
            log.info("[MQ-{}][推送消息] topic:{}, messageId:{}, message:{}, messageProp:{}", this.clientName, topic, message.getMessageId(), message, messageProp);
            int publishRetry = availableMessageProp.getAutoRetryTotal();
            ++publishRetry;
            Throwable throwable = null;

            while(publishRetry > 0) {
                --publishRetry;
                long deliveryTag = this.channel.getNextPublishSeqNo();

                try {
                    this.waitConfirmMsg.put(deliveryTag, new Pair<>(messageStateFuture, messageContext));
                    String exchangeName = this.rabbitMqClientConfig.getPublisher().getExchange().getName();
                    Map<String, Object> headers = availableMessageProp.getHeaders() == null ? new HashMap<>(0) : availableMessageProp.getHeaders();
                    basicPropertiesBuilder.headers(headers);
                    if (availableMessageProp.getDelay() > 0L) {
                        headers.put("x-delay", availableMessageProp.getDelay());
                        this.channel.basicPublish(exchangeName + "_delay", topic, basicPropertiesBuilder.build(), this.codec.encode(messageContext, charsetName));
                    } else {
                        this.channel.basicPublish(exchangeName, topic, basicPropertiesBuilder.build(), this.codec.encode(messageContext, charsetName));
                    }

                    throwable = null;
                    break;
                } catch (Throwable var16) {
                    throwable = var16;
                    this.waitConfirmMsg.remove(deliveryTag);
                    availableMessageProp.setPublishRetryOnFail(availableMessageProp.getPublishRetryOnFail() + 1);
                }
            }

            if (throwable != null) {
                try {
                    messageStateFuture.exception(throwable);
                } catch (Throwable var15) {
                    log.warn("{} StateFuture handle error: [{}]", var15,var15.getMessage());
                }
            }

        });
        return new CallbackFuture<MessageContext>() {
            public void addCallback(FutureCallback<MessageContext> callback) {
                messageStateFuture.addCallback(callback);
            }

            public void removeCallback(FutureCallback<MessageContext> callback) {
                messageStateFuture.removeCallback(callback);
            }

            public boolean cancel(boolean mayInterruptIfRunning) {
                throw new MqException("消息发送中不能被取消");
            }

            public boolean isCancelled() {
                return messageStateFuture.isCancelled();
            }

            public boolean isDone() {
                return messageStateFuture.isDone();
            }

            public MessageContext get() throws InterruptedException, ExecutionException {
                return messageStateFuture.get();
            }

            public MessageContext get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return messageStateFuture.get(timeout, unit);
            }
        };
    }

    private void parse(Map<String, Object> extContext, AMQP.BasicProperties.Builder builder) {
        if (extContext != null) {
            builder.priority((Integer)extContext.get("priority"));
        }
    }

    @Override
    public void subscribe(String topic, String group, boolean share, ConsumerConfig consumerConfig, MessageHandler messageHandler) {
        Assert.notNull(topic, StrUtil.format("Topic不能为空"));
        Assert.notNull(messageHandler, StrUtil.format("MessageHandler不能为空"));
        Assert.isTrue(consumerConfig == null || consumerConfig instanceof RabbitMqClientConfig.RabbitConsumerConfig, StrUtil.format("当前Rabbit不支持的配置类型：{}", consumerConfig == null ? null : consumerConfig.getClass()));
        RabbitMqClientConfig.RabbitConsumerConfig rabbitConsumerConfig = this.mergeConfig((RabbitMqClientConfig.RabbitConsumerConfig) consumerConfig, rabbitMqClientConfig.getConsumer());
        RabbitMqClientConfig.QueueConfig queueConfig = rabbitConsumerConfig.getQueue();
        Map<String, Object> queueProp = new HashMap<>(2);
        queueProp.put("x-max-length", queueConfig.getMaxLength());
        queueProp.put("x-max-length-bytes", queueConfig.getMaxLengthBytes());

        try {
            String queueName = "storm_" + topic + "_" + group;
            if (share) {
                if (StrUtil.isBlank(this.clusterSeq)) {
                    throw ExceptionProviderFactory.ILLEGAL_ARGUMENT_EXCEPTION.newRuntimeException("当前系统集群负载号为空, 不支持使用共享模式");
                }
                queueName = queueName + "_" + this.clusterSeq;
            }

            MessageConsumer messageConsumer = new MessageConsumer(this.clientName, this, messageHandler, this.allSupportCodecs, this.sharedConsumerExecutor, this.messageRepository);
            String exchange = rabbitConsumerConfig.getExchange().getName();
            if (this.declaredQueues.add(queueName)) {
                this.channel.queueDeclare(queueName, queueConfig.isDurable(), false, false, queueProp);
            }
            this.channel.queueBind(queueName, exchange, topic);
            this.channel.basicConsume(queueName, false, messageConsumer);

            if (this.rabbitMqClientConfig.isSupportDelayed()) {
                if (this.declaredQueues.add(queueName + "_delay")) {
                    this.channel.queueDeclare(queueName + "_delay", queueConfig.isDurable(), false, false, queueProp);
                }

                this.channel.queueBind(queueName + "_delay", exchange + "_delay", topic);
                this.channel.basicConsume(queueName + "_delay", false, messageConsumer);
            }

        } catch (IOException var12) {
            throw new NetException(StrUtil.format("[MQ-{}][订阅异常] {}-订阅失败", this.clientName, topic), var12);
        }
    }

    private RabbitMqClientConfig.RabbitConsumerConfig mergeConfig(RabbitMqClientConfig.RabbitConsumerConfig methodConsumerConfig, RabbitMqClientConfig.RabbitConsumerConfig defaultRabbitConsumerConfig) {
        if (null == methodConsumerConfig) {
            return defaultRabbitConsumerConfig;
        } else {
            RabbitMqClientConfig.RabbitConsumerConfig mergeResult = new RabbitMqClientConfig.RabbitConsumerConfig();
            RabbitMqClientConfig.ExchangeConfig methodExchange = methodConsumerConfig.getExchange();
            if (methodExchange != null && StrUtil.isNotBlank(methodExchange.getName())) {
                mergeResult.setExchange(methodExchange);
            } else {
                mergeResult.setExchange(defaultRabbitConsumerConfig.getExchange());
            }

            RabbitMqClientConfig.ChannelConfig methodChannel = methodConsumerConfig.getChannel();
            if (methodChannel != null && methodChannel.getBasicQos() > 0) {
                mergeResult.setChannel(methodChannel);
            } else {
                mergeResult.setChannel(defaultRabbitConsumerConfig.getChannel());
            }

            RabbitMqClientConfig.QueueConfig methodQueue = methodConsumerConfig.getQueue();
            RabbitMqClientConfig.QueueConfig defaultQueue = defaultRabbitConsumerConfig.getQueue();
            if (methodQueue != null) {
                RabbitMqClientConfig.QueueConfig queueConfig = new RabbitMqClientConfig.QueueConfig();
                queueConfig.setDurable(methodQueue.isDurable());
                queueConfig.setMaxLength(methodQueue.getMaxLength() > 0L ? methodQueue.getMaxLength() : defaultQueue.getMaxLength());
                queueConfig.setMaxLengthBytes(methodQueue.getMaxLengthBytes() > 0L ? methodQueue.getMaxLengthBytes() : defaultQueue.getMaxLengthBytes());
                mergeResult.setQueue(queueConfig);
            } else {
                mergeResult.setQueue(defaultQueue);
            }
            return mergeResult;
        }
    }

    @Override
    public void close() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("[MQ-{}] Close channel {}", this.clientName, this.channel);
            }

            this.channel.close();
        } catch (Throwable var2) {
            log.warn("{} [MQ-{}] Close channel {} error", var2,this.clientName, this.channel);
        }
    }

    @Override
    public boolean isActive() {
        return this.channel.isOpen();
    }

    @Override
    public void ack(long deliveryTag, String messageId, Ack ack) {
        int ackRetry = 3;
        Throwable throwable = null;
        boolean success = false;

        while(ackRetry > 0) {
            --ackRetry;

            try {
                switch (ack) {
                    case ACCEPT:
                        this.channel.basicAck(deliveryTag, false);
                        break;
                    case REJECT_REQUEUE:
                        this.channel.basicReject(deliveryTag, true);
                        break;
                    case REJECT:
                        this.channel.basicReject(deliveryTag, false);
                        break;
                    default:
                        log.error("[MQ-{}][消息确认] Unknown ack [{}]", this.clientName, ack);
                        throw new MqException(String.format("[MQ-%s][消息确认] Unknown ack [%s]", this.clientName, ack));
                }

                success = true;
                break;
            } catch (Throwable var9) {
                throwable = var9;
            }
        }

        if (!success) {
            log.warn("{} [MQ-{}][消息确认] {} ack {} fail, wait requeue", throwable,this.clientName, messageId, ack);
        } else {
            log.info("[MQ-{}][消息确认] {}/{} ack success", this.clientName, deliveryTag, ack);
        }

    }

    @Override
    public Channel unwrap() {
        return this.channel;
    }
}
