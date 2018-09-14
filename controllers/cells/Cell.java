package controllers.cells;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The cell class provides some common functionality for all list and accordion cells.
 */
public class Cell extends BaseController {

    // FXML variables
    @FXML
    protected Node cellContainer;

    /**
     * Initializes the Cell by adding double click listener on the container.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        cellContainer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) cellDoubleClick();
        });
    }

    /**
     * Loads a view from the cells directory instead of parent directory.
     */
    @Override
    protected void load() {
        super.load("cells/" + getClass().getSimpleName());
    }

    /**
     * Gets the main cell container to set graphic.
     *
     * @return Returns the cell container node.
     */
    public Node getBox() {
        return cellContainer;
    }

    /**
     * Method that child classes can override to catch double click on the cell.
     */
    protected void cellDoubleClick() {
    }
}
