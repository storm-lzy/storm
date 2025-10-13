package com.storm.boot4.demo;

import com.storm.boot4.aspect.LockType;

/**
 * @author 李治毅
 * @date 2024/9/14
 */
public class LockProvider {

    public static <T> T executeWithLock(String name, String key, AcquiredFunction<T> acquiredFunction) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, LockType lockType, AcquiredFunction<T> acquiredFunction) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return acquiredFunction.execute();
    }

    public static <T> T executeWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return acquiredFunction.execute();
    }


    public static boolean tyeExecuteWithLock(String name, String key, AcquiredVoidFunction acquiredFunction) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, LockType lockType, AcquiredVoidFunction acquiredFunction) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return true;
    }

    public static boolean tryExecuteWithLock(String name, String key, long waitTime, long leaseTime, LockType lockType, AcquiredVoidFunction acquiredFunction, LockTimeoutHandler lockTimeoutHandler, UnLockOpportunityEnum unLockOpportunityEnum) {
        return true;
    }
}
