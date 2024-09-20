package com.storm.boot3.demo;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 李治毅
 * @date 2024/8/4
 */
@Service
public class Demo2 extends AbstractDemo {


    public static void main(String[] args) throws ParseException {
        List<Integer> repairStatus = Arrays.asList(3, 4);
        boolean contains = repairStatus.contains(1);
        System.err.println(contains);

    }
}
