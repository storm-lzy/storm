package com.storm.boot3.enums;

import com.storm.boot3.handle.impl.Demo1Handle;
import com.storm.boot3.handle.impl.Demo2Handle;

/**
 * @author 李治毅
 * @date 2024/7/19
 */
public enum HandleEnum {

    DEMO1(1, Demo1Handle.class),
    DEMO2(1, Demo2Handle.class);


    private int key;
    private Class<?> value;

    HandleEnum(int key,Class<?> value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Class<?> getValue() {
        return value;
    }

    public void setValue(Class<?> value) {
        this.value = value;
    }
}
