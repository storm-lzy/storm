package com.storm.boot3.demo;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author 李治毅
 * @date 2024/8/4
 */
@Service
public class Demo1 extends AbstractDemo{

    public static void main(String[] args) throws InterruptedException, ParseException {

        LocalDateTime parse = LocalDateTime.parse("2024-09-12T12:05:41");

        LocalDateTime parse1 = LocalDateTime.parse("2024-09-11T14:55:21");

        System.err.println(LocalDateTimeUtil.between(parse1, parse, ChronoUnit.SECONDS));

    }
}


