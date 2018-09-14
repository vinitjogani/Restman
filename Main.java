import controllers.BaseController;
import controllers.Home;
import controllers.helpers.Serializer;
import javafx.application.Application;
import javafx.stage.Stage;
import backend.Restaurant;
import backend.employees.EmployeeFactory;

public class Main extends Application {

    // Constants
    private static final String APP_TITLE = "Restman: Restaurant Management System";
    private static final String DATA_PATH = "restaurant.ser";

    // Instance variable
    private Restaurant restaurant;

    /**
     * Constructs the class by loading necessary information.
     */
    public Main() {
        restaurant = new Serializer(DATA_PATH).deserialize();
        if (restaurant == null) {
            restaurant = new Restaurant();
            new EmployeeFactory(restaurant).create("manager", "admin");
        }
    }

    /**
     * Start the application.
     *
     * @param primaryStage The primary window of the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Setup the stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMaximized(true);
        BaseController.setup(primaryStage, restaurant);

        // Serialize for persistence on exit
        primaryStage.setOnCloseRequest(event -> {
            new Serializer(DATA_PATH).serialize(restaurant);
        });

        // Initialize scene and show window
        BaseController homeController = new Home();
        homeController.show();
        primaryStage.show();
    }

    /**
     * The entry point of the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
