package techura.utils;

import techura.models.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class CSVUtil {
    private static final String PRODUCT_CSV = "product.csv";
    private static final String SALES_CSV = "sales.csv";

    public static void saveSale(LocalDateTime dateTime, String productName, int qty, double price, double total) {
        try (FileWriter writer = new FileWriter(SALES_CSV, true)) {
            writer.append(dateTime.toString()).append(",")
                    .append(productName).append(",")
                    .append(String.valueOf(qty)).append(",")
                    .append(String.valueOf(price)).append(",")
                    .append(String.valueOf(total)).append("\n");
        } catch (IOException e) {
            System.err.println("❌ Failed to save sale: " + e.getMessage());
        }
    }

    public static void saveAllProducts(List<Product> products) {
        try (FileWriter writer = new FileWriter(PRODUCT_CSV, false)) {
            for (Product p : products) {
                writer.write(p.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to save products: " + e.getMessage());
        }
    }

}