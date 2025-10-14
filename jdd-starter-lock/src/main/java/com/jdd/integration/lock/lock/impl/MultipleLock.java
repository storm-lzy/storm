package com.jdd.integration.lock.lock.impl;

import com.jdd.integration.lock.lock.Lock;
import com.jdd.integration.lock.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author 李治毅
 */
@Slf4j
public class MultipleLock implements Lock {

    private RLock lock;

    private final LockInfo lockInfo;

    private final Redisson redisson;

    public MultipleLock(LockInfo lockInfo, Redisson redisson) {
        this.lockInfo = lockInfo;
        this.redisson = redisson;
    }

    @Override
    public boolean acquire() {
        long start = System.currentTimeMillis();
        try {
            RLock[] rLocks = buildLocks(lockInfo);
            lock = redisson.getMultiLock(rLocks);
            boolean locked = lock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
            // 抢锁成功,等待时间过长
            long overtime = System.currentTimeMillis() - start;
            if (locked && overtime > OVERTIME) {
                log.info("[Global-Lock][OVERTIME] 等锁时间超出阈值 lockName: [{}] overtime: [{}]", Arrays.toString(lockInfo.getNames()), overtime);
            }
            return locked;
        } catch (InterruptedException e) {
            log.warn("[Global-Lock][INTERRUPTED] 获取多锁被中断: [{}]", Arrays.toString(lockInfo.getNames()), e);
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            log.error("[Global-Lock][ERROR] 获取多锁异常: [{}]", Arrays.toString(lockInfo.getNames()), e);
            return false;
        }
    }

    private RLock[] buildLocks(LockInfo lockInfo) {
        RLock[] rLocks = new RLock[lockInfo.getNames().length];
        for (int i = 0; i < lockInfo.getNames().length; i++) {
            rLocks[i] = redisson.getLock(lockInfo.getNames()[i]);
        }
        return rLocks;
    }

    @Override
    public void release() {
        try {
            lock.unlock();
        } catch (Exception e) {
            log.error("[Global-Lock][RELEASE] 释放锁失败: [{}], ", lockInfo, e);
        }
    }
}
