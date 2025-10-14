package com.jdd.integration.lock.annotation;

import com.jdd.integration.lock.enums.LockTimeoutStrategy;
import com.jdd.integration.lock.enums.LockType;
import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import jodd.util.StringPool;

import java.lang.annotation.*;

/**
 * 声明式锁标记
 *
 * @author 李治毅
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface GlobalLock {

    /**
     * 锁名称
     */
    String name();

    /**
     * 自定义业务key
     */
    String key();

    /**
     * 锁类型，默认可重入锁
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 等待时间，单位秒 默认10秒
     * 注意：
     *      指定-1: 无限等待直到获取到锁
     *      指定 0: 不会等待，只抢一次锁，成功则成功，失败则立刻退出
     *      指定>0: 指定时间内等待，超时则失败
     */
    int waitTime() default 10;

    /**
     * 租赁时间, 单位秒 默认10
     * 注意：
     *      指定<=0：默认租赁30秒，如果30秒内未执行结束，会不断每10秒自动续期
     *      指定 >0：在指定时间内无论逻辑是否执行结束，不会自动续期
     */
    int leaseTime() default 10;

    /**
     * 默认快速失败
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.FAST_FAIL;

    /**                         ↑↑↑↑↑↑
     * 自定义失败策略，该项指定后lockTimeoutStrategy将失效
     */
    String customTimeoutStrategy() default StringPool.EMPTY;

    /**
     * 自定义解锁时机
     */
    UnLockOpportunityEnum unLockOpportunity() default UnLockOpportunityEnum.AFTER_COMPLETION;

    /**
     * 自定义超时异常文案
     */
    String errorMsg() default StringPool.EMPTY;
}