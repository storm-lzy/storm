package com.storm.mq.message;

import lombok.Data;

/**
 *
 */
@Data
public class MessageContext {

    private String messageId;
    private String topic;
    private byte[] payload;
    private String contentEncoding;
    private String contentType;
    private long timestamp;
    private MessageProp messageProp;



}
