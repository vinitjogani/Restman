package controllers.cells;

import backend.foods.MenuIngredient;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * IngredientCell Handles functionality the IngredientCell view for a specific MenuIngredient when adjusting
 * the quantity for a specific MenuItem Order.
 */
public class IngredientCell extends Cell {

    public static final int LEVEL_COUNT = 3;

    @FXML
    private Label nameLabel, priceLabel;
    @FXML
    private ProgressBar moreBar, lessBar;

    private MenuIngredient ingredient;
    private double plusLevelSize, minusLevelSize;
    private int levels;

    /**
     * Creates an IngredientCell for a specific MenuIngredient with the default amount it will increase by
     *
     * @param ingredient The ingredient that this cell is displaying.
     */
    IngredientCell(MenuIngredient ingredient) {
        this.ingredient = ingredient;
        this.plusLevelSize = (ingredient.getMaxQuantity() - ingredient.getDefaultQuantity()) / LEVEL_COUNT;
        this.minusLevelSize = (ingredient.getDefaultQuantity() - ingredient.getMinQuantity()) / LEVEL_COUNT;
        load();
    }

    /**
     * Gets the amount you can increase or decrease this Ingredient by for the MenuItem.
     *
     * @return Returns the level size depending upon the current level.
     */
    private double getLevelSize() {
        return levels > 0 ? plusLevelSize : minusLevelSize;
    }

    /**
     * Overrides the update method from BaseController to update the IngredientCell's view based on user inputs.
     */
    @Override
    public void update() {
        double quantity = ingredient.getDefaultQuantity() + levels * getLevelSize();
        String ingredientName = ingredient.toString() + " (" + String.format("%.1f", quantity) + ")";
        nameLabel.setText(new StringHelper().capitalize(ingredientName));

        moreBar.setProgress(0);
        lessBar.setProgress(0);

        ingredient.setQuantity(quantity);
        priceLabel.setText(String.format("+ $%.2f", ingredient.getExtraCost()));

        if (getLevelSize() > 0) {
            if (levels > 0) moreBar.setProgress(levels * 1.0 / LEVEL_COUNT);
            else lessBar.setProgress(-levels * 1.0 / LEVEL_COUNT);
        }
    }

    /**
     * Increases the ingredient quantity for the MenuIngredient.
     */
    @FXML
    public void increase() {
        if (levels < LEVEL_COUNT) {
            levels++;
            update();
        }
    }

    /**
     * Increases the ingredient quantity for the MenuIngredient.
     */
    @FXML
    public void decrease() {
        if (levels > -LEVEL_COUNT && getLevelSize() > 0) {
            levels--;
            update();
        }
    }
}
