package com.storm.lock.exceptions;

/**
 * 全局锁调用异常
 */
public class GlobalLockInvocationException extends RuntimeException{


    public GlobalLockInvocationException(String message) {
        super(message);
    }

    public GlobalLockInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
