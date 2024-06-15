package com.storm.mq;

import com.storm.mq.concurrent.CallbackFuture;
import com.storm.mq.config.ConsumerConfig;
import com.storm.mq.enums.Ack;
import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import com.storm.mq.message.MessageProp;

public interface MqChannel {
    <T extends Message> CallbackFuture<MessageContext> publish(String var1, T var2, MessageProp var3);

    void subscribe(String var1, String var2, boolean var3, ConsumerConfig consumerConfig, MessageHandler messageHandler);

    void close();

    boolean isActive();

    void ack(long var1, String var3, Ack ack);

    Object unwrap();
}
