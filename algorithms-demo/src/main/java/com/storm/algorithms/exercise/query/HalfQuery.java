package com.storm.algorithms.exercise.query;

/**
 *
 * @author 李治毅
 * @date 2025/10/13
 */
public class HalfQuery {

    public static void main(String[] args) {
        int[] arr = {3, 4, 6, 7, 9, 10, 14, 16, 18};

        System.out.println(query(arr, 7));
    }


    public static int query(int[] arr, int target) {


        int left = 0;
        int right = arr.length - 1;

        while (left <= right){

            int mid = left + ((right - left) >> 1);

            if(target == arr[mid]){
                return mid;
            }else if(target > arr[mid]){
                left = mid + 1;
            }else if(target < arr[mid]){
                right = mid - 1;
            }

        }
        return -1;

    }


}
