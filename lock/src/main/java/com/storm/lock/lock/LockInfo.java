package com.storm.lock.lock;

import com.storm.lock.enums.LockType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 锁信息
 */
@Data
@AllArgsConstructor
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
     * 等待时间
     */
    private long waitTime;

    /**
     * 租赁时间
     */
    private long leaseTime;



}
