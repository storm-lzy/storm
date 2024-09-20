package com.storm.boot3.lock;

import com.storm.boot3.enums.OrderStatusEnum;

import java.util.concurrent.CompletableFuture;

/**
 * @author 李治毅
 * @date 2024/8/3
 */
public class Demno1 {

    public static void main(String[] args) {
        String string = OrderStatusEnum.STATUS_DBJ.toString();
        System.err.println(
                string
        );
    }
}
