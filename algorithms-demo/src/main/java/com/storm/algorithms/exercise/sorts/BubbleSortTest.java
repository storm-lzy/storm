package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class BubbleSortTest {

    public static void main(String[] args) {
        int[] arr = {8,1,9,6,3,8,2,1};

        sort(arr);

        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr) {

        // 最大的放到最后一个

        for (int i = 0; i < arr.length; i++) {

            for (int i1 = 0; i1 < arr.length - i - 1; i1++) {

                if (arr[i1] > arr[i1 + 1]) {
                    swap(arr,i1,i1 + 1);
                }

            }
        }

    }


    public static void swap(int[] arr,int source,int target){
        int temp = arr[source];
        arr[source] = arr[target];
        arr[target] = temp;
    }


}
