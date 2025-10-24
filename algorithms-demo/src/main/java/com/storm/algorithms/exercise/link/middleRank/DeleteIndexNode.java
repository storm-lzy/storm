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

        listNode = process2(listNode, 7);

        System.out.println(listNode);

    }


    /**
     * 快慢指针法
     * 快指针移动n位
     * 快慢指针同时移动知道快指针为空
     * 此时慢指针的下一个节点就是要删除的节点
     */
    public static ListNode process2(ListNode head, int n) {

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode fast = dummy;
        ListNode slow = dummy;


        for (int i = 0; i < n; i++) {
            if(fast.next == null){
                throw new IllegalArgumentException("n大于链表长度");
            }
            fast = fast.next;
        }

        while (fast.next != null){
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;

        return dummy.next;
    }


    /**
     * 计算链表长度，使用长度减去n，计算正数，找到正数节点删除next
     *
     * @author 李治毅
     */
    public static void process(ListNode head, int n) {

        int length = LinkLength.length(head);

        int delAsc = length - n + 1;

        int index = 0;


        ListNode cur = head;
        ListNode prev = null;

        while (cur != null) {

            if (++index == delAsc) {
                if (prev != null) {
                    prev.next = cur.next;
                }
                break;
            }

            prev = cur;
            cur = cur.next;

        }

    }

}
