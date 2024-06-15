package com.example.stormspring.rebbit;

import com.storm.mq.message.Message;
import com.storm.mq.message.MessageContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 用户信息消息体
 */
@Data
public class UserMessage implements Message {

    private String name;
    private int age;
    protected boolean man;

    @Override
    public String getMessageId() {
        return UUID.randomUUID().toString();
    }
}
