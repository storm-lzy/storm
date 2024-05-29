package com.storm.lock.provider.impl;

import com.storm.lock.enums.LockType;
import com.storm.lock.exceptions.GlobalLockTimeoutException;
import com.storm.lock.lock.Lock;
import com.storm.lock.lock.LockFactory;
import com.storm.lock.lock.LockInfo;
import com.storm.lock.provider.LockProvider;
import com.storm.lock.wrocker.AcquiredLockWork;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class LockProviderImpl implements LockProvider {


    private final LockFactory lockFactory;

    public LockProviderImpl(LockFactory lockFactory) {
        this.lockFactory = lockFactory;
    }

    @Override
    public <T> T lock(String lockName, LockType lockType, AcquiredLockWork<T> acquiredLockWork, long waitTime, long leaseTime) throws Exception{

        LockInfo lockInfo = new LockInfo(lockType, lockName, waitTime, leaseTime);

        Lock lock = lockFactory.getLock(lockInfo);

        boolean lockResult = lock.acquire();

        if (lockResult) {
            try {
                log.info("[Global-Lock][ACQUIRE] 获取到锁: [{}], 执行业务逻辑", lockName);
                return acquiredLockWork.invokeAfterLockAcquire();
            } finally {
                boolean releaseResult = lock.release();
                log.info("[Global-Lock][RELEASE] 释放锁: [{}] result: [{}]", lockName, releaseResult);
            }
        }
        throw new GlobalLockTimeoutException(String.format("获取锁[%s]失败", lockName));
    }
}
