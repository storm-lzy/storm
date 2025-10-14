package com.storm.algorithms.sorting;

import java.util.Arrays;

public class ShellSort {
    
    public static void shellSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        int n = arr.length;
        
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }
    
    public static void main(String[] args) {
        int[] arr = {12, 34, 54, 2, 3};
        System.out.println("排序前: " + Arrays.toString(arr));
        shellSort(arr);
        System.out.println("排序后: " + Arrays.toString(arr));
    }
}