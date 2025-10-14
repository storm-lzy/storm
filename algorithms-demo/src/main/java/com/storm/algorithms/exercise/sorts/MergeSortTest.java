package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class MergeSortTest {

    public static void main(String[] args) {
        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        sort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + ((right - left) >> 1);

        sort(arr, left, mid);
        sort(arr, mid + 1, right);

        merge(arr, left, mid, right);

    }


    public static void merge(int[] arr, int left, int mid, int right) {

        int[] temArr = new int[right - left + 1];

        int l = left;
        int r = mid + 1;
        int i = 0;

        while (l <= mid && r <= right){
            if(arr[l] >= arr[r]){
                temArr[i++] = arr[r++];
            }else {
                temArr[i++] = arr[l++];
            }

        }

        while (l <= mid){
            temArr[i++] = arr[l++];
        }

        while(r <= right){
            temArr[i++] = arr[r++];
        }

        System.arraycopy(temArr, 0, arr, left, temArr.length);
    }


}
