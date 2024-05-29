package com.example.stormspring.aspect;

import com.example.stormspring.annotation.DoubleCache;
import com.example.stormspring.enums.CacheType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Aspect
@Component
@Slf4j
public class DoubleCacheAspect {

    @Resource
    private Cache<Object,Object> cache;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Pointcut("@annotation(com.example.stormspring.annotation.DoubleCache)")
    public void cacheAspect(){

    }

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        // 获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        String[] parameterNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            treeMap.put(parameterNames[i],args[i]);
        }
        DoubleCache annotation = method.getAnnotation(DoubleCache.class);
        String elResult = ElParser.parse(annotation.key(), treeMap);
        String realKey = annotation.cacheName() + ":" + elResult;

        CacheType cacheType = annotation.type();

        if(CacheType.PUT.equals(cacheType)){
            log.info("强制更新");
            Object proceed = point.proceed(args);
            log.info("更新 redis & caffeine");
            redisTemplate.opsForValue().set(realKey,proceed,annotation.TimeOut(), TimeUnit.SECONDS);
            cache.put(realKey,proceed);
            return proceed;
        }else if(CacheType.DELETE.equals(cacheType)){
            log.info("删除缓存");
            log.info("删除 redis & caffeine");
            redisTemplate.delete(realKey);
            cache.invalidate(realKey);
            return point.proceed(args);
        } else if (CacheType.FULL.equals(cacheType)) {
            log.info("读写缓存");
            Object object = cache.getIfPresent(realKey);
            if(null != object){
                log.info("来自 caffeine");
                return object;
            }
            object = redisTemplate.opsForValue().get(realKey);
            if(null != object){
                log.info("来自 redis");
                log.info("放入 caffeine");
                cache.put(realKey,object);
                return object;
            }
            object = point.proceed(args);
            if(null != object){
                log.info("放入 caffeine & redis");
                cache.put(realKey,object);
                redisTemplate.opsForValue().set(realKey,object,annotation.TimeOut(), TimeUnit.SECONDS);
            }
            return object;
        }
        return point.proceed();
    }


}
