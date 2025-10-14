package com.storm.algorithms.exercise.sorts;

/**
 *
 * @author 李治毅
 * @date 2025/10/14
 */
public class ReversedOrderTest {

    public static void main(String[] args) {
        int[] arr = {8,1,9,6,3,8,2,1};

        int sum = process(arr,0,arr.length - 1);

        System.out.println(sum);
    }

    private static int process(int[] arr,int left,int right) {
        if(left >= right){
            return 0;
        }

        int mid = left + ((right - left) >> 1);

        return process(arr, left, mid) + process(arr, mid + 1, right) + merge(arr,left,mid,right);

    }


    public static int merge(int[] arr,int left,int mid,int right){
        int[] help = new int[right - left + 1];

        int l = left;
        int r = mid + 1;
        int i = 0;
        int res = 0;

        while (l <= mid && r <= right){

            if(arr[r] < arr[l]){
                for (int k = l; k <= mid; k++) {
                    System.out.println("逆序对：" + arr[k] + "," + arr[r]);
                }
            }

            help[i++] = arr[l] >= arr[r] ? arr[r++] : arr[l++];
        }

        while (l <= mid){
            help[i++] = arr[l++];
        }

        while (r <= right){
            help[i++] = arr[r++];
        }

        System.arraycopy(help,0,arr,left,help.length);

        return res;
    }


}
