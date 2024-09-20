package com.storm.boot3.demo;

import com.storm.boot3.handle.impl.Demo1Handle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 李治毅
 * @date 2024/8/29
 */
//@Configuration
public class DemoConfig {


    @Bean("demo1Handle1")
    public Demo1Handle demo11(){
        return new Demo1Handle();
    }
}
