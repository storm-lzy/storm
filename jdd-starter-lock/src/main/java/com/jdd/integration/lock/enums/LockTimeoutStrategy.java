package com.jdd.integration.lock.enums;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jdd.integration.lock.exceptions.GlobalLockTimeoutException;
import com.jdd.integration.lock.handler.LockTimeoutHandler;
import com.jdd.integration.lock.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RMap;

/**
 * 加锁超时策略
 *
 * @author 李治毅
 */
@Slf4j
public enum LockTimeoutStrategy implements LockTimeoutHandler {


    /**
     * 空
     */
    NONE() {
        @Override
        public void handler(LockInfo lockInfo) {
            log.error("[Global-Lock][EXCEPTION] 获取锁[{}]超时", lockInfo);
        }
    },

    /**
     * 快速失败
     */

    FAST_FAIL() {
        @Override
        public void handler(LockInfo lockInfo) {
            log.error("[Global-Lock][EXCEPTION] 获取锁[{}]超时", lockInfo);
            String errorMsg = lockInfo.getErrorMsg();
            if(StrUtil.isBlank(errorMsg)){
                errorMsg = "获取资源失败,请重试";
            }
            throw new GlobalLockTimeoutException(errorMsg);
        }
    },

    /**
     * 锁占用情况
     */
    FAST_FAIL_AND_LOG() {


        private Redisson redisson;

        @Override
        public void handler(LockInfo lockInfo) {
            log.error("[Global-Lock][EXCEPTION] 获取锁[{}]超时", lockInfo);
            printLockThreadInfo(lockInfo);
            String errorMsg = lockInfo.getErrorMsg();
            if(StrUtil.isBlank(errorMsg)){
                errorMsg = "获取资源失败,请重试";
            }
            throw new GlobalLockTimeoutException(errorMsg);
        }

        private void printLockThreadInfo(LockInfo lockInfo) {
            if (null == redisson) {
                redisson = SpringUtil.getBean(Redisson.class);
            }
            if (LockType.MULTIPLE.equals(lockInfo.getLockType())) {
                for (String name : lockInfo.getNames()) {
                    printLockThreadInfo(name);
                }
            } else {
                printLockThreadInfo(lockInfo.getName());
            }
        }

        private void printLockThreadInfo(String name) {
            RMap<Object, Object> map = redisson.getMap(name);
            if (CollectionUtil.isEmpty(map)) {
                return;
            }
            log.info("[Global-Lock][EXCEPTION] 当前线程[{}]", Thread.currentThread().getId());
            map.forEach((key, value) -> log.info("[Global-Lock][EXCEPTION] 持锁线程 key:[{}] value:[{}]", key, value));
        }
    },
}
