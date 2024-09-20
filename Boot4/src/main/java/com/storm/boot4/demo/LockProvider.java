package com.storm.boot4.demo;

import com.storm.boot4.aspect.LockType;

import java.util.List;

/**
 * @author 李治毅
 * @date 2024/9/14
 */
public class LockProvider {

    public static <T> T lock(String name, String key, AcquiredFunction<T> acquiredFunction) {
        return lock(name,key,acquiredFunction,UnLockOpportunityEnum.NORMAL);
    }

    public static Boolean tryLock(String name, String key, AcquiredFunction<Boolean> acquiredFunction) {
        return lock(name,key,acquiredFunction,UnLockOpportunityEnum.NORMAL);
    }

    public static void lock(String name, String key, AcquiredFunctionVoid acquiredFunction) {
    }

    public static <T> T lock(String name, String key, AcquiredFunction<T> acquiredFunction,UnLockOpportunityEnum unLockOpportunityEnum) {
        return lock(name,key,-1,-1,LockType.REENTRANT,acquiredFunction,null,unLockOpportunityEnum);
    }

    public static <T> T lock(String name, String key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler) {
        return lock(name,key,30,30,LockType.REENTRANT,acquiredFunction,lockTimeoutHandler,UnLockOpportunityEnum.NORMAL);
    }

    public static <T> T lock(String name, String key, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler,UnLockOpportunityEnum unLockOpportunityEnum) {
        return lock(name,key,30,30,LockType.REENTRANT,acquiredFunction,lockTimeoutHandler,unLockOpportunityEnum);
    }

    public static <T> T lock(String name, String key, long waitTime, long leaseTime,LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler,UnLockOpportunityEnum unLockOpportunityEnum) {
        return acquiredFunction.execute();
    }

    public static <T> T lockMulti(String name, List<String> key, LockType lockType, AcquiredFunction<T> acquiredFunction){
        return lockMulti(name,key,-1,-1,LockType.REENTRANT,acquiredFunction,null);
    };

    public static <T> T lockMulti(String name, List<String> key, LockType lockType, AcquiredFunction<T> acquiredFunction,LockTimeoutHandler lockTimeoutHandler){
        return lockMulti(name,key,10,10,LockType.REENTRANT,acquiredFunction,lockTimeoutHandler);
    };

    public static <T> T lockMulti(String name, List<String> key, long waitTime, long leaseTime,LockType lockType, AcquiredFunction<T> acquiredFunction, LockTimeoutHandler lockTimeoutHandler){
        return acquiredFunction.execute();
    };




}
