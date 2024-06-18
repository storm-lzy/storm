package com.storm.lock.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 可重入锁
 */
public class ReentrantLock implements Lock{

    private RLock rLock;

    private final LockInfo lockInfo;

    private final RedissonClient redissonClient;


    public ReentrantLock(LockInfo lockInfo, RedissonClient redissonClient) {
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    public boolean acquire() {
        rLock = redissonClient.getLock(lockInfo.getName());
        try {
            return rLock.tryLock(lockInfo.getWaitTime(),lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean release() {
        if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
            try {
                return rLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
