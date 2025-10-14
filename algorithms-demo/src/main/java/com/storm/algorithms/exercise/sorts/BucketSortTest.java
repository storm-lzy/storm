package com.storm.algorithms.exercise.sorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class BucketSortTest {

    public static void main(String[] args) {
        int[] arr = {8,1,9,6,3,8,2,1};

        sort(arr,5);

        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr,int bucketCount) {

        int max = getMax(arr);
        int min = getMin(arr);

        int range = (int) Math.ceil((double) (max - min) / bucketCount);



        List<Integer>[] bucketArr = new ArrayList[bucketCount];


        for (int i = 0; i < bucketCount; i++) {
            bucketArr[i] = new ArrayList<>();
        }

        for (int j : arr) {
            int bucketIndex = (j - min) / range;

            bucketArr[bucketIndex].add(j);
        }

        int index = 0;
        for (List<Integer> bucket : bucketArr) {

            Collections.sort(bucket);

            for (int num : bucket) {
                arr[index++] = num;
            }
        }





    }

    private static int getMax(int[] arr) {
        int max = 0;
        for (int num : arr) {
            max = Math.max(num, max);
        }
        return max;
    }

    private static int getMin(int[] arr) {
        int min = arr[0];
        for (int num : arr) {
            min = Math.min(num, min);
        }
        return min;
    }


}
