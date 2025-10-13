package com.storm.boot4;

import com.storm.boot4.demo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

        globalDemo.lockMainOrder(request);
    }


    @Test
    public void Test1() {
        BigDecimal bigDecimal = BigDecimal.valueOf(25);
        BigDecimal bigDecima1l = BigDecimal.valueOf(-5);
        System.err.println(bigDecimal.compareTo(bigDecima1l));


    }


}
