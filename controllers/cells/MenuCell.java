package controllers.cells;

import backend.employees.Employee;
import backend.employees.Manager;
import backend.foods.MenuItem;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles functionality for a MenuCell view for a specific MenuItem.
 */
public class MenuCell extends Cell {

    @FXML
    private Label nameLabel;
    @FXML
    private TextField priceLabel;

    private MenuItem item;
    private boolean available, admin;

    /**
     * Creates an instance of MenuCell with the MenuItem it represents and the Employee accessing it.
     *
     * @param menuItem MenuItem it represents
     * @param employee Employee accessing the MenuCell
     */
    MenuCell(MenuItem menuItem, Employee employee) {
        this.item = menuItem;
        this.available = getRestaurant().getInventory().isFeasible(menuItem);
        this.admin = employee instanceof Manager;
        load();
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the MenuCell with a specific
     * MenuIngredients name and price and an event handler it.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        nameLabel.setText(new StringHelper().capitalize(item.getName()));
        priceLabel.setText(String.format("%.2f", item.getPrice()));

        priceLabel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (new StringHelper().isNumeric(priceLabel.getText())) {
                    item.setPrice(Double.parseDouble(priceLabel.getText()));
                }
                priceLabel.setDisable(true);
                priceLabel.setText(String.format("%.2f", item.getPrice()));
            }
        });

        if (!available) {
            cellContainer.setDisable(true);
            cellContainer.setStyle("-fx-background-color: #bdc3c7;");
        }

    }

    /**
     * Allows an admin to change the price of the MenuItem on double click.
     */
    @Override
    protected void cellDoubleClick() {
        if (admin) {
            priceLabel.setDisable(false);
            priceLabel.selectAll();
            priceLabel.requestFocus();
        }
    }
}
