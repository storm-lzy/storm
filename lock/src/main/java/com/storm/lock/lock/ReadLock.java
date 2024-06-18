package com.storm.lock.lock;

import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ReadLock implements Lock {

    private RReadWriteLock rReadWriteLock;

    private final LockInfo lockInfo;

    private final RedissonClient redissonClient;


    public ReadLock(LockInfo lockInfo, RedissonClient redissonClient) {
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    public boolean acquire() {
        rReadWriteLock = redissonClient.getReadWriteLock(lockInfo.getName());
        try {
            return rReadWriteLock.readLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean release() {
        if (rReadWriteLock.readLock().isLocked() && rReadWriteLock.readLock().isHeldByCurrentThread()) {
            try {
                return rReadWriteLock.readLock().forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
