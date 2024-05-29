package com.storm.lock;

import com.storm.lock.provider.LockProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestGlobalLock {

    @Resource
    private LockProvider lockProvider;

    @Test
    public void test1() throws Exception {
        String result = lockProvider.lock("123", () -> "45678910");
        System.out.println(result);

    }

}
