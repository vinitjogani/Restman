package backend;

import controllers.helpers.StringHelper;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import backend.foods.MenuIngredient;
import backend.foods.Order;

/**
 * The Bill class represents the bill for a specific table (all orders made by the customer at a specific table).
 * The Bill class will aggregate the orders made by customers at the table, and calculates the total cost (along with
 * any additional costs of extra ingredients). It then returns a GUI-usable GridPane for appropriate layout.
 */
public class Bill {

    private Table table;

    /**
     * Constructor for the Bill class.
     *
     * @param table table this instance of Bill will be the bill for.
     */
    Bill(Table table) {
        this.table = table;
    }

    /**
     * Generates the total bill for this customer with additional costs in the order.
     *
     * @param customer customer the bill is for
     * @return double total cost of the customers orders
     */
    private double getCustomerTotal(Customer customer) {
        double totalCustomerBill = 0.00;
        for (Order order : customer.getConfirmedOrders()) {
            totalCustomerBill += order.getPrice();
        }
        return Math.round(totalCustomerBill * 100.0) / 100.0;
    }

    /**
     * Generates the total bill for all customers at this table
     *
     * @return double bill of this table
     */
    private double getTableTotal() {
        double totalTableBill = 0.00;
        for (Customer customer : table.getCustomers()) {
            totalTableBill += getCustomerTotal(customer);
        }
        return Math.round(totalTableBill * 100.0) / 100.0;
    }

    /**
     * Helper method to repeat a specified string a specific number of times
     *
     * @param s     string to repeat
     * @param times number of times you want to repeat the string
     * @return the string repeated
     */
    private String repeat(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    /**
     * Generates a comprehensive bill which shows the specific bill details of each customer
     * along with their order and including additional orders. Also shows the bill for the entire table
     * leaving the option of splitting or paying the total to the table to decide
     *
     * @return Returns a GridPane representation of the table's bill
     */
    public GridPane getTableBill() {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-font-size:18px;");
        gridPane.setHgap(20);
        gridPane.add(new Label("Bill for table #" + table.getId()), 0, 0);

        int row = 0;
        for (Customer c : table.getCustomers()) {
            if (c.getConfirmedOrders().size() != 0)
                row = addCustomerBill(gridPane, c, row);
        }

        addExtraCosts(gridPane, getTableTotal(), row, 0);
        return gridPane;
    }

    /**
     * Helper method for generating the portion of the bill specific to each individual customer, which includes
     * the customer ID followed by their orders and any specific extra ingredients added to the bill.
     *
     * @param gridPane Returns a GridPane representation of the customer's bill.
     * @param customer Customer this bill is for.
     * @param row      the current row to add this customer's bill to (used for when there is more than one customer
     *                 on a bill).
     * @return Returns the current row the bill is on (for use of next bill components)
     */
    private int addCustomerBill(GridPane gridPane, Customer customer, int row) {
        gridPane.add(new Label("\t" + repeat("-", 20)), 0, ++row);
        gridPane.add(new Label("\tCustomer #" + customer.getId()), 0, ++row);
        gridPane.add(new Label("\t" + repeat("-", 20)), 0, ++row);

        for (Order o : customer.getConfirmedOrders()) {
            String name = new StringHelper().capitalize(o.getName());
            gridPane.add(new Label("\t\t" + name + " (order #" + o.getId() + ")"), 0, ++row);
            gridPane.add(new Label(String.format("$%.2f", o.getMenuItemPrice())), 2, row);

            for (MenuIngredient i : o.getIngredients()) {
                if (i.getExtraCost() == 0.0) continue;
                gridPane.add(new Label("\t\t\textra " + i.toString()), 0, ++row);
                gridPane.add(new Label(String.format("$%.2f", i.getExtraCost())), 2, row);
            }
        }

        return addExtraCosts(gridPane, getCustomerTotal(customer), row, 1);
    }

    /**
     * Adds the taxes and gratuity to the bill.
     *
     * @param gridPane Returns a GridPane representation of the final 'total' and tax section of the bill.
     * @param subtotal Total of all cost orders on the bill before taxes.
     * @param row      Current row index the bill is on.
     * @param tab      Tab amount for spacing.
     * @return Returns the new row index;
     */
    private int addExtraCosts(GridPane gridPane, double subtotal, int row, int tab) {
        gridPane.add(new Label(repeat("\t", tab) + "Subtotal"), 0, ++row);
        gridPane.add(new Label(String.format("$%.2f", subtotal)), 2, row);

        gridPane.add(new Label(repeat("\t", tab) + "HST (13%)"), 0, ++row);
        gridPane.add(new Label(String.format("$%.2f", subtotal * 0.13)), 2, row);

        gridPane.add(new Label(repeat("\t", tab) + "Gratuity (15%)"), 0, ++row);
        gridPane.add(new Label(String.format("$%.2f", subtotal * 0.15)), 2, row);

        gridPane.add(new Label(repeat("\t", tab) + "Total"), 0, ++row);
        gridPane.add(new Label(String.format("$%.2f", subtotal * 1.28)), 2, row);

        gridPane.add(new Label(" "), 0, ++row);

        return row;
    }
}
