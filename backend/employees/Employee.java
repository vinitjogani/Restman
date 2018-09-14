package backend.employees;
import backend.foods.Order;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The backend.employees.Employee class serves as a parent to the various types of employees in the
 * restaurant: including backend.employees.Server, backend.employees.Cook, and backend.employees.Manager.
 */
public class Employee implements Serializable {

    // Instance variables
    private int id;
    private String name;
    private List<Order> orders;

    /**
     * Constructor for the class.
     *
     * @param name Name of the employee.
     */
    Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.orders = new ArrayList<>();
    }

    /**
     * Getter for name.
     *
     * @return Returns the employee's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param Name String name
     */
    public void setName(String Name) {
        this.name = Name;
    }

    /**
     * Getter for id.
     *
     * @return Returns the employee's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for orders.
     *
     * @return Returns the employee's list of orders.
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Setter for id
     *
     * @param id int id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Cancels the order by setting its state. All observers will remove it from the system.
     *
     * @param order Order to be cancelled
     */
    public void cancelOrder(Order order) {
        order.setState(Order.State.CANCEL);
    }

    /**
     * Overrides Object.equals for checking equality.
     *
     * @param obj Object to check equality against.
     * @return Returns whether the two objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Employee && ((Employee) obj).getId() == getId();
    }

    /**
     * Overrides Object.toString for a string representation of the employee.
     *
     * @return Returns a string with name and id of the employee.
     */
    @Override
    public String toString() {
        return name + " (#" + id + ")";
    }
}
