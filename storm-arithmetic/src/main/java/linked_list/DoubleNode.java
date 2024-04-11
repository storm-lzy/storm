package linked_list;

public class DoubleNode<T> {

    T v;
    DoubleNode<T> pre;
    DoubleNode<T> next;

    public DoubleNode(T v) {
        this.v = v;
    }
}
