package com.storm.mq.exception;

import cn.hutool.core.util.StrUtil;

/**
 *
 */
public class SubscribeException extends MqException{
    public SubscribeException(Class<?> clazz, String message) {
        super(StrUtil.format("[MQ][订阅失败] 当前订阅类：[{}], 原因: [{}]", clazz, message));
    }
}
