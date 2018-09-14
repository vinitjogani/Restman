package controllers.cells;

import backend.foods.MenuIngredient;
import backend.foods.Order;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * OrderCell handles functionality of an OrderCell view. The OrderCell view is for managers and servers to see
 * the details about a specific order for a specific customer.
 */
public class OrderCell extends Cell {

    @FXML
    private Label itemLabel, stateLabel, idLabel, priceLabel, descriptionLabel;

    private Order item;

    /**
     * Creates an OrderCell instance for a specific Order.
     *
     * @param order The Order
     */
    OrderCell(Order order) {
        this.item = order;
        load();
    }

    /**
     * Overrides the initialize method from Initializable, to set the information about the order for the OrderCell's
     * view.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idLabel.setText("Order #" + item.getId());
        priceLabel.setText(String.format("$%.2f", item.getPrice()));
        itemLabel.setText(new StringHelper().capitalize(item.getName()));
        stateLabel.setText(item.getState().toString());

        StringBuilder description = new StringBuilder();
        MenuIngredient previous = null;
        int i = 0;

        for (MenuIngredient ingredient : item.getChangeableIngredients()) {
            if (ingredient.getQuantity() != ingredient.getDefaultQuantity()) {
                if (i == 0) description.append("With ");
                else description.append(getDescription(previous)).append(", ");

                previous = ingredient;
                i++;
            }
        }
        if (i > 1) description.append("and ");
        if (previous != null) description.append(getDescription(previous)).append(".");

        descriptionLabel.setText(description.toString());
    }

    /**
     * Gets a description about the order. Description entails details about any differences in MenuIngredient's
     * quantity from their default values for the Order's MenuItem.
     *
     * @param i MenuIngredient
     * @return String description
     */
    private String getDescription(MenuIngredient i) {
        if (i.getQuantity() > i.getDefaultQuantity()) {
            double levelSize = (i.getMaxQuantity() - i.getDefaultQuantity()) / IngredientCell.LEVEL_COUNT;
            int level = (int) Math.round((i.getQuantity() - i.getDefaultQuantity()) / levelSize);
            switch (level) {
                case 1:
                    return "a little more " + i.toString();
                case 2:
                    return "extra " + i.toString();
                case 3:
                    return "a lot of " + i.toString();
            }
        } else {
            double levelSize = (i.getMinQuantity() - i.getDefaultQuantity()) / IngredientCell.LEVEL_COUNT;
            int level = (int) Math.round((i.getQuantity() - i.getDefaultQuantity()) / levelSize);
            switch (level) {
                case 1:
                    return "a little less " + i.toString();
                case 2:
                    return "less " + i.toString();
                case 3:
                    return (i.getQuantity() == 0 ? "no " : "very less ") + i.toString();
            }
        }
        return i.toString();
    }
}
