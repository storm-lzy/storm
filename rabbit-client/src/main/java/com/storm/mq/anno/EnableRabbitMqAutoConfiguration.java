package com.storm.mq.anno;

import com.storm.mq.configuration.MqSubscriberAutoScanner;
import com.storm.mq.configuration.MqSubscriberInitializer;
import com.storm.mq.configuration.rabbit.RabbitMqClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {MqSubscriberAutoScanner.class, RabbitMqClientAutoConfiguration.class,MqSubscriberInitializer.class})
public @interface EnableRabbitMqAutoConfiguration {
}
