package controllers.cells;

import backend.foods.MenuIngredient;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles the functionality for MenuIngredientCell view which involves changing default values and min/max quantities
 * for a specific MenuIngredient of a specific MenuItem.
 */
public class MenuIngredientCell extends Cell {

    private MenuIngredient menuIngredient;

    @FXML
    private Button editMinQuan, editMaxQuan, editDefQuan;
    @FXML
    private TextField minQuantityField, maxQuantityField, defQuantityField;

    /**
     * Creates an instance of MenuIngredientCell.
     *
     * @param menuIngredient MenuIngredient the MenuIngredientCell will represent.
     */
    public MenuIngredientCell(MenuIngredient menuIngredient) {
        this.menuIngredient = menuIngredient;
        load();
    }

    /**
     * Overrides the initialize method from Initializable, to set event listeners for the buttons to change
     * the MenuIngredient attributes.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        editMinQuan.setOnAction(event -> minQuantityField.setDisable(false));
        editMaxQuan.setOnAction(event -> maxQuantityField.setDisable(false));
        editDefQuan.setOnAction(event -> defQuantityField.setDisable(false));
        cancel();
    }

    /**
     * Overrides the update method from BaseController to update the MenuIngredient's view based on users changes.
     */
    @Override
    public void update() {
        minQuantityField.setText(String.format("%.2f", menuIngredient.getMinQuantity()));
        maxQuantityField.setText(String.format("%.2f", menuIngredient.getMaxQuantity()));
        defQuantityField.setText(String.format("%.2f", menuIngredient.getDefaultQuantity()));
    }

    /**
     * Saves changes (confirms and sets the MenuIngredient's attributes) made by the user.
     */
    @FXML
    public void save() {
        StringHelper sh = new StringHelper();
        if (sh.isNumeric(new String[]{
                defQuantityField.getText(),
                maxQuantityField.getText(),
                minQuantityField.getText()})) {

            double max = Double.parseDouble(maxQuantityField.getText());
            double min = Double.parseDouble(minQuantityField.getText());
            double def = Double.parseDouble(defQuantityField.getText());

            if (min <= def && def <= max) {
                menuIngredient.setMaxQuantity(max);
                menuIngredient.setMinQuantity(min);
                menuIngredient.setDefaultQuantity(def);
                cancel();
            } else new Alert(Alert.AlertType.ERROR,
                    "Hmm. Seems like min-default-max aren't in increasing order.").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Some numbers weren't formatted correctly!").showAndWait();
        }
    }

    /**
     * Cancels any changes the user made to the MenuIngredient.
     */
    @FXML
    public void cancel() {
        update();
        minQuantityField.setDisable(true);
        maxQuantityField.setDisable(true);
        defQuantityField.setDisable(true);
    }
}
