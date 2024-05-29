package observer;

/**
 *
 */
public class ObServerTwo implements Observer{

    @Override
    public void update(String msg) {
        System.out.println(this.getClass().getName() + msg);
    }
}
