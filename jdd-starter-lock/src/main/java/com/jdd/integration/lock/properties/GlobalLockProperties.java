package com.jdd.integration.lock.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 李治毅
 * @date 2024/9/14
 */
@Data
@ConfigurationProperties(prefix = "global.lock")
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
    private Integer database;

    /**
     * 编解码器 org.redisson.codec.JsonJacksonCodec
     */
    private String codec;

    /**
     * 等待时间，单位秒 默认5秒
     * 注意：
     *      指定-1: 无限等待直到获取到锁
     *      指定 0: 不会等待，只抢一次锁，成功则成功，失败则立刻退出
     *      指定>0: 指定时间内等待，超时则失败
     */
    private int waitTime = 10;

    /**
     * 租赁时间, 单位秒 默认0
     * 注意：
     *      指定<=0：默认租赁30秒，如果30秒内未执行结束，会不断每10秒自动续期
     *      指定 >0：在指定时间内无论逻辑是否执行结束，不会自动续期
     */
    private int leaseTime = 10;

    /**
     * NioEventLoopGroup工作线程数,默认使用CPU核心数*2
     */
    private int nioThreadNum = 0;

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

    @Getter
    @Setter
    public static class Cluster {

        /**
         * 节点
         * host:port
         */
        private String[] node;

    }



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
        private int minIdle = 0;
        /**
         * 连接空闲超时[ms]
         */
        private Integer idleTimeout = 1000;
        /**
         * 连接超时[ms]
         */
        private Integer connectionTimout = 5 * 1000;
        /**
         * 命令等待超时[ms]
         */
        private Integer commandTimeout = 10 * 1000;

    }
}
