package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/14
 */
public class QuickSortTest {

    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 3, 7, 6, 1, 8};

        quickV1(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }


    public static void quickV1(int[] arr, int left, int right) {
        if (left < 0 || right > arr.length - 1) {
            return;
        }

        int num = arr[right];
        int cur = left;

        while (cur <= right) {
            if (arr[cur] < num) {
                swap(arr, cur, left++);
                cur++;
            } else if (arr[cur] > num) {
                swap(arr, cur, right--);
                cur++;
            } else {
                cur++;
            }
        }
        System.out.println("--");

        quickV1(arr,0,left - 1);
        quickV1(arr,left + 1,right);

        Arrays.sort(arr);

    }

    public static void swap(int[] arr, int source, int target) {
        int tem = arr[source];
        arr[source] = arr[target];
        arr[target] = tem;
    }


}
