package com.storm.algorithms.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.storm.algorithms.util.IdChecker;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author 李治毅
 * @date 2025/10/14
 */
public class PerformanceTest {

    public static void main(String[] args) {
        // 测试用的ID字符串
        String idStr = "122,352,382,383,307,363,522,345,385,262,292,338,481,116,312,432,537,342,320,290,365,362,325,293,503,474,244,258,291,356,222,118,268,302,354,332,316,322,373,265,263,313,308,378,422,318,284,323,406,353,457,376,384,221,267,368,319,1438,295,303,394,297,315,374,390,355,357,387,334,369,300,301,375,317,257,364,358,427,309,361,326,324,367,311,327,360,471,397,348,314,261,260,279,120,359,321,235,443,335,296";


        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            // 测试存在的ID
            IdChecker.isIdExists(idStr, "122");    // true
            IdChecker.isIdExists(idStr, "1438");   // true
            IdChecker.isIdExists(idStr, "296");    // true

            // 测试不存在的ID
            IdChecker.isIdExists(idStr, "999");    // false
            IdChecker.isIdExists(idStr, "12");     // false（部分匹配）
            IdChecker.isIdExists(idStr, "1223");   // false（扩展匹配）
        }

        System.out.println("执行总时长：" + (System.currentTimeMillis() - start));

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {

            List<String> split = StrUtil.split(idStr, ",");

            // 测试存在的ID
            split.contains("122");    // true
            split.contains("1438");   // true
            split.contains("296");    // true

            // 测试不存在的ID
            split.contains("999");    // false
            split.contains("12");     // false（部分匹配）
            split.contains("1223");   // false（扩展匹配）
        }

        System.out.println("执行总时长：" + (System.currentTimeMillis() - start1));


    }



}
