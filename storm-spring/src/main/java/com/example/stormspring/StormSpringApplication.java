package com.example.stormspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.example.stormspring.**")
@EnableCaching
public class StormSpringApplication extends SpringApplication{

    public static void main(String[] args) {
        StormSpringApplication.run(StormSpringApplication.class, args);
    }

}
