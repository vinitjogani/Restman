package backend;

import backend.foods.Order;

import java.util.List;

/**
 * This class managers holds and managers orders of the entire restaurant. It helps users in the restaurant know
 * what global orders are still pending or are in need of remake.
 */
public class OrderManager extends ObjectManager<Order> implements Observer {

    /**
     * Gets a list of any orders that are still pending (have not begun to be prepared by a cook) in the entire
     * restaurant.
     * @return a list of the pending orders.
     */
    public List<Order> getPendingOrders() {
        return getObjects(o -> o.getState() == Order.State.PLACED);
    }

    /**
     * Gets a list of any orders that still need to be remade (have not begun to be prepared by a cook) in the entire
     * restaurant.
     * @return a list of the orders needed to be remade.
     */
    public List<Order> getRemakeOrders() {
        return getObjects(o -> o.getState() == Order.State.REMAKE);
    }

    /**
     * Updates when there is a change in an Order. If an order is placed add it to pendingOrders. When an order is
     * canceled it is removed from the system.
     *
     * @param o   The order that changed.
     * @param arg State
     */
    @Override
    public void update(Observable o, Order.State arg) {
        if (o instanceof Order) {
            Order order = (Order) o;
            if (arg == Order.State.PLACED) {
                addObject(order);
            } else if (arg == Order.State.CANCEL) {
                removeObject(order.getId());
            }
        }
    }
}
