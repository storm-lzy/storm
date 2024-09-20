package com.storm.boot4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Boot4Application {

    public static void main(String[] args) {
        SpringApplication.run(Boot4Application.class, args);
    }

}
