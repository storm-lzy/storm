package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;
import com.storm.algorithms.exercise.link.primary.TailInsertNode;

/**
 *
 * @author 李治毅
 * @date 2025/10/17
 */
public class ExistsCycleNode {

    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 6, 8, 10, 11};

        ListNode listNode = BuildLink.buildListNode(arr);

        boolean flag = process(listNode);

        System.out.println(flag);
    }


    public static boolean process(ListNode listNode) {
        if(null == listNode || null == listNode.next){
            return false;
        }

        ListNode fast = listNode.next;
        ListNode slow = listNode;

        boolean flag = true;
        while (fast != slow){

            if(fast.next == null || fast.next.next == null){
                flag = false;
                break;
            }

            fast = fast.next.next;
            slow = slow.next;
        }

        return flag;


    }
}
