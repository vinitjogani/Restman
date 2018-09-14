package controllers.employees.manager;

import backend.employees.Employee;
import backend.foods.Ingredient;
import backend.foods.MenuIngredient;
import backend.foods.MenuItem;
import controllers.BaseController;
import controllers.SearchController;
import controllers.cells.CustomCellFactory;
import controllers.cells.MenuIngredientCell;
import controllers.helpers.InputDialogFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The MenuManager class is used to create, read, update and delete menu items offered by the restaurant.
 */
public class MenuManager extends SearchController<MenuItem> {

    // FXML variables
    @FXML
    private ListView<MenuItem> primaryList;
    @FXML
    private Accordion secondaryList;
    @FXML
    private Label sceneTitle, optionsLabel;
    @FXML
    private Button actionButton;
    @FXML
    private HBox optionsBox;

    // Instance variables
    private Employee employee;

    public MenuManager(Employee employee) {
        super((i, s) -> i.getName().toLowerCase().trim().contains(s));
        this.employee = employee;
        load("ListAccordion");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components, and adds
     * context menu to primary list items.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actionButton.setText("New");
        sceneTitle.setText("Menu Manager");
        primaryList.setCellFactory(param -> {
            CustomCellFactory<MenuItem> cell = new CustomCellFactory<>(employee);
            cell.addOption("Delete this item").setOnAction(e -> {
                if (cell.getItem() != null) getRestaurant().getMenu().remove(cell.getItem());
                update();
            });
            return cell;
        });
        primaryList.setOnMouseClicked(event -> primaryClick());
        primaryList.refresh();
    }

    /**
     * Update the items of the primary list and add items to second list based on selection.
     */
    @Override
    public void update() {
        primaryList.refresh();
        primaryList.setItems(FXCollections.observableList(filter(getRestaurant().getMenu())));
    }

    /**
     * Handle click on primary list.
     */
    public void primaryClick() {
        MenuItem menuItem = primaryList.getSelectionModel().getSelectedItem();
        if (menuItem != null) {
            secondaryList.getPanes().clear();
            for (MenuIngredient i : menuItem.getIngredients()) {
                TitledPane pane = new TitledPane(
                        i.toString().substring(0, 1).toUpperCase() + i.toString().substring(1),
                        new MenuIngredientCell(i).getBox()
                );

                ContextMenu contextMenu = new ContextMenu();
                javafx.scene.control.MenuItem delete = new javafx.scene.control.MenuItem("Delete this ingredient");
                delete.setOnAction(e -> {
                    menuItem.getIngredients().remove(i);
                    secondaryList.getPanes().clear();
                    update();
                });
                contextMenu.getItems().add(delete);
                pane.setContextMenu(contextMenu);

                secondaryList.getPanes().add(pane);
            }

            if (optionsBox.getChildren().size() == 1) {
                optionsLabel.setText("Change ingredients");
                Button newIngredient = new Button("Add");
                newIngredient.setOnAction(event -> addIngredient());
                optionsBox.getChildren().addAll(newIngredient);
            }
        }
    }

    /**
     * Add new menu ingredient to the selected menu item.
     */
    private void addIngredient() {
        MenuItem selectedItem = primaryList.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Ingredient baseIngredient = new InputDialogFactory(
                "Select ingredient", "", "Select an ingredient from inventory:"
        ).getChoice(BaseController.getRestaurant().getInventory().getInventoryIngredients());
        if (baseIngredient == null) return;
        if (selectedItem.getIngredient(baseIngredient.getName()) == null) {
            selectedItem.getIngredients().add(new MenuIngredient(baseIngredient, 0));
            primaryClick();
        } else
            new Alert(Alert.AlertType.ERROR, "The menu item already has this ingredient!").showAndWait();
    }

    /**
     * Create a new menu item on action button click.
     */
    @FXML
    public void action() {
        String itemName = new InputDialogFactory(
                "New Menu Item",
                "Create a new menu item",
                "Enter the name of the new item: "
        ).getString();

        if (itemName != null && !itemName.trim().equals("")) {
            MenuItem newItem = new MenuItem(itemName.trim(), 0);
            getRestaurant().getMenu().add(newItem);
            primaryList.getItems().add(newItem);
        }
        update();
    }
}