package com.storm.mq.rabbit;

import cn.hutool.core.thread.ThreadUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MetricsCollector;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Slf4j
public class SimpleMetricsCollector implements MetricsCollector {

    private final AtomicInteger connectionCounter = new AtomicInteger();
    private final AtomicInteger channelCounter = new AtomicInteger();
    private final AtomicInteger publishCounter = new AtomicInteger();
    private final AtomicInteger consumerCounter = new AtomicInteger();
    private final AtomicInteger consumerOkCounter = new AtomicInteger();
    private final AtomicInteger consumerErrorCounter = new AtomicInteger();

    public SimpleMetricsCollector(String clientName) {
        Thread thread = new Thread(() -> {
            while(true) {
                ThreadUtil.sleep(30L, TimeUnit.SECONDS);
                if (log.isDebugEnabled()) {
                    log.debug("[MQ-{}][MONITOR] 连接数: {}, 通道数: {}, 消息发布数: {}, 消息消费数: {}, 消费成功数: {}, 消费失败数: {}", new Object[]{clientName, this.connectionCounter, this.channelCounter, this.publishCounter, this.consumerCounter, this.consumerOkCounter, this.consumerErrorCounter});
                }
            }
        }, String.format("%s-MQ监控", clientName));
        thread.setDaemon(true);
        thread.start();
    }
    public void newConnection(Connection connection) {
        this.connectionCounter.incrementAndGet();
    }

    public void closeConnection(Connection connection) {
        this.connectionCounter.decrementAndGet();
    }

    public void newChannel(Channel channel) {
        this.channelCounter.incrementAndGet();
    }

    public void closeChannel(Channel channel) {
        this.channelCounter.decrementAndGet();
    }

    public void basicPublish(Channel channel) {
        this.publishCounter.incrementAndGet();
    }

    public void consumedMessage(Channel channel, long deliveryTag, boolean autoAck) {
    }

    public void consumedMessage(Channel channel, long deliveryTag, String consumerTag) {
    }

    public void basicAck(Channel channel, long deliveryTag, boolean multiple) {
        this.consumerCounter.incrementAndGet();
        this.consumerOkCounter.incrementAndGet();
    }

    public void basicNack(Channel channel, long deliveryTag) {
        this.consumerCounter.incrementAndGet();
        this.consumerErrorCounter.incrementAndGet();
    }

    public void basicReject(Channel channel, long deliveryTag) {
        this.consumerCounter.incrementAndGet();
        this.consumerErrorCounter.incrementAndGet();
    }

    public void basicConsume(Channel channel, String consumerTag, boolean autoAck) {
    }

    public void basicCancel(Channel channel, String consumerTag) {
    }
}
