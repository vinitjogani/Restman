package controllers.employees.shared;

import backend.Inventory;
import backend.employees.Employee;
import backend.employees.Manager;
import backend.foods.Ingredient;
import controllers.SearchController;
import controllers.cells.InventoryCell;
import controllers.helpers.InputDialogFactory;
import controllers.helpers.StringHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;

/**
 * The InventoryManager screen gives the ability to create, read, update and delete ingredients from the
 * inventory.
 */
public class InventoryManager extends SearchController<Ingredient> {

    // Instance variables
    private Inventory inventory;
    private Employee employee;

    // FXML variables
    @FXML
    private Accordion ingredientList;

    /**
     * Constructs an inventory manager screen with specified employee.
     *
     * @param employee The employee accessing this screen.
     */
    public InventoryManager(Employee employee) {
        super((p, s) -> p.getName().toLowerCase().trim().contains(s));

        this.inventory = getRestaurant().getInventory();
        this.employee = employee;
        load();
    }

    /**
     * Updates the list of ingredients on navigation or on calls to update.
     */
    @Override
    public void update() {
        ingredientList.getPanes().clear();
        for (Ingredient i : filter(inventory.getInventoryIngredients())) {
            TitledPane pane = new TitledPane(new StringHelper().capitalize(i.getName()),
                    new InventoryCell(i, inventory, employee instanceof Manager).getBox());

            ContextMenu contextMenu = new ContextMenu();
            MenuItem delete = new MenuItem("Delete this ingredient");
            delete.setOnAction(e -> {
                inventory.getInventoryIngredients().remove(i);
                update();
            });
            contextMenu.getItems().add(delete);
            pane.setContextMenu(contextMenu);

            ingredientList.getPanes().add(pane);
        }
    }

    /**
     * Adds a new ingredient to the list on action button click (if it doesn't exist already).
     */
    @FXML
    public void action() {
        String ingredientName = new InputDialogFactory(
                "New ingredient", "Create a new ingredient", "Ingredient name: "
        ).getString();
        if (ingredientName != null)
            inventory.getIngredient(ingredientName);
        update();
    }
}
