package backend.foods;

/**
 * An order consisting of a single MenuItem. Order can be modified, by for instance modifying the
 * amount of ingredient quantities. Total price, notes and state can also be retrieved. It extends MenuItem
 * which is an observable - making an Order easily observable by all interested parties.
 */
public class Order extends MenuItem {

    // Instance variables
    private int id;
    private String notes;  // Additional notes from server
    private State state;

    /**
     * Constructs an Order.
     *
     * @param menuItem the MenuItem which has been ordered by the Customer
     */
    public Order(int id, MenuItem menuItem) {
        super(menuItem.getName(), menuItem.getPrice());
        this.notes = "";
        this.state = State.CONSTRUCTING;
        this.id = id;
        for (MenuIngredient i : menuItem.getIngredients()) {
            getIngredients().add(i.copy());
        }
    }

    /**
     * Returns the order id
     *
     * @return returns the integer id of this Order
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the total price of this Order including the
     * price of the MenuItem and also price of additional ingredients
     *
     * @return double total price of this Order
     */
    public double getPrice() {
        double totalPrice = super.getPrice();
        for (MenuIngredient i : getIngredients()) {
            totalPrice += i.getExtraCost();
        }
        return totalPrice;
    }

    /**
     * Returns the price for the MenuItem of this Order.
     *
     * @return Double price
     */
    public double getMenuItemPrice() {
        return super.getPrice();
    }

    /**
     * Sets the quantity of an Ingredient to a specific quantity (if valid)
     *
     * @param ingredientName Name of the ingredient to be modified.
     * @param quantity       Quantity which this ingredient needs to be modified to.
     */
    public void setIngredientQuantity(String ingredientName, double quantity) {
        MenuIngredient ingredient = getIngredient(ingredientName);
        if (ingredient != null && ingredient.isValidQuantity(quantity))
            getIngredient(ingredientName).setQuantity(quantity);
    }

    /**
     * Adds a note to the order regarding preparation instructions about the order for the chef.
     *
     * @param note String
     */
    public void addNote(String note) {
        notes += "\n" + note;
    }

    /**
     * Gets the notes for the Order.
     *
     * @return String note
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the state that the Order is in (one of the enums at the bottom of the file).
     *
     * @return State
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the order.
     *
     * @param s The state. One of PLACED, PREPARING, READY, DELIVERED.
     */
    public void setState(State s) {
        this.state = s;
        notifyObservers(s);
    }

    /**
     * Returns a string representation of this Order giving
     * the Order name and price
     *
     * @return String stating Order name and price
     */
    @Override
    public String toString() {
        return getName() + " ($" + getPrice() + ")";
    }

    /**
     * Returns whether a given Object is equal to this Order
     * based on class instance and Order ID
     *
     * @param obj Object being passed in to be compared
     * @return Returns true
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Order && ((Order) obj).getId() == getId();
    }

    /**
     * States that the order can be in.
     */
    public enum State {
        CONSTRUCTING,
        PLACED,
        PREPARING,
        READY,
        DELIVERED,
        REMAKE,
        CANCEL
    }
}