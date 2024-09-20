package com.storm.boot4;

import com.storm.boot4.demo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootTest(classes = Boot4Application.class)
public class Boot4ApplicationTests {

    @Resource
    private GlobalDemo globalDemo;

    @Test
    public void contextLoads() {
        GlobalRequest request = new GlobalRequest();

        request.setName("456");
        request.setOrderNo("123");

        globalDemo.lockMainOrder2(request);
    }


    @Test
    public void Test1() {
        GlobalRequest result = LockProvider.lock("order-status", "orderNo", () -> {
            GlobalRequest request = new GlobalRequest();
            request.setName("获取锁成功");
            return request;

        });


        AcquiredFunction<GlobalRequest> execute = new AcquiredFunction<GlobalRequest>() {
            @Override
            public GlobalRequest execute() {
                GlobalRequest request = new GlobalRequest();
                request.setName("获取锁成功");
                return request;
            }
        };

        LockTimeoutHandler handler = new LockTimeoutHandler() {
            @Override
            public void handler() {
                System.err.println("获取锁失败");
            }
        };

        GlobalRequest result1 = LockProvider.lock("order-status", "orderNo", execute,handler);

        AcquiredFunction<Boolean> booleanAcquiredFunction = () -> {
            return false;
        };
        Boolean tryLock = LockProvider.tryLock("", "", booleanAcquiredFunction);

    }


}
