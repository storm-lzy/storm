package com.jdd.integration.lock.provider;

import com.jdd.integration.lock.enums.LockTimeoutStrategy;
import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import com.jdd.integration.lock.function.AcquiredFunction;
import com.jdd.integration.lock.function.AcquiredVoidFunction;
import com.jdd.integration.lock.handler.LockTimeoutHandler;
import com.jdd.integration.lock.lock.Lock;
import com.jdd.integration.lock.lock.LockFactory;
import com.jdd.integration.lock.lock.LockInfo;
import com.jdd.integration.lock.properties.GlobalLockProperties;
import com.jdd.integration.lock.provider.config.LockConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.List;

/**
 * 全局锁编程式提供者
 *
 * @author 李治毅
 */
@Slf4j
public class LockMultipleProvider implements ApplicationRunner {

    @Resource
    private GlobalLockProperties preGlobalLockProperties;

    private static GlobalLockProperties globalLockProperties = new GlobalLockProperties();


    public static <T, S> T executeWithLock(String name, List<S> keys, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, keys, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, keys, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return executeWithLock(name, keys, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, keys, waitTime, globalLockProperties.getLeaseTime(), acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, keys, waitTime, globalLockProperties.getLeaseTime(), acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, lockTimeoutHandler,unLockOpportunityEnum,null);
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, AcquiredFunction<T> acquiredFunction, LockConfig lockConfig) {
        LockInfoProvider.initLockConfig(lockConfig);
        return executeWithLock(name, keys, lockConfig.getWaitTime(), lockConfig.getLeaseTime(),acquiredFunction, lockConfig.getLockTimeoutHandler(), lockConfig.getUnLockOpportunityEnum(), lockConfig.getErrorMsg());
    }

    public static <T, S> T executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum,String errorMsg) {
        LockInfo lockInfo = LockInfoProvider.getMultipleLockInfo(name, keys, waitTime, leaseTime, unLockOpportunityEnum,errorMsg);
        Lock lock = LockFactory.getLock(lockInfo);

        if (lock.acquire()) {
            try {
                log.debug("[Global-Lock][ACQUIRE] 获取到锁: [{}], 执行业务逻辑", lockInfo);
                return acquiredFunction.execute();
            } finally {
                release(lockInfo, lock);
                log.debug("[Global-Lock][RELEASE] 释放锁: [{}]", lockInfo);
            }
        } else if (null != lockTimeoutHandler) {
            lockTimeoutHandler.handler(lockInfo);
        }
        return null;
    }

    private static void release(LockInfo lockInfo, Lock lock) {
        if (lockInfo.shouldReleaseAfterTransaction() && TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(TransactionProvider.buildTransactionSynchronization(lockInfo.getUnLockOpportunityEnum(), lock));
        } else {
            lock.release();
        }
    }


    public static <S> void executeWithLock(String name, List<S> keys, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, keys, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    public static <S> void executeWithLock(String name, List<S> keys, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        executeWithLock(name, keys, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    public static <S> void executeWithLock(String name, List<S> keys, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, keys, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, keys, waitTime, globalLockProperties.getLeaseTime(), acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, keys, waitTime, globalLockProperties.getLeaseTime(), acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        executeWithLock(name, keys, waitTime, leaseTime, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum,null);
    }

    public static <S> void executeWithLock(String name, List<S> keys, AcquiredVoidFunction acquiredFunction, LockConfig lockConfig) {
        LockInfoProvider.initLockConfig(lockConfig);
        executeWithLock(name, keys, lockConfig.getWaitTime(), lockConfig.getLeaseTime(),acquiredFunction, lockConfig.getLockTimeoutHandler(), lockConfig.getUnLockOpportunityEnum(), lockConfig.getErrorMsg());
    }

    public static <S> void executeWithLock(String name, List<S> keys, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum,String errorMsg) {
        LockInfo lockInfo = LockInfoProvider.getMultipleLockInfo(name, keys, waitTime, leaseTime, unLockOpportunityEnum,errorMsg);
        Lock lock = LockFactory.getLock(lockInfo);

        if (lock.acquire()) {
            try {
                log.debug("[Global-Lock][ACQUIRE] 获取到锁: [{}], 执行业务逻辑", lockInfo);
                acquiredFunction.execute();
            } finally {
                release(lockInfo, lock);
                log.debug("[Global-Lock][RELEASE] 释放锁: [{}]", lockInfo);
            }
        } else if (null != lockTimeoutHandler) {
            lockTimeoutHandler.handler(lockInfo);
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        LockMultipleProvider.globalLockProperties = this.preGlobalLockProperties;
    }
}
