package com.example.stormspring.rebbit.handler;

import com.example.stormspring.rebbit.UserMessage;
import com.storm.mq.anno.MqSubscriber;
import com.storm.mq.codec.Codec;
import com.storm.mq.enums.Ack;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@MqSubscriber(topics = {"storm-user"},group = "user")
@Slf4j
public class UserMessageHandler {


    public Ack handler(UserMessage userMessage, Codec codec) {
        log.info("[UserMessageHandler] 收到用户消息{}", userMessage);
        return Ack.ACCEPT;
    }
}
