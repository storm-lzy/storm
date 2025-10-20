package com.storm.algorithms.exercise.link.primary;

import com.storm.algorithms.exercise.link.node.ListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class LinkLength {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        int length = length(listNode);

        System.out.println(length);

    }


    public static int length(ListNode listNode){


        int len = 0;
        while (listNode != null){
            len++;
            listNode = listNode.next;
        }
        return len;
    }
}
