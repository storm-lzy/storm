package com.storm.boot4.aspect;

/**
 * @author 李治毅
 * @date 2024/11/1
 */
public enum TestEnum implements EnumInterface {

    RED
    ;

    @Override
    public String lockKey() {
        return "";
    }
}
