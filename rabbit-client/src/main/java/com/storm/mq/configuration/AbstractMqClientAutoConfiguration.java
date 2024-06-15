package com.storm.mq.configuration;

import com.storm.mq.codec.Codec;
import com.storm.mq.codec.impl.JsonCodec;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class AbstractMqClientAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private final Codec defaultCodec = new JsonCodec();
    public static Environment environment;
    protected ApplicationContext applicationContext;

    protected Codec defaultCodec() {
        return this.defaultCodec;
    }

    protected List<Codec> allSupportCodecs() {
        ArrayList<Codec> list = new ArrayList<>();
        list.add(this.defaultCodec);
        return list;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
