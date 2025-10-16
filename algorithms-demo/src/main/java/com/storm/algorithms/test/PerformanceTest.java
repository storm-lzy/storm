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

        int[] arr = {8,1,9,6,3,8,2,1};


        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            swap(arr,0,1);
        }

        System.out.println("执行总时长：" + (System.currentTimeMillis() - start));

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            swap2(arr,0,1);
        }

        System.out.println("执行总时长：" + (System.currentTimeMillis() - start1));


    }



    public static void swap2(int[] arr,int source,int target){
        arr[source] = arr[source] ^ arr[target];
        arr[target] = arr[source] ^ arr[target];
        arr[source] = arr[source] ^ arr[target];
    }


    public static void swap(int[] arr,int source,int target){
        int temp = arr[source];
        arr[source] = arr[target];
        arr[target] = temp;
    }



}
