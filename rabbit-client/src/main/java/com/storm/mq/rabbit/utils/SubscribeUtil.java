package com.storm.mq.rabbit.utils;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.storm.mq.MessageHandler;
import com.storm.mq.MqClient;
import com.storm.mq.anno.ConsumerMethod;
import com.storm.mq.anno.MqSubscriber;
import com.storm.mq.anno.RabbitMqConsumerConfig;
import com.storm.mq.codec.Codec;
import com.storm.mq.configuration.AbstractMqClientAutoConfiguration;
import com.storm.mq.enums.Ack;
import com.storm.mq.exception.SubscribeException;
import com.storm.mq.exception.factory.ExceptionProviderFactory;
import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import com.storm.mq.rabbit.config.RabbitMqClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class SubscribeUtil {

    private static final String SKY_WALKING_DYNAMIC_METHOD = "setSkyWalkingDynamicField";
    private static final List<Class<?>> CODEC_CLASS = new ArrayList<>();

    public static void subscribe(Function<String, MqClient> clientProvider, Object messageHandlerBean, Class<?> beanClass, String defaultGroup) {
        Assert.notNull(clientProvider, "MqClient Provider不能为空");
        Assert.notNull(messageHandlerBean, "订阅者不能为空");
        Assert.notNull(beanClass, "订阅者类型不能为空");
        MqSubscriber subscriber = beanClass.getAnnotation(MqSubscriber.class);
        if (null == subscriber.topics() || subscriber.topics().length == 0) {
            throw new SubscribeException(beanClass, "MqSubscriber.topics不能为空");
        }

        String clientName = StrUtil.blankToDefault(StrUtil.isBlank(subscriber.clientName()) ? subscriber.clientName() : subscriber.clientName() + "MqClient", "defaultMqClient");
        String group = StrUtil.blankToDefault(subscriber.group(), defaultGroup);
        if (StrUtil.isBlank(group)) {
            throw new SubscribeException(beanClass, "消息分组不能为空");
        }

        MqClient mqClient = clientProvider.apply(clientName);
        if (null == mqClient) {
            throw new SubscribeException(beanClass, String.format("Not found client %s", clientName));
        }

        RabbitMqClientConfig.RabbitConsumerConfig consumerConfig = convert(beanClass.getAnnotation(RabbitMqConsumerConfig.class));
        Method consumerMethod = chooseConsumerMethod(beanClass);
        Parameter[] parameters = consumerMethod.getParameters();
        Arrays.stream(subscriber.topics()).forEach(topic -> {
            if (topic.startsWith("${") && topic.endsWith("}")) {
                String property = topic.replace("${", "").replace("}", "");
                if (StrUtil.isBlank(AbstractMqClientAutoConfiguration.environment.getProperty(property))) {
                    throw ExceptionProviderFactory.ILLEGAL_STATE_EXCEPTION.newRuntimeException(String.format("[MQ-%s][订阅异常] Topic 未获取到%s值", clientName, topic));
                }

                topic = AbstractMqClientAutoConfiguration.environment.getProperty(property);
            }

            log.info("[MQ-{}][订阅] Topic: [{}] / MessageHandler: [{}]", clientName, topic, beanClass);

            mqClient.subscribe(topic, group, subscriber.share(), consumerConfig, new MessageHandler() {
                @Override
                public Ack handle(MessageContext messageContext, Codec codec) throws Throwable {
                    try {
                        Object[] params = new Object[parameters.length];

                        for(int i = 0; i < params.length; ++i) {
                            Parameter parameter = parameters[i];
                            Class<?> type = parameter.getType();
                            if (MessageContext.class.isAssignableFrom(type)) {
                                if (messageContext != null) {
                                    Class<? extends MessageContext> messageContextClass = messageContext.getClass();
                                    if (type.isAssignableFrom(messageContextClass)) {
                                        params[i] = messageContext;
                                    }
                                }
                            } else if (type.equals(Codec.class)) {
                                params[i] = codec;
                            } else if (messageContext != null) {
                                params[i] = codec.decode(messageContext.getPayload(), parameter.getParameterizedType(), messageContext.getContentEncoding());
                            }
                        }
                        return (Ack)consumerMethod.invoke(messageHandlerBean, params);
                    } catch (InvocationTargetException var8) {
                        throw var8.getCause();
                    }
                }
            });



        });
    }

    private static Method chooseConsumerMethod(Class<?> beanClass) {
        Method[] methods = beanClass.getMethods();
        if (methods.length == 0) {
            throw new SubscribeException(beanClass, "订阅类没有consumer方法可用");
        } else {
            List<Method> candidates = Arrays.stream(methods).filter((method -> validate(method, beanClass))).collect(Collectors.toList());
            if (candidates.isEmpty()) {
                throw new SubscribeException(beanClass, "订阅类没有consumer方法可用");
            } else {
                Method consumerMethod;
                if(candidates.size() == 1){
                    consumerMethod = candidates.get(0);
                }else {
                    List<Method> declaredConsumerMethod = candidates.stream().filter(method -> method.getAnnotation(ConsumerMethod.class) != null).collect(Collectors.toList());
                    if(declaredConsumerMethod.size() != 1){
                        throw new SubscribeException(beanClass, String.format("订阅类[%s]有多个符合要求的Consumer方法, [%s, %s]", beanClass, candidates, declaredConsumerMethod));
                    }else {
                        consumerMethod = declaredConsumerMethod.get(0);
                    }
                }
                return consumerMethod;
            }
        }
    }

    private static boolean validate(Method method, Class<?> beanClass) {
        if (method.getName().equals("setSkyWalkingDynamicField")) {
            return false;
        } else {
            Class<?> returnType = method.getReturnType();
            if (!Void.TYPE.equals(returnType) && !Ack.class.equals(returnType)) {
                return false;
            } else {
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0) {
                    return false;
                } else if (!method.getDeclaringClass().equals(beanClass)) {
                    return false;
                } else {
                    boolean hasObject = false;
                    int var1 = parameters.length;
                    for (int var2 = 0; var2 < var1; var2++) {
                        Parameter parameter = parameters[var2];
                        Class<?> type = parameter.getType();
                        if (!Message.class.isAssignableFrom(type) && !CODEC_CLASS.contains(type)) {
                            if (hasObject) {
                                return false;
                            }
                            hasObject = true;
                        }
                    }
                    return true;
                }
            }
        }
    }

    private static RabbitMqClientConfig.RabbitConsumerConfig convert(RabbitMqConsumerConfig rabbitMqConsumerConfig) {
        if (rabbitMqConsumerConfig == null) {
            return null;
        }
        RabbitMqClientConfig.RabbitConsumerConfig consumerConfig = new RabbitMqClientConfig.RabbitConsumerConfig();

        if (ObjUtil.isEmpty(rabbitMqConsumerConfig.exchange())) {
            consumerConfig.setExchange(null);
        } else {
            RabbitMqClientConfig.ExchangeConfig exchange = consumerConfig.getExchange();
            RabbitMqConsumerConfig.ExchangeConfig exchangeConfig = rabbitMqConsumerConfig.exchange()[0];
            exchange.setName(exchangeConfig.name());
        }

        if (ObjUtil.isEmpty(rabbitMqConsumerConfig.channel())) {
            consumerConfig.setChannel(null);
        } else {
            RabbitMqClientConfig.ChannelConfig channel = consumerConfig.getChannel();
            RabbitMqConsumerConfig.ChannelConfig channelConfig = rabbitMqConsumerConfig.channel()[0];
            channel.setBasicQos(channel.getBasicQos());
        }

        if (ObjUtil.isEmpty(rabbitMqConsumerConfig.queue())) {
            consumerConfig.setQueue(null);
        } else {
            RabbitMqClientConfig.QueueConfig queue = consumerConfig.getQueue();
            RabbitMqConsumerConfig.QueueConfig queueConfig = rabbitMqConsumerConfig.queue()[0];
            queue.setDurable(queueConfig.durable());
            queue.setMaxLength(queueConfig.maxLength());
            queue.setMaxLengthBytes(queueConfig.maxLengthBytes());
        }
        return consumerConfig;
    }
}
