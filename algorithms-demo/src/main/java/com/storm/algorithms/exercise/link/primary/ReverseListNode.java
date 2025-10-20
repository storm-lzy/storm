package com.storm.algorithms.exercise.link.primary;

import com.storm.algorithms.exercise.link.node.ListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class ReverseListNode {

    public static void main(String[] args) {
        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        listNode = process(listNode);

        System.out.println(listNode);
    }


    public static ListNode process(ListNode head) {


        ListNode cur = head;
        ListNode prev = null;

        while (cur != null){
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        return prev;
    }
}
