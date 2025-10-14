package com.jdd.integration.lock.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jdd.integration.lock.enums.LockTimeoutStrategy;
import com.jdd.integration.lock.enums.LockType;
import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import com.jdd.integration.lock.exceptions.GlobalLockInvocationException;
import com.jdd.integration.lock.lock.LockInfo;
import com.jdd.integration.lock.properties.GlobalLockProperties;
import com.jdd.integration.lock.provider.config.LockConfig;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * @author 李治毅
 */
public class LockInfoProvider implements ApplicationRunner, EnvironmentAware, ApplicationContextAware {

    private Environment environment;

    /**
     * 环境
     */
    private static String keyPrefix;

    /**
     * 环境
     */
    private static GlobalLockProperties globalLockProperties;



    public static LockInfo getLockInfo(LockType lockType, String name, Object key, int waitTime, int leaseTime, UnLockOpportunityEnum unLockOpportunityEnum, String errorMsg) {
        if (StrUtil.isBlank(name)) {
            throw new GlobalLockInvocationException("获取锁失败 锁参数：name 不可为空");
        }
        if (null == key || StrUtil.isBlank(key.toString())) {
            throw new GlobalLockInvocationException("获取锁失败 锁参数：key 不可为空");
        }
        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockType(lockType);
        lockInfo.setName(keyPrefix + name + key);
        lockInfo.setWaitTime(waitTime);
        lockInfo.setLeaseTime(leaseTime);
        lockInfo.setUnLockOpportunityEnum(unLockOpportunityEnum);
        lockInfo.setErrorMsg(errorMsg);
        return lockInfo;
    }

    public static <S> LockInfo getMultipleLockInfo(String name, List<S> keys, int waitTime, int leaseTime, UnLockOpportunityEnum unLockOpportunityEnum, String errorMsg) {
        if (StrUtil.isBlank(name)) {
            throw new GlobalLockInvocationException("获取锁失败 name 不可为空");
        }
        if (ObjectUtil.isEmpty(keys)) {
            throw new GlobalLockInvocationException("获取锁失败 keys 不可为空");
        }
        if (keys.size() > 200) {
            throw new GlobalLockInvocationException("请检查数据是否正常 多锁key不可超出200");
        }
        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockType(LockType.MULTIPLE);
        lockInfo.setNames(buildKeysName(name, keys));
        lockInfo.setWaitTime(waitTime);
        lockInfo.setLeaseTime(leaseTime);
        lockInfo.setUnLockOpportunityEnum(unLockOpportunityEnum);
        lockInfo.setErrorMsg(errorMsg);
        return lockInfo;
    }

    private static <S> String[] buildKeysName(String name, List<S> keys) {
        String[] keysName = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            keysName[i] = keyPrefix + name + keys.get(i);
        }
        Arrays.sort(keysName);
        return keysName;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String profile = environment.getProperty("spring.profiles.active");
        String serverName = environment.getProperty("spring.application.name");
        if (StrUtil.isBlank(profile) || StrUtil.isBlank(serverName)) {
            throw new GlobalLockInvocationException("分布式锁获取锁前缀失败");
        }
        LockInfoProvider.keyPrefix = profile + ":" + serverName + ":";
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LockInfoProvider.globalLockProperties = applicationContext.getBean(GlobalLockProperties.class);
    }

    /**
     * 初始化锁入参
     *
     * @param lockConfig:
     * @author 李治毅
     */
    public static void initLockConfig(LockConfig lockConfig) {
        lockConfig.setLockType(null == lockConfig.getLockType() ? LockType.REENTRANT : lockConfig.getLockType());
        lockConfig.setLockTimeoutHandler(null == lockConfig.getLockTimeoutHandler() ? LockTimeoutStrategy.FAST_FAIL : lockConfig.getLockTimeoutHandler());
        lockConfig.setWaitTime(null == lockConfig.getWaitTime() ? globalLockProperties.getWaitTime() : lockConfig.getWaitTime());
        lockConfig.setLeaseTime(null == lockConfig.getLeaseTime() ? globalLockProperties.getLeaseTime() : lockConfig.getLeaseTime());
        lockConfig.setUnLockOpportunityEnum(null == lockConfig.getUnLockOpportunityEnum() ? UnLockOpportunityEnum.AFTER_COMPLETION : lockConfig.getUnLockOpportunityEnum());
        lockConfig.setErrorMsg(null == lockConfig.getErrorMsg() ? null : lockConfig.getErrorMsg());
    }
}
