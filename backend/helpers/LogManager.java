package backend.helpers;

import backend.employees.Employee;
import backend.foods.Ingredient;
import backend.foods.Order;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The log manager is a Singleton class keeps track of any important data changes
 * which occur at the restaurant. These include any change to the inventory and records
 * of when an order is placed, prepared, delivered or cancelled. The data collected through
 * this class is further used for analysis in the DataManager class
 */

public class LogManager {

    private static File logFile = new File("log.txt");

    private static LogManager instance = new LogManager();

    public static LogManager getInstance() {
        return instance;
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd | HH:mm:ss");

    /**
     * Constructor for the LogManager class
     */
    private LogManager() {
        try {
            logFile.createNewFile();
        } catch (IOException ignored) {
        }
    }

    /**
     * Logs in a specified piece of text into log.txt. This method is used as a
     * helper in the other log methods.
     *
     * @param text String piece of text which needs to be logged
     * @return boolean true or false depending on if the text was logged properly
     */
    private boolean log(String text) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(logFile, true));
            fileWriter.write(text + System.lineSeparator());
            fileWriter.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Logs in any change to the state of an order. This includes order placed, ready,
     * prepared and delivered.
     *
     * @param o Order whose state must be logged
     * @param e Employee corresponding to the specific change of state of this order
     * @return boolean true or false based on if the log went through properly
     */
    public boolean log(Order o, Employee e) {
        String note = !o.getNotes().trim().equals("") ?
                " | Note: " + o.getNotes().trim().replace("\n", "; ") : "";

        return log(dateFormat.format(new Date()) + " | ORDER " + o.getState() +
                " | " + e.toString() + " | Order ID:" + o.getId() +
                " | Ordered Item: " + o.getName() + " | Order Price: " + Math.round(o.getPrice() * 100.0) / 100.0 +
                note);
    }

    /**
     * Logs in any  particular change to the Inventory. This includes when ingredients
     * are added into or removed from the inventory
     *
     * @param ingredient  Ingredient whose inventory quantity is changed
     * @param oldQuantity the origianl quantity of the ingredient
     * @param newQuantity the new quantity of the ingredient upon update
     * @return Boolean true or false depending on if the Inventory change was logged properly
     */
    public boolean log(Ingredient ingredient, double oldQuantity, double newQuantity) {
        // Round numbers to two decimal places
        newQuantity = Math.round(newQuantity * 100) / 100.0;
        oldQuantity = Math.round(oldQuantity * 100) / 100.0;

        if (newQuantity - oldQuantity == 0) return false;

        return log(dateFormat.format(new Date()) + " | INGREDIENT " +
                (newQuantity > oldQuantity ? "ADDED" : "USED") + " | Old Quantity: " +
                oldQuantity +
                " | New Quantity: " + newQuantity + " | Change: " +
                Math.round(Math.abs(newQuantity - oldQuantity) * 100.0) / 100.0 + " | Cost: "
                + Math.round(Math.abs(newQuantity - oldQuantity) *
                ingredient.getUnitCost() * 100.0) / 100.0);
    }

    /**
     * Logs in whenever a customer is seated at a table.
     *
     * @param tableNumber  int tablenumber at which the customer is seated
     * @param numCustomers int number of customer seated atthe table
     * @return boolean true or false based on whether it was logged correctly
     */
    public boolean log(int tableNumber, int numCustomers) {

        return log(dateFormat.format(new Date()) + " | CUSTOMERS SEATED |" + " " +
                "Number of Customers: " +
                numCustomers + " | Table Number: " + tableNumber);
    }
}
