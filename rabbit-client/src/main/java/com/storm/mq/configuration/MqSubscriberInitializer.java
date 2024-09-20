package com.storm.mq.configuration;

import com.storm.mq.MqClient;
import com.storm.mq.anno.MqSubscriber;
import com.storm.mq.rabbit.utils.SubscribeUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public class MqSubscriberInitializer implements ApplicationContextAware, SmartInitializingSingleton {
    private ApplicationContext applicationContext;



    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotation = this.applicationContext.getBeansWithAnnotation(MqSubscriber.class);
        Function<String, MqClient> mqClientProvider = (clientName) -> this.applicationContext.getBean(clientName,MqClient.class);
        String applicationName = this.applicationContext.getEnvironment().getProperty("spring.application.name");
        for (Object messageHandlerBean : beansWithAnnotation.values()) {
            SubscribeUtil.subscribe(mqClientProvider, messageHandlerBean, ClassUtils.getUserClass(messageHandlerBean), applicationName);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
