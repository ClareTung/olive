package com.olive.ads.start.linkedlist;

import com.olive.ads.start.linkedlist.entity.ListNode;

import java.util.Stack;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/10/12 16:27
 */
public class LinkedListTest {

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


    /*
    题目描述
在一个排序的链表中，存在重复的结点，请删除该链表中重复的结点，重复的结点不保留。

样例 1

输入：1->2->3->3->4->4->5

输出：1->2->5
样例 2

输入：1->1->1->2->3

输出：2->3
     */
    /**
     * 删除链表重复的节点
     *
     * @param head 链表头节点
     * @return 删除重复节点后的链表
     */
    public ListNode deleteDuplication(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        if (head.next.val == head.val) {
            if (head.next.next == null) {
                return null;
            }
            if (head.next.next.val == head.val) {
                return deleteDuplication(head.next);
            }
            return deleteDuplication(head.next.next);
        }
        head.next = deleteDuplication(head.next);
        return head;
    }


    /*
    题目描述
输入一个链表，输出该链表中倒数第 k 个结点。

解法
pre 指针走 k-1 步。之后 cur 指针指向 phead，然后两个指针同时走，直至 pre 指针到达尾结点。

当用一个指针遍历链表不能解决问题的时候，可以尝试用两个指针来遍历链表。可以让其中一个指针遍历的速度快一些。

此题需要考虑一些特殊情况。比如 k 的值小于 0 或者大于链表长度。
     */
    /**
     * 找出链表倒数第k个节点，k从1开始
     * @param head 链表头部
     * @param k 第k个节点
     * @return 倒数第k个节点
     */
    public ListNode FindKthToTail(ListNode head,int k) {
        if (head == null || k < 1) {
            return null;
        }

        ListNode pre = head;
        for (int i = 0; i < k - 1; ++i) {
            if (pre.next != null) {
                pre = pre.next;
            } else {
                return null;
            }
        }

        ListNode cur = head;
        while (pre.next != null) {
            pre = pre.next;
            cur = cur.next;
        }
        return cur;
    }

   /*
   题目描述
给一个链表，若其中包含环，请找出该链表的环的入口结点，否则，输出null。

解法
先利用快慢指针。若能相遇，说明存在环，且相遇点一定是在环上；若没有相遇，说明不存在环，返回 null。
固定当前相遇点，用一个指针继续走，同时累积结点数。计算出环的结点个数 cnt。
指针 p1 先走 cnt 步，p2 指向链表头部，之后 p1,p2 同时走，相遇时，相遇点一定是在环的入口处。因为 p1 比 p2 多走了环的一圈。
   */
    /**
     * 求链表环的入口，若没有环，返回null
     * @param pHead 链表头
     * @return 环的入口点
     */
    public ListNode EntryNodeOfLoop(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return null;
        }
        ListNode fast = pHead;
        ListNode slow = pHead;
        boolean flag = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (fast == slow) {
                flag = true;
                break;
            }
        }

        // 快指针与慢指针没有相遇，说明无环，返回 null
        if (!flag) {
            return null;
        }

        ListNode cur = slow.next;
        // 求出环中结点个数
        int cnt = 1;
        while (cur != slow) {
            cur = cur.next;
            ++cnt;
        }

        // 指针p1先走cnt步
        ListNode p1 = pHead;
        for (int i = 0; i < cnt; ++i) {
            p1 = p1.next;
        }

        // p2指向链表头，然后p1/p2同时走，首次相遇的地方就是环的入口
        ListNode p2 = pHead;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }

    /*
    题目描述
输入一个链表，反转链表后，输出新链表的表头。

解法
解法一
利用头插法解决。
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(-1);
        ListNode p = head;
        ListNode q = head.next;
        while (q != null) {
            p.next = dummy.next;
            dummy.next = p;
            p = q;
            q = p.next;
        }
        p.next = dummy.next;
        return p;
    }

    /*
    题目描述
输入两个递增排序的链表，合并这两个链表并使新链表中的结点仍然是按照递增排序的。

样例

输入：1->3->5 , 2->4->5

输出：1->2->3->4->5->5
     */
    public ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val < l2.val) {
            l1.next = merge(l1.next, l2);
            return l1;
        }
        l2.next = merge(l1, l2.next);
        return l2;
    }


    /*

 题目描述
输入两个链表，找出它们的第一个公共结点。

样例

给出两个链表如下所示：
A：        a1 → a2
                   ↘
                     c1 → c2 → c3
                   ↗
B:     b1 → b2 → b3

输出第一个公共节点c1
解法
先遍历两链表，求出两链表的长度，再求长度差 |n1 - n2|。

较长的链表先走 |n1 - n2| 步，之后两链表再同时走，首次相遇时的节点即为两链表的第一个公共节点。
     */
    /**
     * 求两链表第一个公共节点
     *
     * @param headA 链表A
     * @param headB 链表B
     * @return 第一个公共节点
     */
    public ListNode findFirstCommonNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        int n1 = len(headA), n2 = len(headB);
        ListNode p1 = headA, p2 = headB;
        if (n1 > n2) {
            for (int i = 0; i < n1 - n2; ++i) {
                p1 = p1.next;
            }
        } else if (n1 < n2) {
            for (int i = 0; i < n2 - n1; ++i) {
                p2 = p2.next;
            }
        }
        while (p1 != p2 && p1 != null && p2 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return (p1 == null || p2 == null) ? null : p1;
    }

    private int len(ListNode head) {
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            ++n;
            cur = cur.next;
        }
        return n;
    }





}
