package com.storm.lock.handler;

import com.storm.lock.lock.Lock;
import com.storm.lock.lock.LockInfo;
import org.aspectj.lang.JoinPoint;

/**
 * 获取锁超时处理策略
 */
public interface LockTimeoutHandler {

    /**
     * 处理
     *
     * @param lockInfo 锁信息
     * @param lock 锁
     * @param joinPoint 连接点
     */
    void handler(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}
