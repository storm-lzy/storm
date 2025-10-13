package com.storm.boot3.lock;

import com.storm.boot3.enums.OrderStatusEnum;
import org.springframework.aop.framework.AopContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author 李治毅
 * @date 2024/8/3
 */
@Service
public class Demno1  implements DemoInterface{


    public void test1() {
//        Object target = AopProxyUtils.ultimateTargetClass(proxy);
//        System.err.println(object);
    }
}
