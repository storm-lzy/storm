package com.storm.lock.aspect;

import com.storm.lock.annotation.GlobalLock;
import com.storm.lock.exceptions.GlobalLockInvocationException;
import com.storm.lock.lock.Lock;
import com.storm.lock.lock.LockFactory;
import com.storm.lock.lock.LockInfo;
import com.storm.lock.provider.LockInfoProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Aspect
@Slf4j
public class GlobalLockAspectHandler {


    @Resource
    private LockFactory lockFactory;

    @Resource
    private LockInfoProvider lockInfoProvider;

    private final Map<String, LockResult> currentThreadLock = new ConcurrentHashMap<String, LockResult>();


    @Around("@annotation(globalLock)")
    public Object around(ProceedingJoinPoint joinPoint, GlobalLock globalLock) throws Throwable {

        LockInfo lockInfo = lockInfoProvider.get(joinPoint, globalLock);

        String currentLockId = this.getCurrentLockId(lockInfo.getName());
        currentThreadLock.put(currentLockId, new LockResult(lockInfo, false));

        Lock lock = lockFactory.getLock(lockInfo);

        boolean lockResult = lock.acquire();
        if (!lockResult) {
            log.warn("[Global-Lock][EXCEPTION] 获取锁[{}]失败", lockInfo.getName());

            if (!StringUtils.isEmpty(globalLock.customLockTimeoutStrategy())) {
                return this.handleCustomLockTimeout(globalLock.customLockTimeoutStrategy(), joinPoint);
            } else {
                globalLock.lockTimeoutStrategy().handler(lockInfo, lock, joinPoint);
            }
        }

        currentThreadLock.get(currentLockId).setLock(lock);
        currentThreadLock.get(currentLockId).setSuccess(true);
        log.info("[Global-Lock][ACQUIRE] 获取到锁[{}], 开始执行业务逻辑", lockInfo.getName());

        Object proceed = joinPoint.proceed();

        /**释放资源*/
        this.releaseLock(globalLock, joinPoint, currentLockId);
        this.cleanUpThreadLocal(currentLockId);

        return proceed;
    }

    /**
     * afterThrowing
     *
     * @param joinPoint  连接点
     * @param globalLock 全局锁
     * @param throwable  异常
     * @throws Throwable 异常
     */
    @AfterThrowing(value = "@annotation(globalLock)", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, GlobalLock globalLock, Throwable throwable) throws Throwable {
        String currentLockId = this.getCurrentLockId(lockInfoProvider.get(joinPoint, globalLock).getName());

        this.releaseLock(globalLock, joinPoint, currentLockId);
        this.cleanUpThreadLocal(currentLockId);

        throw throwable;
    }

    /**
     * 清理线程本地
     *
     * @param currentLockId 当前锁标识
     */
    private void cleanUpThreadLocal(String currentLockId) {
        // 避免内存泄漏
        currentThreadLock.remove(currentLockId);
    }

    /**
     * 释放锁
     *
     * @param globalLock    全局锁
     * @param joinPoint     连接点
     * @param currentLockId 当前锁标识
     * @throws Throwable 异常
     */
    private void releaseLock(GlobalLock globalLock, JoinPoint joinPoint, String currentLockId) throws Throwable {
        LockResult lockResult = currentThreadLock.get(currentLockId);
        if (Objects.isNull(lockResult)) {
            throw new NullPointerException(String.format("当前线程获取锁和释放锁的键值不同: [%s]", currentLockId));
        }

        if (lockResult.getSuccess()) {
            boolean releaseResult = lockResult.getLock().release();
            // 避免在下面发生异常时释放锁两次
            lockResult.setSuccess(false);

            if (!releaseResult) {
                this.handleReleaseTimeout(globalLock, lockResult.getLockInfo(), joinPoint);
            }

            log.info("[Global-Lock][RELEASE] 释放锁[{}]", currentLockId);
        }
    }

    /**
     * 处理释放锁时超时
     *
     * @param globalLock 全局锁
     * @param lockInfo   锁信息
     * @param joinPoint  连接点
     * @throws Throwable 异常
     */
    private void handleReleaseTimeout(GlobalLock globalLock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {
        log.warn("[Global-Lock][EXCEPTION] 释放锁[{}]失败，锁可能租期结束已自动释放", lockInfo.getName());

        if (!StringUtils.isEmpty(globalLock.customReleaseTimeoutStrategy())) {
            this.handleCustomReleaseTimeout(globalLock.customReleaseTimeoutStrategy(), joinPoint);
        } else {
            globalLock.releaseTimeoutStrategy().handler(lockInfo);
        }
    }

    /**
     * 处理自定义释放锁超时
     *
     * @param releaseTimeoutHandler 释放超时处理程序
     * @param joinPoint             连接点
     * @throws Throwable 异常
     */
    private void handleCustomReleaseTimeout(String releaseTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        final Method handleMethod;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(releaseTimeoutHandler, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("未找到[%s]自定义释放锁超时处理程序", releaseTimeoutHandler), e);
        }

        Object[] args = joinPoint.getArgs();
        try {
            handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new GlobalLockInvocationException(String.format("自定义释放锁超时处理程序[%s]调用异常", releaseTimeoutHandler), e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * 处理自定义加锁超时
     *
     * @param lockTimeoutHandler 加锁超时处理程序
     * @param joinPoint          连接点
     * @return {@link Object}
     * @throws Throwable 异常
     */
    private Object handleCustomLockTimeout(String lockTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Object target = joinPoint.getTarget();
        final Method handleMethod;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(lockTimeoutHandler, currentMethod.getParameterTypes());

            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("未找到[%s]自定义加锁超时处理程序", lockTimeoutHandler), e);
        }

        Object[] args = joinPoint.getArgs();

        Object response;
        try {
            response = handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new GlobalLockInvocationException(String.format("自定义加锁超时处理程序[%s]调用异常", lockTimeoutHandler), e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        return response;
    }

    /**
     * 获取当前线程锁标识
     *
     * @param lockName 锁名称
     * @return {@link String}
     */
    private String getCurrentLockId(String lockName) {
        return Thread.currentThread().getId() + lockName;
    }

    /**
     * 锁结果
     *
     * @author zhangliuyang
     * @date 2022/04/19
     * @since 1.0.0
     */
    @Getter
    @Setter
    private static class LockResult {

        /**
         * 锁
         */
        private Lock lock;

        /**
         * 锁信息
         */
        private LockInfo lockInfo;

        /**
         * 是否成功
         */
        private Boolean success;

        /**
         * 锁结果
         *
         * @param lockInfo 锁信息
         * @param success  成功
         */
        public LockResult(LockInfo lockInfo, boolean success) {
            this.lockInfo = lockInfo;
            this.success = success;
        }
    }
}
