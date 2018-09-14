package backend;

import backend.foods.Order;

/**
 * Custom interface for an Observer to go with the custom implementation of Observable class.
 */
public interface Observer {
    /**
     * Update method for the observer.
     *
     * @param observable The observable object which is calling this update.
     * @param args       The new state of the object calling this update.
     */
    void update(Observable observable, Order.State args);
}
