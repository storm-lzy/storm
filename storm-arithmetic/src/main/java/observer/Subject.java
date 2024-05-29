package observer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Subject {

    List<Observer> observers;

    abstract void registerObserver(Observer observer);
    abstract void removeObserver(Observer observer);
    abstract void notifyObservers(String msg);

    public Subject(){
        observers = new ArrayList<Observer>();
    }

    public static void main(String[] args) {
        ObjectFor3D objectFor3D = new ObjectFor3D();
        ObServerOne obServerOne = new ObServerOne();
        ObServerTwo obServerTwo = new ObServerTwo();
        objectFor3D.registerObserver(obServerOne);
        objectFor3D.registerObserver(obServerTwo);

        objectFor3D.notifyObservers("通知观察者！！！");

        objectFor3D.removeObserver(obServerOne);

        objectFor3D.notifyObservers("再次通知观察者！！！");

    }

}
