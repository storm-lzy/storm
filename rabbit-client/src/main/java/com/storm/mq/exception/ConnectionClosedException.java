package com.storm.mq.exception;

/**
 *
 */
public class ConnectionClosedException extends MqException{
    public ConnectionClosedException(String message) {
        super(message);
    }

    public ConnectionClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionClosedException(Throwable cause) {
        super(cause);
    }
}
