package com.storm.boot4.demo;

/**
 * 编程式加锁处理接口
 *
 * @author 李治毅
 */
@FunctionalInterface
public interface AcquiredFunction<T> {

    T execute();
}
