package com.jdd.integration.lock.lock;

import com.jdd.integration.lock.enums.LockType;
import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import com.jdd.integration.lock.provider.config.LockConfig;
import lombok.Data;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author 李治毅
 */
@Data
public class LockInfo {

    /**
     * 类型
     */
    private LockType lockType;

    /**
     * 名称
     */
    private String name;

    /**
     * 多锁
     */
    private String[] names;

    /**
     * 等待时间
     */
    private int waitTime;

    /**
     * 租赁时间
     */
    private int leaseTime;

    /**
     * 解锁时机
     */
    private UnLockOpportunityEnum unLockOpportunityEnum;

    /**
     * 抢占锁超时异常文案
     */
    private String errorMsg;


    public LockInfo(LockType lockType, String name, int waitTime, int leaseTime) {
        this.lockType = lockType;
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public LockInfo(LockType lockType, String[] names, int waitTime, int leaseTime) {
        this.lockType = lockType;
        this.names = names;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public LockInfo() {
    }

    public boolean shouldReleaseAfterTransaction() {
        return !UnLockOpportunityEnum.NORMAL.equals(this.getUnLockOpportunityEnum());
    }
}
