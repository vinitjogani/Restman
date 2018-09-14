package backend;

import backend.foods.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom observable class which is serializable and implements the bare minimum functionality required.
 */
public class Observable implements Serializable {

    // Instance variables
    private List<Observer> observers;

    // Constructs an observable.
    protected Observable() {
        observers = new ArrayList<>();
    }

    // Adds an observer to the list of observers.
    public void addObserver(Observer o) {
        observers.add(o);
    }

    // Loops through all observers and updates them with the new state of the order.
    protected void notifyObservers(Order.State args) {
        for (Observer o : observers) {
            o.update(this, args);
        }
    }
}
