package linked_list;

public class DoubleNodeQueue<E> {

    DoubleNode<E> head;
    DoubleNode<E> tail;

    int size;


    /**
     * 尾部加入
     *
     * @param e
     */
    public void offer(E e) {
        DoubleNode<E> doubleNode = new DoubleNode<E>(e);
        if (null == head) {
            head = doubleNode;
            tail = doubleNode;
        } else {
            doubleNode.pre = tail;
            tail.next = doubleNode;
            tail = doubleNode;
        }
        size++;
    }

    /**
     * 头部出
     *
     * @return
     */
    public E poll() {
        E e = null;
        if (head != null) {
            e = head.v;
            head = head.next;
            size--;
        }
        if (head == null) {
            tail = null;
        } else {
            head.pre = null;
        }
        return e;
    }

    /**
     * 头部加入
     *
     * @param e
     */
    public void offerFirst(E e) {
        DoubleNode<E> doubleNode = new DoubleNode<E>(e);
        if (head == null) {
            head = doubleNode;
            tail = doubleNode;
        } else {
            head.pre = doubleNode;
            doubleNode.next = head;
            head = doubleNode;
        }
        size++;
    }

    /**
     * 尾部出
     * @return
     */
    public E pollLast(){
        E e = null;
        if(tail != null){
            e = tail.v;
            tail = tail.pre;
            size--;
        }
        if(tail != null){
            tail.next = null;
        }else {
            head = null;
        }
        return e;
    }


    public DoubleNodeQueue() {
        head = null;
        tail = null;
        this.size = 0;
    }

    public static void main(String[] args) {
        DoubleNodeQueue<Integer> queue = new DoubleNodeQueue<Integer>();
        queue.offer(1);
        queue.offer(2);
        queue.offerFirst(5);
        System.out.println(queue.pollLast());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

    }

}
