package com.jdd.integration.lock.aspect;

import cn.hutool.core.util.StrUtil;
import com.jdd.integration.lock.annotation.GlobalLock;
import com.jdd.integration.lock.enums.LockType;
import com.jdd.integration.lock.exceptions.GlobalLockInvocationException;
import com.jdd.integration.lock.lock.Lock;
import com.jdd.integration.lock.lock.LockFactory;
import com.jdd.integration.lock.lock.LockInfo;
import com.jdd.integration.lock.provider.LockInfoProvider;
import com.jdd.integration.lock.provider.TransactionProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * 声明式锁切面
 *
 * @author 李治毅
 */
@Aspect
@Slf4j
public class GlobalLockAspect implements ApplicationContextAware {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private BeanFactoryResolver beanFactoryResolver;

    @Around("@annotation(globalLock)")
    public Object around(ProceedingJoinPoint joinPoint, GlobalLock globalLock) throws Throwable {

        StandardEvaluationContext beanFactoryEvaluationContext = createBeanFactoryEvaluationContext(joinPoint);

        LockInfo lockInfo = buildLockInfo(globalLock, beanFactoryEvaluationContext);

        Lock lock = LockFactory.getLock(lockInfo);

        if (!lock.acquire()) {
            return executeTimeoutStrategy(globalLock, lockInfo, beanFactoryEvaluationContext);
        }
        log.debug("[Global-Lock][ACQUIRE] 获取到锁: [{}], 执行业务逻辑", lockInfo);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } finally {
            this.releaseLock(lock, lockInfo);
        }
        return proceed;
    }


    private Object executeTimeoutStrategy(GlobalLock globalLock, LockInfo lockInfo, StandardEvaluationContext beanFactoryEvaluationContext) {
        if (StrUtil.isNotBlank(globalLock.customTimeoutStrategy())) {
            Expression expression = PARSER.parseExpression(globalLock.customTimeoutStrategy());
            return expression.getValue(beanFactoryEvaluationContext);
        } else {
            globalLock.lockTimeoutStrategy().handler(lockInfo);
        }
        return null;
    }

    private void releaseLock(Lock lock, LockInfo lockInfo) {
        log.debug("[Global-Lock][RELEASE] 释放锁: [{}]", lockInfo);
        if (lockInfo.shouldReleaseAfterTransaction() && TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(TransactionProvider.buildTransactionSynchronization(lockInfo.getUnLockOpportunityEnum(), lock));
        } else {
            lock.release();
        }

    }


    private LockInfo buildLockInfo(GlobalLock globalLock, StandardEvaluationContext context) {
        try {
            // 解析 SpEL 表达式
            if (globalLock.lockType().equals(LockType.MULTIPLE)) {
                return LockInfoProvider.getMultipleLockInfo(globalLock.name(), PARSER.parseExpression(globalLock.key()).getValue(context, List.class), globalLock.waitTime(), globalLock.leaseTime(), globalLock.unLockOpportunity(),globalLock.errorMsg());
            } else {
                Expression expression = null;
                if(globalLock.key().contains("#") || globalLock.key().contains("@")){
                    expression = PARSER.parseExpression(globalLock.key());
                }
                return LockInfoProvider.getLockInfo(globalLock.lockType(), globalLock.name(), null == expression ? globalLock.key() : expression.getValue(context, String.class), globalLock.waitTime(), globalLock.leaseTime(), globalLock.unLockOpportunity(),globalLock.errorMsg());
            }
        } catch (SpelEvaluationException e) {
            log.error("[Global-Lock][SPEL] 参数解析失败,[{}]未找到", globalLock.key(), e);
            throw new GlobalLockInvocationException("锁业务key未找到");
        }
    }


    private StandardEvaluationContext createBeanFactoryEvaluationContext(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 创建 SpEL 上下文并设置方法参数
        StandardEvaluationContext context = new StandardEvaluationContext(joinPoint.getTarget());
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(), args[i]);
        }
        context.setBeanResolver(beanFactoryResolver);
        return context;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化 BeanFactoryResolver
        this.beanFactoryResolver = new BeanFactoryResolver(applicationContext);
    }
}
