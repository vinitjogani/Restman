package controllers.employees.cook;

import backend.employees.Cook;
import backend.foods.Order;
import controllers.BaseController;
import controllers.cells.CustomCellFactory;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class CookOrders extends BaseController {

    // FXML variables
    @FXML
    private ListView<Order> toBeSeenQueue, preparingQueue, remakeQueue;
    @FXML
    private Label sceneTitle;

    // Instance variables
    private Cook cook;

    /**
     * Constructs the cook orders screen for specified cook instance.
     *
     * @param cook The cook whose screen you want to see.
     */
    public CookOrders(Cook cook) {
        this.cook = cook;
        load("CookOrders");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components, and
     * setup all the cook ListViews.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        sceneTitle.setText("Orders");
        setupList(toBeSeenQueue, "Mark order as seen", "Begin preparing");
        setupList(preparingQueue, "Confirm order prepared", "Confirm completion");
        setupList(remakeQueue, "Remake orders", "Begin remaking");

        //give option to adjust inventory from dialog after remake.
    }

    /**
     * Takes in a list view, attaches a cell factory to it, and attaches mouse click handler.
     *
     * @param list         The list to set up.
     * @param title        The title of the alert dialog.
     * @param confirmation The text on confirmation button.
     */
    private void setupList(ListView<Order> list, String title, String confirmation) {
        list.setPlaceholder(new Label("There are no orders to display!"));
        list.setCellFactory(param -> new CustomCellFactory<>(cook));
        list.setOnMouseClicked(event -> handleCookEvent(title, confirmation, event, list));
    }

    /**
     * Handles a double click event on any of the cook's order cells, by creating an alert and in event
     * of confirmation, sees or prepares an order.
     *
     * @param title        The title of the alert dialog to be shown.
     * @param confirmation The text on confirmation button to be shown.
     * @param event        The mouse event of double click.
     * @param list         The ListView that this method is handling clicks on.
     */
    private void handleCookEvent(String title, String confirmation, MouseEvent event, ListView<Order> list) {
        int index = list.getSelectionModel().getSelectedIndex();
        Order order = list.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && order != null && createCookAlert(title, confirmation, order)) {
            if (list == preparingQueue) cook.orderReady(order);
            else if (index == 0) cook.orderSeen(order);
            else new Alert(Alert.AlertType.ERROR, "Handle next order in Queue first!").showAndWait();
            update();
        }
    }

    /**
     * Creates an alert to be shown to the cook with a confirmation action, cancel action and exit action.
     *
     * @param title         The title of the dialog to be shown.
     * @param confirmation  The text on the confirmation button to be shown.
     * @param selectedOrder The selected order which the alert box manipulates.
     * @return Returns whether the confirmation box was clicked.
     */
    private boolean createCookAlert(String title, String confirmation, Order selectedOrder) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Order #" + selectedOrder.getId() + " (" + selectedOrder.getName() + ")");
        alert.setHeaderText(title);
        alert.setGraphic(null);

        ButtonType cancelButton = new ButtonType("Cancel Order");
        ButtonType confirmButton = new ButtonType(confirmation);
        alert.getButtonTypes().setAll(confirmButton, cancelButton, new ButtonType("Exit"));
        alert.showAndWait();

        if (alert.getResult() == confirmButton) return true;
        else if (alert.getResult() == cancelButton) cancel(selectedOrder);
        return false;
    }

    /**
     * Cancel an order if confirmation is received..
     *
     * @param order The order to cancel.
     */
    public void cancel(Order order) {
        boolean confirmation = new InputDialogFactory("Warning",
                "", "Are you sure you would like to cancel this Order?").getConfirmation();
        if (confirmation) cook.cancelOrder(order);
        update();
    }

    /**
     * Updates the items of all the respective ListViews.
     */
    @Override
    public void update() {
        toBeSeenQueue.setItems(FXCollections.observableList(getRestaurant().getOrderManager().getPendingOrders()));
        preparingQueue.setItems(FXCollections.observableList(cook.getOrders()));
        remakeQueue.setItems(FXCollections.observableList(getRestaurant().getOrderManager().getRemakeOrders()));
    }
}
