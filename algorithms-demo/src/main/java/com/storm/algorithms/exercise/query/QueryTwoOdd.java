package com.storm.algorithms.exercise.query;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class QueryTwoOdd {

    public static void main(String[] args) {

        int[] arr = {10,4,1,5,5,1,2,10,4,3};

        int xorResult = 0;
        for (int i = 0; i < arr.length; i++) {
            xorResult ^= arr[i];
        }

        System.out.println(xorResult);

        // 2. 找到xorResult中任意一个为1的位
        // 这个位说明两个数字在这一位上的值不同
        int mask = 1;
        while ((xorResult & mask) == 0) {
            mask <<= 1;  // 左移直到找到第一个为1的位
        }

        // 3. 根据mask将数组分为两组并分别计算异或
        int a = 0, b = 0;
        for (int num : arr) {
            // 第mask位为0的数字分到一组
            if ((num & mask) == 0) {
                a ^= num;
            }
            // 第mask位为1的数字分到另一组
            else {
                b ^= num;
            }
        }

        System.out.println(a);
        System.out.println(b);




    }



}
