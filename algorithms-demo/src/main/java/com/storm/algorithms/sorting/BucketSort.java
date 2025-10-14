package com.storm.algorithms.sorting;

import java.util.*;

public class BucketSort {
    
    public static void bucketSort(float[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        int n = arr.length;
        List<Float>[] buckets = new List[n];
        
        for (int i = 0; i < n; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        for (float num : arr) {
            int bucketIndex = (int) (n * num);
            buckets[bucketIndex].add(num);
        }
        
        for (List<Float> bucket : buckets) {
            Collections.sort(bucket);
        }
        
        int index = 0;
        for (List<Float> bucket : buckets) {
            for (float num : bucket) {
                arr[index++] = num;
            }
        }
    }
    
    public static void bucketSort(int[] arr, int bucketSize) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            if (num < min) {
                min = num;
            }
            if (num > max) {
                max = num;
            }
        }
        int bucketCount = (max - min) / bucketSize + 1;
        
        List<Integer>[] buckets = new List[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        for (int num : arr) {
            buckets[(num - min) / bucketSize].add(num);
        }
        
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
        }
        
        int index = 0;
        for (List<Integer> bucket : buckets) {
            for (int num : bucket) {
                arr[index++] = num;
            }
        }
    }
    
    public static void main(String[] args) {
        float[] arr1 = {0.897f, 0.565f, 0.656f, 0.1234f, 0.665f, 0.3434f};
        System.out.println("浮点数排序前: " + Arrays.toString(arr1));
        bucketSort(arr1);
        System.out.println("浮点数排序后: " + Arrays.toString(arr1));
        
        int[] arr2 = {29, 25, 3, 49, 9, 37, 21, 43};
        System.out.println("整数排序前: " + Arrays.toString(arr2));
        bucketSort(arr2, 10);
        System.out.println("整数排序后: " + Arrays.toString(arr2));
    }
}