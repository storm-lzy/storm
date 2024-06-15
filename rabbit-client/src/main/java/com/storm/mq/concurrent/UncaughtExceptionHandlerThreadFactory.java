package com.storm.mq.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 *
 */
public class UncaughtExceptionHandlerThreadFactory implements ThreadFactory {
    private final ThreadFactory delegate;
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public UncaughtExceptionHandlerThreadFactory(ThreadFactory delegate, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.delegate = delegate;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    public Thread newThread(Runnable r) {
        Thread thread = this.delegate.newThread(r);
        if (thread.getUncaughtExceptionHandler() == null) {
            thread.setUncaughtExceptionHandler(this.uncaughtExceptionHandler);
        }

        return thread;
    }
}
