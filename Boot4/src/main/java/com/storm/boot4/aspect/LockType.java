package com.storm.boot4.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁类型
 *
 * @author 李治毅
 */
@Getter
@AllArgsConstructor
public enum LockType {

    /**
     * 可重入锁
     */
    REENTRANT,

    /**
     * 多锁
     */
    MULTIPLE,

    /**
     * 公平锁
     */
    FAIR,

    /**
     * 读锁
     */
    READ,

    /**
     * 写锁
     */
    WRITE,

}
