package com.storm.mq.store;

import com.storm.mq.enums.MessageStatus;
import com.storm.mq.enums.MessageType;
import lombok.Data;

/**
 *
 */
@Data
public class MessageEntity {

    private String id;
    private String topic;
    private String payload;
    private String contentEncoding;
    private String contentType;
    private long timestamp;
    private int autoRetryTotal = 3;
    private boolean persistent = true;
    private long delay = 0L;
    private String extContext;
    private long deliveryTag;
    private String errorContext;
    private int publishRetryOnFail;
    private MessageType messageType;
    private MessageStatus messageStatus;


}
