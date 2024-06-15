package com.storm.mq.exception;

/**
 *
 */
public class MqException extends RuntimeException {
    public MqException() {
    }

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqException(Throwable cause) {
        super(cause);
    }
}
