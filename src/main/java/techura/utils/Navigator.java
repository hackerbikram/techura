package techura.utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import techura.Main;

public class Navigator {

    public static void navigateTo(Node view) {
        try {
            Main.rootPane.setCenter(view);  // Your main navigation logic
        } catch (Exception e) {
            System.err.println("❌ Failed to load view: " + e.getMessage());
            e.printStackTrace();
            Main.rootPane.setCenter(buildErrorFallback(e.getMessage()));  // Fallback error UI
        }
    }

    private static VBox buildErrorFallback(String errorMessage) {
        Label errorLabel = new Label("⚠️ Error Loading View:\n" + errorMessage);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabel.setWrapText(true);

        Button closeButton = new Button("Back");
        closeButton.setOnAction(e -> Main.rootPane.setCenter(null));  // Just clear or go back to dashboard

        VBox errorLayout = new VBox(10, errorLabel, closeButton);
        errorLayout.setAlignment(Pos.CENTER);
        errorLayout.setPadding(new Insets(20));

        return errorLayout;
    }
}