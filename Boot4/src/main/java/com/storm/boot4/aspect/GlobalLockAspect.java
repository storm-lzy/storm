package com.storm.boot4.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class GlobalLockAspect implements BeanFactoryAware , ApplicationContextAware {


    @Autowired
    private ApplicationContext applicationContext;

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(globalLock)")
    public Object handleGlobalLock(ProceedingJoinPoint pjp, GlobalLock globalLock) throws Throwable {
        // 获取方法参数
        Object[] args = pjp.getArgs();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        // 创建 SpEL 上下文并设置方法参数
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(pjp.getTarget());
        for (int i = 0; i < method.getParameters().length; i++) {
            context.setVariable(method.getParameters()[i].getName(), args[i]);
        }

        // 设置 ApplicationContext 以支持 @beanName 的解析
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));

        // 解析 SpEL 表达式
        Expression expression = parser.parseExpression(globalLock.key());
        List<String> lockKey = expression.getValue(context, List.class);
        System.err.println("分布式锁 key = " + lockKey);


        return pjp.proceed();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
