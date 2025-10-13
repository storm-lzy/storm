package com.storm.boot4.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李治毅
 * @date 2024/9/2
 */
@Service
public class KeyProvider {

    public List<String> getKey(String order) {
        System.err.println("动态获取key" + order);
//        return order;
        ArrayList<String> strings = new ArrayList<>();
        strings.add("123");
        strings.add("456");
        return strings;
    }
}
