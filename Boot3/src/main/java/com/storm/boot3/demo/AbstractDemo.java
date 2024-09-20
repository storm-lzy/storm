package com.storm.boot3.demo;

import com.storm.boot3.handle.AbstractHandle;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 李治毅
 * @date 2024/8/4
 */
@Component
public abstract class AbstractDemo {

    @Autowired
    private Map<String, AbstractHandle> abstractHandleMap;

    @PostConstruct
    public void init() {
        System.err.println(abstractHandleMap);
    }

}
