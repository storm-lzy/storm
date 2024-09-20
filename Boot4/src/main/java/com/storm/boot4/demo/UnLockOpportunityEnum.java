package com.storm.boot4.demo;

public enum UnLockOpportunityEnum {

    /**
     * 事务正常提交
     */
    NORMAL,

    /**
     * 事务提交前释放
     */
    BEFORE_COMMIT,

    /**
     * 事务提交后释放
     */
    AFTER_COMMIT,

    /**
     * 事务完成前释放
     */
    BEFORE_COMPLETION,

    /**
     * 事务完成后释放
     */
    AFTER_COMPLETION,



}
