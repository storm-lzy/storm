package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;

/**
 *
 * @author 李治毅
 * @date 2025/10/17
 */
public class MergeListNode {

    public static void main(String[] args) {

        int[] arr1 = {1, 4, 5, 7, 9};
        int[] arr2 = {1, 3, 4, 6, 8, 10,11};

        ListNode listNode1 = BuildLink.buildListNode(arr1);
        ListNode listNode2 = BuildLink.buildListNode(arr2);

        ListNode mergeNode = process(listNode1, listNode2);

        System.out.println(mergeNode);

    }


    public static ListNode process(ListNode listNode1, ListNode listNode2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (listNode1 != null && listNode2 != null) {
            if(listNode1.val < listNode2.val){
                current.next = listNode1;
                listNode1 = listNode1.next;
            }else {
                current.next = listNode2;
                listNode2 = listNode2.next;
            }
            current = current.next;
        }

        current.next = null != listNode1 ? listNode1 : listNode2;

        return dummy.next;
    }


}
