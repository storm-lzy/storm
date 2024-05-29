package com.example.stormspring.annotation;


import com.example.stormspring.enums.CacheType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {

    String cacheName();
    String key();//支持springEl表达式
    long TimeOut() default 15;
    CacheType type() default CacheType.FULL;
}
