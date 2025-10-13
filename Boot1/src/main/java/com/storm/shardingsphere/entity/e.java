package com.storm.shardingsphere.entity;

/**
 *
 * @author 李治毅
 * @date 2025/9/29
 */
public class e {

    public static void main(String[] args) {


        // 构建第一个链表：9->9->9->9->9->9->9
        ListNode l1 = buildList(new int[]{9, 9, 9, 9, 9, 9, 9});
        // 构建第二个链表：9->9->9->9
        ListNode l2 = buildList(new int[]{9, 9, 9, 9});

        // 哑节点，简化头节点处理
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        // 进位，初始为0
        int carry = 0;


        while (l1 != null || l2 != null || carry != 0) {
            // 获取当前节点值（null时为0）
            int val1 = (l1 != null) ? l1.val : 0;
            int val2 = (l2 != null) ? l2.val : 0;

            // 计算当前位总和（包含进位）
            int sum = val1 + val2 + carry;
            // 新的进位（sum >=10 则为1，否则为0）
            carry = sum / 10;

            // 当前位的结果值
            int currentVal = sum % 10;

            // 创建新节点并移动指针
            current.next = new ListNode(currentVal);
            current = current.next;

            // 移动原链表指针（非null时才移动）
            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;

        }
    }



    /**
     * 辅助方法：根据数组构建链表
     */
    private static ListNode buildList(int[] values) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int val : values) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next;
    }

    /**
     * 辅助方法：打印链表
     */
    private static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) {
                System.out.print("->");
            }
            current = current.next;
        }
        System.out.println();
    }




    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }
}
