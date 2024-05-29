package com.storm.lock.annotation;

import com.storm.lock.enums.LockTimeoutStrategy;
import com.storm.lock.enums.LockType;
import com.storm.lock.enums.ReleaseTimeoutStrategy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface GlobalLock {

    /**
     * 锁名称
     */
    String name() default "";

    /**
     * 自定义业务key
     */
    String[] keys() default {};

    /**
     * 锁类型，默认可重入锁
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 等待时间
     */
    long waitTime() default Long.MAX_VALUE;

    /**
     * 租赁时间
     */
    long leaseTime() default Long.MAX_VALUE;

    /**
     * 获取锁超时策略
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NONE;

    /**
     * 获取锁超时策略
     */
    String customLockTimeoutStrategy() default "";

    /**
     * 释放锁超时策略
     */
    ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NONE;

    /**
     * 自定义释放锁超时处理策略
     */
    String customReleaseTimeoutStrategy() default "";
}
