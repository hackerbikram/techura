package techura.utils;

import techura.models.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDataUtil {
    private static final String FILE_PATH = "products.csv";

    // Save a single product by appending to the file
    public static void saveProductToCSV(Product product) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.append(product.toCSV()).append("\n");
        } catch (IOException e) {
            System.err.println("❌ Failed to save product: " + e.getMessage());
        }
    }

    // Load all products from the CSV file
    public static List<Product> loadProduct_from_csv() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            System.out.println("⚠️ CSV file not found: creating a new empty list.");
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    try {
                        String id = parts[0];
                        String name = parts[1];
                        String date = parts[2];
                        double price = Double.parseDouble(parts[3]);
                        String type = parts[4];
                        int stock = Integer.parseInt(parts[5]);

                        products.add(new Product(id, name, date, price, type, stock));
                    } catch (NumberFormatException nfe) {
                        System.err.println("❌ Skipping bad data line: " + line);
                    }
                } else {
                    System.err.println("❌ Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to load products: " + e.getMessage());
        }
        return products;
    }

    // Save a full list of products (overwrite file)
    public static void saveAllProductsToCSV(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Product product : products) {
                String line = String.format("%s,%s,%s,%.2f,%s,%d",
                        product.getId(),
                        product.getName(),
                        product.getDate(),
                        product.getPrice(),
                        product.getType(),
                        product.getStock()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to save all products: " + e.getMessage());
        }
    }
    public static boolean deleteProductById(String id) {
        List<Product> products = loadProduct_from_csv();
        boolean removed = products.removeIf(product -> product.getId().equals(id));

        if (removed) {
            saveAllProductsToCSV(products);
        }
        return removed;
    }

    public static boolean editProduct(String id, String newName, double newPrice, int newStock) {
        List<Product> products = loadProduct_from_csv();
        boolean updated = false;

        for (Product product : products) {
            if (product.getId().equals(id)) {
                product.setName(newName);
                product.setPrice(newPrice);
                product.setStock(newStock);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAllProductsToCSV(products);
        }

        return updated;
    }
    public static Product getProductById(String id) {
        return loadProduct_from_csv().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    public static double getTotalStockValue() {
        List<Product> products = loadProduct_from_csv();
        double total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }
    public static Product findProductById(String id) {
        List<Product> products = loadProduct_from_csv();
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

}