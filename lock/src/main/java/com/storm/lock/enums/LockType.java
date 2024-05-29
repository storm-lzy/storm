package com.storm.lock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockType {

    /**
     * 可重入锁
     */
    REENTRANT,

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
