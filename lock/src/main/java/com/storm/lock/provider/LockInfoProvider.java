package com.storm.lock.provider;

import com.storm.lock.annotation.GlobalLock;
import com.storm.lock.enums.LockType;
import com.storm.lock.lock.LockInfo;
import com.storm.lock.properties.GlobalLockProperties;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;

/**
 * 锁信息提供者
 */
@Slf4j
public class LockInfoProvider {

    private final GlobalLockProperties globalLockProperties;

    @Resource
    private LockKeyProvider lockKeyProvider;

    public LockInfoProvider(GlobalLockProperties globalLockProperties) {
        this.globalLockProperties = globalLockProperties;
    }

    /**
     * 获取锁信息
     *
     * @param joinPoint  连接点
     * @param globalLock 全局锁
     * @return {@link LockInfo}
     */
    public LockInfo get(JoinPoint joinPoint, GlobalLock globalLock){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = globalLock.lockType();

        String keyName = lockKeyProvider.getKeyName(joinPoint, globalLock);

        String lockName = globalLockProperties.getPrefix() +  ":" + this.getLockName(globalLock.name(), signature) + keyName;

        long waitTime = this.getWaitTime(globalLock);
        long leaseTime = this.getLeaseTime(globalLock);

        if (leaseTime == -1) {
            log.warn("[Glock-Lock] [{}]锁释放时间设置为-1, 可能会导致死锁", lockName);
        }
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    /**
     * 获取锁名称
     *
     * @param keyName   键名
     * @param signature 签名
     * @return {@link String}
     */
    private String getLockName(String keyName, MethodSignature signature) {
        if (StringUtil.isBlank(keyName)) {
            // 如果没有指定则按全类名拼接方法名处理
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return keyName;
        }
    }

    /**
     * 获取加锁等待时间
     *
     * @param globalLock 全局锁
     * @return long
     */
    private long getWaitTime(GlobalLock globalLock) {
        return globalLock.waitTime() == Long.MIN_VALUE ? globalLockProperties.getWaitTime() : globalLock.waitTime();
    }

    /**
     * 获取锁的租赁时间
     *
     * @param globalLock 全局锁
     * @return long
     */
    private long getLeaseTime(GlobalLock globalLock) {
        return globalLock.leaseTime() == Long.MIN_VALUE ? globalLockProperties.getLeaseTime() : globalLock.leaseTime();
    }
}
