package com.storm.mq.anno;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RabbitMqConsumerConfig {

    QueueConfig[] queue() default {};

    ExchangeConfig[] exchange() default {};

    ChannelConfig[] channel() default {};


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface QueueConfig{
        boolean durable();

        long maxLength() default -1L;

        long maxLengthBytes() default -1L;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface ExchangeConfig{
        String name();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface ChannelConfig{
        int basicQos() default -1;
    }
}
