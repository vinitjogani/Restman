package backend;

import backend.foods.Ingredient;
import backend.foods.MenuIngredient;
import backend.foods.MenuItem;
import backend.foods.Order;
import backend.helpers.LogManager;
import backend.helpers.RequestsManager;

import java.io.Serializable;
import java.util.*;

/**
 * Holds Ingredients with their respective quantities.
 * Each Ingredient has limited number of additions/subtractions.
 * backend.employees.Manager can check the Inventory and see a printout of all Inventory items and quantities.
 * Receiver can scan each item (or list of items) back into the Inventory when a new shipment of ingredients arrives.
 */

public class Inventory implements Observer, Serializable {

    private static final double DEFAULT_REQUEST = 10;   // the default quantity threshold for when to request

    private HashMap<Ingredient, Double> ingredients;    // Ingredient to quantity in inventory

    /**
     * Constructor for the class.
     */
    public Inventory() {
        this.ingredients = new HashMap<>();
    }

    /**
     * Returns the Ingredient object that the String parameter ingredientName represents, if any.
     *
     * @param ingredientName String name of ingredient requested.
     * @return Ingredient that ingredientName represents.
     */
    public Ingredient getIngredient(String ingredientName) {
        for (Ingredient ingredient : ingredients.keySet()) {
            if (ingredient.getName().toLowerCase().equals(ingredientName.toLowerCase())) return ingredient;
        }

        Ingredient newIngredient = new Ingredient(ingredientName);
        ingredients.put(newIngredient, 0.0);
        return newIngredient;
    }

    /**
     * Returns the Ingredient objects currently in the Inventory.
     *
     * @return  Inventory ingredients key sey.
     */
    public Set<Ingredient> getInventoryIngredients() {
        return ingredients.keySet();
    }

    /**
     * Receives an order and decrements the required Ingredient from the Inventory. Also calls the log manager
     * to log such event.
     *
     * @param ingredientName Ingredient (String name) to be removed from the inventory.
     * @param quantity       The quantity of ingredient that is to be removed.
     * @return whether it was actually able to use the ingredient
     */
    private boolean use(String ingredientName, double quantity) {
        return setQuantity(ingredientName, getQuantity(ingredientName) - quantity);
    }

    /**
     * Inventory receives a shipment and adds the required Ingredient to the Inventory.
     *
     * @param ingredientName Ingredient (String name) to be added from the inventory.
     * @param quantity       The quantity of ingredient that is to be added.
     */
    public boolean add(String ingredientName, double quantity) {
        return setQuantity(ingredientName, getQuantity(ingredientName) + quantity);
    }

    /**
     * Gets the quantity of the Ingredient that matches the String ingredientName.
     *
     * @param ingredientName Ingredient whose quantity has to be returned
     * @return Returns the number of ingredients.
     */
    public double getQuantity(String ingredientName) {
        return ingredients.getOrDefault(getIngredient(ingredientName), 0.0);
    }

    /**
     * Setter for ingredient quantity for a String ingredientName that represents an Ingredient.
     *
     * @param ingredientName The ingredient whose quantity has to be set.
     * @param quantity       The quantity to set the ingredient to.
     * @return Returns whether the set was performed successfully.
     */
    public boolean setQuantity(String ingredientName, double quantity) {
        if (quantity >= 0) {
            double oldQuantity = getQuantity(ingredientName);
            ingredients.put(getIngredient(ingredientName), quantity);
            LogManager.getInstance().log(getIngredient(ingredientName), oldQuantity,
                    getQuantity(ingredientName));

            return true;
        } else return false;
    }

    /**
     * Checks if this menuItem is feasible to make (i.e there are enough ingredients to make it)
     *
     * @param menuItem menuItem to check
     * @return boolean (true if able to make)
     */
    public boolean isFeasible(MenuItem menuItem) {
        for (MenuIngredient i : menuItem.getIngredients()) {
            if (i.getQuantity() > ingredients.getOrDefault(i.getBaseIngredient(), 0.0)) return false;
        }
        return true;
    }

    /**
     * Overrides Object.toString to return String
     *
     * @return Returns the String of Inventory
     */
    @Override
    public String toString() {
        StringBuilder inventory = new StringBuilder();
        for (Ingredient ingredient : ingredients.keySet()) {
            inventory.append(ingredient.getName()).append(" : ").append(ingredients.get(ingredient)).append("\n");
        }
        return inventory.toString();
    }

    /**
     * Updates the inventory when Order's state is changed. More specifically when an order is placed. Every ingredient
     * in the MenuItem of the order will be updated.
     *
     * @param o   Observable order
     * @param arg state of the order
     */
    @Override
    public void update(Observable o, Order.State arg) {
        if (o instanceof Order && arg == Order.State.PLACED) {
            for (MenuIngredient i : ((Order) o).getIngredients()) {
                use(i.getBaseIngredient().getName(), i.getQuantity());

                if (getQuantity(i.getBaseIngredient().getName()) <= i.getBaseIngredient().getThreshold())
                    RequestsManager.getInstance().addIngredient(i.toString(), DEFAULT_REQUEST);
            }
        }
    }
}
