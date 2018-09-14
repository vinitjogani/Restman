package controllers.employees.manager;

import backend.employees.Employee;
import backend.employees.EmployeeFactory;
import backend.employees.Manager;
import backend.foods.Order;
import controllers.SplitList;
import controllers.cells.CustomCellFactory;
import controllers.helpers.InputDialogFactory;
import controllers.helpers.StringHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The EmployeeManager screen is used for creating, reading, updating and deleting employees of a restaurant.
 */
public class EmployeeManager extends SplitList<Employee, Order> {

    // Instance variables
    private Manager employee;

    /**
     * Constructs EmployeeManager class by taking in a manager.
     *
     * @param employee The manager instance who is viewing this screen.
     */
    public EmployeeManager(Manager employee) {
        super(employee, (i, s) -> i.toString().toLowerCase().contains(s) && i.getId() != employee.getId());
        this.employee = employee;
        load("SplitList");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components, and adds
     * context menu to primary list items.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        actionButton.setText("New");
        sceneTitle.setText("Employee Manager");
        primaryList.setCellFactory(param -> {
            CustomCellFactory<Employee> cell = new CustomCellFactory<>(employee);
            cell.addOption("Delete employee").setOnAction(event -> {
                getRestaurant().getEmployeeManager().removeObject(cell.getItem().getId());
                update();
            });
            return cell;
        });
    }

    /**
     * Override single click on primary list to update secondary list.
     *
     * @param item The item that was single-clicked on.
     */
    @Override
    protected void primarySingleClick(Employee item) {
        optionsLabel.setText("Employee #" + item.getId());
        secondaryList.setItems(FXCollections.observableList(item.getOrders()));
        update();
    }

    /**
     * Update the items of the primary list.
     */
    @Override
    public void update() {
        primaryList.setItems(FXCollections.observableList(filter(getRestaurant().getEmployeeManager().getObjects())));
        super.update();
    }

    /**
     * Create a new employee on action button click.
     */
    @FXML
    public void action() {
        String newEmployee = new InputDialogFactory(
                "Create new employee", "Enter type of employee and name (like server:Bob)", ""
        ).getString();

        if (newEmployee == null) return;
        Matcher matcher = Pattern.compile("\\b(\\S+)\\b\\s*:\\s*(\\S+\\s?\\S+?)\\b\\s*").matcher(newEmployee);
        int g = matcher.groupCount();
        if (!(matcher.find() && matcher.groupCount() >= 2 && new StringHelper().isAlpha(matcher.group(2))) ||
                !new EmployeeFactory(getRestaurant()).create(matcher.group(1).toLowerCase(),
                        new StringHelper().capitalize(matcher.group(2))))
            new Alert(Alert.AlertType.ERROR, "Input doesn't match format.").showAndWait();
        update();
    }
}
