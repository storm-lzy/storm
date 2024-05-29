package com.storm.lock.wrocker;

@FunctionalInterface
public interface AcquiredLockWork<T> {

    T invokeAfterLockAcquire() throws Exception;

}
