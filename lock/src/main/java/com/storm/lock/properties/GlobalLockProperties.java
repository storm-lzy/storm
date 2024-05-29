package com.storm.lock.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 全局锁属性
 */
@Data
@ConfigurationProperties(prefix = "storm.lock")
public class GlobalLockProperties {

    /**
     * 地址
     */
    private String address = "redis://localhost:6379";

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private int database = 15;

    /**
     * 编解码器
     */
    private String codec = "org.redisson.codec.JsonJacksonCodec";

    /**
     * 获取锁等待时间
     */
    private long waitTime = 60;

    /**
     * 锁超时释放时间
     */
    private long leaseTime = 60;

    /**
     * 锁前缀
     */
    private String prefix = "storm:global-lock";

    /**
     * 连接池
     */
    private Pool pool = new Pool();

    /**
     * 集群
     */
    private Cluster cluster;

    /**
     * 哨兵
     */
    private Sentinel sentinel;

    /**
     * 主从
     */
    private MasterSlave masterSlave;


    /**
     * 主从
     *
     * @author lizhiyi
     * @date 2024/05/29
     * @since 1.0.0
     */
    @Getter
    @Setter
    public static class MasterSlave {

        /**
         * 主
         */
        private String master;

        /**
         * 节点
         * host:port
         */
        private String[] slaves;

    }


    /**
     * 哨兵
     *
     * @author lizhiyi
     * @date 2024/05/29
     * @since 1.0.0
     */
    @Setter
    @Getter
    public static class Sentinel {

        /**
         * 主
         */
        private String master;

        /**
         * 节点
         * host:port
         */
        private String[] nodes;

    }

    /**
     * 集群
     *
     * @author lizhiyi
     * @date 2024/05/29
     * @since 1.0.0
     */
    @Getter
    @Setter
    public static class Cluster {

        /**
         * 节点
         * host:port
         */
        private String[] node;

    }

    /**
     * 连接池
     *
     * @author lizhiyi
     * @date 2024/05/29
     * @since 1.0.0
     */
    @Getter
    @Setter
    public static class Pool {

        /**
         * 连接池大小
         */
        private int size = 20;
        /**
         * 最小空闲连接数
         */
        private int minIdle = 1;
        /**
         * 连接空闲超时[ms]
         */
        private Integer idleTimeout = 60000;
        /**
         * 连接超时[ms]
         */
        private Integer connectionTimout = 3 * 1000;
        /**
         * 命令等待超时[ms]
         */
        private Integer commandTimeout = 10 * 1000;

    }
}
