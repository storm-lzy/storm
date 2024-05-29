package com.storm.lock.configuration;

import com.storm.lock.aspect.GlobalLockAspectHandler;
import com.storm.lock.lock.LockFactory;
import com.storm.lock.properties.GlobalLockProperties;
import com.storm.lock.provider.LockInfoProvider;
import com.storm.lock.provider.LockKeyProvider;
import com.storm.lock.provider.LockProvider;
import com.storm.lock.provider.impl.LockProviderImpl;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;

/**
 *
 */
@Configuration
@EnableConfigurationProperties(GlobalLockProperties.class)
@Import(value = GlobalLockAspectHandler.class)
public class GlobalLockAutoConfiguration {

    @Resource
    private GlobalLockProperties globalLockProperties;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() throws Exception {

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
        Codec codec = (Codec) ClassUtils.forName(globalLockProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }


    /**
     * 锁工厂
     *
     */
    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }

    /**
     * 锁信息提供者
     *
     * @param globalLockProperties 全局锁属性
     */
    @Bean
    public LockInfoProvider lockInfoProvider(GlobalLockProperties globalLockProperties) {
        return new LockInfoProvider(globalLockProperties);
    }

    /**
     * 锁定键提供者
     *
     */
    @Bean
    public LockKeyProvider lockKeyProvider() {
        return new LockKeyProvider();
    }

    /**
     * 分布式锁提供者
     */
    @Bean
    public LockProvider lockProvider() {
        return new LockProviderImpl(lockFactory());
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
     * @param globalLockProperties 全局锁属性0-
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
