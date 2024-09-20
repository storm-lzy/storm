package com.storm.boot3.handle.impl;

import com.storm.boot3.handle.AbstractHandle;
import org.springframework.stereotype.Service;

/**
 * @author 李治毅
 * @date 2024/7/19
 */
//@Service("Demo2Handle")
public class Demo2Handle implements AbstractHandle {
    @Override
    public void handleMessage() {
        System.err.println("handle demo2");
    }
}
