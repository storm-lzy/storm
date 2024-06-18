package com.storm.shard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
@MapperScan(basePackages = "com.storm.shard.**")
public class PoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoolApplication.class, args);
    }
}
