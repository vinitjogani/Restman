package backend.employees;

/**
 * backend.employees.Manager can access Inventory and see printout of all items and add Employees to log in.
 */

public class Manager extends Employee {

    /**
     * Constructs a manager.
     *
     * @param name name of Manager
     */
    Manager(int id, String name) {
        super(id, name);
    }

    /**
     * Overrides backend.employees.Employee.toString for a string representation of the employee.
     *
     * @return Returns a string with manager: followed by name and id of the employee.
     */
    @Override
    public String toString() {
        return "manager: " + super.toString();
    }
}