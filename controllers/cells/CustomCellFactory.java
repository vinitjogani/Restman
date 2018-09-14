package controllers.cells;

import backend.Customer;
import backend.Table;
import backend.employees.Cook;
import backend.employees.Employee;
import backend.foods.MenuIngredient;
import backend.foods.MenuItem;
import backend.foods.Order;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * A Factory for creating cells within the program. This factory handles the logic for deciding what cell to create.
 * The cell to create depends on what object the cell represents and for which employee's display screen.
 *
 * @param <T> Type of object the Cell will represent
 */
public class CustomCellFactory<T> extends ListCell<T> {

    private Employee employee;

    /**
     * Creates a CustomCellFactory with specific employee.
     *
     * @param employee The employee who is calling for cell creation.
     */
    public CustomCellFactory(Employee employee) {
        this.employee = employee;
    }

    /**
     * Creates options to add and delete for an object for a view.
     *
     * @param title Title of option
     * @return Menuitem (if applicable)
     */
    public javafx.scene.control.MenuItem addOption(String title) {
        javafx.scene.control.MenuItem item = new javafx.scene.control.MenuItem(title);
        if (this.getContextMenu() == null) this.setContextMenu(new ContextMenu());
        this.getContextMenu().getItems().add(item);
        return item;
    }

    /**
     * Overrides the super class Cell's updateItem to update the cell that is craeted.
     *
     * @param item  item to update
     * @param empty true if cell is empty
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        Cell cell = getCell(item);
        if (!empty && cell != null) setGraphic(cell.getBox());
        else setGraphic(new HBox());
    }

    /**
     * Returns the cell appropriate for the Employee this CustomCellFactory is for and the object that
     * the Cell will represent
     *
     * @param item object the cell will represent
     * @return the appropriate cell
     */
    private Cell getCell(T item) {
        if (item instanceof Customer) {
            return new CustomerCell((Customer) item);
        } else if (item instanceof MenuIngredient) {
            return new IngredientCell((MenuIngredient) item);
        } else if (item instanceof Employee) {
            return new EmployeeCell((Employee) item);
        } else if (item instanceof Order && employee instanceof Cook) {
            return new CookOrderCell((Order) item);
        } else if (item instanceof Order) {
            return new OrderCell((Order) item);
        } else if (item instanceof MenuItem && employee != null) {
            return new MenuCell((MenuItem) item, employee);
        } else if (item instanceof Table) {
            return new TableCell((Table) item);
        }
        return null;
    }
}
