package com.storm.mq.enums;

/**
 *
 */
public enum MessageStatus {

    INIT("INIT", "初始化状态"),
    PROCESSING("PROCESSING", "处理中状态"),
    DONE("DONE", "处理完成状态");

    private final String code;
    private final String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    private MessageStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
