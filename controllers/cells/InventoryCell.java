package controllers.cells;

import backend.Inventory;
import backend.foods.Ingredient;
import controllers.helpers.InputDialogFactory;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * InventoryCell handles the functionality for the InventoryCell view and changes or displays for a
 * specific ingredient for the inventory.
 */
public class InventoryCell extends Cell {

    private boolean admin;
    private Inventory inventory;
    private Ingredient ingredient;

    @FXML
    private Button editPrice, editThreshold;
    @FXML
    private TextField priceField, thresholdField, quantityField;

    /**
     * Creates an InventoryCell for the inventory and a specific ingredient.
     *
     * @param ingredient ingredient the cell represents
     * @param inventory  inventory the ingredient is part of
     * @param admin      the administrator accessing the inventory
     */
    public InventoryCell(Ingredient ingredient, Inventory inventory, boolean admin) {
        this.ingredient = ingredient;
        this.inventory = inventory;
        this.admin = admin;
        load();
    }

    /**
     * Overrides the initialize method for Initializable to initialize the InventoryCell.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        cancel();
    }

    @Override
    public void update() {
        priceField.setText(String.format("%.2f", ingredient.getUnitCost()));
        thresholdField.setText(String.format("%.2f", ingredient.getThreshold()));
        quantityField.setText(String.format("%.2f", inventory.getQuantity(ingredient.getName())));
    }

    /**
     * saves any changes made to the ingredient in the instance of the ingredient.
     */
    @FXML
    public void save() {
        StringHelper sh = new StringHelper();
        if (sh.isNumeric(new String[]{quantityField.getText(), thresholdField.getText(), priceField.getText()})) {

            ingredient.setThreshold(Double.parseDouble(thresholdField.getText()));
            ingredient.setUnitCost(Double.parseDouble(priceField.getText()));
            inventory.setQuantity(ingredient.getName(), Double.parseDouble(quantityField.getText()));
            cancel();

        } else new Alert(Alert.AlertType.ERROR, "Some numbers weren't formatted correctly!").showAndWait();
    }

    /**
     * Reverts any changes the user made to the ingredient.
     */
    @FXML
    public void cancel() {
        update();
        quantityField.setDisable(true);
        priceField.setDisable(true);
        thresholdField.setDisable(true);

        if (!admin) {
            editPrice.setDisable(true);
            editThreshold.setDisable(true);
        } else {
            editPrice.setOnAction(event -> priceField.setDisable(false));
            editThreshold.setOnAction(event -> thresholdField.setDisable(false));
        }
    }

    /**
     * Increases the ingredient quantity in the inventory by a user defined amount.
     */
    public void increase() {
        Double increaseBy = new InputDialogFactory(
                "Receive ingredient", "Edit quantity for " + ingredient.getName(), "Increase by: "
        ).getDouble();

        if (increaseBy != null && increaseBy > 0)
            quantityField.setText(String.format("%.2f", (Double.parseDouble(quantityField.getText())) + increaseBy));
        else new Alert(Alert.AlertType.ERROR, "Incorrect ingredient quantity.").showAndWait();
    }

    /**
     * Decreases the ingredient quantity in the inventory by a user defined amount.
     */
    public void decrease() {
        Double decreaseBy = new InputDialogFactory(
                "Use ingredient", "Edit quantity for " + ingredient.getName(), "Decrease by: "
        ).getDouble();

        if (decreaseBy != null && decreaseBy <= Double.parseDouble(quantityField.getText()))
            quantityField.setText(String.format("%.2f", (Double.parseDouble(quantityField.getText())) - decreaseBy));
        else new Alert(Alert.AlertType.ERROR, "Incorrect ingredient quantity.").showAndWait();
    }
}
