package observer;

/**
 *
 */
public class ObServerOne implements Observer {
    @Override
    public void update(String msg) {
        System.out.println(this.getClass().getName() + msg);
    }
}
