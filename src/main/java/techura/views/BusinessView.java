package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import techura.utils.Navigator;

public class BusinessView {
    public static VBox getView() {
        // Title
        Text title = new Text("🏢 Business Management");
        title.setFont(Font.font("Arial", 26));
        title.setStyle("-fx-fill: linear-gradient(to right, #00ffe7, #00bfff); -fx-effect: dropshadow(gaussian, #00ffe7, 10, 0.5, 1, 1);");

        // Back Button
        Button back = new Button("← Dashboard");
        back.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));
        back.getStyleClass().add("nav-back");

        // Input Field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Business Name");
        nameField.setMaxWidth(300);
        nameField.getStyleClass().add("glow-input");

        // Save Button
        Button saveButton = new Button("💾 Save Business");
        saveButton.getStyleClass().add("glow-button");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            if (!name.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Business '" + name + "' saved successfully!");
                alert.show();
                nameField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "⚠️ Please enter a valid business name.");
                alert.show();
            }
        });

        // Layout Setup
        VBox layout = new VBox(20, back, title, nameField, saveButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60));
        layout.getStyleClass().add("page");

        return layout;
    }
}