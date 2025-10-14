package com.jdd.integration.lock.provider;

import com.jdd.integration.lock.enums.LockTimeoutStrategy;
import com.jdd.integration.lock.enums.LockType;
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

/**
 * 全局锁编程式提供者
 *
 * @author 李治毅
 */
@Slf4j
public class LockProvider implements ApplicationRunner {

    @Resource
    private GlobalLockProperties preGlobalLockProperties;

    private static GlobalLockProperties globalLockProperties = new GlobalLockProperties();

    /**
     * 根据锁名称加业务key进行加锁
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, key, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, key, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return executeWithLock(name, key, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param lockType:         锁类型
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, LockType lockType, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, key, lockType, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param lockType:           锁类型
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, key, lockType, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return executeWithLock(name, key, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), lockType, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param leaseTime:        租赁时间
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, key, waitTime, leaseTime, LockType.REENTRANT, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, key, waitTime, globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, key, waitTime, globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param leaseTime:        租赁时间
     * @param lockType:         锁类型
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction) {
        return executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param leaseTime:          租赁时间
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, key, waitTime, leaseTime, LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param leaseTime:          租赁时间
     * @param lockType:           锁类型
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * @param name:             锁名称
     * @param key:              业务key
     * @param acquiredFunction: 业务方法体
     * @param lockConfig:       锁参数
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, AcquiredFunction<T> acquiredFunction, LockConfig lockConfig) {
        LockInfoProvider.initLockConfig(lockConfig);
        return executeWithLock(name, key, lockConfig.getWaitTime(), lockConfig.getLeaseTime(), lockConfig.getLockType(), acquiredFunction, lockConfig.getLockTimeoutHandler(), lockConfig.getUnLockOpportunityEnum(), lockConfig.getErrorMsg());
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param waitTime:              等锁时间
     * @param leaseTime:             租赁时间
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum, null);
    }


    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param waitTime:              等锁时间
     * @param leaseTime:             租赁时间
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @param errorMsg:              超时异常文案
     * @author 李治毅
     */
    public static <T> T executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum, String errorMsg) {
        lockType = lockType == LockType.MULTIPLE ? LockType.REENTRANT : lockType;
        LockInfo lockInfo = LockInfoProvider.getLockInfo(lockType, name, key, waitTime, leaseTime, unLockOpportunityEnum, errorMsg);
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


    /**
     * 根据锁名称加业务key进行加锁
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, key, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, key, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        executeWithLock(name, key, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param lockType:         锁类型
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, LockType lockType, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, key, lockType, acquiredFunction, LockTimeoutStrategy.FAST_FAIL);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param lockType:           锁类型
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, key, lockType, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        executeWithLock(name, key, globalLockProperties.getWaitTime(), globalLockProperties.getLeaseTime(), lockType, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param leaseTime:        租赁时间
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, key, waitTime, leaseTime, LockType.REENTRANT, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, key, waitTime, globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, key, waitTime, globalLockProperties.getLeaseTime(), LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型
     *
     * @param name:             锁名称
     * @param key:              业务key
     * @param waitTime:         等锁时间
     * @param leaseTime:        租赁时间
     * @param lockType:         锁类型
     * @param acquiredFunction: 业务方法体
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction) {
        executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, LockTimeoutStrategy.FAST_FAIL, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param leaseTime:          租赁时间
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, key, waitTime, leaseTime, LockType.REENTRANT, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略
     *
     * @param name:               锁名称
     * @param key:                业务key
     * @param waitTime:           等锁时间
     * @param leaseTime:          租赁时间
     * @param lockType:           锁类型
     * @param acquiredFunction:   业务方法体
     * @param lockTimeoutHandler: 超时策略方法
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, lockTimeoutHandler, UnLockOpportunityEnum.AFTER_COMPLETION);
    }

    /**
     * @param name:             锁名称
     * @param key:              业务key
     * @param acquiredFunction: 业务方法体
     * @param lockConfig:       锁参数
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, AcquiredVoidFunction acquiredFunction, LockConfig lockConfig) {
        LockInfoProvider.initLockConfig(lockConfig);
        executeWithLock(name, key, lockConfig.getWaitTime(), lockConfig.getLeaseTime(), lockConfig.getLockType(), acquiredFunction, lockConfig.getLockTimeoutHandler(), lockConfig.getUnLockOpportunityEnum(), lockConfig.getErrorMsg());
    }


    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param waitTime:              等锁时间
     * @param leaseTime:             租赁时间
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        executeWithLock(name, key, waitTime, leaseTime, lockType, acquiredFunction, lockTimeoutHandler, unLockOpportunityEnum,null);

    }

    /**
     * 根据锁名称加业务key进行加锁,指定等待锁的上限时间,指定获取成功后的租赁时间,指定锁的类型,指定获取锁的超时策略,指定释放锁的时机
     *
     * @param name:                  锁名称
     * @param key:                   业务key
     * @param waitTime:              等锁时间
     * @param leaseTime:             租赁时间
     * @param lockType:              锁类型
     * @param acquiredFunction:      业务方法体
     * @param lockTimeoutHandler:    超时策略方法
     * @param unLockOpportunityEnum: 释放锁时机
     * @param errorMsg:              锁超时文案
     * @author 李治毅
     */
    public static void executeWithLock(String name, Object key, int waitTime, int leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum, String errorMsg) {
        lockType = lockType == LockType.MULTIPLE ? LockType.REENTRANT : lockType;
        LockInfo lockInfo = LockInfoProvider.getLockInfo(lockType, name, key, waitTime, leaseTime, unLockOpportunityEnum, errorMsg);
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
        LockProvider.globalLockProperties = this.preGlobalLockProperties;
    }
}
