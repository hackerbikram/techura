package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import techura.controlls.ApplyTheme;
import techura.models.Employe;
import techura.utils.LoggedInEmployee;

import java.net.URL;

public class SettingView {

    private static boolean isDarkMode = false;
    private static String currentTheme = "light";

    private static final Label toastLabel = new Label();
    private static final StackPane toastBox = new StackPane(toastLabel);

    public static BorderPane getSettingView() {
        String username = LoggedInEmployee.getCurrent().getName();
        String role = LoggedInEmployee.getCurrent().getRole();
        Label heading = new Label("⚙️ Settings");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Round profile image on top
        ImageView profileImage = new ImageView(getDefaultImage());
        profileImage.setFitWidth(80);
        profileImage.setFitHeight(80);
        profileImage.setClip(new CircleClip(40));

        VBox profileBox = new VBox(profileImage);
        profileBox.setAlignment(Pos.TOP_CENTER);
        profileBox.setPadding(new Insets(10));

        Label userLabel = new Label(username);
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userLabel.setTextFill(Color.DARKSLATEBLUE);

        Label roleLabel = new Label("Role: " + role);
        roleLabel.setFont(Font.font("Arial", 14));
        roleLabel.setTextFill(Color.DARKGRAY);

        VBox userInfo = new VBox(5, heading, userLabel, roleLabel);
        userInfo.setAlignment(Pos.CENTER);

        // Theme toggle
        CheckBox darkModeToggle = new CheckBox("🌗 Dark Mode");
        darkModeToggle.setSelected(isDarkMode);
        darkModeToggle.setOnAction(e -> {
            isDarkMode = darkModeToggle.isSelected();
            currentTheme = isDarkMode ? "dark" : "light";
            ApplyTheme.applyTheme(currentTheme);
            showToast("Theme set to " + currentTheme);
        });

        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("English", "日本語", "नेपाली", "Español", "Français");
        langBox.setValue("English");

        ComboBox<String> fontSizeBox = new ComboBox<>();
        fontSizeBox.getItems().addAll("Small", "Medium", "Large", "Extra Large");
        fontSizeBox.setValue("Medium");

        CheckBox notifToggle = new CheckBox("🔔 Notifications");
        notifToggle.setSelected(true);

        CheckBox soundToggle = new CheckBox("🔊 Sound Effects");
        soundToggle.setSelected(true);

        CheckBox privacyToggle = new CheckBox("🔒 Privacy Mode");
        privacyToggle.setSelected(false);

        Label versionLabel = new Label("Techura v1.0.0 © 2025");
        versionLabel.setFont(Font.font("Arial", 12));
        versionLabel.setTextFill(Color.GRAY);

        Button saveBtn = new Button("💾 Save");
        saveBtn.setOnAction(e -> showToast("✅ Settings Saved"));

        Button resetBtn = new Button("🔁 Reset");
        resetBtn.setOnAction(e -> {
            darkModeToggle.setSelected(false);
            langBox.setValue("English");
            fontSizeBox.setValue("Medium");
            notifToggle.setSelected(true);
            soundToggle.setSelected(true);
            privacyToggle.setSelected(false);
            showToast("Settings Reset");
        });

        Button aboutBtn = new Button("ℹ️ About");
        Label aboutLabel = new Label("Techura is a next-gen digital system for modern businesses.\nCrafted with ❤️ in Japan & Nepal.");
        aboutLabel.setWrapText(true);
        aboutLabel.setFont(Font.font("Arial", 13));
        aboutLabel.setTextFill(Color.DARKSLATEGRAY);
        aboutLabel.setVisible(false);

        aboutBtn.setOnAction(e -> aboutLabel.setVisible(!aboutLabel.isVisible()));

        toastLabel.setStyle("-fx-background-color: #000000cc; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8;");
        toastBox.setVisible(false);
        toastBox.setMaxWidth(300);
        toastBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox settingsList = new VBox(12,
                darkModeToggle,
                new Label("🌐 Language:"), langBox,
                new Label("🔠 Font Size:"), fontSizeBox,
                notifToggle, soundToggle, privacyToggle,
                versionLabel, new HBox(10, saveBtn, resetBtn), aboutBtn, aboutLabel
        );
        settingsList.setPadding(new Insets(20));
        settingsList.setAlignment(Pos.TOP_LEFT);

        ScrollPane scroll = new ScrollPane(settingsList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        VBox centerBox = new VBox(userInfo, scroll);
        centerBox.setSpacing(20);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setStyle("-fx-background-color: " + (isDarkMode ? "#1e1e1e" : "#ffffff") + "; -fx-background-radius: 12; -fx-border-color: #cccccc; -fx-border-radius: 12;");

        BorderPane root = new BorderPane();
        root.setTop(profileBox);
        root.setCenter(centerBox);
        root.setBottom(toastBox);
        BorderPane.setAlignment(toastBox, Pos.BOTTOM_CENTER);
        return root;
    }

    private static Image getDefaultImage() {
        try {
            URL resource = SettingView.class.getResource("/images/default.jpeg");
            return resource != null ? new Image(resource.toExternalForm()) : new Image("https://via.placeholder.com/100");
        } catch (Exception e) {
            return new Image("https://via.placeholder.com/100");
        }
    }

    private static void showToast(String message) {
        toastLabel.setText(message);
        toastBox.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> toastBox.setVisible(false));
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private static class CircleClip extends javafx.scene.shape.Circle {
        CircleClip(double radius) {
            super(radius, radius, radius);
        }
    }
}