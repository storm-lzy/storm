package com.storm.boot4.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 15690
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalLock {

    /**
     *
     */
    Class<? extends Enum<?>> value();
    /**
     * 锁名称
     */
    String name();

    /**
     * 自定义业务key
     */
    String key() default "";


    /**
     * 锁类型，默认可重入锁
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 等待时间
     */
    int waitTime() default 60 * 60 * 24;

    /**
     * 租赁时间
     */
    int leaseTime() default 60 * 60 * 24;

    /**
     * 获取锁超时策略
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NONE;


    String custonLockTimeoutStrategy() default "";
}
