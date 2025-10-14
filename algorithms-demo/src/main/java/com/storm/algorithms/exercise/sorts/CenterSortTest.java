package com.storm.algorithms.exercise.sorts;

import java.util.Arrays;

/**
 *
 * @author 李治毅
 * @date 2025/10/14
 */
public class CenterSortTest {


    /**
     *
     *
     * 问题一 和一个数num，请把小于等于num的数放在数 组的左边，大于num的给定一个数组arr，数放在数组的右边。要求额外空间复杂度0(1)，时间复杂度0(N)
     * <p>
     * <p>
     * 问题二(荷兰国旗问题)给定一个数组arr，和一个数num，请把小于num的数放在数组的左边，等于num的数放在数组的中间，大于num的数放在数组的 右边。要求额外空间复杂度0(1)，时间复杂度0(N)
     *
     */

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1, 7, 7, 7};

        int var = 7;

        process2(arr, var);

        System.out.println(Arrays.toString(arr));
    }


    public static void process2(int[] arr, int var) {

        int left = 0;
        int right = arr.length - 1;
        int current = 0;


        while (current <= right) {

            if (arr[current] < var) {
                swap(arr, current, left);
                left++;
                current++;
            } else if (arr[current] > var) {
                swap(arr, current, right);
                right--;
                current++;
            } else {
                current++;
            }

        }


    }

    public static void swap(int[] arr, int source, int target) {
        arr[source] = arr[source] ^ arr[target];
        arr[target] = arr[source] ^ arr[target];
        arr[source] = arr[source] ^ arr[target];
    }


    public static void process(int[] arr, int var) {

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {

            while (left < right && arr[left] <= var) {
                left++;
            }

            while (left < right && arr[right] > var) {
                right--;
            }

            if (left < right) {
                arr[left] = arr[left] ^ arr[right];
                arr[right] = arr[left] ^ arr[right];
                arr[left] = arr[left] ^ arr[right];
            }

        }


    }
}
