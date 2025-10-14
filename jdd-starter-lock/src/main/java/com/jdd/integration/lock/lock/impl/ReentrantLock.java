package com.jdd.integration.lock.lock.impl;

import com.jdd.integration.lock.lock.Lock;
import com.jdd.integration.lock.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author 李治毅
 */
@Slf4j
public class ReentrantLock implements Lock {

    private RLock lock;

    private final LockInfo lockInfo;

    private final Redisson redisson;

    public ReentrantLock(LockInfo lockInfo, Redisson redisson) {
        this.lockInfo = lockInfo;
        this.redisson = redisson;
    }


    @Override
    public boolean acquire() {
        long start = System.currentTimeMillis();
        try {
            lock = redisson.getLock(lockInfo.getName());
            boolean locked = lock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
            // 抢锁成功,等待时间过长
            long overtime = System.currentTimeMillis() - start;
            if (locked && overtime > OVERTIME) {
                log.info("[Global-Lock][OVERTIME] 等锁时间超出阈值 lockName: [{}] overtime: [{}]", lockInfo.getName(), overtime);
            }
            return locked;
        } catch (InterruptedException e) {
            log.warn("[Global-Lock][INTERRUPTED] 获取锁被中断: [{}]", lockInfo.getName(), e);
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            log.error("[Global-Lock][ERROR] 获取锁异常: [{}]", lockInfo.getName(), e);
            return false;
        }
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
