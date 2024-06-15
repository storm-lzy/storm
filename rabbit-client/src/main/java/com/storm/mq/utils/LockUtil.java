package com.storm.mq.utils;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 *
 */
public class LockUtil {

    public static void runWithLock(Lock lock,Runnable runnable){
        lock.lock();
        try {
            runnable.run();
        }finally {
            lock.unlock();
        }
    }


    public static <T> T runWithLock(Lock lock, Supplier<T> task) {
        lock.lock();

        T var2;
        try {
            var2 = task.get();
        } finally {
            lock.unlock();
        }

        return var2;
    }

}
