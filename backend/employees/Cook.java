package backend.employees;

import backend.Observable;
import backend.Observer;
import backend.foods.Order;
import backend.helpers.LogManager;


/**
 * Cook class. Sees and prepares order. When the cook sees an order he/she begins preparing it.
 * When it is finished being prepared he/she says so.
 */
public class Cook extends Employee implements Observer {

    /**
     * Constructs a cook.
     *
     * @param name name of Cook
     */
    Cook(int id, String name) {
        super(id, name);
    }

    /**
     * The cook sees the order. Order's state is changed to PREPARING and is added to this Cook's preparingQueue.
     *
     * @param order Order object which the Cooks sees (and begins preparing).
     */
    public void orderSeen(Order order) {
        getOrders().add(order);
        order.addObserver(this);
        order.setState(Order.State.PREPARING);
    }

    /**
     * The Cook finishes preparing an Order. Order's state changes to READY and is removed from the preparingQueue.
     *
     * @param order Order object that the cook is done preparing.
     */
    public void orderReady(Order order) {
        getOrders().remove(order);
        order.setState(Order.State.READY);
        LogManager.getInstance().log(order, this);
    }

    /**
     * Overrides Employee.toString for a string representation of the employee.
     *
     * @return Returns a string with cook: followed by name and id of the employee.
     */
    @Override
    public String toString() {
        return "cook: " + super.toString();
    }

    /**
     * Removes the Order from the Cooks order list (preparing orders) if necessary.
     *
     * @param o   Observable Order
     * @param arg State
     */
    public void update(Observable o, Order.State arg) {
        if (o instanceof Order) {
            if (arg == Order.State.CANCEL) {
                getOrders().remove(o);
            }
        }
    }
}
