package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;

/**
 *
 * @author 李治毅
 * @date 2025/10/21
 */
public class PalindromeListNode {

    public static void main(String[] args) {

        int[] arr = {8,1,9,6,6,9,1,8};

        ListNode listNode = BuildLink.buildListNode(arr);

        boolean flag =  process(listNode);

        System.out.println(flag);

    }


    public static boolean process(ListNode head){

        // 中点
        ListNode centreNode = queryCentre(head);

        // 中点后进行反转
        ListNode reverseNode = reverseNode(centreNode.next);

        boolean flag = true;
        // 比较
        while (reverseNode != null){

            if(head.val != reverseNode.val){
                flag = false;
                break;
            }

            head = head.next;
            reverseNode = reverseNode.next;

        }

        return flag;

    }


    public static ListNode queryCentre(ListNode head){

        ListNode fast = head;
        ListNode slow = head;

        while (fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }


    public static ListNode reverseNode(ListNode head){

        ListNode cur = head;
        ListNode prev = null;
        while (null != cur){
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

}
