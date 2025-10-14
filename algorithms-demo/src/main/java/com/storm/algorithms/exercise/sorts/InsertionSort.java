package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class InsertionSort {

    public static void main(String[] args) {
        int[] arr = {8,1,9,6,3,8,2,1};

        sort(arr);

        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr) {

        for (int i = 1; i < arr.length; i++) {

            int key = arr[i];

            int j = i - 1;

            while (j >= 0 && arr[j] > key){
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;


        }

    }


    public static void swap(int[] arr,int source,int target){
        int temp = arr[source];
        arr[source] = arr[target];
        arr[target] = temp;
    }

}
