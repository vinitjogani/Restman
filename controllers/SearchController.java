package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * This class offers functionality for searching/filtering objects in lists using a search predicate.
 *
 * @param <T> The type of parameters that need to be searched.
 */
public abstract class SearchController<T> extends BaseController {

    // Instance variable
    private BiPredicate<T, String> searchPredicate;

    // FXML variables
    @FXML
    private TextField searchField;

    /**
     * Constructs the search controllers with a search BiPredicate.
     *
     * @param searchPredicate Given a search String, this should return whether the <T> item matches it.
     */
    public SearchController(BiPredicate<T, String> searchPredicate) {
        this.searchPredicate = searchPredicate;
    }

    /**
     * This method is called onKeyPressed on searchFields and is used to automatically update the view
     * when someone presses enter on search.
     *
     * @param event The key event related to KeyPress on search field.
     */
    @FXML
    public void search(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            update();
        }
    }

    /**
     * Filters a list using the provided search predicate and searchField string.
     *
     * @param mainList The main list to be filtered.
     * @return Returns a filtered list of items that match the search criteria.
     */
    protected List<T> filter(Collection<T> mainList) {
        List<T> searchResults = new ArrayList<>(mainList);
        searchResults.removeIf(x -> !searchPredicate.test(x, searchField.getText().toLowerCase().trim()));
        return searchResults;
    }
}
