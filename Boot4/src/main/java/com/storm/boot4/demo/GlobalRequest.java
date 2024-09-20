package com.storm.boot4.demo;

import lombok.Data;

import java.util.List;

/**
 * @author 李治毅
 * @date 2024/9/2
 */
@Data
public class GlobalRequest {

    private String name;

    private String orderNo;

    private List<String> orderNoList;

    private Order order;

    private List<Order> orderList;

    @Data
    public static class Order{

        private String orderNo;
    }
}
