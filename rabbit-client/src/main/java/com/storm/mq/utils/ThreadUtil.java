package com.storm.mq.utils;

import cn.hutool.core.util.StrUtil;
import com.storm.mq.concurrent.NamedThreadFactory;
import com.storm.mq.concurrent.ThreadPoolConfig;
import com.storm.mq.concurrent.UncaughtExceptionHandlerThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.*;

/**
 *
 */
@Slf4j
public class ThreadUtil {

    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = (thread, throwable) -> {
        log.warn(String.format("%s Thread [%s] an exception occurred during execution", throwable,thread.getName()));
    };

    public static ThreadPoolExecutor newThreadPool(int corePoolSize, int maximumPoolSize, int queueSize, String threadName, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        threadPoolConfig.setCorePoolSize(corePoolSize);
        threadPoolConfig.setMaximumPoolSize(maximumPoolSize);
        if (queueSize <= 0) {
            threadPoolConfig.setWorkQueue(new LinkedBlockingQueue<>());
        } else {
            threadPoolConfig.setWorkQueue(new ArrayBlockingQueue<>(queueSize));
        }

        threadPoolConfig.setThreadFactory(new NamedThreadFactory(StrUtil.blankToDefault(threadName, "thread")));
        threadPoolConfig.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return newThreadPool(threadPoolConfig);
    }

    public static ThreadPoolExecutor newThreadPool(ThreadPoolConfig threadPoolConfig) {
        Assert.notNull(threadPoolConfig, "Thread pool config must be not null");
        Assert.isTrue(threadPoolConfig.getMaximumPoolSize() >= threadPoolConfig.getCorePoolSize(), String.format("The maximum size of the thread pool [%s] should be greater than or equal to the core size [%s]", threadPoolConfig.getMaximumPoolSize(), threadPoolConfig.getCorePoolSize()));
        BlockingQueue<Runnable> workQueue = threadPoolConfig.getWorkQueue();
        if (workQueue == null) {
            workQueue = new ArrayBlockingQueue<>(threadPoolConfig.getQueueSize());
        }

        ThreadFactory threadFactory = threadPoolConfig.getThreadFactory();
        if (threadFactory == null) {
            ThreadFactory threadFactoryOriginal = Executors.defaultThreadFactory();
            threadFactory = (runnable) -> {
                Thread thread = threadFactoryOriginal.newThread(runnable);
                thread.setContextClassLoader(threadPoolConfig.getDefaultContextClassLoader());
                return thread;
            };
        }

        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = threadPoolConfig.getUncaughtExceptionHandler();
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = UNCAUGHT_EXCEPTION_HANDLER;
        }

        RejectedExecutionHandler rejectedExecutionHandler = threadPoolConfig.getRejectedExecutionHandler();
        if (rejectedExecutionHandler == null) {
            rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        }

        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaximumPoolSize(), threadPoolConfig.getKeepAliveTime(), threadPoolConfig.getTimeUnit(), workQueue, new UncaughtExceptionHandlerThreadFactory(threadFactory, uncaughtExceptionHandler), rejectedExecutionHandler);
    }

}
