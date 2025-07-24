package techura.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import techura.models.Product;
import techura.utils.Navigator;
import techura.utils.ProductDataUtil;

import java.util.List;

public class StockView {

    public static VBox getView() {
        Button back = new Button("🔙 Back");
        back.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));
        back.setStyle("-fx-background-color: #dddddd; -fx-font-size: 14;");


        Label mtitle = new Label("Stock");
        TableView tableView = new TableView<>();
        TableColumn<Product ,String>idCul = new TableColumn<>("Product-Id");
        idCul.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<Product,String>nameCul = new TableColumn<>("Product-Name");
        nameCul.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<Product,Integer> stockCul = new TableColumn<>("Curent-Stock");
        stockCul.setCellValueFactory(new PropertyValueFactory<>("Stock"));

        //tableView setting

        tableView.getColumns().addAll(idCul, nameCul,stockCul);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(400);
        tableView.setPlaceholder(new Label("📭 No products available"));

        //load product from csv

        // Load data from CSV
        ObservableList<Product> products1 = FXCollections.observableArrayList(
                ProductDataUtil.loadProduct_from_csv()
        );
        tableView.setItems(products1);



        Label title = new Label("📦 Stock In / Out");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKSLATEGRAY);

        TextField productId = new TextField();
        TextField quantity = new TextField();
        ChoiceBox<String> action = new ChoiceBox<>();
        action.getItems().addAll("Stock In", "Stock Out");

        productId.setPromptText("🔍 Enter Product ID");
        quantity.setPromptText("🔢 Enter Quantity");
        productId.setPrefWidth(250);
        quantity.setPrefWidth(250);
        action.setPrefWidth(250);

        Button updateBtn = new Button("✔️ Update Stock");
        updateBtn.setStyle("-fx-background-color: #0a9396; -fx-text-fill: white; -fx-font-size: 15;");
        updateBtn.setPrefWidth(250);
        updateBtn.setOnAction(e -> {
            String id = productId.getText();
            String qtyText = quantity.getText();
            String selectedAction = action.getValue();

            if (id.isEmpty() || qtyText.isEmpty() || selectedAction == null) {
                showAlert("Please fill all fields.");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                List<Product> products = ProductDataUtil.loadProduct_from_csv();
                boolean found = false;

                for (Product product : products) {
                    if (product.getId().equals(id)) {
                        found = true;
                        int newStock = selectedAction.equals("Stock In")
                                ? product.getStock() + qty
                                : product.getStock() - qty;

                        if (newStock < 0) {
                            showAlert("❌ Not enough stock.");
                            return;
                        }

                        product.setStock(newStock);
                        ProductDataUtil.saveAllProductsToCSV(products);
                        showAlert("✅ Stock updated successfully.");

                        productId.clear();
                        quantity.clear();
                        action.setValue(null);
                        break;
                    }
                }

                if (!found) {
                    showAlert("⚠️ Product ID not found.");
                }

            } catch (NumberFormatException ex) {
                showAlert("❌ Quantity must be a number.");
            }
        });

        VBox form = new VBox(15,tableView, productId, quantity, action, updateBtn);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 10; -fx-background-radius: 10;");
        form.setEffect(new DropShadow(5, Color.GRAY));

        VBox layout = new VBox(20, back, title, form);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }

    private static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}