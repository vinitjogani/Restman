package backend;

import backend.employees.Employee;
import backend.foods.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Restaurant class handles matters relating to the Employees, MenuItems, Orders, and Inventory,
 * as well as methods for adding or removing employees, and adding or removing MenuItems.
 */

public class Restaurant implements Serializable {

    // Instance variables
    private ObjectManager<Employee> employeeManager;
    private ObjectManager<Table> tableManager;
    private OrderManager orderManager;
    private List<MenuItem> menu;
    private Inventory inventory;


    /**
     * Constructs a new Restaurant object.
     */
    public Restaurant() {
        // All the object managers
        this.employeeManager = new ObjectManager<>();
        this.tableManager = new ObjectManager<>();
        this.orderManager = new OrderManager();
        // All the other objects
        this.menu = new ArrayList<>();
        this.inventory = new Inventory();
    }

    /**
     * Gets this Restaurant's EmployeeManager.
     *
     * @return Returns the employee manager.
     */
    public ObjectManager<Employee> getEmployeeManager() {
        return employeeManager;
    }

    /**
     * Gets this Restaurant's OrderManager
     *
     * @return Returns the order manager.
     */
    public OrderManager getOrderManager() {
        return orderManager;
    }

    /**
     * Gets this Restaurant's TableManager
     *
     * @return Returns the table manager.
     */
    public ObjectManager<Table> getTableManager() {
        return tableManager;
    }

    /**
     * Gets this Restaurant's inventory.
     *
     * @return Returns the inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets this Restaurant's menu.
     *
     * @return Returns the menu.
     */
    public List<MenuItem> getMenu() {
        return menu;
    }
}