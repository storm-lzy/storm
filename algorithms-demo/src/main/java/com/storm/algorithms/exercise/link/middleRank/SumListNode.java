package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;

/**
 *
 * @author 李治毅
 * @date 2025/10/21
 */
public class SumListNode {

    public static void main(String[] args) {

        int[] arr1 = {2, 4, 3, 5};
        int[] arr2 = {5, 6, 4};

        ListNode listNode1 = BuildLink.buildListNode(arr1);
        ListNode listNode2 = BuildLink.buildListNode(arr2);

        ListNode listNode = process(listNode1, listNode2);

        System.out.println(listNode);
    }


    public static ListNode process(ListNode node1, ListNode node2) {
        if (null == node1 || null == node2) {
            return node1 == null ? node2 : node1;
        }

        ListNode dummy = new ListNode(0);

        ListNode cur = dummy;


        ListNode p1 = node1;
        ListNode p2 = node2;

        int carry = 0;

        while (p1 != null || p2 != null || carry != 0) {

            int p1Val = p1 == null ? 0 : p1.val;
            int p2Val = p2 == null ? 0 : p2.val;

            int sum = p1Val + p2Val + carry;

            carry = sum / 10;

            sum = sum % 10;

            dummy.next = new ListNode(sum);
            dummy = dummy.next;

            p1 = p1 == null ? null : p1.next;
            p2 = p2 == null ? null : p2.next;
        }

        return cur.next;
    }


}
