package com.example.stormspring;

import com.storm.mq.anno.EnableRabbitMqAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.example.stormspring.**")
@EnableCaching
@EnableRabbitMqAutoConfiguration
public class StormSpringApplication extends SpringApplication{

    public static void main(String[] args) {
        StormSpringApplication.run(StormSpringApplication.class, args);
    }

}
