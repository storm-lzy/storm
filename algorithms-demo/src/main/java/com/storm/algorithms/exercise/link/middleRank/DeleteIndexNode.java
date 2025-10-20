package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;
import com.storm.algorithms.exercise.link.primary.LinkLength;

/**
 *
 * @author 李治毅
 * @date 2025/10/17
 */
public class DeleteIndexNode {

    /**
     * 删除倒处第n个节点
     */
    public static void main(String[] args) {

        int[] arr = {1, 3, 4, 6, 8, 10, 11};

        ListNode listNode = BuildLink.buildListNode(arr);

        process(listNode,3);

        System.out.println(listNode);

    }


    public static void process(ListNode head,int n){

        int length = LinkLength.length(head);

        int delAsc = length - n + 1;

        int index = 0;


        ListNode cur = head;
        ListNode prev = null;

        while (cur != null){

            if(++index == delAsc){
                if(prev != null){
                    prev.next = cur.next;
                }
                break;
            }

            prev = cur;
            cur = cur.next;

        }

    }

}
