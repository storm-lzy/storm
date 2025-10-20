package com.storm.algorithms.exercise.link.middleRank;

import com.storm.algorithms.exercise.link.node.ListNode;
import com.storm.algorithms.exercise.link.primary.BuildLink;

/**
 *
 * @author 李治毅
 * @date 2025/10/17
 */
public class QueryCentreNode {

    public static void main(String[] args) {

        int[] arr = {1, 3, 4, 6, 8, 10, 11};

        ListNode listNode = BuildLink.buildListNode(arr);

        int var = query(listNode);

        System.out.println(var);
    }

    public static int query(ListNode listNode) {

        ListNode fastCur = listNode;
        ListNode slowCur = listNode;

        while (fastCur.next != null && fastCur.next.next != null) {
            fastCur = fastCur.next.next;
            slowCur = slowCur.next;
        }

        return slowCur.val;
    }

}
