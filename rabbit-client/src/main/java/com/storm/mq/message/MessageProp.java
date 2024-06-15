package com.storm.mq.message;

import lombok.Data;

import java.util.Map;

/**
 *
 */
@Data
public class MessageProp {

    private int autoRetryTotal = 3;
    private int publishRetryOnFail = -1;
    private boolean persistent = true;
    private long delay = 0L;
    private Map<String,Object> headers;
    private Map<String,Object> extContext;



}
