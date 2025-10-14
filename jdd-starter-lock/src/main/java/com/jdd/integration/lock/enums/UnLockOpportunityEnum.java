package com.jdd.integration.lock.enums;

/**
 * 锁释放时机
 *
 * @author 李治毅
 */
public enum UnLockOpportunityEnum {
    /**
     * 正常解锁
     */
    NORMAL,
    /**
     * 事务完成前释放
     */
    BEFORE_COMPLETION,

    /**
     * 事务完成后释放
     */
    AFTER_COMPLETION,



}
