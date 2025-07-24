package techura.utils;

import techura.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductStock {
    public static List<Product> products = new ArrayList<>();

    public static String addStock(String id, int count) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                if (count > 0) {
                    int currentStock = product.getStock();
                    product.setStock(currentStock + count);
                    return "Product stock updated: " + product.getName();
                } else {
                    return "Stock must be positive!";
                }
            }
        }
        return "Product with ID " + id + " not found!";
    }

    public static String decreaseStock(String id, int count) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                if (count > 0 && product.getStock() >= count) {
                    int currentStock = product.getStock();
                    product.setStock(currentStock - count);
                    return "Stock decreased for: " + product.getName();
                } else {
                    return "Not enough stock or invalid count!";
                }
            }
        }
        return "Product with ID " + id + " not found!";
    }

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static String getProductStock() {
        StringBuilder stockInfo = new StringBuilder();
        for (Product prod : products) {
            stockInfo.append("Product: ")
                    .append(prod.getName())
                    .append(", Stock: ")
                    .append(prod.getStock())
                    .append("\n");
        }
        return stockInfo.toString();
    }

}