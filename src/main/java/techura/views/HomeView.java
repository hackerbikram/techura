package techura.views;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import techura.models.Employe;
import techura.utils.EmployeDataUtil;
import techura.utils.LoggedInEmployee;
import techura.utils.Navigator;

import static techura.views.EmployeeView.*;

public class HomeView {

    public static VBox getView() {
        // Title
        Label title = new Label("🌟 Techura Inventory System 🌟");
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: #00eaff; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #00eaff, 20, 0.3, 0, 0);");

        // Buttons
        Button loginBtn = createGlowingButton("👤 Login");
        Button createNew = createGlowingButton("👤Create new Employe");
        Button dashboardBtn = createGlowingButton("📊 Dashboard");
        Button timeCardBtn = createGlowingButton("⏰ Time Card (Press 'E')");

        // Button Actions
        loginBtn.setOnAction(e -> Navigator.navigateTo(LoginView.getView()));
        createNew.setOnAction(e->showAddEmployeeForm());

        dashboardBtn.setOnAction(e -> {
            Employe islogin = LoggedInEmployee.getCurrent();
            if (islogin == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Required");
                alert.setHeaderText(null);
                alert.setContentText("‼️ Please login first!");
                alert.showAndWait();
            } else {
                Navigator.navigateTo(DashboardView.getView());
            }
        });

        timeCardBtn.setOnAction(e -> Navigator.navigateTo(TimeCardView.getView()));

        // Top Bar Branding
        Label brand = new Label("🔷 Techura by Bikram");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        HBox topBar = new HBox(brand);
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #3F51B5, #5C6BC0);");
        topBar.setPadding(new Insets(12));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Center Layout
        VBox centerBox = new VBox(20, title, loginBtn,createNew, dashboardBtn, timeCardBtn);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        centerBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20;");
        VBox.setVgrow(centerBox, Priority.ALWAYS);

        VBox mainContainer = new VBox(topBar, centerBox);
        mainContainer.setSpacing(10);
        mainContainer.setPadding(new Insets(10));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");

        // Key Press Shortcut
        mainContainer.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.E) {
                Navigator.navigateTo(TimeCardView.getView());
            }
        });

        // Fade-in Animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), centerBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        return mainContainer;
    }

    private static Button createGlowingButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-font-size: 18px;
            -fx-padding: 12px 24px;
            -fx-background-color: #00eaff;
            -fx-text-fill: black;
            -fx-font-weight: bold;
            -fx-background-radius: 30px;
            -fx-effect: dropshadow(gaussian, #00eaff, 15, 0.5, 0, 0);
            """);
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-font-size: 18px;
            -fx-padding: 12px 24px;
            -fx-background-color: #00ffff;
            -fx-text-fill: black;
            -fx-font-weight: bold;
            -fx-background-radius: 30px;
            -fx-effect: dropshadow(gaussian, #00ffff, 25, 0.7, 0, 0);
            """));
        btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-font-size: 18px;
            -fx-padding: 12px 24px;
            -fx-background-color: #00eaff;
            -fx-text-fill: black;
            -fx-font-weight: bold;
            -fx-background-radius: 30px;
            -fx-effect: dropshadow(gaussian, #00eaff, 15, 0.5, 0, 0);
            """));
        return btn;
    }
    private static void showAddEmployeeForm() {
        Stage stage = new Stage();
        stage.setTitle("Add New Employee");

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Developer", "Manager", "HR", "Tester", "Support");
        roleCombo.setPromptText("Select Role");

        TextField rateField = new TextField();
        rateField.setPromptText("Hourly Rate");

        TextField workedField = new TextField();
        workedField.setPromptText("Hours Worked");

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox form = new VBox(10,
                labelWithField("ID", idField),
                labelWithField("Name", nameField),
                labelWithField("Password", passwordField),
                labelWithField("Address", addressField),
                labelWithField("Age", ageField),
                labelWithField("Role", roleCombo),
                labelWithField("Hourly Rate", rateField),
                labelWithField("Hours Worked", workedField),
                buttons);
        form.setPadding(new Insets(20));

        saveBtn.setOnAction(e -> {
            try {
                String role = roleCombo.getValue();
                if (role == null || role.isEmpty()) {
                    showAlert("Please select a role.");
                    return;
                }

                int age = Integer.parseInt(ageField.getText().trim());
                double rate = Double.parseDouble(rateField.getText().trim());
                double worked = Double.parseDouble(workedField.getText().trim());

                Employe emp = new Employe(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        passwordField.getText(),
                        addressField.getText().trim(),
                        age,
                        role,
                        rate,
                        worked
                );

                EmployeDataUtil.saveEmployToCSV(emp);
                showAlert("Employee added successfully!");
                stage.close();
                refreshList();

            } catch (NumberFormatException nfe) {
                showAlert("Please enter valid numeric values for Age, Hourly Rate, and Hours Worked.");
            } catch (Exception ex) {
                showAlert("Error: " + ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> stage.close());

        Scene scene = new Scene(form, 350, 450);
        stage.setScene(scene);
        stage.show();
    }
}