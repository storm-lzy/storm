package com.storm.mq;

import com.storm.mq.codec.Codec;
import com.storm.mq.enums.Ack;
import com.storm.mq.message.MessageContext;

import java.lang.reflect.Type;

/**
 *
 */
public abstract class MessageHandler {

    protected final Type type = MessageContext.class;

    public MessageHandler() {
    }

    public Type getType() {
        return this.type;
    }

    public abstract Ack handle(MessageContext var1, Codec var2) throws Throwable;
}
