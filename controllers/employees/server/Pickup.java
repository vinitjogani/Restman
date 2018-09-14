package controllers.employees.server;

import backend.Customer;
import backend.Table;
import backend.employees.Server;
import backend.foods.Order;
import controllers.SearchController;
import controllers.cells.CustomCellFactory;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Pickup extends SearchController<Order> {

    // FXML variables
    @FXML
    private ListView<Order> primaryList;
    @FXML
    private HBox infoBox, buttonBox;
    @FXML
    private Label customerLabel, tableLabel, orderLabel, optionsLabel;

    // Instance variables
    private Server server;

    /**
     * Constructs a pickup screen with the specified server.
     *
     * @param server The server whose pickup queue has to be shown.
     */
    public Pickup(Server server) {
        super((c, s) -> c.getName().toLowerCase().contains(s));
        this.server = server;
        load();
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
        primaryList.setPlaceholder(new Label("There are no orders to display!"));
        primaryList.setCellFactory(param -> new CustomCellFactory<>(server));
        primaryList.setOnMouseClicked(event -> update());
    }

    /**
     * Update the items of the primary list and change values in the secondary list.
     */
    @Override
    public void update() {
        primaryList.setItems(FXCollections.observableList(filter(server.getOrders())));
        Order selectedItem = primaryList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            optionsLabel.setText("Pick up order");
            buttonBox.setVisible(true);
            infoBox.setVisible(true);
            for (Table table : getRestaurant().getTableManager().getObjects()) {
                Customer customer = table.getCustomerByOrder(selectedItem.getId());
                if (customer != null) {
                    orderLabel.setText("Order #" + selectedItem.getId());
                    tableLabel.setText("Table #" + table.getId());
                    customerLabel.setText("Customer #" + customer.getId());
                    break;
                }
            }
        } else {
            optionsLabel.setText("");
            infoBox.setVisible(false);
            buttonBox.setVisible(false);
        }
    }

    /**
     * Handle click event for the confirm button.
     */
    @FXML
    public void confirm() {
        Order selectedItem = primaryList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            boolean confirmation = new InputDialogFactory(
                    "Are you sure?", "This order will be added to the bill now.", ""
            ).getConfirmation();
            if (confirmation) server.confirmOrder(selectedItem);
            update();
        }
    }

    /**
     * Handle click event for the reject button.
     */
    public void reject() {
        Order selectedItem = primaryList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String reason = new InputDialogFactory(
                    "Reject reason", "Enter a note for remaking the order", ""
            ).getString();
            if (reason != null) server.rejectOrder(selectedItem, reason);
            update();
        }
    }
}
