package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import techura.models.Employe;
import techura.utils.LoggedInEmployee;
import techura.utils.Navigator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardView {
    private static String name = LoggedInEmployee.getCurrent().getName();
    private static String role = LoggedInEmployee.getCurrent().getRole();
    public static BorderPane getView() {
        BorderPane root = new BorderPane();

        // Sidebar Navbar
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");

        // Branding
        Text logo = new Text("Techura\nBy Bikram");
        logo.setFont(Font.font("Arial", 28));
        logo.setFill(Color.web("#00ffff"));

        // Create buttons
        Button homeBtn = createSidebarButton("\uD83C\uDFE0 Home");
        Button productBtn = createSidebarButton("\uD83D\uDCE6 Products");
        Button stockBtn = createSidebarButton("\uD83D\uDCCA Stock");
        Button settingsBtn = createSidebarButton("\u2699\uFE0F Settings");
        Button logoutBtn = createSidebarButton("\uD83D\uDEAA Logout");
        Button salesBtn = createSidebarButton("\uD83E\uDDFE Sales");
        Button employeeBtn = createSidebarButton("\uD83D\uDC77\u200D\u2640 Employees");

        // Set button actions
        homeBtn.setOnAction(event -> Navigator.navigateTo(HomeView.getView()));
        productBtn.setOnAction(event -> Navigator.navigateTo(ProductView.getView()));
        stockBtn.setOnAction(event -> Navigator.navigateTo(StockView.getView()));
        settingsBtn.setOnAction(event -> Navigator.navigateTo(SettingView.getSettingView()));
        salesBtn.setOnAction(event -> Navigator.navigateTo(SalesDashboardView.getView()));
        employeeBtn.setOnAction(event -> Navigator.navigateTo(EmployeeView.getView()));
        logoutBtn.setOnAction(event -> {
            LoggedInEmployee.setCurrent(null);
            Navigator.navigateTo(HomeView.getView());
        });

        VBox.setMargin(logo, new Insets(10, 0, 30, 10));
        sidebar.getChildren().addAll(
                logo, homeBtn, productBtn, stockBtn,
                salesBtn, employeeBtn, settingsBtn, logoutBtn
        );

        // Main Dashboard Area
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Top Info Section
        HBox topInfo = new HBox(20);
        topInfo.setAlignment(Pos.CENTER);

        Text welcome = new Text("Welcome " + name + " (" + role + ")");
        welcome.setFont(Font.font("Arial", 20));
        welcome.setFill(Color.web("#00ffff"));

        Text currentTime = new Text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        currentTime.setFont(Font.font("Arial", 16));
        currentTime.setFill(Color.web("#cccccc"));

        topInfo.getChildren().addAll(welcome, currentTime);

        // Sales Chart (Pie Chart Example)
        PieChart salesChart = new PieChart();
        salesChart.setTitle("Sales Distribution");
        salesChart.getData().addAll(
                new PieChart.Data("Electronics", 30),
                new PieChart.Data("Clothing", 25),
                new PieChart.Data("Food", 45)
        );

        salesChart.setPrefSize(300, 300);

        // Placeholder for Climate and Profit/Loss Info
        VBox infoBox = new VBox(15);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Text climate = new Text("Climate: Sunny, 30°C");
        Text profit = new Text("Profit: ¥120,000");
        Text loss = new Text("Loss: ¥20,000");

        climate.setFont(Font.font("Arial", 16));
        profit.setFont(Font.font("Arial", 16));
        loss.setFont(Font.font("Arial", 16));

        climate.setFill(Color.LIGHTBLUE);
        profit.setFill(Color.LIGHTGREEN);
        loss.setFill(Color.SALMON);

        infoBox.getChildren().addAll(climate, profit, loss);

        HBox dashboardGrid = new HBox(50);
        dashboardGrid.setAlignment(Pos.CENTER);
        dashboardGrid.getChildren().addAll(salesChart, infoBox);

        mainContent.getChildren().addAll(topInfo, dashboardGrid);

        root.setLeft(sidebar);
        root.setCenter(mainContent);

        return root;
    }

    private static Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
                "-fx-background-radius: 12;" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #30475e;" +
                        "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-radius: 12;" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #3f5e7e;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-radius: 12;" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #30475e;"
        ));
        return button;
    }
}