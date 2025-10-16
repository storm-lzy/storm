package com.storm.algorithms.exercise.link.node;

public class DoubleListNode {
    public int val;
    public DoubleListNode prev;
    public DoubleListNode next;

    public DoubleListNode(int val) {
        this.val = val;
    }

    // 从头向后打印
    public void printForward() {
        DoubleListNode cur = this;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" -> ");
            cur = cur.next;
        }
        System.out.println();
    }

    // 从尾向前打印
    public void printBackward() {
        DoubleListNode tail = this;
        while (tail.next != null) {
            tail = tail.next;
        }
        DoubleListNode cur = tail;
        while (cur != null){
            System.out.print(cur.val);
            if (cur.prev != null) System.out.print(" <- ");
            cur = cur.prev;
        }

        System.out.println();
    }
}
