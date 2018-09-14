package controllers.employees.manager;

import backend.helpers.DataManager;
import controllers.BaseController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StatsViewer extends BaseController {

    // FXML variables
    @FXML
    private VBox secondaryBox;
    @FXML
    private DatePicker startDate, endDate;

    // Constants
    private final String[] WEEK_DAYS = {"ALL", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};

    // Instance variables
    private Map<String, Number> data;
    private List<Node> charts;
    private int chartType;
    private ComboBox<String> dropDown;

    /**
     * Constructs teh StatsViewer screen.
     */
    public StatsViewer() {
        load("DataManager");
        data = new HashMap<>();
        charts = new ArrayList<>();
    }

    /**
     * Gets the xAxis of week days.
     *
     * @return Returns the CategoryAxis.
     */
    private CategoryAxis getXAxis() {
        CategoryAxis xAxis = new CategoryAxis(
                FXCollections.observableList(Arrays.asList(WEEK_DAYS).subList(1, WEEK_DAYS.length))
        );
        xAxis.setLabel("Day of week");
        return xAxis;
    }

    /**
     * Creates a y-axis with a specified label.
     *
     * @param name The axis label.
     * @return Returns the number axis.
     */
    private NumberAxis getYAxis(String name) {
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(name);
        return yAxis;
    }

    /**
     * Initializes the date picker values of the screen.
     *
     * @param location  The location it was initialized with.
     * @param resources The resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        startDate.setValue(LocalDate.of(2018, 1, 1));
        endDate.setValue(LocalDate.now());
        endDate.setOnAction(event -> checkValidEndDate());
    }

    /**
     * Creates a XYChart.Series out of the data HashMap.
     *
     * @param name The name of the new series.
     * @return Returns the created series from data.
     */
    private XYChart.Series<String, Number> getSeries(String name) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String key : data.keySet()) {
            series.getData().add(new XYChart.Data<>(key, data.get(key)));
        }
        series.setName(name);
        return series;
    }

    /**
     * Creates the busy hours per day of week chart.
     */
    @FXML
    public void busy() {
        chartType = 1;
        if (charts.size() > 0) secondaryBox.getChildren().removeAll(charts);
        if (!(secondaryBox.getChildren().contains(dropDown))) {
            dropDown = new ComboBox<>();
            dropDown.getItems().setAll(WEEK_DAYS);
            dropDown.getSelectionModel().select(0);
            dropDown.valueProperty().addListener((observable, oldValue, newValue) -> busy(newValue));
            dropDown.setPrefWidth(200);
            VBox.setMargin(dropDown, new Insets(10, 10, 10, 10));
            charts.add(dropDown);
            secondaryBox.getChildren().add(dropDown);
            busy("ALL");
        }
    }

    /**
     * Creates the busy hours chart on the given weekday.
     *
     * @param weekday The weekday to get busy hours of.
     */
    public void busy(String weekday) {
        chartType = 1;
        data = new DataManager(startDate.getValue(), endDate.getValue().plusDays(1))
                .getHourToCustomerAverage(weekday);
        XYChart<String, Number> customersChart = new BarChart<>(
                new CategoryAxis(), getYAxis("Number of customers"));
        customersChart.getData().add(getSeries("Customer count"));
        secondaryBox.requestFocus();

        if (charts.size() > 0) secondaryBox.getChildren().removeAll(charts);
        charts.clear();
        charts.add(customersChart);
        charts.add(dropDown);
        secondaryBox.getChildren().addAll(dropDown, customersChart);
    }

    /**
     * Creates a pie chart of consumption of different menu items.
     */
    @FXML
    public void consumption() {
        chartType = 2;
        data = new DataManager(startDate.getValue(), endDate.getValue().plusDays(1))
                .getConsumptionData();
        List<PieChart.Data> series = new ArrayList<>();
        for (String key : data.keySet()) series.add(new PieChart.Data(key, data.get(key).doubleValue()));
        PieChart salesChart = new PieChart(FXCollections.observableList(series));
        salesChart.setTitle("Consumption customers");

        if (charts.size() > 0) secondaryBox.getChildren().removeAll(charts);
        charts.clear();
        charts.add(salesChart);
        secondaryBox.getChildren().add(salesChart);
    }

    /**
     * Creates a line chart of sales values.
     */
    @FXML
    private void sales() {
        chartType = 3;
        data = new DataManager(startDate.getValue(), endDate.getValue().plusDays(1))
                .getWeekdayToSales();
        XYChart<String, Number> salesChart = new LineChart<>(
                getXAxis(), getYAxis("Amount of sales")
        );
        salesChart.getData().add(getSeries("Sales value"));

        if (charts.size() > 0) secondaryBox.getChildren().removeAll(charts);
        charts.clear();
        charts.add(salesChart);
        secondaryBox.getChildren().add(salesChart);
    }

    /**
     * Creates a bar chart of number of customers coming on different days of week.
     */
    @FXML
    public void customers() {
        chartType = 4;

        data = new DataManager(startDate.getValue(), endDate.getValue().plusDays(1))
                .getWeekdayToCustomers();
        XYChart<String, Number> customersChart = new BarChart<>(
                getXAxis(), getYAxis("Number of customers")
        );
        customersChart.getData().add(getSeries("Customer count"));

        if (charts.size() > 0) secondaryBox.getChildren().removeAll(charts);
        charts.clear();
        charts.add(customersChart);
        secondaryBox.getChildren().add(customersChart);
    }

    /**
     * Checks if an end date is valid, and if not, sets it to a week after start date.
     */
    private void checkValidEndDate() {
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        if (end.isBefore(start)) {
            endDate.setValue(start.plusWeeks(1));
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setGraphic(null);
            alert.setContentText("Warning End Date must be after Start Date");
            alert.showAndWait();
        }

    }

    /**
     * Update chart type on click of update button.
     */
    @FXML
    public void update() {
        if (chartType == 1) busy();
        else if (chartType == 2) consumption();
        else if (chartType == 3) sales();
        else if (chartType == 4) customers();
    }
}