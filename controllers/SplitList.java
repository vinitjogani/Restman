package controllers;

import controllers.cells.CustomCellFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import backend.employees.Employee;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiPredicate;

/**
 * The SplitList class is a parent class to a repetitively used SplitList view. This offers direct access to
 * FXML nodes and does most of the setup of the two lists in the view.
 *
 * @param <T> The tpe of objects in the primary list.
 * @param <E> The type of objects in the secondary list.
 */
public abstract class SplitList<T, E> extends SearchController<T> {

    // FXML variables
    @FXML
    protected Label sceneTitle, optionsLabel;
    @FXML
    protected Button actionButton;
    @FXML
    protected ListView<T> primaryList;
    @FXML
    protected ListView<E> secondaryList;
    @FXML
    private HBox optionsBox;

    // Instance variables
    private Employee employee;
    private int initialOptions;
    private List<Node> extraControls;

    /**
     * Constructs a SplitList controllers.
     *
     * @param employee        The employee using the list.
     * @param searchPredicate The search predicate used for filtering results.
     */
    public SplitList(Employee employee, BiPredicate<T, String> searchPredicate) {
        super(searchPredicate);
        extraControls = new ArrayList<>();
        this.employee = employee;
    }

    /**
     * Initializes the view by setting cell factories.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        primaryList.setCellFactory(param -> new CustomCellFactory<>(employee));
        secondaryList.setCellFactory(param -> new CustomCellFactory<>(employee));

        primaryList.setOnMouseClicked(event -> {
            T item = primaryList.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && item != null) primaryDoubleClick(item);
            else if (item != null) primarySingleClick(item);
            else update();
        });

        initialOptions = optionsBox.getChildren().size();
    }

    /**
     * Updates the view on change in navigation.
     */
    @Override
    public void update() {
        primaryList.refresh();
        secondaryList.refresh();

        optionsLabel.setText("");
        if (optionsBox.getChildren().size() > initialOptions)
            optionsBox.getChildren().remove(initialOptions, optionsBox.getChildren().size());
        secondaryList.getItems().clear();
    }

    /**
     * Adds an extra control to the optionsBox.
     *
     * @param control The button to be added.
     * @param handler The event handler for button click.
     */
    protected void addExtraControl(Button control, EventHandler<ActionEvent> handler) {
        control.setOnAction(handler);
        extraControls.add(control);
    }

    /**
     * To be overridden by child classes to optionally handle a double click on an item in primary list.
     *
     * @param item The item that was double-clicked on.
     */
    protected void primaryDoubleClick(T item) {
    }

    /**
     * To be overridden by child classes to optionally handle a single click on an item in primary list.
     *
     * @param item The item that was single-clicked on.
     */
    protected void primarySingleClick(T item) {
        if (optionsBox.getChildren().size() == initialOptions) optionsBox.getChildren().addAll(extraControls);
    }

}
