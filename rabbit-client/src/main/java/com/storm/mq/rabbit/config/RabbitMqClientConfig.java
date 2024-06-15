package com.storm.mq.rabbit.config;

import com.storm.mq.anno.RabbitMqConsumerConfig;
import com.storm.mq.config.AbstractClientConfig;
import com.storm.mq.config.ConsumerConfig;
import com.storm.mq.config.PublisherConfig;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 *
 */
@Data
public class RabbitMqClientConfig extends AbstractClientConfig<RabbitMqClientConfig.RabbitConsumerConfig> {

    private String virtualHost = "/";

    private RabbitPublisherConfig publisher = new RabbitPublisherConfig();


    @Data
    public static class RabbitConsumerConfig implements ConsumerConfig {
        private ExchangeConfig exchange = new ExchangeConfig();
        private QueueConfig queue = new QueueConfig();
        private ChannelConfig channel = new ChannelConfig();

    }

    @Data
    public static class RabbitPublisherConfig implements PublisherConfig{
        private ExchangeConfig exchange = new ExchangeConfig();
    }

    @Data
    public static class ExchangeConfig {
        private String name = "storm-cloud";
        private boolean durable = true;
        private boolean autoDelete = false;
    }

    @Data
    public static class QueueConfig{
        private boolean durable = true;
        private @Min(1L) long maxLength = 1000000L;
        private @Min(1024L) long maxLengthBytes = 1073741824L;
    }

    @Data
    public static class ChannelConfig{
        private @Min(1L) int basicQos = 100;


    }
}
