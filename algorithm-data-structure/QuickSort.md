## 快速排序

### 算法思想
* 选定一个基准，设置一个最左的起点left，设置一个最右的起点right
* 从right开始，找到第一个比基准X小的元素，和X交换
* 从left开始，找到第一个比基准X大的元素，和right当前位置交换
* 从right开始，再找比X小的元素，和left当前位置交换，直到left等于right，一次排序停止
* 以left等于right位置为分割点，对其左右两部分分别递归排序

### 代码实现
```java
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

```