package com.storm.algorithms.exercise.link;

import com.storm.algorithms.exercise.link.node.ListNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class DeleteNode {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        ListNode process = process(1, listNode);

        System.out.println(process);
    }

    public static ListNode process(int val,ListNode head){

        if(null == head){
            return null;
        }

        ListNode prev = null;
        ListNode cur = head;
        while (cur != null){

            if(cur.val == val){
                if(null != prev){
                    prev.next = cur.next;
                }
            }
            prev = cur;
            cur = cur.next;
        }
        return head;
    }
}
