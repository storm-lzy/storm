package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class SelectionSortTest {

    public static void main(String[] args) {
        int[] arr = {8,1,9,6,3,8,2,1};

        sort(arr);

        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int i1 = i + 1; i1 < arr.length; i1++) {
                if(arr[minIndex] > arr[i1]){
                    minIndex = i1;
                }
            }

            if(minIndex != i){
                swap(arr,minIndex,i);
            }

        }


    }


    public static void swap(int[] arr,int source,int target){
        int temp = arr[source];
        arr[source] = arr[target];
        arr[target] = temp;
    }
}
