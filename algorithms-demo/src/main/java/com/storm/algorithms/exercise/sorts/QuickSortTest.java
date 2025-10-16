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
        if (left >= right) {
            return;
        }

        int pivotIndex = partition(arr,left,right);

        quickV1(arr,left,pivotIndex - 1);
        quickV1(arr,pivotIndex + 1,right);

    }


    public static int partition(int[] arr,int left,int right){

        int pivot = arr[right];
        int i = left;

        for (int j = left; j < right; j++) {
            if(arr[j] < pivot){
                swap(arr,i++,j);
            }
        }

        swap(arr,i,right);
        return i;

    }

    public static void swap(int[] arr, int source, int target) {
        int tem = arr[source];
        arr[source] = arr[target];
        arr[target] = tem;
    }


}
