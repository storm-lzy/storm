package com.storm.algorithms.exercise.link.primary;

import com.storm.algorithms.exercise.link.node.DoubleListNode;
import com.storm.algorithms.exercise.link.node.ListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class BuildLink {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

//        ListNode head = buildListNode(arr);
        DoubleListNode head = buildDoubleNode(arr);

        head.printForward();
        head.printBackward();

    }


    public static ListNode buildListNode(int[] arr) {
        if (arr.length == 0) {
            return null;
        }

        ListNode head = null;
        ListNode prev = null;

        for (int var : arr) {
            ListNode listNode = new ListNode(var);
            if (head == null) {
                head = listNode;
            } else {
                prev.next = listNode;
            }
            prev = listNode;
        }

        return head;
    }



    public static DoubleListNode buildDoubleNode(int[] arr) {

        if(arr.length == 0){
            return null;
        }

        DoubleListNode head = null;
        DoubleListNode prev = null;

        for (int var : arr) {

            DoubleListNode doubleListNode = new DoubleListNode(var);
            if(head == null){
                head = doubleListNode;
            }else {
                prev.next = doubleListNode;
                doubleListNode.prev = prev;
            }

            prev = doubleListNode;
        }


        return head;
    }


}
