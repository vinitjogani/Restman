package backend.foods;

import java.io.Serializable;

/**
 * A class representing an Ingredients specific to a MenuItem. Some MenuItems share the same types of ingredients
 * (e.g. Tomatoes); however, this class specifies the maximum, minimum, and default amounts of this Ingredient that
 * the MenuItem can have. (e.g. A burger and grilled cheese both have the default of 1 cheese, but burgers
 * can have between 0 and 2 cheeses, * while grilled cheese can have between 1 and 3 cheeses...)
 * <p>
 * This uses the decorator design pattern with respect to the ingredient class.
 */
public class MenuIngredient implements Serializable {

    // Instance variables
    private Ingredient baseIngredient;
    private double maxQuantity;
    private double minQuantity;
    private double defaultQuantity;
    private double orderQuantity;

    /**
     * Create a menuIngredient with its specified base Ingredient and orderQuantity. Min, max, and default quantity
     * set to the orderQuantity.
     *
     * @param baseIngredient The Ingredient object this is based on.
     * @param quantity       orderQuantity this MenuIngredient will have. (equal to max, min, and default).
     */
    public MenuIngredient(Ingredient baseIngredient, double quantity) {
        this(baseIngredient, quantity, quantity, quantity);
    }

    /**
     * Create a menuIngredient with specified base Ingredient, min and max quantities, and OrderQuantity value
     * (also equal to default quantity)
     *
     * @param baseIngredient The Ingredient object this is based on.
     * @param minQuantity    max amount of this type of ingredient can be added to respective MenuItem.
     * @param maxQuantity    min amount of this type of ingredient can be added to respective MenuItem.
     * @param quantity       initial quantity of this ingredient.
     */
    public MenuIngredient(Ingredient baseIngredient, double minQuantity, double maxQuantity, double quantity) {
        this.baseIngredient = baseIngredient;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.orderQuantity = quantity;
        this.defaultQuantity = quantity;
    }

    /**
     * Returns the maximum quantity of this ingredient which can be in a MenuItem.
     *
     * @return Returns the maximum quantity of this ingredient that can be added.
     */
    public double getMaxQuantity() {
        return maxQuantity;
    }

    /**
     * Sets the maximum quantity that can be ordered for this ingredient.
     *
     * @param max The new maximum quantity that can be ordered of this menu ingredient.
     */
    public void setMaxQuantity(double max) {
        maxQuantity = max;
    }

    /**
     * Returns the minimum quantity of this ingredient which can be in a MenuItem.
     *
     * @return double minimum quantity of this ingredient
     */
    public double getMinQuantity() {
        return minQuantity;
    }

    /**
     * Set the minimum quantity the MenuIngredient can have for its related MenuItem.
     *
     * @param min The new minimum quantity that can be ordered of this menu ingredient.
     */
    public void setMinQuantity(double min) {
        minQuantity = min;
    }

    /**
     * Returns the default quantity of this ingredient which is in a MenuItem before changes are made.
     *
     * @return double default quantity of this ingredient in this MenuItem
     */
    public double getDefaultQuantity() {
        return defaultQuantity;
    }

    /**
     * Sets the default quantity for this MenuIngredient when a MenuItem using it is created.
     *
     * @param newQuantity The new default quantity for the ingredient.
     */
    public void setDefaultQuantity(double newQuantity) {
        if (!isValidQuantity(newQuantity)) return;
        if (orderQuantity == defaultQuantity) orderQuantity = newQuantity;
        defaultQuantity = newQuantity;
    }

    /**
     * Returns the ordered quantity of this ingredient in a MenuItem.
     *
     * @return Returns the ordered quantity of this ingredient in a MenuItem.
     */
    public double getQuantity() {
        return orderQuantity;
    }

    /**
     * Getter for the base ingredient.
     *
     * @return Returns the base ingredient.
     */
    public Ingredient getBaseIngredient() {
        return baseIngredient;
    }

    /**
     * Sets the ordered quantity of this ingredient if it is valid.
     *
     * @param quantity The new quantity of the ingredient.
     * @return Returns whether the change was made successfully.
     */
    public boolean setQuantity(double quantity) {
        if (isValidQuantity(quantity)) {
            this.orderQuantity = quantity;
            return true;
        } else return false;
    }

    /**
     * Return the extra cost of any extra amounts of this ingredient added to an Order.
     *
     * @return Returns the extra cost for this ingredient based on its unit cost and extra order quantity.
     */
    public double getExtraCost() {
        return orderQuantity > defaultQuantity ? (orderQuantity - defaultQuantity) * baseIngredient.getUnitCost() : 0.0;
    }

    /**
     * Check if the quantity asked is within the max and min allowed quantities.
     *
     * @param quantity The quantity to check the validity of.
     * @return Returns whether this quantity is a valid quantity.
     */
    public boolean isValidQuantity(double quantity) {
        return quantity <= maxQuantity && quantity >= minQuantity;
    }

    /**
     * String representation of this MenuIngredient.
     *
     * @return Returns the name of the base ingredient.
     */
    @Override
    public String toString() {
        return baseIngredient.getName();
    }

    /**
     * Returns an exact copy of this MenuIngredient.
     *
     * @return Returns an exact copy of this MenuIngredient.
     */
    public MenuIngredient copy() {
        return new MenuIngredient(baseIngredient, minQuantity, maxQuantity, defaultQuantity);
    }

    /**
     * Checks to see if this MenuIngredient is the same as another. If their parents are the same and they have
     * all the same quantities (min, max, default, order), then they are the same.
     *
     * @param obj other object to be compared to
     * @return Returns whether the two objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MenuIngredient)) return false;
        MenuIngredient ingredient = (MenuIngredient) obj;
        return super.equals(ingredient) && minQuantity == ingredient.getMinQuantity() &&
                maxQuantity == ingredient.getMaxQuantity() && orderQuantity == ingredient.getQuantity() &&
                defaultQuantity == ingredient.getDefaultQuantity();
    }
}
