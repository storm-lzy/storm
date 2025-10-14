package com.jdd.integration.lock.configuration;

import cn.hutool.core.util.StrUtil;
import com.jdd.integration.lock.aspect.GlobalLockAspect;
import com.jdd.integration.lock.properties.GlobalLockProperties;
import com.jdd.integration.lock.provider.LockInfoProvider;
import com.jdd.integration.lock.provider.LockMultipleProvider;
import com.jdd.integration.lock.provider.LockProvider;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;

/**
 * @author 李治毅
 */
@Configuration
@EnableConfigurationProperties(GlobalLockProperties.class)
@Import(GlobalLockAspect.class)
public class GlobalLockAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(GlobalLockAutoConfiguration.class);
    @Resource
    private GlobalLockProperties globalLockProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass({"com.jdd.integration.redis.configuration.RedissonConfig"})
    public Redisson redisson() throws Exception {
        log.info("加载新版本 jdd-lock-starter...");

        final Config config;
        // 集群 > 哨兵 > 主从 > 单点
        if (globalLockProperties.getCluster() != null) {
            config = getClusterServersConfig(globalLockProperties);
        } else if (globalLockProperties.getSentinel() != null) {
            config = getSentinelServersConfig(globalLockProperties);
        } else if (globalLockProperties.getMasterSlave() != null) {
            config = getMasterSlaveServersConfig(globalLockProperties);
        } else {
            config = getSingleServerConfig(globalLockProperties);
        }
        if (StrUtil.isNotBlank(globalLockProperties.getCodec())) {
            Codec codec = (Codec) ClassUtils.forName(globalLockProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
            config.setCodec(codec);
        }
        config.setEventLoopGroup(new NioEventLoopGroup(globalLockProperties.getNioThreadNum()));
        return (Redisson) Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public LockProvider lockProvider() {
        return new LockProvider();
    }

    @Bean
    public LockMultipleProvider multipleLockProvider() {
        return new LockMultipleProvider();
    }

    /**
     * 获取集群服务器配置
     *
     * @param globalLockProperties 全局锁属性
     * @return {@link Config}
     */
    private static Config getClusterServersConfig(GlobalLockProperties globalLockProperties) {
        Config config = new Config();
        GlobalLockProperties.Pool pool = globalLockProperties.getPool();
        config.useClusterServers()
                .addNodeAddress(globalLockProperties.getCluster().getNode())
                .setPassword(globalLockProperties.getPassword())
                .setMasterConnectionPoolSize(pool.getSize())
                .setMasterConnectionMinimumIdleSize(pool.getMinIdle())
                .setSlaveConnectionPoolSize(pool.getSize())
                .setSlaveConnectionMinimumIdleSize(pool.getMinIdle())
                .setIdleConnectionTimeout(pool.getIdleTimeout())
                .setConnectTimeout(pool.getConnectionTimout())
                .setTimeout(pool.getCommandTimeout());

        return config;
    }

    /**
     * 获取哨兵服务器配置
     *
     * @param globalLockProperties 全局锁属性
     * @return {@link SentinelServersConfig}
     */
    private static Config getSentinelServersConfig(GlobalLockProperties globalLockProperties) {
        Config config = new Config();

        GlobalLockProperties.Pool pool = globalLockProperties.getPool();
        config.useSentinelServers()
                .setMasterName(globalLockProperties.getSentinel().getMaster())
                .addSentinelAddress(globalLockProperties.getSentinel().getNodes())
                .setDatabase(globalLockProperties.getDatabase())
                .setPassword(globalLockProperties.getPassword())
                .setMasterConnectionPoolSize(pool.getSize())
                .setMasterConnectionMinimumIdleSize(pool.getMinIdle())
                .setSlaveConnectionPoolSize(pool.getSize())
                .setSlaveConnectionMinimumIdleSize(pool.getMinIdle())
                .setIdleConnectionTimeout(pool.getIdleTimeout())
                .setConnectTimeout(pool.getConnectionTimout())
                .setTimeout(pool.getCommandTimeout());

        return config;
    }

    /**
     * 获取主从服务器配置
     *
     * @param globalLockProperties 全局锁属性
     * @return {@link MasterSlaveServersConfig}
     */
    private static Config getMasterSlaveServersConfig(GlobalLockProperties globalLockProperties) {
        Config config = new Config();

        GlobalLockProperties.Pool pool = globalLockProperties.getPool();
        config.useMasterSlaveServers()
                .setMasterAddress(globalLockProperties.getMasterSlave().getMaster())
                .addSlaveAddress(globalLockProperties.getMasterSlave().getSlaves())
                .setPassword(globalLockProperties.getPassword())
                .setDatabase(globalLockProperties.getDatabase())
                .setMasterConnectionPoolSize(pool.getSize())
                .setMasterConnectionMinimumIdleSize(pool.getMinIdle())
                .setSlaveConnectionPoolSize(pool.getSize())
                .setSlaveConnectionMinimumIdleSize(pool.getMinIdle())
                .setIdleConnectionTimeout(pool.getIdleTimeout())
                .setConnectTimeout(pool.getConnectionTimout())
                .setTimeout(pool.getCommandTimeout());

        return config;
    }

    /**
     * 获取单机服务器配置
     *
     * @param globalLockProperties 全局锁属性
     * @return {@link Config}
     */
    private static Config getSingleServerConfig(GlobalLockProperties globalLockProperties) {
        Config config = new Config();
        GlobalLockProperties.Pool pool = globalLockProperties.getPool();
        config.useSingleServer()
                .setAddress(globalLockProperties.getAddress())
                .setDatabase(globalLockProperties.getDatabase())
                .setPassword(globalLockProperties.getPassword())
                .setConnectTimeout(pool.getConnectionTimout())
                .setConnectionMinimumIdleSize(pool.getMinIdle())
                .setConnectionPoolSize(pool.getSize())
                .setTimeout(pool.getCommandTimeout())
                .setIdleConnectionTimeout(pool.getIdleTimeout());

        return config;
    }

}
