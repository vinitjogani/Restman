package controllers.employees.shared;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The ShowBill class contains a scrollable list of the overall bill for a table.
 */
public class ShowBill extends BaseController {

    // FXML variables
    @FXML
    private VBox mainBox;

    // Instance variables
    private GridPane bill;

    /**
     * Constructs the ShowBill screen with the specified bill.
     *
     * @param bill The bill gridpane to be displayed.
     */
    ShowBill(GridPane bill) {
        this.bill = bill;
        load("MonoBox");
    }

    /**
     * Overrides the initialize method from Initializable, to set the values for the scene components.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScrollPane pane = new ScrollPane(bill);
        pane.setPadding(new Insets(50, 50, 50, 50));
        mainBox.getChildren().add(pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
    }
}
