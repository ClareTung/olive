package com.olive.ads.start.sort;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {34, 56, 23, 19, 78, 95, 64, 13, 8, 66, 77, 23,34, 87, 36};

        doQuickSort(arr, 0, arr.length -1);

        System.out.println(Arrays.toString(arr));
    }


    public static void doQuickSort(int[] arr, int left, int right){
        if(left < right){
            // 选择一个基准X
            int x = arr[left];

            int i = left;
            int j = right;
            while (i < j){
                // 从右向左找到第一个比X小的元素
                while (i < j && arr[j] >= x){
                    j--;
                }
                if(i < j){
                    arr[i++] = arr[j];
                }

                // 从左向右找到第一个大于等于X的元素
                while (i< j && arr[i] < x){
                    i++;
                }
                if(i < j){
                    arr[j--] = arr[i];
                }
            }

            arr[i] = x;
            doQuickSort(arr, left, i-1);
            doQuickSort(arr, i+1, right);
        }
    }
}
