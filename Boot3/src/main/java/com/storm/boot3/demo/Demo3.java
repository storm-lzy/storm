package com.storm.boot3.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author 李治毅
 * @date 2024/8/19
 */
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 11; i++) {
            countDownLatch.countDown();
        }

        countDownLatch.await();
    }
}
