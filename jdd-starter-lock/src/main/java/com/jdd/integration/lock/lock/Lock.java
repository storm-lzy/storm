package com.jdd.integration.lock.lock;

/**
 * @author 李治毅
 */
public interface Lock {

    long OVERTIME = 2000L;

    /**
     * 获取锁
     *
     * @return boolean
     */
    boolean acquire();

    /**
     * 释放锁
     *
     * @return boolean
     */
    void release();
}
