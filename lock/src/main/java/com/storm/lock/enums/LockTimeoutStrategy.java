package com.storm.lock.enums;

import com.storm.lock.exceptions.GlobalLockTimeoutException;
import com.storm.lock.handler.LockTimeoutHandler;
import com.storm.lock.lock.Lock;
import com.storm.lock.lock.LockInfo;
import org.aspectj.lang.JoinPoint;

import java.util.concurrent.TimeUnit;

/**
 * 加锁超时策略
 *
 * @date 2024/5/29
 */
public enum LockTimeoutStrategy implements LockTimeoutHandler {

    /**
     * 空
     */
    NONE() {
        public void handler(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {

        }
    },

    /**
     * 快速失败
     */
    FAST_FAIL() {
        public void handler(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            throw new GlobalLockTimeoutException(String.format("获取锁[%s]超时 Timeout[%s]", lockInfo.getName(), lockInfo.getWaitTime()));
        }
    },

    /**
     * 持续获取
     */
    KEEP_ACQUIRE() {

        /**
         * 默认时间间隔
         */
        private static final long DEFAULT_INTERVAL = 100L;

        /**
         * 默认最大时间间隔
         */
        private static final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000L;

        public void handler(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            long interval = DEFAULT_INTERVAL;

            while (!lock.acquire()) {
                if (interval > DEFAULT_MAX_INTERVAL) {
                    throw new GlobalLockTimeoutException(String.format("持续获取锁[%s]失败, 可能发生死锁", lockInfo.getName()));
                }
                try {
                    Thread.sleep(TimeUnit.MILLISECONDS.toMillis(interval));
                } catch (InterruptedException var4) {
                    throw new RuntimeException(var4);
                }
                interval <<= 1;

            }

        }
    }
}
