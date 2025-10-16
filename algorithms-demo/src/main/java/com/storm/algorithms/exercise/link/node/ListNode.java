package com.storm.algorithms.exercise.link.node;

public class ListNode {
    public int val;         // 节点存储的值
    public ListNode next;   // 指向下一个节点的引用

    public ListNode(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val + " -> " + next;
    }
}
