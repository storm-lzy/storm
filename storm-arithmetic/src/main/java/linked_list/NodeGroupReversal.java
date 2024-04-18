package linked_list;

public class NodeGroupReversal {

    public static void main(String[] args) {
        Node<Integer> head = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        Node<Integer> node7 = new Node<>(7);
        Node<Integer> node8 = new Node<>(8);
        Node<Integer> node9 = new Node<>(9);
        Node<Integer> node10 = new Node<>(10);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        node7.next = node8;
        node8.next = node9;
        node9.next = node10;

        head = groupReversal(head, 3);


        while (head != null) {
            System.out.println(head.v);
            head = head.next;
        }
    }

    /**
     * 链表组内反转
     *
     * @param head
     * @param k    每组个数
     * @param <T>
     * @return
     */
    public static <T> Node<T> groupReversal(Node<T> head, int k) {
        Node<T> start = head;
        Node<T> end = null;
        Node<T> lastStart = null;
        if ((end = getKGroupEnd(start, k)) == null) {
            return head;
        }
        head = end;
        reverse(start, end);
        lastStart = start;

        while ((start = start.next) != null && null != (end = getKGroupEnd(start, k))) {
            reverse(start, end);
            lastStart.next = end;
            lastStart = start;
        }

        return head;
    }

    public static <T> Node<T> getKGroupEnd(Node<T> start, int k) {
        while (--k > 0 && start != null) {
            start = start.next;
        }
        return start;
    }

    public static <T> void reverse(Node<T> start, Node<T> end) {
        end = end.next;
        Node<T> pre = null;
        Node<T> cur = start;
        Node<T> next = null;
        while (cur != end) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        start.next = end;
    }
}
