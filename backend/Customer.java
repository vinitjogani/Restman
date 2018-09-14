package backend;

import backend.foods.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Customer class contains all the information pertaining to a customer of
 * the restaurant, including the orders that the customer made and their id (id is only specific to
 * the table they are at).
 *
 * It maintains all the orders corresponding to himself in this class by observing them.
 */
public class Customer implements Serializable, Observer {

    // Instance variables
    private int id;
    private List<Order> pendingOrders;      //orders the customer made but has not yet received
    private List<Order> confirmedOrders;    //orders the customer has received and accepted

    /**
     * The constructor for the Customer class.
     */
    public Customer(int id) {
        this.id = id;
        this.confirmedOrders = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
    }

    /**
     * Getter for id.
     *
     * @return Returns the customer's identifier.
     */
    public int getId() {
        return id;
    }


    /**
     * Getter for pendingOrders.
     *
     * @return Returns the pending orders of the customer.
     */
    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    /**
     * Gets all the orders that the customer did not reject.
     *
     * @return Returns all prepared, non-rejected orders.
     */
    public List<Order> getConfirmedOrders() {
        return confirmedOrders;
    }

    /**
     * Gets all order of the customer - pending and confirmed.
     *
     * @return Returns all pending or confirmed orders of the customer.
     */
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        orders.addAll(getPendingOrders());
        orders.addAll(getConfirmedOrders());
        return orders;
    }

    /**
     * The customer updates its own list of pending orders and confirmed orders by observing the order.
     *
     * @param o   The observable order.
     * @param arg The new state of the order.
     */
    @Override
    public void update(Observable o, Order.State arg) {
        if (o instanceof Order) {
            Order order = (Order) o;
            if (arg == Order.State.PLACED) {
                pendingOrders.add(order);
            } else if (arg == Order.State.DELIVERED && pendingOrders.contains(order)) {
                pendingOrders.remove(order);
                confirmedOrders.add(order);
            } else if (arg == Order.State.CANCEL) {
                pendingOrders.remove(order);
            }
        }
    }
}
