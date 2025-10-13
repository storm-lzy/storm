package com.storm.boot3.lock;

import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * @author 李治毅
 * @date 2024/8/3
 */
@Service
public class Demno2 implements ApplicationRunner {

    @Resource
    private DemoInterface demoInterface;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        demoInterface.test1();
    }
}
