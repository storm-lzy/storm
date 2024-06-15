package com.storm.mq.enums;

public enum MessageType {

    RABBIT("RABBIT", "RABBIT_MQ");

    private final String code;
    private final String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    private MessageType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
