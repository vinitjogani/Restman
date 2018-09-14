package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import backend.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The BaseController class is an abstract class from which all other controllers inherit. It
 * handles the FXML loading functionality, as well as navigation for controllers.
 */
public abstract class BaseController implements Initializable {

    // Static variables
    private static Stage stage;
    private static Restaurant restaurant;
    private static List<BaseController> history = new ArrayList<>();
    // Instance variable
    private Parent root;

    /**
     * Static method to setup the stage and the restaurant for all controllers.
     *
     * @param stage      The primary stage of the JavaFX application.
     * @param restaurant The restaurant object.
     */
    public static void setup(Stage stage, Restaurant restaurant) {
        BaseController.stage = stage;
        BaseController.restaurant = restaurant;
    }

    /**
     * Getter for restaurant.
     *
     * @return Returns the restaurant object provided in setup.
     */
    protected static Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Overloaded method to load the FXML file from the class name.
     */
    protected void load() {
        load(getClass().getSimpleName());
    }

    /**
     * Overloaded method to load the FXML file from the name specified.
     *
     * @param viewName The name of the view to load.
     */
    protected void load(String viewName) {
        String viewPath = "../layouts/" + viewName + ".fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BaseController.class.getResource(viewPath));
            loader.setController(this);
            root = loader.load();
        } catch (IOException | IllegalStateException ignored) {
        }
    }

    /**
     * Displays this controllers's view on the stage.
     */
    private void display() {
        if (root != null && stage != null) {
            if (stage.getScene() == null) {
                stage.setScene(new Scene(root));
                stage.getScene().getStylesheets().add(
                        getClass().getResource("../layouts/styles.css").toExternalForm()
                );
            } else {
                stage.getScene().setRoot(root);
            }
            update();
        }
    }

    /**
     * Displays this controllers's view and clears navigation history.
     */
    public void show() {
        display();
        history.clear();
    }

    /**
     * Navigates to this controllers's view from another controllers.
     *
     * @param current The current controllers to go "back" to.
     */
    public void navigate(BaseController current) {
        display();
        history.add(current);
    }

    /**
     * Goes back to the last controllers in navigation history.
     */
    public void back() {
        if (history.size() > 0) {
            history.remove(history.size() - 1).display();
        }
    }

    /**
     * Empty method to be overridden to update the view when someone navigates back to it.
     */
    public void update() {
    }

    /**
     * Overrides the initialize method to automatically update the controllers's view.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }
}
