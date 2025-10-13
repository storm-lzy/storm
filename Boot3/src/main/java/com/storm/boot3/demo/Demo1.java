package com.storm.boot3.demo;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 李治毅
 * @date 2024/8/4
 */
@Service
public class Demo1 extends AbstractDemo{

    public static void main(String[] args) throws InterruptedException, ParseException {

        BigDecimal b = new BigDecimal(1);
        BigDecimal b1 = new BigDecimal("1.00");

        System.err.println(b.compareTo(BigDecimal.ONE));
        System.err.println(b1.compareTo(BigDecimal.ONE));




    }
}


