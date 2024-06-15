package com.storm.mq.rabbit;

import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.nio.NioParams;
import com.storm.mq.MessageHandler;
import com.storm.mq.MqChannel;
import com.storm.mq.MqClient;
import com.storm.mq.concurrent.CallbackFuture;
import com.storm.mq.concurrent.NamedThreadFactory;
import com.storm.mq.config.ConnectionConfig;
import com.storm.mq.config.ConsumerConfig;
import com.storm.mq.exception.ConnectionClosedException;
import com.storm.mq.exception.MqException;
import com.storm.mq.exception.NetException;
import com.storm.mq.exception.factory.ExceptionProviderFactory;
import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import com.storm.mq.message.MessageProp;
import com.storm.mq.rabbit.config.RabbitMqClientConfig;
import com.storm.mq.utils.LockUtil;
import com.storm.mq.utils.ThreadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
@Data
@Slf4j
public class RabbitMqClient implements MqClient {

    private static final String DEFAULT_IO_THREAD_NAME = "rabbitMQ-io-thread";
    private static final String DEFAULT_PUBLISHER_THREAD_NAME = "rabbitMQ-publisher-thread";
    private static final String DEFAULT_CONSUMER_THREAD_NAME = "rabbitMQ-consumer-thread";
    private final RabbitMqClientConfig rabbitMqClientConfig;
    private final String clientName;
    private final ConnectionFactory factory;
    private final ReadWriteLock startLock;
    private final AtomicInteger currentConnection;
    private final Set<String> subscribes;
    private ThreadPoolExecutor publishExecutor;
    private ThreadPoolExecutor consumerExecutor;
    private boolean start;
    private Connection[] connections;
    private ObjectPool<MqChannel> channelPool;
    private ExecutorService ioExecutorService;
    private final String clusterSeq;

    public RabbitMqClient(RabbitMqClientConfig clientConfig, String clientName, String clusterSeq) {
        this(clientConfig, clientName, clusterSeq, true);
    }
    public RabbitMqClient(RabbitMqClientConfig clientConfig, String clientName, String clusterSeq, boolean autoClose) {
        this.start = false;
        Assert.notNull(clientConfig, String.format("[MQ-%s][参数异常] 客户端配置不能为空", clientName));
        this.rabbitMqClientConfig = clientConfig;
        this.clientName = clientName;
        this.clusterSeq = clusterSeq;
        this.subscribes = new HashSet<>();
        this.startLock = new ReentrantReadWriteLock();
        this.currentConnection = new AtomicInteger();
        this.factory = new ConnectionFactory();
        this.factory.setHost(clientConfig.getHost());
        this.factory.setPort(clientConfig.getPort());
        this.factory.setUsername(clientConfig.getUsername());
        this.factory.setPassword(clientConfig.getPassword());
        this.factory.setVirtualHost(clientConfig.getVirtualHost());
        this.factory.setConnectionTimeout(clientConfig.getConnection().getConnectionTimeout());
        this.factory.setThreadFactory(new NamedThreadFactory("rabbitMQ-io-thread"));
        this.factory.setAutomaticRecoveryEnabled(true);
        NioParams nioParams = new NioParams();
        nioParams.setReadByteBufferSize(clientConfig.getConnection().getReadBufferSize());
        nioParams.setWriteByteBufferSize(clientConfig.getConnection().getWriteBufferSize());
        this.factory.useNio();
        this.factory.setNioParams(nioParams);
        this.factory.setMetricsCollector(new SimpleMetricsCollector(clientName));
        if (autoClose) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        }
    }

    @Override
    public void start() {
        LockUtil.runWithLock(this.startLock.writeLock(),() -> {
            this.ioExecutorService = ThreadUtil.newThreadPool(
                    this.rabbitMqClientConfig.getIoThread().getThreadCount(),
                    this.rabbitMqClientConfig.getIoThread().getThreadCount(),
                    this.rabbitMqClientConfig.getIoThread().getQueueSize(),
                    "rabbitMQ-io-thread",(thread,throwable) -> {
                        log.error("[MQ-{}][处理异常] IO期间发生了未处理的异常", this.clientName);
                    });

            this.factory.getNioParams().setNioExecutor(ioExecutorService);
            int connectionCount = Math.max(1, Math.min(5, this.rabbitMqClientConfig.getConnection().getMaxConnection() / 5));
            this.connections = new Connection[connectionCount];
            boolean connectionError = false;

            try {
                for(int i = 0; i < this.connections.length; ++i) {
                    this.connections[i] = this.factory.newConnection();
                }
            } catch (TimeoutException var8) {
                throw new com.storm.mq.exception.TimeoutException(var8);
            } catch (IOException var9) {
                connectionError = true;
                throw new NetException(var9);
            } finally {
                if (connectionError) {
                    this.ioExecutorService.shutdown();
                    this.closeConnection();
                }
            }

            this.publishExecutor = ThreadUtil.newThreadPool(this.rabbitMqClientConfig.getProducerThread().getThreadCount(), this.rabbitMqClientConfig.getProducerThread().getThreadCount(), this.rabbitMqClientConfig.getProducerThread().getQueueSize(), "rabbitMQ-publisher-thread", (thread, throwable) -> {
                log.error("[MQ-{}][处理异常] 消息发布过程中发生未处理异常", this.clientName);
            });
            this.consumerExecutor = ThreadUtil.newThreadPool(this.rabbitMqClientConfig.getProducerThread().getThreadCount(), this.rabbitMqClientConfig.getProducerThread().getThreadCount(), this.rabbitMqClientConfig.getProducerThread().getQueueSize(), "rabbitMQ-consumer-thread", (thread, throwable) -> {
                log.error("[MQ-{}][处理异常] 消息消费过程中发生未处理异常", this.clientName);
            });
            this.initChannelPool();
            this.initExchange();
            this.start = true;


        });
    }

    private void initExchange() {
        RabbitMqClientConfig.RabbitPublisherConfig publisherConfig = this.rabbitMqClientConfig.getPublisher();
        RabbitMqClientConfig.RabbitConsumerConfig consumerConfig = this.rabbitMqClientConfig.getConsumer();
        this.initExchange(publisherConfig.getExchange());
        this.initExchange(consumerConfig.getExchange());
    }

    private void initExchange(RabbitMqClientConfig.ExchangeConfig exchangeConfig) {
        MqChannel mqChannel = this.getChannel();
        Channel channel = (Channel)mqChannel.unwrap();

        try {
            channel.exchangeDeclare(exchangeConfig.getName(), BuiltinExchangeType.TOPIC.getType(), true, false, null);
            if (this.rabbitMqClientConfig.isSupportDelayed()) {
                channel.exchangeDeclare(exchangeConfig.getName() + "_delay", "x-delayed-message", true, false, Collections.singletonMap("x-delayed-type", BuiltinExchangeType.TOPIC.getType()));
            }
        } catch (IOException var8) {
            throw new NetException(var8);
        } finally {
            this.returnChannel(mqChannel);
        }
    }

    private MqChannel getChannel() {
        try {
            return this.channelPool.borrowObject();
        } catch (Exception var2) {
            throw new MqException(String.format("[MQ-%s][处理异常] 获取通道异常", this.clientName), var2);
        }
    }

    private void returnChannel(MqChannel channel) {
        try {
            this.channelPool.returnObject(channel);
        } catch (Exception var3) {
            throw new MqException(String.format("[MQ-%s][处理异常] 归还通道异常", this.clientName), var3);
        }
    }

    private void initChannelPool() {
        GenericObjectPool<MqChannel> channelPool = new GenericObjectPool<>(new PooledObjectFactory<MqChannel>() {
            @Override
            public PooledObject<MqChannel> makeObject() throws Exception {
                return new DefaultPooledObject<>(RabbitMqClient.this.newChannel());
            }

            @Override
            public void destroyObject(PooledObject<MqChannel> pooledObject) throws Exception {
                pooledObject.getObject().close();
            }

            @Override
            public boolean validateObject(PooledObject<MqChannel> pooledObject) {
                return pooledObject.getObject().isActive();
            }

            @Override
            public void activateObject(PooledObject<MqChannel> pooledObject) throws Exception {
                if (!pooledObject.getObject().isActive()) {
                    throw new ConnectionClosedException(String.format("[MQ-%s][处理异常] 连接已经关闭", RabbitMqClient.this.clientName));
                }
            }

            @Override
            public void passivateObject(PooledObject<MqChannel> pooledObject) throws Exception {

            }
        });
        ConnectionConfig connectionConfig = this.rabbitMqClientConfig.getConnection();
        channelPool.setMaxTotal(connectionConfig.getMaxConnection());
        channelPool.setMaxIdle(connectionConfig.getMaxConnection());
        channelPool.setMaxWaitMillis(-1L);
        channelPool.setMinIdle(connectionConfig.getMinIdle());
        channelPool.setMinEvictableIdleTimeMillis(connectionConfig.getMinIdleTime());
        this.channelPool = channelPool;
    }

    private MqChannel newChannel() {
        int index = this.currentConnection.getAndAdd(1);
        if(index < 0){
            synchronized (this.currentConnection){
                index = this.currentConnection.getAndAdd(1);
                if(index < 0){
                    index = 0;
                    this.currentConnection.set(1);
                }
            }
        }
        index %= this.connections.length;
        return new RabbitMqChannel(this.connections[index], this.rabbitMqClientConfig, this.clientName, this.publishExecutor, this.consumerExecutor, this.clusterSeq);
    }


    public void close() {
        LockUtil.runWithLock(this.startLock.writeLock(),() -> {
            if(this.start){
                log.warn(String.format("[MQ-%s] 关闭客户端",this.clientName));
                this.publishExecutor.shutdown();
                this.consumerExecutor.shutdown();
                this.channelPool.close();
                this.closeConnection();
                this.start = false;
                this.ioExecutorService.shutdown();
            }
        });
    }

    @Override
    public <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message, MessageProp messageProp) {
        return LockUtil.runWithLock(this.startLock.readLock(), () -> {
            this.checkStart();
            Assert.notNull(topic, String.format("Topic不能为空 %s", this.clientName));
            Assert.notNull(message.getMessageId(), String.format("消息标识不能为空 %s", this.clientName));
            Assert.notNull(message, String.format("消息属性不能为空 %s", this.clientName));
            Assert.isTrue(messageProp.getDelay() <= 0L || this.rabbitMqClientConfig.isSupportDelayed(), String.format("当前系统配置不支持延迟消息 %s", this.clientName));
            MqChannel mqChannel = this.getChannel();

            CallbackFuture<MessageContext> var5;
            try {
                var5 = mqChannel.publish(topic, message, messageProp);
            } finally {
                this.returnChannel(mqChannel);
            }
            return var5;
        });
    }

    @Override
    public void subscribe(String topic, String group, boolean share, ConsumerConfig consumerConfig, MessageHandler messageHandler) {
        LockUtil.runWithLock(this.startLock.writeLock(), () -> {
            this.checkStart();
            if(!this.subscribes.add(topic)){
                throw ExceptionProviderFactory.ILLEGAL_STATE_EXCEPTION.newRuntimeException(String.format("[MQ-%s][订阅异常] Topic [%s] 已存在订阅", this.clientName, topic));
            }else {
                MqChannel mqChannel = this.newChannel();
                try {
                    mqChannel.subscribe(topic,group,share,consumerConfig,messageHandler);
                } catch (Error | RuntimeException var8) {
                    log.error("{} [MQ-{}][订阅异常] Topic [{}] 订阅失败]", var8,this.clientName, topic);
                    mqChannel.close();
                    throw var8;
                }
            }
        });
    }

    private void checkStart() {
        if (!this.start) {
            throw new IllegalStateException(StrUtil.format("[MQ-{}][处理异常] 当前客户端未启动", this.clientName));
        }
    }

    private void closeConnection() {
        if(this.connections != null){
            Connection[] var1 = this.connections;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; var3++) {
                Connection connection = var1[var3];
                try {
                    if(connection != null && connection.isOpen()){
                        connection.close();
                    }
                }catch (IOException e){
                    log.error(String.format("%s [MQ-%s][处理异常] 关闭连接 [%s] 异常",this.clientName,connection,e));
                }
            }
            this.connections = null;
            this.currentConnection.set(0);
        }
    }

}
