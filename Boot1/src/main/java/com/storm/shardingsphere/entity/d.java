package com.storm.shardingsphere.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 李治毅
 * @date 2025/9/26
 */
public class d {

    public static void main(String[] args) {
        int[] source = {6, 9, 2, 8, 0};

        int[] result = twoNumbersSum(source, 10);

        System.out.println("满足条件的两个数的索引分别是：" + result[0] + " 和 " + result[1]);
    }
    /**
     * 给定数组和目标值，返回数组内任意两数之和等于目标值的索引
     *
     * @param source:
     * @param target:
     */
    public static int[] twoNumbersSum(int[] source, int target) {
        
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < source.length; i++) {

            int diff = target - source[i];

            if(map.containsKey(diff)){
                return new int[]{map.get(diff), i};
            }

            map.put(source[i], i);
        }

        throw new IllegalArgumentException("No solution exists");
    }
}
