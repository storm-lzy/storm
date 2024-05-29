package com.storm.lock.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 */
public class FairLock implements Lock {

    private RLock rLock;

    private final LockInfo lockInfo;

    private final RedissonClient redissonClient;


    public FairLock(LockInfo lockInfo, RedissonClient redissonClient) {
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getFairLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
            try {
                return rLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
