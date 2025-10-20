package com.storm.algorithms.exercise.link.primary;

import com.storm.algorithms.exercise.link.node.DoubleListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class ReverseDoubleListNode {

    public static void main(String[] args) {
        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        DoubleListNode listNode = BuildLink.buildDoubleNode(arr);

        listNode.printForward();
        listNode.printBackward();

        listNode = process(listNode);

        System.out.println();

        listNode.printForward();
        listNode.printBackward();
    }


    public static DoubleListNode process(DoubleListNode head) {

        DoubleListNode cur = head;
        DoubleListNode prev = null;

        while (cur != null){

            DoubleListNode next = cur.next;
            cur.next = prev;
            cur.prev = next;
            prev = cur;
            cur = next;


        }

        return prev;
    }
}
