package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import techura.models.Product;
import techura.utils.Navigator;
import techura.utils.ProductDataUtil;

import java.time.LocalDate;

public class ProductFormControllers {

    public static VBox getview() {
        // Back Button
        Button back = new Button("\uD83D\uDD19 Back");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #00ffff; -fx-font-size: 14px;");
        back.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));

        // Title
        Label title = new Label("\uD83D\uDED2 Add New Product");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #00ffe7;");

        // Input Fields
        TextField idField = createStyledField("Enter product ID:");
        TextField nameField = createStyledField("Enter product name:");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select expiry date:");
        datePicker.setStyle("-fx-background-color: #222; -fx-text-fill: white;");

        TextField priceField = createStyledField("Enter price:");
        ChoiceBox<String> typeField = new ChoiceBox<>();
        typeField.getItems().addAll("Food", "Clothes", "Tech", "Study", "Medicine", "Drink", "Vehicle", "Others");
        typeField.setValue("Food");
        typeField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        TextField stockField = createStyledField("Enter stock:");

        // Save Button
        Button saveButton = new Button("\uD83D\uDCBE Save Product");
        saveButton.setStyle(
                "-fx-background-color: linear-gradient(#00bfa5, #00695c);" +
                        "-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,255,255,0.5), 8, 0, 0, 0);"
        );

        // Save Logic
        saveButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            LocalDate localDate = datePicker.getValue();
            String priceText = priceField.getText();
            String type = typeField.getValue();
            String stockText = stockField.getText();

            if (id.isEmpty() || name.isEmpty() || localDate == null || priceText.isEmpty() || stockText.isEmpty()) {
                showAlert("\u26A0\uFE0F Please fill in all fields correctly.");
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
                String date = localDate.toString();

                Product product = new Product(id, name, date, price, type, stock);
                ProductDataUtil.saveProductToCSV(product);
                showAlert("\u2705 Product saved successfully!");

                // Clear fields
                idField.clear();
                nameField.clear();
                priceField.clear();
                stockField.clear();
                datePicker.setValue(null);
                typeField.setValue("Food");

            } catch (NumberFormatException ex) {
                showAlert("\uD83D\uDEAB Price and Stock must be numbers.");
            }
        });

        // Layout
        VBox root = new VBox(15,
                back,
                title,
                idField,
                nameField,
                datePicker,
                priceField,
                typeField,
                stockField,
                saveButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #121212;");

        return root;
    }

    private static TextField createStyledField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: #1f1f1f;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #888;" +
                        "-fx-border-color: #00ffe7;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
        return field;
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}