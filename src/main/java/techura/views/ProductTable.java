package techura.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.effect.DropShadow;
import techura.models.Product;
import techura.utils.Navigator;
import techura.utils.ProductDataUtil;

public class ProductTable {

    public static VBox getview() {
        // Back Button
        Button back = new Button("🔙 Back");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #00ffff; -fx-font-size: 14px;");
        back.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));

        // Title
        Label title = new Label("📦 All Products");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#00ffe7"));

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Top Bar with Back + Title
        HBox topBar = new HBox(10, back, spacer, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));

        // Table
        TableView<Product> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");

        String columnStyle = "-fx-background-color: #1a1a1a; -fx-text-fill: #00ffff; -fx-alignment: CENTER;";

        // Define columns
        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setStyle(columnStyle);

        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setStyle(columnStyle);

        TableColumn<Product, String> dateCol = new TableColumn<>("Expiry Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle(columnStyle);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setStyle(columnStyle);

        TableColumn<Product, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setStyle(columnStyle);

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setStyle(columnStyle);

        // Table settings
        tableView.getColumns().addAll(idCol, nameCol, dateCol, priceCol, typeCol, stockCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(400);
        tableView.setPlaceholder(new Label("📭 No products available"));

        // Load data from CSV
        ObservableList<Product> products = FXCollections.observableArrayList(
                ProductDataUtil.loadProduct_from_csv()
        );
        tableView.setItems(products);

        // Main layout
        VBox root = new VBox(20, topBar, tableView);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        // Optional shadow effect
        tableView.setEffect(new DropShadow(10, Color.web("#00ffe7")));

        return root;
    }
}