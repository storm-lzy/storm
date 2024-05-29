package linked_list;

public class NodeStack <T> {

    private Node<T> head;

    private int size;


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(T e) {
        Node<T> node = new Node<T>(e);
        if (head == null) {
            head = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }


    public T pop() {
        T e = null;
        if (head != null) {
            e = head.v;
            head = head.next;
            size--;
        }
        return e;
    }

    public static void main(String[] args) {
        NodeStack<Integer> nodeStack = new NodeStack<Integer>();
        for (int i = 1; i <= 10; i++) {
            nodeStack.push(i);
        }
        while (!nodeStack.isEmpty()){
            System.out.println(nodeStack.pop());
        }
    }

}
