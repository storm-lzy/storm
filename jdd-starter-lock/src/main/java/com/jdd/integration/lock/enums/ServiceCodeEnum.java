package com.jdd.integration.lock.enums;

public enum ServiceCodeEnum {

    LOCK_TIMEOUT_CODE("103700000", "全局锁加锁异常"),
    LOCK_BIZ_CODE("103700001", "全局锁业务异常");

    private String value;
    private String msg;

    public String getValue() {
        return this.value;
    }

    public String getMsg() {
        return this.msg;
    }

    private ServiceCodeEnum(final String value, final String msg) {
        this.value = value;
        this.msg = msg;
    }
}
