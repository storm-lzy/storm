package com.storm.algorithms.exercise.link.primary;

import com.storm.algorithms.exercise.link.node.ListNode;

import java.util.NoSuchElementException;

/**
 *
 * @author 李治毅
 * @date 2025/10/16
 */
public class QueryVarNode {

    public static void main(String[] args) {

        int[] arr = {8, 1, 9, 6, 3, 8, 2, 1};

        ListNode listNode = BuildLink.buildListNode(arr);

        int val = 8;

        ListNode res = queryListNode(val,listNode);

        System.out.println(res);

    }

    public static ListNode queryListNode(int val,ListNode listNode){

        while (listNode != null){

            if(listNode.val == val){
                return listNode;
            }
            listNode = listNode.next;
        }

        throw new NoSuchElementException("未查到到元素");
    }

}
