package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import techura.models.Employe;
import techura.models.Salary;
import techura.utils.EmployeDataUtil;
import techura.utils.LoggedInEmployee;
import techura.utils.Navigator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TimeCardView {
    private static final List<Salary> salaryRecords = new ArrayList<>();

    public static VBox getView() {
        Label titleLabel = new Label("🕒 Time Card Entry");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // TABLE
        TableView<Employe> employeeTable = new TableView<>();
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.setPrefHeight(250);

        TableColumn<Employe, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Employe, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Employe, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            String status = "❌ Not Clocked In";
            String today = LocalDate.now().toString();
            for (Salary s : salaryRecords) {
                if (s.getId().equals(data.getValue().getId()) && s.getDay().equals(today)) {
                    if (s.getInTime() != null && s.getOutTime() == null) {
                        status = "⏳ Working";
                    } else if (s.getInTime() != null && s.getOutTime() != null) {
                        status = "✅ Completed";
                    }
                }
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        employeeTable.getColumns().addAll(idCol, nameCol, statusCol);
        employeeTable.getItems().addAll(EmployeDataUtil.loadAllEmployees());

        // Choice and Time
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll("In", "Out");
        typeChoice.setValue("In");

        ComboBox<LocalTime> timeBox = new ComboBox<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 5) {
                timeBox.getItems().add(LocalTime.of(hour, min));
            }
        }
        timeBox.setValue(LocalTime.now().withSecond(0).withNano(0));

        Button submit = new Button("✅ Submit");
        Button backButton = new Button("← Back");
        Label result = new Label();

        backButton.setOnAction(e -> {
            Employe emp = LoggedInEmployee.getCurrent();
            if (emp ==null){
                Navigator.navigateTo(HomeView.getView());
            }else {
                Navigator.navigateTo(EmployeeView.getView());
            }
        });

        submit.setOnAction(e -> {
            Employe selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                result.setText("⚠ Please select an employee.");
                return;
            }

            String cardId = selected.getId();
            String entryType = typeChoice.getValue();
            LocalTime selectedTime = timeBox.getValue();
            String dayString = LocalDate.now().toString();

            // Get existing salary record for today if it exists
            Salary existingSalary = salaryRecords.stream()
                    .filter(s -> s.getId().equals(cardId) && s.getDay().equals(dayString))
                    .findFirst()
                    .orElse(null);

            LocalTime inTime = null;
            LocalTime outTime = null;

            if (existingSalary != null) {
                inTime = existingSalary.getInTime();
                outTime = existingSalary.getOutTime();
            }

            // Update based on type
            if (entryType.equals("In")) {
                inTime = selectedTime;
            } else if (entryType.equals("Out")) {
                if (inTime == null) {
                    result.setText("⚠ Cannot clock out without clocking in first.");
                    return;
                }
                outTime = selectedTime;
            }

            // Create updated salary record
            Salary updatedSalary = new Salary(
                    selected.getId(),
                    selected.getName(),
                    selected.getHourlyRate(),
                    dayString,
                    inTime,
                    outTime
            );

            // Update list
            if (existingSalary != null) salaryRecords.remove(existingSalary);
            salaryRecords.add(updatedSalary);

            boolean saved = saveSalaryToCSV(updatedSalary);
            result.setText(saved ? "✅ Entry saved." : "❌ Failed to save.");
            employeeTable.refresh();
        });

        // LAYOUT
        VBox layout = new VBox(12,
                titleLabel,
                employeeTable,
                new HBox(10, new Label("Type:"), typeChoice),
                new HBox(10, new Label("Time:"), timeBox),
                new HBox(10, backButton, submit),
                result
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #f5f7fa, #d6e0f0);
            -fx-border-color: #a4b0be;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
        """);

        employeeTable.setStyle("""
            -fx-background-color: white;
            -fx-font-size: 14px;
            -fx-border-color: #dcdde1;
        """);

        employeeTable.setRowFactory(tv -> {
            TableRow<Employe> row = new TableRow<>();
            row.setStyle("-fx-background-color: #ffffff;");
            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f1f2f6;");
                } else {
                    row.setStyle("-fx-background-color: #ffffff;");
                }
            });
            return row;
        });

        return layout;
    }

    private static boolean saveSalaryToCSV(Salary salary) {
        try {
            java.nio.file.Path file = java.nio.file.Paths.get("salary.csv");
            java.nio.file.Files.writeString(file, salary.toCSV() + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}