package observer;

import java.util.Collections;

/**
 *
 */
public class ObjectFor3D extends Subject {

    @Override
    void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    void notifyObservers(String msg) {
        if(null != observers && !observers.isEmpty()){
            for (Observer observer : observers) {
                observer.update(msg);
            }
        }
    }
}
