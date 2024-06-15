package com.storm.mq;

import com.storm.mq.concurrent.CallbackFuture;
import com.storm.mq.config.ConsumerConfig;
import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import com.storm.mq.message.MessageProp;

public interface MqClient {
    void start();

    void close();

    default <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message) {
        return this.publish(topic, message, new MessageProp());
    }

    default <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message, boolean persistent) {
        MessageProp prop = new MessageProp();
        prop.setPersistent(persistent);
        return this.publish(topic, message, prop);
    }

    default <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message, long delay) {
        MessageProp prop = new MessageProp();
        prop.setDelay(delay);
        return this.publish(topic, message, prop);
    }

    <T extends Message> CallbackFuture<MessageContext> publish(String topic, T message, MessageProp messageProp);

    void subscribe(String topic, String group, boolean share, ConsumerConfig consumerConfig, MessageHandler messageHandler);
}
