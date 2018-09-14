package backend.employees;

import backend.Customer;
import backend.Observable;
import backend.Observer;
import backend.Restaurant;
import backend.foods.Order;
import backend.helpers.LogManager;

/**
 * The Server class handles placing, confirming and rejecting orders. Servers also maintain their "own" orders -
 * i.e. the orders that are in their pickup queue.
 */
public class Server extends Employee implements Observer {

    // Instance variables
    private Restaurant restaurant;

    /**
     * Constructor for the Server class.
     *
     * @param name       The name of the server.
     * @param restaurant The restaurant that the server picks up orders from.
     */
    Server(int id, String name, Restaurant restaurant) {
        super(id, name);
        this.restaurant = restaurant;
    }

    /**
     * Adds all observers to the order and adds it to customer's list of pending orders.
     *
     * @param o The order to be placed.
     */
    public boolean placeOrder(Order o, Customer c) {
        if (restaurant.getInventory().isFeasible(o)) {
            o.addObserver(this);
            o.addObserver(c);
            o.addObserver(restaurant.getOrderManager());
            o.addObserver(restaurant.getInventory());
            o.setState(Order.State.PLACED);
            LogManager.getInstance().log(o, this);
            return true;
        }
        return false;
    }

    /**
     * Picks up an order from the Cook's kitchen and confirms it.
     *
     * @param o The order that is picked up and confirmed.
     */
    public void confirmOrder(Order o) {
        o.setState(Order.State.DELIVERED);
        LogManager.getInstance().log(o, this);
    }

    /**
     * Picks up an order from the kitchen and rejects it with specified reason.
     *
     * @param o The order that is picked up and rejected.
     */
    public void rejectOrder(Order o, String reason) {
        o.addNote(reason);
        o.setState(Order.State.REMAKE);
        LogManager.getInstance().log(o, this);
    }

    /**
     * Overrides Object.equals to check equality.
     *
     * @param obj Object to check equality against.
     * @return Returns whether the two objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Server && ((Server) obj).getId() == getId();
    }

    /**
     * Overrides backend.employees.Employee.toString for a string representation of the employee.
     *
     * @return Returns a string with server: followed by name and id of the employee.
     */
    @Override
    public String toString() {
        return "server: " + super.toString();
    }

    /**
     * Updates when there is a change in an order. If PLACED add required observers to the order. If ready add it to
     * the pickupQueue.
     *
     * @param o   Order that has changed.
     * @param arg New state of the order.
     */
    @Override
    public void update(Observable o, Order.State arg) {
        if (o instanceof Order) {
            if (arg == Order.State.READY) getOrders().add((Order) o);
            else if (getOrders().contains(o)) getOrders().remove(o);
        }
    }
}
