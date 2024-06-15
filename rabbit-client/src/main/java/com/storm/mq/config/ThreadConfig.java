package com.storm.mq.config;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 *
 */
@Data
public class ThreadConfig {
    @Min(1L)
    private int threadCount;
    private int queueSize;
    private String threadName = "default-thread";

    public ThreadConfig(int threadCount, int queueSize) {
        this.threadCount = threadCount;
        this.queueSize = queueSize;
    }

    public ThreadConfig(int threadCount, int queueSize, String threadName) {
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.threadName = threadName;
    }
}
