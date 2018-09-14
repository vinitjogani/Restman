package backend.foods;

import java.io.Serializable;

/**
 * A class representing an ingredient. Each ingredient can be modified in quantity, unit price and
 * a threshold value for which it will be automatically placed in the requests.txt
 */
public class Ingredient implements Serializable {

    // Instance variables
    private String name;
    private double unitCost;
    private double threshold;

    /**
     * Create an ingredient with a name and default cost of 0.
     *
     * @param name Name of ingredient.
     */
    public Ingredient(String name) {
        this(name, 0.0);
    }

    /**
     * Create an ingredient with name and specified cost. Default threshold of 10.
     *
     * @param name     Name of ingredient.
     * @param unitCost Cost per unit of ingredient.
     */
    public Ingredient(String name, double unitCost) {
        this(name, unitCost, 10.0);
    }

    /**
     * Create an ingredient with name and specified cost and specified threshold.
     *
     * @param name      Name of ingredient.
     * @param unitCost  Cost per unit of ingredient.
     * @param threshold Threshold amount for when this ingredient needs to be ordered.
     */
    public Ingredient(String name, double unitCost, double threshold) {
        this.name = name.toLowerCase();
        this.unitCost = unitCost;
        this.threshold = threshold;
    }

    /**
     * Getter which returns the name of this ingredient.
     *
     * @return String name of the ingredient.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the unit cost of this ingredient.
     *
     * @return Returns the double unit cost of the ingredient.
     */
    public double getUnitCost() {
        return this.unitCost;
    }

    /**
     * Sets the unit cost of this ingredient.
     *
     * @param newCost New unit cost for ingredient.
     */
    public void setUnitCost(double newCost) {
        unitCost = newCost;
    }

    /**
     * Get the threshold for this ingredient
     *
     * @return Returns the threshold value.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold for this ingredient.
     *
     * @param newThreshold New threshold value.
     */
    public void setThreshold(double newThreshold) {
        threshold = newThreshold;
    }

    /**
     * Returns true if the other object is an ingredient with the same name, or a string of the same name as this
     * Ingredient
     *
     * @param obj Other object to be compared to
     * @return Returns whether equality was met.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Ingredient && ((Ingredient) obj).getName().equals(getName())) ||
                (obj instanceof String && obj.equals(getName()));
    }

    /**
     * Gets the hashcode for the Ingredient
     *
     * @return Returns the name hashcode for use in hashmap.
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Returns the string name of the Ingredient.
     *
     * @return Returns the name of the ingredient.
     */
    @Override
    public String toString() {
        return getName();
    }

}
