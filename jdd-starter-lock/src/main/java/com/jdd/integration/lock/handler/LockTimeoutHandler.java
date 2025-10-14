package com.jdd.integration.lock.handler;

import com.jdd.integration.lock.lock.LockInfo;

/**
 * 超时策略接口
 *
 * @author 李治毅
 */
@FunctionalInterface
public interface LockTimeoutHandler {

    void handler(LockInfo lockInfo);
}
