package controllers.cells;

import backend.Table;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Table Cell handles the functionality for the TableCell view. More specifically it will set the TableCell's view
 * to show details about a specific table in the restaurant (id and number of customers).
 */
public class TableCell extends Cell {

    @FXML
    private Label tableLabel, occupiedLabel;

    private Table table;

    /**
     * Creates a TableCell for a specific table
     *
     * @param table Table
     */
    TableCell(Table table) {
        this.table = table;
        load();
    }

    /**
     * Overrides the initialize method from Initializable, to set the information about table for the TableCell view.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableLabel.setText("Table #" + table.getId());
        occupiedLabel.setText(String.valueOf(table.getCustomers().size()));
    }
}
