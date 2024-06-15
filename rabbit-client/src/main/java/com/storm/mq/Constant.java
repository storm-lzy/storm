package com.storm.mq;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Constant {
    String APPLICATION_NAME_PROP = "spring.application.name";
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    Charset NET_TRANSFER_CHARSET = StandardCharsets.UTF_8;
    String RABBIT_MQ_CLIENT_CONFIG_PROP_PREFIX = "spring.mq.rabbit";
    String SYSTEM_CLUSTER_SEQ_PROP = "sys.clusterSeq";
    String CLIENT_NAME_SUFFIX = "MqClient";
    String DEFAULT_CLIENT_NAME = "defaultMqClient";
    int DEFAULT_RETRY_ON_FAIL = 3;
    String DELAY_SUFFIX = "_delay";
    String DELAY_HEADER = "x-delay";
    String DELAY_MESSAGE_HEADER = "x-delayed-message";
    String DELAY_TYPE_HEADER = "x-delayed-type";
    String MAX_QUEUE_LENGTH = "x-max-length";
    String MAX_QUEUE_LENGTH_BYTES = "x-max-length-bytes";
    String QUEUE_NAME_PREFIX = "dindo_";
    String SPRING_AMQP_CONTENT_TYPE = "application/json";
}
