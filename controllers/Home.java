package controllers;

import controllers.employees.EmployeeHome;
import controllers.employees.cook.CookOrders;
import controllers.employees.manager.EmployeeManager;
import controllers.employees.manager.MenuManager;
import controllers.employees.manager.StatsViewer;
import controllers.employees.server.Pickup;
import controllers.employees.shared.InventoryManager;
import controllers.employees.shared.TableManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import backend.employees.Cook;
import backend.employees.Employee;
import backend.employees.Manager;
import backend.employees.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Home controllers is the main login screen used to send people to other screens.
 */
public class Home extends BaseController {

    // FXML variables
    @FXML
    private ComboBox<String> employeesComboBox;

    /**
     * Constructor loads the view.
     */
    public Home() {
        load();
    }

    /**
     * On update, this method populates the combo-box of employees.
     */
    @Override
    public void update() {
        employeesComboBox.getItems().clear();
        for (Employee e : getRestaurant().getEmployeeManager().getObjects()) {
            employeesComboBox.getItems().add(e.toString());
        }
    }

    /**
     * Handles button click on the login button.
     */
    @FXML
    public void login() {
        String selectedItem = employeesComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Matcher matcher = Pattern.compile("(.*):.*\\(#([0-9]+)\\)").matcher(selectedItem);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(2));
            switch (matcher.group(1).trim()) {
                case "server":
                    serverHome(id);
                    break;
                case "cook":
                    cookHome(id);
                    break;
                case "manager":
                    managerHome(id);
                    break;
            }
        }
    }

    /**
     * Gets the server at specified ID and constructs his home layout.
     *
     * @param id The id of the server employee.
     */
    private void serverHome(int id) {
        Server server = getRestaurant().getEmployeeManager().getObject(x -> x.getId() == id, Server.class);
        if (server != null) {
            EmployeeHome home = new EmployeeHome(server);
            home.addMenuItem("View tables", new TableManager(server));
            home.addMenuItem("View pickup queue", new Pickup(server));
            home.addMenuItem("View inventory", new InventoryManager(server));
            home.addPickupQueueNotifier();
            home.navigate(this);
        }
    }

    /**
     * Gets the cook at specified ID and constructs his home layout.
     *
     * @param id The id of the cook employee.
     */
    private void cookHome(int id) {
        Cook cook = getRestaurant().getEmployeeManager().getObject(x -> x.getId() == id, Cook.class);
        if (cook != null) {
            EmployeeHome home = new EmployeeHome(cook);
            home.addMenuItem("View orders", new CookOrders(cook));
            home.addMenuItem("View inventory", new InventoryManager(cook));
            home.navigate(this);
        }
    }

    /**
     * Gets the manager at specified ID and constructs his home layout.
     *
     * @param id The id of the manager employee.
     */
    private void managerHome(int id) {
        Manager manager = getRestaurant().getEmployeeManager().getObject(x -> x.getId() == id, Manager.class);
        if (manager != null) {
            EmployeeHome home = new EmployeeHome(manager);
            home.addMenuItem("View tables", new TableManager(manager));
            home.addMenuItem("View employees", new EmployeeManager(manager));
            home.addMenuItem("View menu", new MenuManager(manager));
            home.addMenuItem("View inventory", new InventoryManager(manager));
            home.addMenuItem("View statistics", new StatsViewer());
            home.navigate(this);
        }
    }
}
