package com.storm.algorithms.exercise.query;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class QueryOneOdd {

    public static void main(String[] args) {

        int[] arr = {10,4,1,5,5,1,2,10,4};

        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            res ^= arr[i];
        }
        System.out.println(res);
    }




}
