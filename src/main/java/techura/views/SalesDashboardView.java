package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import techura.models.Product;
import techura.utils.CSVUtil;
import techura.utils.Navigator;
import techura.utils.ProductDataUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class SalesDashboardView {

    private static final List<Product> selectedProducts = new ArrayList<>();
    private static final Map<Product, Integer> productQuantities = new HashMap<>();
    private static final VBox receiptBox = new VBox();
    private static Stage mainStage; // 🔧 Hold reference to the main stage

    // 🧠 Set the main stage before calling getView()
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static Node getView() {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label title = new Label("🛒 Sales Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        layout.getChildren().add(title);

        FlowPane productPane = new FlowPane();
        productPane.setHgap(15);
        productPane.setVgap(15);
        productPane.setPadding(new Insets(10));
        productPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd;");

        for (Product product : ProductDataUtil.loadProduct_from_csv()) {
            Button productBtn = new Button(product.getName());
            productBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 10px;");
            productBtn.setOnAction(e -> {
                selectedProducts.add(product);
                productQuantities.put(product, productQuantities.getOrDefault(product, 0) + 1);
                updateReceipt();
            });
            productPane.getChildren().add(productBtn);
        }

        HBox buttonRow = new HBox(15);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back");
        backButton.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px;");

        Button payButton = new Button("💳 Make Payment");
        payButton.setStyle("-fx-background-color: #2ecc71; -fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px 30px;");
        payButton.setOnAction(e -> handlePayment());

        Button printButton = new Button("🖨️ Print/Export Receipt");
        printButton.setOnAction(e -> exportReceipt());
        printButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");

        buttonRow.getChildren().addAll(backButton, payButton, printButton);

        receiptBox.setSpacing(10);
        updateReceipt();

        layout.getChildren().addAll(productPane, buttonRow, new Label("🧾 Receipt:"), receiptBox);

        return layout;
    }

    private static void handlePayment() {
        double grandTotal = 0;

        List<Product> allProducts = ProductDataUtil.loadProduct_from_csv();

        for (Product product : selectedProducts) {
            int qty = productQuantities.get(product);
            double price = product.getPrice();
            double total = qty * price;
            grandTotal += total;

            CSVUtil.saveSale(LocalDateTime.now(), product.getName(), qty, price, total);

            for (Product p : allProducts) {
                if (p.getId().equals(product.getId())) {
                    p.setStock(p.getStock() - qty);
                }
            }
        }

        CSVUtil.saveAllProducts(allProducts);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Payment Success");
        alert.setHeaderText("Sale Completed");
        alert.setContentText("Total Amount: ¥" + grandTotal);
        alert.showAndWait();

        selectedProducts.clear();
        productQuantities.clear();
        updateReceipt();
    }

    private static void updateReceipt() {
        receiptBox.getChildren().clear();
        for (Product product : productQuantities.keySet()) {
            int qty = productQuantities.get(product);
            double total = qty * product.getPrice();
            Label line = new Label(product.getName() + " x " + qty + " = ¥" + total);
            line.setStyle("-fx-font-size: 16px;");
            receiptBox.getChildren().add(line);
        }
    }

    private static void exportReceipt() {
        // Export to file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Receipt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("receipt_" + LocalDateTime.now().toString().replace(":", "-") + ".txt");

        if (mainStage == null) return;
        try {
            var file = fileChooser.showSaveDialog(mainStage);
            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    for (Product product : productQuantities.keySet()) {
                        int qty = productQuantities.get(product);
                        double total = qty * product.getPrice();
                        writer.write(product.getName() + " x " + qty + " = ¥" + total + "\n");
                    }
                }
            }

            // Optional print:
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(mainStage)) {
                boolean printed = job.printPage(receiptBox);
                if (printed) job.endJob();
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to export receipt: " + e.getMessage());
        }
    }
}