package com.storm.boot3.run;

import com.storm.boot3.context.HandleContext;
import com.storm.boot3.enums.HandleEnum;
import com.storm.boot3.handle.AbstractHandle;
import com.storm.boot3.handle.impl.Demo1Handle;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author 李治毅
 * @date 2024/7/19
 */
@Component
@ConditionalOnBean(value = AbstractHandle.class)
public class RunDemo1 implements ApplicationRunner {

    @Resource
    HandleContext handleContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        AbstractHandle handle = handleContext.getHandle(HandleEnum.DEMO2);
//        handle.handleMessage();
        System.err.println("Hello World!");
    }
}
