package com.storm.mq.config;

import com.storm.mq.codec.Codec;
import com.storm.mq.codec.enums.CodecType;
import com.storm.mq.rabbit.config.RabbitMqClientConfig;
import com.storm.mq.store.MessageRepository;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 */
@Data
public class AbstractClientConfig<T extends ConsumerConfig> implements ClientConfig{

    private String host = "localhost";
    private Integer port = 5672;
    private String codecId = CodecType.JSON.name();
    private String username = "guest";
    private String password = "guest";
    private boolean supportDelayed = false;
    private ConnectionConfig connection = new ConnectionConfig();
    private ThreadConfig ioThread = new ThreadConfig(3,-1);
    private ThreadConfig producerThread = new ThreadConfig(3,-1);
    private ThreadConfig consumerThread = new ThreadConfig(3,-1);
    private @NotNull RabbitMqClientConfig.RabbitConsumerConfig consumer;
    private String repositoryId;
    private @NotNull Codec codec;
    private @NotEmpty List<Codec> allSupportCodecs;
    private MessageRepository messageRepository;


}
