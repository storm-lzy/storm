package com.storm.lock.provider;

import com.storm.lock.annotation.GlobalLock;
import com.storm.lock.annotation.LockKey;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LockKeyProvider {


    private final ParameterNameDiscoverer defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser spelExpressionParser = new SpelExpressionParser();

    /**
     * 获取键值名称
     *
     * @param joinPoint  连接点
     * @param globalLock 全局锁
     * @return {@link String}
     */
    public String getKeyName(JoinPoint joinPoint, GlobalLock globalLock) {
        Method method = this.getMethod(joinPoint);

        List<String> definitionKeys = this.getDefinitionKeys(globalLock.keys(), method, joinPoint.getArgs());
        List<String> keyList = new ArrayList<>(definitionKeys);

        List<String> parameterKeys = this.getParameterKeys(method.getParameters(), joinPoint.getArgs());
        keyList.addAll(parameterKeys);

        return StringUtils.collectionToDelimitedString(keyList, "", "-", "");


    }

    /**
     * 获取方法
     *
     * @param joinPoint 连接点
     * @return {@link Method}
     */
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    /**
     * 获取定义键值
     *
     * @param definitionKeys  定义键
     * @param method          方法
     * @param parameterValues 参数值
     * @return {@link List}<{@link String}>
     */
    private List<String> getDefinitionKeys(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();

        for (String definitionKey : definitionKeys) {
            if (!ObjectUtils.isEmpty(definitionKey)) {
                EvaluationContext context =
                        new MethodBasedEvaluationContext(null, method, parameterValues, defaultParameterNameDiscoverer);

                Object objKey = spelExpressionParser.parseExpression(definitionKey).getValue(context);
                definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
            }
        }

        return definitionKeyList;
    }


    /**
     * 获取参数键值
     *
     * @param parameters      参数
     * @param parameterValues 参数值
     * @return {@link List}<{@link String}>
     */
    private List<String> getParameterKeys(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKeys = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(LockKey.class) != null) {
                LockKey keyAnnotation = parameters[i].getAnnotation(LockKey.class);

                if (keyAnnotation.value().isEmpty()) {
                    Object parameterValue = parameterValues[i];
                    parameterKeys.add(ObjectUtils.nullSafeToString(parameterValue));
                } else {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    Object key = spelExpressionParser.parseExpression(keyAnnotation.value()).getValue(context);
                    parameterKeys.add(ObjectUtils.nullSafeToString(key));
                }
            }
        }

        return parameterKeys;
    }


}
