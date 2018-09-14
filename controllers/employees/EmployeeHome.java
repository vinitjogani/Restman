package controllers.employees;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import backend.ObjectManager;
import backend.employees.Employee;
import backend.employees.Server;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeHome extends BaseController {

    @FXML
    private VBox mainBox;
    @FXML
    private Label sceneTitle;

    private Employee employee;

    private Label notificationLabel;

    public EmployeeHome(Employee employee) {
        this.employee = employee;
        load("MonoBox");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sceneTitle.setText("Hi, " + employee.getName());
    }

    public void addMenuItem(String name, BaseController controller) {
        Button menuButton = new Button(name);
        menuButton.setOnAction(event -> controller.navigate(this));
        menuButton.setPrefWidth(270);
        menuButton.setPadding(new Insets(15, 15, 15, 15));
        menuButton.setFont(new Font(20));
        VBox.setMargin(menuButton, new Insets(15, 0, 15, 0));
        mainBox.getChildren().add(menuButton);
    }

    public void addPickupQueueNotifier(){
        if (employee instanceof Server){
            if (employee.getOrders().size() > 0){
                notificationLabel = new Label("There are Orders in your Pickup Queue");
                notificationLabel.setWrapText(true);
                notificationLabel.setPadding(new Insets(15, 15, 15, 15));
                notificationLabel.setFont(new Font(20));
                VBox.setMargin(notificationLabel, new Insets(15, 0, 15, 0));
                notificationLabel.setTextFill(Color.RED);
                mainBox.getChildren().add(notificationLabel);
            }
        }
    }



    @Override
    public void update() {
        if (employee instanceof Server && !(notificationLabel == null)) {
            if (employee.getOrders().size() > 0) {
                notificationLabel.setText("There are Orders in your Pickup Queue");
                notificationLabel.setTextFill(Color.RED);
            } else {
                notificationLabel.setText(".");
                notificationLabel.setTextFill(Color.TRANSPARENT);
            }
        }
    }

}
