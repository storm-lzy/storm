package com.storm.lock.exceptions;

/**
 * 全局锁超时异常
 */
public class GlobalLockTimeoutException extends RuntimeException{

    public GlobalLockTimeoutException(String message) {
        super(message);
    }

    public GlobalLockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
