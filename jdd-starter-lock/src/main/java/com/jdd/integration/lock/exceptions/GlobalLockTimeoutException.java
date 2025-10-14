package com.jdd.integration.lock.exceptions;

import com.jdd.integration.lock.enums.ServiceCodeEnum;
import com.jdd.integration.pojo.common.exception.BaseBusinessException;

/**
 * 全局锁超时异常
 *
 * @author 李治毅
 */
public class GlobalLockTimeoutException extends BaseBusinessException {

    public GlobalLockTimeoutException(String message) {
        super(message);
        this.serviceCode = ServiceCodeEnum.LOCK_TIMEOUT_CODE.getValue();
    }

}
