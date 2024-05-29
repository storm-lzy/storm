package com.storm.lock.provider;

import com.storm.lock.enums.LockType;
import com.storm.lock.wrocker.AcquiredLockWork;

public interface LockProvider {

    default <T> T lock(String lockName, AcquiredLockWork<T> acquiredLockWork) throws Exception{
        return this.lock(lockName, LockType.REENTRANT, acquiredLockWork, 60, 60);
    }


    default <T> T lock(String lockName, LockType lockType, AcquiredLockWork<T> acquiredLockWork) throws Exception{
        return this.lock(lockName, lockType, acquiredLockWork, 60, 60);
    }

    default <T> T lock(String lockName, AcquiredLockWork<T> acquiredLockWork, long waitTime, long leaseTim) throws Exception{
        return this.lock(lockName, LockType.REENTRANT, acquiredLockWork, 60, 60);
    }

    <T> T lock(String lockName, LockType lockType, AcquiredLockWork<T> acquiredLockWork, long waitTime, long leaseTime)  throws Exception;

}
