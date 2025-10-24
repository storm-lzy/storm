package com.storm.algorithms.exercise.link.advanced;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;

/**
 *
 * @author 李治毅
 * @date 2025/10/21
 */
public class GroupReverseNode {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        int k = 3;

        ListNode res = process(listNode, k);

        System.out.println(res);
    }


    public static ListNode process(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head;
        }

        ListNode hummy = new ListNode(0);
        hummy.next = head;

        ListNode prevTail = hummy;

        while (true) {

            ListNode end = prevTail;
            for (int i = 0; i < k; i++) {
                end = end.next;
                if (end == null) {
                    return hummy.next;
                }
            }

            ListNode nextGroup = end.next;
            ListNode start = prevTail.next;

            prevTail.next = reverse(start, end);
            start.next = nextGroup;

            prevTail = start;
        }
    }


    /**
     * 反转从start到end的链表（闭区间），返回反转后的头节点（原end）
     */
    private static ListNode reverse(ListNode start, ListNode end) {
        ListNode prev = null;
        ListNode curr = start;
        ListNode stop = end.next;

        while (curr != stop) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }


}
