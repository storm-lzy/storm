package day1;

/**
 *
 * @author 李治毅
 * @date 2025/10/9
 */
public class MergeSort {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        process(arr, 0, arr.length - 1);

        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    private static void process(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + ((right - left) >> 1);

        process(arr, left, mid);
        process(arr, mid + 1, right);

        merge(arr, left, mid, right);

    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];

        int l = left;
        int r = mid + 1;
        int i = 0;

        while (l <= mid && r <= right) {
            if (arr[l] >= arr[r]) {
                temp[i++] = arr[r++];
            } else {
                temp[i++] = arr[l++];
            }
        }

        while (l <= mid) {
            temp[i++] = arr[l++];
        }

        while (r <= right) {
            temp[i++] = arr[r++];
        }

        for (i = 0; i < temp.length; i++) {
            arr[left + i] = temp[i];
        }
    }

}
