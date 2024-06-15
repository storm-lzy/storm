package com.storm.mq.configuration.rabbit;

import cn.hutool.core.util.StrUtil;
import com.storm.mq.MqClient;
import com.storm.mq.codec.Codec;
import com.storm.mq.configuration.AbstractMqClientAutoConfiguration;
import com.storm.mq.rabbit.RabbitMqClient;
import com.storm.mq.exception.MqException;
import com.storm.mq.exception.factory.ExceptionProviderFactory;
import com.storm.mq.rabbit.config.MultiRabbitMqClientConfig;
import com.storm.mq.rabbit.config.RabbitMqClientConfig;
import com.storm.mq.store.MessageRepository;
import com.storm.mq.utils.HostInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class RabbitMqClientAutoConfiguration extends AbstractMqClientAutoConfiguration {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        log.info("[MQ] 启用 RabbitClient 自动配置, 开始初始化···");
        BindResult<MultiRabbitMqClientConfig> clientConfigBindResult = Binder.get(environment).bind("spring.mq.rabbit", MultiRabbitMqClientConfig.class);
        if (!clientConfigBindResult.isBound()) {
            log.warn("[MQ-RabbitClient] 当前未配置 RabbitClient 客户端, 结束自动配置");
        } else {
            String clusterSeq = this.parseClusterSeq();
            Codec defaultCodec = super.defaultCodec();
            List<Codec> allSupportCodecs = super.allSupportCodecs();
            clientConfigBindResult.get().getClient().forEach((clientName, clientConfig) -> {
                String codecId = StrUtil.blankToDefault(clientConfig.getCodecId(), defaultCodec.id());
                for (Codec supportCodec : allSupportCodecs) {
                    if (codecId.equals(supportCodec.id())) {
                        clientConfig.setCodec(supportCodec);
                        break;
                    }
                }
                if(clientConfig.getCodec() == null){
                    String allSupportCodecStr = allSupportCodecs.stream().map(Codec::id).collect(Collectors.joining(","));
                    throw ExceptionProviderFactory.UNSUPPORTED_OPERATION_EXCEPTION.newRuntimeException(String.format("[MQ-RabbitClient] 系统不支持当前配置的Codec [%s]。所有支持的Codec [%s]", codecId, allSupportCodecStr));
                }else {
                    clientConfig.setAllSupportCodecs(allSupportCodecs);
                    this.registerMqService(beanDefinitionRegistry,clientName+"MqClient",clusterSeq,clientConfig);
                }
            });
        }

    }

    private void registerMqService(BeanDefinitionRegistry beanDefinitionRegistry, String clientName, String clusterSeq, RabbitMqClientConfig clientConfig) {
        boolean primary = "defaultMqClient".equals(clientName);
        AbstractBeanDefinition abstractBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MqClient.class, () -> {
            if (StrUtil.isNotBlank(clientConfig.getRepositoryId())) {
                MessageRepository messageRepository = this.applicationContext.getBean(clientConfig.getRepositoryId(), MessageRepository.class);
                clientConfig.setMessageRepository(messageRepository);
            }
            MqClient mqClient = new RabbitMqClient(clientConfig, clientName, clusterSeq, false);
            try {
                mqClient.start();
                return mqClient;
            } catch (Exception e) {
                log.error(String.format("%s [MQ-%s][处理异常] 客户端初始化失败",e,clientName));
                throw new MqException(String.format("%s [MQ-%s][处理异常] 客户端初始化失败",e,clientName),e);
            }
        }).setPrimary(primary).getRawBeanDefinition();
        abstractBeanDefinition.setDestroyMethodName("close");
        beanDefinitionRegistry.registerBeanDefinition(clientName,abstractBeanDefinition);
    }

    private String parseClusterSeq() {
        String clusterSeq = environment.getProperty("sys.clusterSeq");
        if (StrUtil.isBlank(clusterSeq)) {
            String regex = "([0-9]+)(\\..*)?$";
            Pattern pattern = Pattern.compile(regex);
            String hostName = HostInfo.getInstance().getHostName();
            Matcher matcher = pattern.matcher(hostName);
            if (matcher.find()) {
                clusterSeq = String.format("%02d", Integer.parseInt(matcher.group(1)));
            }
        }
        return clusterSeq;
    }
}
