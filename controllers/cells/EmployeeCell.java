package controllers.cells;

import backend.employees.Employee;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * EmployeeCell handles the functionality for the EmployeeCell view that displays Employee information.
 */
public class EmployeeCell extends Cell {

    @FXML
    private TextField nameField;

    private Employee employee;

    /**
     * Creates a EmployeeCell for a specific Employee.
     *
     * @param employee Employee to make EmployeeCell for.
     */
    EmployeeCell(Employee employee) {
        this.employee = employee;
        load();
    }

    /**
     * Overrides the initialize method for Initializable to initialize an EmployeeCell.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        nameField.setText(employee.getName());
        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        nameField.setDisable(true);
    }

    /**
     * Sets double click to be be able to change the employee's name
     */
    @Override
    protected void cellDoubleClick() {
        nameField.setDisable(false);
        nameField.selectAll();
    }

    /**
     * Save the changes to the employee's name.
     */
    @FXML
    public void save() {
        if (new StringHelper().isAlpha(nameField.getText())) {
            employee.setName(nameField.getText());
            nameField.setDisable(true);
        } else
            new Alert(Alert.AlertType.ERROR, "The name wasn't in an appropriate format.").showAndWait();
    }
}