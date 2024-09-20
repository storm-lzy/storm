package com.storm.boot4.demo;

import org.springframework.stereotype.Service;

/**
 * @author 李治毅
 * @date 2024/9/2
 */
@Service
public class KeyProvider {

    public String getKey(String order) {
        System.err.println("动态获取key" + order);
        return order;
    }
}
