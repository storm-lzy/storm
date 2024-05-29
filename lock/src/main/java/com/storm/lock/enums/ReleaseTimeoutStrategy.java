package com.storm.lock.enums;

import com.storm.lock.exceptions.GlobalLockTimeoutException;
import com.storm.lock.handler.ReleaseTimeoutHandler;
import com.storm.lock.lock.LockInfo;

/**
 * 释放超时策略
 *
 * @date 2024/5/29
 */
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {


    NONE() {
        @Override
        public void handler(LockInfo lockInfo) {

        }
    },
    FAST_FAIL() {
        @Override
        public void handler(LockInfo lockInfo) {
            throw new GlobalLockTimeoutException(String.format("释放锁[%s]超时 Timeout[%s]", lockInfo.getName(), lockInfo.getWaitTime()));
        }
    }
}
