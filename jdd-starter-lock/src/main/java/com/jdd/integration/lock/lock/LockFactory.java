package com.jdd.integration.lock.lock;

import cn.hutool.extra.spring.SpringUtil;
import com.jdd.integration.lock.lock.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;

/**
 * @author 李治毅
 */
@Slf4j
public class LockFactory {

    private static Redisson redisson;

    public static Lock getLock(LockInfo lockInfo) {
        if(null == redisson){
            redisson = SpringUtil.getBean(Redisson.class);
        }
        switch (lockInfo.getLockType()) {
            case REENTRANT:
                return new ReentrantLock(lockInfo, redisson);
            case FAIR:
                return new FairLock(lockInfo, redisson);
            case READ:
                return new ReadLock(lockInfo, redisson);
            case WRITE:
                return new WriteLock(lockInfo, redisson);
            case MULTIPLE:
                return new MultipleLock(lockInfo, redisson);
            default:
                log.info("[Global-Lock] [{}]为获取到可用锁类型，默认使用可重入锁", lockInfo);
                return new ReentrantLock(lockInfo, redisson);
        }
    }

}
