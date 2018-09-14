package backend;

import backend.foods.Order;
import backend.helpers.LogManager;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Table holds the customers that will sit at an instance of the table. The restaurant can bill an entire
 * table or individual customers at the table.
 */
public class Table implements Serializable {

    private int id;
    private ArrayList<Customer> customers;

    /**
     * Creates a table instance with a specific table id number.
     *
     * @param id identification number
     */
    public Table(int id) {
        this.id = id;
        customers = new ArrayList<>();
    }

    /**
     * Getter for the table id.
     *
     * @return id of the table
     */
    public int getId() {
        return this.id;
    }

    /**
     * Adds a given number of new customers to the table.
     *
     * @param numCustomers number of customers to add
     */
    public void addCustomers(int numCustomers) {
        for (int i = 0; i < numCustomers; i++) {
            int customerId = customers.size() + 1;
            customers.add(new Customer(customerId));
        }
        LogManager.getInstance().log(id, numCustomers);
    }

    /**
     * Returns a list of all the orders for all the customers at the table.
     *
     * @return list of orders
     */
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (Customer c : customers) {
            orders.addAll(c.getOrders());
        }
        return orders;
    }

    /**
     * Returns the Customer at this table by the customer's id number
     *
     * @param id id of the customer to get
     * @return Customer if one exists at the table
     */
    public Customer getCustomerByOrder(int id) {
        for (Customer c : customers) {
            if (new ArrayList<>(c.getOrders()).removeIf(o -> o.getId() == id)) return c;
        }
        return null;
    }

    /**
     * Gets the bill for the entire table. (all customers who have yet to be billed). This will also clear all
     * the customers from the table.
     *
     * @return Bill that represents the orders made at this table.
     */
    public GridPane getBill() {
        GridPane tableBill = new Bill(this).getTableBill();
        customers.clear();
        return tableBill;
    }

    /**
     * Getter for the list of Customers at this table.
     *
     * @return list of Customers.
     */
    public List<Customer> getCustomers() {
        return customers;
    }
}
