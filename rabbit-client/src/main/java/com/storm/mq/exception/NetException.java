package com.storm.mq.exception;

/**
 *
 */
public class NetException extends MqException{
    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }
}
