package com.storm.mq.concurrent;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Data
public class ThreadPoolConfig {

    private @Min(1L) int corePoolSize = 10;
    private @Min(1L) int maximumPoolSize = 10;
    private @Min(1L) long keepAliveTime = 300L;
    private @NotNull TimeUnit timeUnit;
    private BlockingQueue<Runnable> workQueue;
    private @Min(1L) int queueSize;
    private ThreadFactory threadFactory;
    private RejectedExecutionHandler rejectedExecutionHandler;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private ClassLoader defaultContextClassLoader;

    public ThreadPoolConfig() {
        this.timeUnit = TimeUnit.SECONDS;
        this.queueSize = 100;
    }
}
