package com.storm.lock.lock;

import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class WriteLock implements Lock {

    private RReadWriteLock rReadWriteLock;

    private final LockInfo lockInfo;

    private final RedissonClient redissonClient;


    public WriteLock(LockInfo lockInfo, RedissonClient redissonClient) {
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }


    @Override
    public boolean acquire() {
        rReadWriteLock = redissonClient.getReadWriteLock(lockInfo.getName());
        try {
            return rReadWriteLock.writeLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (rReadWriteLock.writeLock().isLocked() && rReadWriteLock.writeLock().isHeldByCurrentThread()) {
            try {
                return rReadWriteLock.writeLock().forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
