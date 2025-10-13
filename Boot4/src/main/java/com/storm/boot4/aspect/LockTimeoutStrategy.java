package com.storm.boot4.aspect;


import com.storm.boot4.demo.LockTimeoutHandler;

/**
 * 加锁超时策略
 *
 */
public enum LockTimeoutStrategy implements LockTimeoutHandler {

    /**
     * 空
     */
    NONE() {
        @Override
        public void handler() {

        }
    },

    /**
     * 快速失败
     */
    FAST_FAIL() {
        @Override
        public void handler() {
        }
    },

    /**
     * 持续获取
     */
    KEEP_ACQUIRE() {
        @Override
        public void handler() {

        }
    }
}
