package com.storm.algorithms.exercise.link;

import com.storm.algorithms.exercise.link.node.ListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class HeadInsertNode {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        int val = 7;

        System.out.println("before：" + listNode);

        ListNode newHead = process(val, listNode);

        System.out.println("after：" + newHead);
    }

    public static ListNode process(int val, ListNode head) {

        ListNode newHead = new ListNode(val);

        newHead.next = head;

        return newHead;
    }
}
