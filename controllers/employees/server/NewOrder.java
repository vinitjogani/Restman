package controllers.employees.server;

import backend.Customer;
import backend.employees.Server;
import backend.foods.MenuIngredient;
import backend.foods.MenuItem;
import backend.foods.Order;
import controllers.SplitList;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The NewOrder screen is used to create a new order, change its ingredients, and place it.
 */
public class NewOrder extends SplitList<MenuItem, MenuIngredient> {

    // Instance variables
    private Order newOrder;
    private Customer customer;
    private Server server;

    /**
     * Constructs a NewOrder screen with the specified server and customer.
     *
     * @param server   The server creating the new order.
     * @param customer The customer who the order is created for.
     */
    public NewOrder(Server server, Customer customer) {
        super(server, (p, s) -> p.getName().toLowerCase().trim().contains(s));
        this.customer = customer;
        this.server = server;
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

        sceneTitle.setText("New order");
        actionButton.setText("Place");
        primaryList.setPlaceholder(new Label("There are no menu items to display!"));
        secondaryList.setPlaceholder(new Label("There are no changeable ingredients to display!"));
    }

    /**
     * Override single click on primary list to update secondary list (if item is feasible).
     *
     * @param item The item that was single-clicked on.
     */
    @Override
    protected void primarySingleClick(MenuItem item) {
        if (getRestaurant().getInventory().isFeasible(item)) {
            this.newOrder = new Order(getRestaurant().getOrderManager().getNextId(), item);
            sceneTitle.setText("New order: " + newOrder.getName());
            optionsLabel.setText("Change ingredients");
            secondaryList.setItems(FXCollections.observableList(newOrder.getChangeableIngredients()));
        }
    }

    /**
     * Update the items of the primary list.
     */
    @Override
    public void update() {
        primaryList.setItems(FXCollections.observableList(filter(getRestaurant().getMenu())));
        super.update();
    }

    /**
     * Place the new order on action button click.
     */
    @FXML
    public void action() {
        if (newOrder == null) return;

        if (!server.placeOrder(newOrder, customer))
            new Alert(Alert.AlertType.ERROR, "Insufficient ingredients!").showAndWait();
        else {
            String note = new InputDialogFactory("", "Enter a note if necessary", "").getString();
            newOrder.addNote(note == null ? "" : note);

            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").showAndWait();
            back();
        }
    }
}
