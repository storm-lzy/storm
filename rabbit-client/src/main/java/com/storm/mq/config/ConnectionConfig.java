package com.storm.mq.config;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 *
 */
@Data
public class ConnectionConfig {

    @Min(0L)
    private int connectionTimeout = 3000;
    @Min(1L)
    private int maxConnection = 3000;
    @Min(0L)
    private int minIdle = 10;
    private long minIdleTime = 30000L;
    @Min(512L)
    private int readBufferSize = 1048576;
    @Min(512L)
    private int writeBufferSize = 1048576;

}
