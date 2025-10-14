package com.jdd.integration.lock.provider.config;

import com.jdd.integration.lock.enums.LockType;
import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import com.jdd.integration.lock.handler.LockTimeoutHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 李治毅
 * @date 2024/12/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LockConfig {

    /*
     * 以下参数为空时走默认参数
     */

    /**
     * 类型
     */
    private LockType lockType;

    /**
     * 等待时间
     */
    private Integer waitTime;

    /**
     * 租赁时间
     */
    private Integer leaseTime;

    /**
     * 超时策略
     */
    private LockTimeoutHandler lockTimeoutHandler;

    /**
     * 解锁时机
     */
    private UnLockOpportunityEnum unLockOpportunityEnum;

    /**
     * 抢占锁超时异常文案
     */
    private String errorMsg;
}
