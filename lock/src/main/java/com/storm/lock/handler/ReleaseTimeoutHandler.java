package com.storm.lock.handler;

import com.storm.lock.lock.LockInfo;

/**
 * 释放锁超时策略
 */
public interface ReleaseTimeoutHandler {

    /**
     * 处理
     * @param lockInfo 锁信息
     */
    void handler(LockInfo lockInfo);
}
