package com.storm.lock.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;

/**
 *
 */
@Slf4j
public class LockFactory {

    @Resource
    private RedissonClient redissonClient;

    public Lock getLock(LockInfo lockInfo) {
        switch (lockInfo.getLockType()) {
            case REENTRANT:
                return new ReentrantLock(lockInfo, redissonClient);
            case FAIR:
                return new FairLock(lockInfo, redissonClient);
            case READ:
                return new ReadLock(lockInfo, redissonClient);
            case WRITE:
                return new WriteLock(lockInfo, redissonClient);
            default:
                log.info("[Global-Lock] [{}]为获取到可用锁类型，默认使用可重入锁", lockInfo);
                return new ReentrantLock(lockInfo, redissonClient);
        }
    }
}
