package controllers.cells;

import backend.Customer;
import backend.foods.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CustomerCell handles the functionality for the CustomerCell view that displays Customer information.
 */
public class CustomerCell extends Cell {

    @FXML
    private Label tableLabel, pendingLabel, confirmedLabel, billLabel;

    private Customer customer;

    /**
     * Creates a CustomerCell for a specific customer.
     *
     * @param customer customer the cell will represent.
     */
    CustomerCell(Customer customer) {
        this.customer = customer;
        load();
    }

    /**
     * Overrides the initialize method for Initializable to initialize a CustomerCell.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableLabel.setText("Customer #" + customer.getId());
        pendingLabel.setText(customer.getPendingOrders().size() + " Pending Orders");
        confirmedLabel.setText(customer.getConfirmedOrders().size() + " Confirmed Orders");

        double total = 0;
        for (Order o : customer.getOrders()) total += o.getPrice();
        billLabel.setText(String.format("$%.2f", total));
    }

}
