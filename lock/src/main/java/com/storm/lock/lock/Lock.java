package com.storm.lock.lock;

import org.redisson.api.RedissonClient;

/**
 * 锁
 *
 * @date 2024/5/29
 */
public interface Lock {


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
    boolean release();
}
