package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import techura.models.Employe;
import techura.utils.EmployeDataUtil;
import techura.utils.LoggedInEmployee;
import techura.utils.Navigator;

public class LoginView {
    public static VBox getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #202020, #1f1c2c);");

        // Top navigation with back button
        HBox topNav = new HBox();
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> Navigator.navigateTo(HomeView.getView())); // Replace with your home or previous view
        topNav.getChildren().add(backBtn);
        topNav.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Techura Login");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));

        TextField idField = new TextField();
        idField.setPromptText("Employee ID");
        idField.setMaxWidth(280);
        idField.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-background-color: #f2f2f2;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(280);
        passField.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-background-color: #f2f2f2;");

        Button loginBtn = new Button("🚀 Login");
        loginBtn.setStyle("""
                -fx-background-color: #3b82f6;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-background-radius: 20;
                -fx-padding: 10 30;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.8), 10, 0.3, 0, 2);
                """);

        // Hover glow effect
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("""
                -fx-background-color: #60a5fa;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-background-radius: 20;
                -fx-padding: 10 30;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(96,165,250,0.9), 20, 0.4, 0, 2);
                """));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("""
                -fx-background-color: #3b82f6;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-background-radius: 20;
                -fx-padding: 10 30;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.8), 10, 0.3, 0, 2);
                """));

        Label message = new Label();
        message.setTextFill(Color.RED);
        message.setStyle("-fx-font-size: 14px;");

        loginBtn.setOnAction(e -> {
            String id = idField.getText();
            String pass = passField.getText();

            Employe emp = EmployeDataUtil.findByIdAndPassword(id, pass);
            if (emp != null) {
                LoggedInEmployee.setCurrent(emp);
                Navigator.navigateTo(DashboardView.getView());
            } else {
                message.setText("❌ Invalid credentials");
            }
        });

        VBox formBox = new VBox(15, title, idField, passField, loginBtn, message);
        formBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(topNav, formBox);
        return root;
    }
}