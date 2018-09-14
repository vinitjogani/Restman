package backend.employees;

import backend.ObjectManager;
import backend.Restaurant;

/**
 * A Factory for Creating Employees of different types.
 */
public class EmployeeFactory {

    private Restaurant restaurant; //restaurant the EmployeeFactory will add Employees to

    /**
     * Creates an instance of the EmployeeFactory
     * @param restaurant restaurant
     */
    public EmployeeFactory(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Creates an Employee based on the string input and adds it to the restaurant's EmployeeManager
     * @param type type of employee to make
     * @param name name of the employee
     */
    public boolean create(String type, String name) {
        ObjectManager<Employee> employeeManager = restaurant.getEmployeeManager();
        switch (type) {
            case "cook":
                employeeManager.addObject(new Cook(employeeManager.getNextId(), name));
                return true;
            case "server":
                employeeManager.addObject(new Server(employeeManager.getNextId(), name, restaurant));
                return true;
            case "manager":
                employeeManager.addObject(new Manager(employeeManager.getNextId(), name));
                return true;
            default:
                return false;
        }
    }
}
