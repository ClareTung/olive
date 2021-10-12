package com.olive.ads.start.linkedlist;

import com.olive.ads.start.linkedlist.entity.ListNode;

import java.util.Stack;

/**
 * @description: 从尾到头打印链表
 * @program: olive
 * @author: dtq
 * @create: 2021/10/12 16:27
 */
public class EndToEndPrinting {

    /*
     题目描述
输入一个链表的头结点，按照 从尾到头 的顺序返回节点的值。

返回的结果用数组存储。

样例

输入：[2, 3, 5]
返回：[5, 3, 2]
解法
遍历链表，每个链表结点值 push 进栈，最后将栈中元素依次 pop 到数组中。
     */


    public int[] printListReversingly(ListNode head) {
        if (head == null) {
            return null;
        }

        Stack<Integer> stack = new Stack<>();
        ListNode cur = head;
        int cnt = 0;
        if (cur != null) {
            stack.push(cur.val);
            cur = cur.next;

            ++cnt;
        }

        int[] res = new int[cnt];
        int i = 0;
        while (!stack.isEmpty()) {
            res[i] = stack.pop();
        }

        return res;
    }

}
