package backend.foods;

import backend.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds each item of the menu and manipulates the ingredients
 * respective to each item
 */
public class MenuItem extends Observable implements Serializable {

    // Instance variables
    private String name;
    private ArrayList<MenuIngredient> ingredients;
    private double price;


    /**
     * Constructor for the MenuItem class.
     *
     * @param name  Name of the item on the menu
     * @param price Price of this specific item
     */
    public MenuItem(String name, double price) {
        this.name = name.toLowerCase();
        this.ingredients = new ArrayList<>();
        this.price = price;
    }

    /**
     * Getter for the name of this MenuItem.
     *
     * @return Returns the name of the MenuItem.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the price of this MenuItem.
     *
     * @return Returns the price of the MenuItem,
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Sets the price of this MenuItem.
     *
     * @param newPrice The new price for the menu item.
     */
    public void setPrice(double newPrice) {
        price = newPrice;
    }

    /**
     * Returns the ArrayList of Ingredients of this MenuItem.
     *
     * @return Returns a list of ingredients in the menu item.
     */
    public List<MenuIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * Returns an list of ingredients whose orderQuantity can be changed.
     *
     * @return Returns an list of ingredients whose orderQuantity can be changed.
     */
    public List<MenuIngredient> getChangeableIngredients() {
        List<MenuIngredient> changeableIngredients = new ArrayList<>();
        for (MenuIngredient i : this.ingredients) {
            if (i.getMaxQuantity() > i.getMinQuantity()) {
                changeableIngredients.add(i);
            }
        }
        return changeableIngredients;
    }

    /**
     * If said ingredient is part of this MenuItem, returns the ingredient otherwise returns null.
     *
     * @param ingredientName Name of the Ingredient being searched for.
     * @return Ingredient object if ingredient is part of this MenuItem else returns null.
     */
    public MenuIngredient getIngredient(String ingredientName) {
        for (MenuIngredient i : this.ingredients) {
            if (i.toString().equals(ingredientName.toLowerCase())) return i;
        }
        return null;
    }

    /**
     * Checks whether a given MenuItem is equal to this one based on class instance, name and price.
     *
     * @param obj Object being passed in to be compared.
     * @return True or false based on whether Object is same as this MenuItem.
     */
    public boolean equals(Object obj) {
        return obj instanceof MenuItem && ((MenuItem) obj).getName().equals(this.name)
                && ((MenuItem) obj).getPrice() == this.price;
    }

    /**
     * Returns a string representation of this MenuItem.
     *
     * @return Returns a string representation of this MenuItem.
     */
    @Override
    public String toString() {
        return "(" + getName() + ":" + getPrice() + ")";
    }
}
