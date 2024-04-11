package linked_list;

public class LinkedMerge {

    public static void main(String[] args) {
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(3);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(7);


        Node<Integer> node6 = new Node<>(2);
        Node<Integer> node7 = new Node<>(3);
        Node<Integer> node8 = new Node<>(4);
        Node<Integer> node9 = new Node<>(5);
        Node<Integer> node10 = new Node<>(8);



        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        node6.next = node7;
        node7.next = node8;
        node8.next = node9;
        node9.next = node10;


        node1 = linkedMerge(node1,node6);
        while (node1 != null) {
            System.out.println(node1.v);
            node1 = node1.next;
        }
    }



    public static <T> Node<Integer> linkedMerge(Node<Integer> head1, Node<Integer> head2) {
        Node<Integer> res = head1.v > head2.v ? head2 : head1;
        Node<Integer> pre = head1.v > head2.v ? head1 : head2;
        head1 = head1.next;
        head2 = head2.next;
        res.next = pre;
        while (head1 != null || head2 != null){
            if(head1 != null && head2 != null){
                if(head1.v > head2.v){
                    pre.next = head2;
                    pre = head2;
                    head2 = head2.next;
                }else {
                    pre.next = head1;
                    pre = head1;
                    head1 = head1.next;
                }
                continue;
            }
            if(head1 == null){
                pre.next = head2;
                pre = head2;
                head2 = head2.next;
            }else {
                pre.next = head1;
                pre = head1;
                head1 = head1.next;
            }
        }
        return res;
    }
}
