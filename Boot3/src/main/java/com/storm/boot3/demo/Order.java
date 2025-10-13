package com.storm.boot3.demo;

import lombok.Data;

/**
 * @author 李治毅
 * @date 2024/8/23
 */
@Data
public class Order {

    private Long workerId;
    private Integer orderId;

    private String[] arr;

    public Order() {
        arr = new String[10];
        arr[0] = "1";
    }
}
