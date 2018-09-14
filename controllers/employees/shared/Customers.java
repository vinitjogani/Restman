package controllers.employees.shared;

import backend.Customer;
import backend.Table;
import backend.employees.Employee;
import backend.employees.Server;
import backend.foods.Order;
import controllers.SplitList;
import controllers.employees.server.NewOrder;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The customers screen lists all customers and their orders at a given table, and can be used by servers
 * to create new orders.
 */
public class Customers extends SplitList<Customer, Order> {

    // Instance variables
    private Employee employee;
    private Table table;

    /**
     * Creates a new customer screen with the specified employee at the specified table.
     *
     * @param employee The employee who is accessing the screen.
     * @param table    The table whose customers are to be shown.
     */
    public Customers(Employee employee, Table table) {
        super(employee, (c, s) -> Double.toString(c.getId()).contains(s));

        this.table = table;
        this.employee = employee;
        load("SplitList");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        actionButton.setText("New");
        sceneTitle.setText("Customers at table #" + table.getId());
        primaryList.setPlaceholder(new Label("There are no customers to display!"));
        secondaryList.setPlaceholder(new Label("There are no orders to display!"));
    }

    /**
     * Overrides the double click event on the primary list to create a new order.
     *
     * @param item The item that was double-clicked on.
     */
    @Override
    protected void primaryDoubleClick(Customer item) {
        if (employee instanceof Server) {
            if (employee.getOrders().size() == 0) new NewOrder((Server) employee, item).navigate(this);
            else new Alert(Alert.AlertType.ERROR, "There are orders you must pickup first.").showAndWait();
        }
    }

    /**
     * Overrides the single click event on the primary list to update secondary list.
     *
     * @param item The item that was single-clicked on.
     */
    @Override
    protected void primarySingleClick(Customer item) {
        optionsLabel.setText("Orders of customer #" + item.getId());
        secondaryList.setItems(FXCollections.observableList(item.getOrders()));
    }

    /**
     * Creates a dialog to add more customers to a table.
     */
    @FXML
    public void action() {
        Integer numCustomers = new InputDialogFactory("Number of Customers", "",
                "Input number of customers:").getInteger();
        if(numCustomers != null) table.addCustomers(numCustomers);
        update();
    }

    /**
     * Refreshes the primary list items on navigation update.
     */
    @Override
    public void update() {
        primaryList.setItems(FXCollections.observableList(filter(table.getCustomers())));
        super.update();
    }
}
