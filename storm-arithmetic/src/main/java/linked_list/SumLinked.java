package linked_list;

import javax.swing.text.LabelView;

public class SumLinked {

    public static void main(String[] args) {
        Node<Integer> head1 = new Node<Integer>(7);

        Node<Integer> node2 = new Node<Integer>(3);
        Node<Integer> node3 = new Node<Integer>(6);
        Node<Integer> node4 = new Node<Integer>(9);


        Node<Integer> head2 = new Node<Integer>(4);
        Node<Integer> node6 = new Node<Integer>(2);
        Node<Integer> node7 = new Node<Integer>(8);



        head1.next = node2;
        node2.next = node3;
        node3.next = node4;

        head2.next = node6;
        node6.next = node7;


        head1 = sumLinked(head1,head2);
        while (head1 != null) {
            System.out.println(head1.v);
            head1 = head1.next;

        }
    }

    /**
     * 两个单链表相加
     *
     * @param head1
     * @param head2
     * @param <T>
     * @return
     */
    public static <T> Node<Integer> sumLinked(Node<Integer> head1, Node<Integer> head2) {
        int lenHead1 = lengthLinked(head1);
        int lenHead2 = lengthLinked(head2);
        Node<Integer> longNode = lenHead1 >= lenHead2 ? head1 : head2;
        Node<Integer> shortNode = lenHead2 <= lenHead1 ? head2 : head1;
        Node<Integer> lCur = longNode;
        Node<Integer> sCur = shortNode;
        int carry = 0;
        int curSum = 0;
        while (sCur != null) {
            curSum = sCur.v + lCur.v + carry;
            lCur.v = curSum % 10;
            carry = curSum / 10;
            sCur = sCur.next;
            lCur = lCur.next;
        }
        while (lCur != null) {
            curSum = lCur.v + carry;
            lCur.v = curSum % 10;
            carry = curSum / 10;
            sCur = lCur;
            lCur = lCur.next;
        }

        if(carry > 0){
            sCur.next = new Node<Integer>(carry);
        }
        return longNode;

    }

    private static <T> int lengthLinked(Node<Integer> head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }
}
