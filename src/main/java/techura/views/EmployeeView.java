package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import techura.models.Employe;
import techura.utils.EmployeDataUtil;
import techura.utils.Navigator;
import techura.utils.TimeUtil;

import java.util.List;

public class EmployeeView {

    private static ListView<String> listView;

    public static VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F5F7FA;");

        Label title = new Label("👨‍💼 Employee Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2C3E50"));

        listView = new ListView<>();
        listView.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BFC9CA;");
        refreshList();

        Button backBtn = new Button("🔙 Back");
        Button addBtn = new Button("➕ Add Employee");
        Button updateBtn = new Button("✏️ Edit Selected");
        Button deleteBtn = new Button("🗑️ Delete Selected");
        Button timecardBtn = new Button("⏰ TimeCard");
        Button salaryBtn = new Button("💸Salary");

        HBox btnRow = new HBox(10, backBtn, addBtn, updateBtn, deleteBtn, timecardBtn, salaryBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        for (Button btn : List.of(backBtn, addBtn, updateBtn, deleteBtn, timecardBtn, salaryBtn)) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            btn.setStyle("""
                    -fx-background-color: #3498DB;
                    -fx-text-fill: white;
                    -fx-background-radius: 8;
                    -fx-cursor: hand;
                    """);
        }

        backBtn.setOnAction(e -> Navigator.navigateTo(DashboardView.getView()));

        addBtn.setOnAction(e -> showAddEmployeeForm());

        salaryBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("⚠️ Select an employee to view salary");
                return;
            }

            String[] parts = selected.split("\\|");
            String empId = parts[0].trim();

            Employe emp = EmployeDataUtil.getEmployeById(empId);
            if (emp == null) {
                showAlert("❌ Employee not found.");
                return;
            }

            double totalMinutes = TimeUtil.getTotalMinutesWorked(empId);

            if (totalMinutes <= 0) {
                showAlert("No timecard records found for " + emp.getName());
                return;
            }

            double hourlyRate = emp.getHourlyRate();
            double totalHours = totalMinutes / 60.0;
            double totalSalary = totalHours * hourlyRate;

            showAlert("💰 Salary Details for " + emp.getName() +
                    "\nWorked Hours: " + String.format("%.2f", totalHours) +
                    "\nHourly Rate: ₹" + hourlyRate +
                    "\nTotal Salary: ₹" + String.format("%.2f", totalSalary));
        });

        deleteBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("⚠️ Select an employee to delete.");
                return;
            }
            String[] parts = selected.split("\\|");
            String empId = parts[0].trim();
            boolean removed = EmployeDataUtil.deleteEmployee(empId);
            if (removed) {
                showAlert("🗑️ Deleted successfully.");
                refreshList();
            } else {
                showAlert("❌ Failed to delete.");
            }
        });

        timecardBtn.setOnAction(e -> Navigator.navigateTo(TimeCardView.getView()));

        root.getChildren().addAll(title, listView, btnRow);
        return root;
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
        roleCombo.getItems().addAll("Owner", "Manager", "Developer", "HR", "Tester", "Support", "Worker", "PartTime");
        roleCombo.setPromptText("Select Role");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Auto Salary");
        salaryField.setEditable(false);

        roleCombo.setOnAction(e -> {
            String role = roleCombo.getValue();
            String salary = switch (role) {
                case "Owner" -> "25000";
                case "Manager" -> "1800";
                case "Developer" -> "1200";
                case "HR" -> "1600";
                case "Tester" -> "1100";
                case "Support" -> "720";
                case "Worker" -> "715";
                case "PartTime" -> "700";
                default -> "Not Defined";
            };
            salaryField.setText(salary);
        });

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
                labelWithField("Auto Salary", salaryField),
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
                double rate = Double.parseDouble(salaryField.getText().trim());
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

        Scene scene = new Scene(form, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    static HBox labelWithField(String labelText, Control field) {
        Label label = new Label(labelText);
        label.setPrefWidth(120);
        label.setAlignment(Pos.CENTER_LEFT);
        return new HBox(10, label, field);
    }

    static void refreshList() {
        listView.getItems().clear();
        List<Employe> employees = EmployeDataUtil.loadAllEmployees();
        for (Employe emp : employees) {
            listView.getItems().add(emp.getId() + " | " + emp.getName() + " | " + emp.getRole());
        }
    }

    static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}