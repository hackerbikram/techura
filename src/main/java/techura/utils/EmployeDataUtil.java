package techura.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import techura.models.Employe;
import techura.models.Salary;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeDataUtil {

    private static final String EMPLOYEE_CSV = "employee.csv";
    private static final String SALARY_FOLDER = "salary"; // All daily files inside this folder

    // Save one employee
    public static void saveEmployToCSV(Employe employe) {
        try (FileWriter writer = new FileWriter(EMPLOYEE_CSV, true)) {
            writer.append(employe.toCSV()).append("\n");
        } catch (IOException e) {
            System.err.println("❌ Failed to save employee: " + e.getMessage());
        }
    }

    // Overwrite all employees
    public static void saveAllEmployeesToCSV(List<Employe> employes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_CSV))) {
            for (Employe employe : employes) {
                writer.write(employe.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to save all employees: " + e.getMessage());
        }
    }

    // Load all employees
    public static List<Employe> loadAllEmployees() {
        List<Employe> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(EMPLOYEE_CSV))) {
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (data.length == 8) {
                    Employe emp = new Employe(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5],
                            Double.parseDouble(data[6]), Double.parseDouble(data[7])
                    );
                    employees.add(emp);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("📁 CSV file not found, returning empty list.");
        } catch (IOException | CsvValidationException e) {
            System.err.println("❌ Error reading CSV: " + e.getMessage());
        }
        return employees;
    }

    public static Employe getEmployeById(String id) {
        return loadAllEmployees().stream()
                .filter(emp -> emp.getId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    public static boolean deleteEmployee(String id) {
        List<Employe> employees = loadAllEmployees();
        boolean removed = employees.removeIf(emp -> emp.getId().equalsIgnoreCase(id.trim()));
        if (removed) {
            saveAllEmployeesToCSV(employees);
        }
        return removed;
    }

    public static boolean updateEmployee(Employe updatedEmp) {
        List<Employe> employees = loadAllEmployees();
        boolean updated = false;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equalsIgnoreCase(updatedEmp.getId().trim())) {
                employees.set(i, updatedEmp);
                updated = true;
                break;
            }
        }
        if (updated) {
            saveAllEmployeesToCSV(employees);
        }
        return updated;
    }

    public static boolean login(String id, String password) {
        Employe emp = getEmployeById(id);
        return emp != null && emp.getPassword().equals(password);
    }

    // ✅ Save salary to CSV with filename = today (e.g., 2025-07-23.csv)
    public static boolean saveSalaryToCSV(Salary salary) {
        String date = salary.getDay(); // e.g., 2025-07-23
        String fileName = SALARY_FOLDER + "/" + date + ".csv";

        try {
            Path dirPath = Paths.get(SALARY_FOLDER);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            boolean fileExists = Files.exists(Paths.get(fileName));
            StringBuilder dataToWrite = new StringBuilder();

            if (!fileExists) {
                dataToWrite.append("ID,Name,HourlyRate,Day,InTime,OutTime,TotalHours,Salary\n");
            }

            dataToWrite.append(salary.toCSV()).append("\n");

            Files.writeString(
                    Paths.get(fileName),
                    dataToWrite.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to save salary data: " + e.getMessage());
            return false;
        }
    }
    public static Employe findByIdAndPassword(String id, String password) {
        return loadAllEmployees().stream()
                .filter(emp -> emp.getId().equalsIgnoreCase(id.trim()) && emp.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}