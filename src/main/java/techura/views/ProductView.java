package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import techura.models.Product;
import techura.utils.Navigator;
import techura.utils.ProductDataUtil;

public class ProductView {
    public static VBox getView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2c5364);");

        Label title = new Label("📦 Product Management");
        title.setStyle("-fx-font-size: 26px; -fx-text-fill: #00ffe7;");

        Button addBtn = new Button("➕ Add Product");
        addBtn.setOnAction(e -> Navigator.navigateTo(ProductFormControllers.getview()));

        Button deleteBtn = new Button("− Delete Product");
        deleteBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete Product");
            dialog.setHeaderText("Enter Product ID to Delete:");
            dialog.setContentText("Product ID:");
            dialog.showAndWait().ifPresent(productId -> {
                boolean deleted = ProductDataUtil.deleteProductById(productId.trim());
                showAlert(deleted ? "✅ Product deleted successfully." : "❌ Product ID not found.");
            });
        });

        Button editBtn = new Button("✏️ Edit Product");
        editBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Product");
            dialog.setHeaderText("Enter Product ID to Edit:");
            dialog.setContentText("Product ID:");
            dialog.showAndWait().ifPresent(productId -> {
                Product product = ProductDataUtil.getProductById(productId.trim());
                if (product != null) {
                    showEditForm(product);
                } else {
                    showAlert("❌ Product not found.");
                }
            });
        });

        Button viewProductBtn = new Button("🔍 View Products");
        viewProductBtn.setOnAction(e -> Navigator.navigateTo(ProductTable.getview()));

        Button totalValueBtn = new Button("💰 Show Total Stock Value");
        // Include in the button loop or separately
        totalValueBtn.setPrefWidth(220);
        totalValueBtn.setStyle("-fx-background-color: #006600; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;");
        totalValueBtn.setOnMouseEntered(e -> totalValueBtn.setStyle("-fx-background-color: #009900; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;"));
        totalValueBtn.setOnMouseExited(e -> totalValueBtn.setStyle("-fx-background-color: #006600; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;"));
        totalValueBtn.setOnAction(e -> {
            double total = ProductDataUtil.getTotalStockValue();
            showAlert("📊 Total Stock Value: ¥" + String.format("%.2f", total));
        });

        Button backBtn = new Button("⬅ Back to Dashboard");
        backBtn.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));

        for (Button btn : new Button[]{addBtn, deleteBtn, editBtn, viewProductBtn, backBtn}) {
            btn.setPrefWidth(220);
            btn.setStyle("-fx-background-color: #004d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #007777; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #004d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-cursor: hand;"));
        }

        layout.getChildren().addAll(title, addBtn, deleteBtn, editBtn, viewProductBtn, totalValueBtn,backBtn);
        return layout;
    }

    private static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    private static void showEditForm(Product product) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");

        Label nameLabel = new Label("New Name:");
        TextField nameField = new TextField(product.getName());

        Label priceLabel = new Label("New Price:");
        TextField priceField = new TextField(String.valueOf(product.getPrice()));

        Label stockLabel = new Label("New Stock:");
        TextField stockField = new TextField(String.valueOf(product.getStock()));

        VBox content = new VBox(10, nameLabel, nameField, priceLabel, priceField, stockLabel, stockField);
        content.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(content);

        ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == updateBtn) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());

                    boolean updated = ProductDataUtil.editProduct(product.getId(), name, price, stock);
                    showAlert(updated ? "✅ Product updated successfully." : "❌ Failed to update product.");
                } catch (Exception ex) {
                    showAlert("❌ Invalid input.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}