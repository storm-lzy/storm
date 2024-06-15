package com.storm.mq.rabbit;

import cn.hutool.core.thread.ThreadUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.storm.mq.MessageHandler;
import com.storm.mq.MqChannel;
import com.storm.mq.codec.Codec;
import com.storm.mq.enums.Ack;
import com.storm.mq.exception.MqException;
import com.storm.mq.message.MessageContext;
import com.storm.mq.store.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class MessageConsumer implements Consumer {

    private final String clientName;
    private final MqChannel mqChannel;
    private final MessageHandler messageHandler;
    private final Map<String, Codec> supportCodecs;
    private final ThreadPoolExecutor sharedConsumerExecutor;
    private final MessageRepository messageRepository;

    public MessageConsumer(String clientName, MqChannel mqChannel, MessageHandler messageHandler, Map<String, Codec> supportCodecs, ThreadPoolExecutor sharedConsumerExecutor, MessageRepository messageRepository) {
        this.clientName = clientName;
        this.mqChannel = mqChannel;
        this.messageHandler = messageHandler;
        this.supportCodecs = supportCodecs;
        this.sharedConsumerExecutor = sharedConsumerExecutor;
        this.messageRepository = messageRepository;
    }

    public void handleConsumeOk(String consumerTag) {
        log.debug("handleConsumeOk, clientName: [{}], consumerTag: [{}]", this.clientName, consumerTag);
    }

    public void handleCancelOk(String consumerTag) {
        log.debug("handleCancelOk, clientName: [{}], consumerTag: [{}]", new Object[]{this.clientName, consumerTag});
    }

    public void handleCancel(String consumerTag) {
        log.debug("handleCancel, clientName: [{}], consumerTag: [{}]", new Object[]{this.clientName, consumerTag});
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        log.debug("handleShutdownSignal, clientName: [{}], consumerTag: [{}] sig: [{}]", new Object[]{this.clientName, consumerTag, sig});
    }

    public void handleRecoverOk(String consumerTag) {
        log.debug("handleRecoverOk, clientName: [{}], consumerTag: [{}]", new Object[]{this.clientName, consumerTag});
    }

    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        this.sharedConsumerExecutor.execute(new ConsumerWorker(envelope, properties, body));
    }

    private final class ConsumerWorker implements Runnable {
        private final Envelope envelope;
        private final AMQP.BasicProperties basicProperties;
        private final byte[] body;

        private ConsumerWorker(Envelope envelope, AMQP.BasicProperties basicProperties, byte[] body) {
            this.envelope = envelope;
            this.basicProperties = basicProperties;
            this.body = body;
        }

        public void run() {
            try {
                long deliveryTag = this.envelope.getDeliveryTag();
                String contentEncoding = this.basicProperties.getContentEncoding();
                String contentType = this.basicProperties.getContentType();
                if (MessageConsumer.log.isInfoEnabled()) {
                    MessageConsumer.log.info("[MQ-{}][收到消息] deliveryTag:{}, message:[{}]", MessageConsumer.this.clientName, deliveryTag, new String(this.body, contentEncoding));
                }

                Codec codec;
                if ("application/json".equals(contentType)) {
                    codec = MessageConsumer.this.supportCodecs.get("JSON");
                } else {
                    codec = MessageConsumer.this.supportCodecs.get(contentType);
                }

                if (codec == null) {
                    MessageConsumer.log.error("Un Support codec {}", contentType);
                } else {
                    MessageContext messageContext;
                    try {
                        messageContext = codec.decode(this.body, MessageConsumer.this.messageHandler.getType(), contentEncoding);
                    } catch (Throwable var18) {
                        MessageConsumer.log.error("Codec error {}", contentEncoding);
                        return;
                    }

                    Ack ack = null;
                    long start = System.currentTimeMillis();
                    boolean success = false;
                    boolean needAck = true;

                    try {
                        if (MessageConsumer.this.messageRepository != null && messageContext.getMessageProp().isPersistent()) {
                            MessageConsumer.log.debug("[MQ-{}][消息持久化] 当前使用[{}]进行消息持久化", MessageConsumer.this.clientName, MessageConsumer.this.messageRepository.getClass());
                            MessageConsumer.this.messageRepository.save(messageContext, deliveryTag);
                            needAck = false;
                            MessageConsumer.this.mqChannel.ack(deliveryTag, messageContext.getMessageId(), Ack.ACCEPT);
                            MessageConsumer.this.messageHandler.handle(messageContext, codec);
                        } else {
                            ack = MessageConsumer.this.messageHandler.handle(messageContext, codec);
                            if (ack == null) {
                                ack = Ack.ACCEPT;
                            }
                        }

                        success = true;
                    } catch (Throwable var19) {
                        ack = Ack.REJECT_REQUEUE;
                        MessageConsumer.log.error("{} [MQ-{}][消费异常] topic {}, messageId {}", var19, MessageConsumer.this.clientName, messageContext.getTopic(), messageContext.getMessageId());
                        ThreadUtil.sleep(100L, TimeUnit.MILLISECONDS);
                    } finally {
                        MessageConsumer.log.info("[MQ-{}][消息处理完毕] topic:{}, messageId:{}, cost:{}, success:{}, ack:{}", new Object[]{MessageConsumer.this.clientName, messageContext.getTopic(), messageContext.getMessageId(), System.currentTimeMillis() - start, success, needAck ? ack : Ack.ACCEPT});
                    }

                    if (needAck) {
                        MessageConsumer.this.mqChannel.ack(deliveryTag, messageContext.getMessageId(), ack);
                    }
                }
            } catch (Throwable var21) {
               throw new MqException(var21);
            }
        }
    }
}
