package linked_list;

public class NodeQueue<T> {

    public NodeQueue() {
        this.size = 0;
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void offer(T e){
        Node<T> node = new Node<T>(e);
        if(size == 0){
            head = node;
            tail = node;
        }else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T poll(){
        T e = null;
        if(null != head){
            e = head.v;
            head = head.next;
            size--;
        }
        if(head == null){
            tail = null;
        }
        return e;
    }
}
