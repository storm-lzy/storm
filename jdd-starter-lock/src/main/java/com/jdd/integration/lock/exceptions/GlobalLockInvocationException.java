package com.jdd.integration.lock.exceptions;

import com.jdd.integration.lock.enums.ServiceCodeEnum;
import com.jdd.integration.pojo.common.exception.BaseBusinessException;

/**
 * 全局锁调用异常
 *
 * @author 李治毅
 */
public class GlobalLockInvocationException extends BaseBusinessException {

    public GlobalLockInvocationException(String message) {
        super(message);
        this.serviceCode = ServiceCodeEnum.LOCK_BIZ_CODE.getValue();
    }


}
