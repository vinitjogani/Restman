package controllers.employees.shared;

import backend.Table;
import backend.employees.Employee;
import backend.employees.Manager;
import backend.foods.Order;
import controllers.SplitList;
import controllers.cells.CustomCellFactory;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The TableManager screen gives the ability to create, read, update and delete tables. It also shows
 * all the orders of any given table.
 */
public class TableManager extends SplitList<Table, Order> {

    // Instance variables
    private Employee employee;

    /**
     * Constructs a TableManager screen with the specified employee
     *
     * @param employee The employee accessing this screen.
     */
    public TableManager(Employee employee) {
        super(employee, (t, s) -> Integer.toString(t.getId()).contains(s));

        this.employee = employee;
        load("SplitList");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components. It also
     * adds a context menu item to delete tables.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        sceneTitle.setText("Tables");
        actionButton.setText("New");
        if (!(employee instanceof Manager)) actionButton.setVisible(false);

        primaryList.setPlaceholder(new Label("There are no tables to display!"));
        secondaryList.setPlaceholder(new Label("There are no orders to display!"));


        primaryList.setCellFactory(param -> {
            CustomCellFactory<Table> cell = new CustomCellFactory<>(employee);
            if (employee instanceof Manager && cell.getItem() != null && cell.getItem().getCustomers().size() > 0) {
                cell.addOption("Delete table").setOnAction(event -> {
                    if (cell.getItem().getCustomers().size() == 0)
                        getRestaurant().getTableManager().removeObject(cell.getItem().getId());
                    else new Alert(Alert.AlertType.ERROR, "There are customers on the table.");
                    update();
                });
            }
            return cell;
        });

        addExtraControl(new Button("Bill"), event -> bill());
    }

    /**
     * Overrides the double click event of primary list to open that table.
     *
     * @param item The item that was double-clicked on.
     */
    @Override
    protected void primaryDoubleClick(Table item) {
        new Customers(employee, item).navigate(this);
    }

    /**
     * Overrides the single click event of the primary list to update secondary list with orders of a table.
     *
     * @param item The item that was single-clicked on.
     */
    @Override
    protected void primarySingleClick(Table item) {
        super.primarySingleClick(item);
        optionsLabel.setText("Orders at table #" + item.getId());
        secondaryList.setItems(FXCollections.observableList(item.getOrders()));
    }

    /**
     * Updates items of the primary list on navigation.
     */
    @Override
    public void update() {
        primaryList.setItems(FXCollections.observableList(filter(getRestaurant().getTableManager().getObjects())));
        super.update();
    }

    /**
     * Creates a new table on action button click.
     */
    @FXML
    public void action() {
        getRestaurant().getTableManager().addObject(new Table(getRestaurant().getTableManager().getNextId()));
        update();
    }

    /**
     * Bills a table after asking for confirmation (if the table is not empty), and navigates to the
     * ShowBill screen.
     */
    private void bill() {
        Table table = primaryList.getSelectionModel().getSelectedItem();

        boolean confirmation = table != null && table.getCustomers().size() > 0 && new InputDialogFactory(
                "Are you sure?", "This will remove the customers from the system", ""
        ).getConfirmation();

        if (confirmation) {
            if (!table.getOrders().removeIf(o -> o.getState() != Order.State.DELIVERED)) {
                new ShowBill(table.getBill()).navigate(this);
                update();
            } else new Alert(Alert.AlertType.ERROR,
                    "Table has pending orders that need to be cancelled first.").showAndWait();
        }
    }
}
