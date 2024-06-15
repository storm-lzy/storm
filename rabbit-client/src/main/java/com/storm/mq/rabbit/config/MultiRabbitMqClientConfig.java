package com.storm.mq.rabbit.config;

import com.storm.mq.config.ClientConfig;
import lombok.Data;

import java.util.HashMap;

/**
 *
 */
@Data
public class MultiRabbitMqClientConfig implements ClientConfig {

    private HashMap<String,RabbitMqClientConfig> client = new HashMap<>();


}
