package com.storm.boot3.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author 李治毅
 * @date 2024/8/19
 */
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {

        LocalDateTime beforeTime = LocalDateTime.now().minusDays(93);
        System.err.println(beforeTime);

    }
}
