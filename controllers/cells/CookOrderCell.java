package controllers.cells;

import backend.foods.MenuIngredient;
import backend.foods.Order;
import controllers.helpers.StringHelper;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class provides the functionality for orders on the Cook's Order screen. This cell updates the CookOrderCell
 * view with an Order's name, id, and ingredients.
 */
public class CookOrderCell extends Cell {
    @FXML
    private Label foodItemLabel, orderId;
    @FXML
    private ListView<String> ingredientList;
    @FXML
    private HBox notesBox;

    private Order order;

    /**
     * Creates and Loads a CookOrderCell
     *
     * @param order Order this cell will represent
     */
    CookOrderCell(Order order) {
        this.order = order;
        load();
    }

    /**
     * Overrides the initialize method from Initializable to update the CookOrderCell view with
     * the associated order's information.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        foodItemLabel.setText(new StringHelper().capitalize(order.getName()));
        orderId.setText("Order # " + order.getId());

        if (order.getNotes().trim().equals(""))
            notesBox.getChildren().clear();

        for (MenuIngredient ingredient : order.getIngredients()) {
            String ingredientName = new StringHelper().capitalize(ingredient.toString());
            String ingredientQuantity = String.format("%.2f", ingredient.getQuantity());
            ingredientList.getItems().add(ingredientQuantity + "    " + ingredientName);
        }

        ingredientList.setFixedCellSize(24);
        ingredientList.setPrefHeight(24 * order.getIngredients().size() + 2);
    }

    /**
     * Creates an alert dialog to show the notes associated with the order.
     */
    @FXML
    public void getNotes() {
        if (!order.getNotes().trim().equals(""))
            new Alert(Alert.AlertType.INFORMATION, order.getNotes().trim()).showAndWait();
        else new Alert(Alert.AlertType.INFORMATION, "No notes found.").showAndWait();
    }
}
