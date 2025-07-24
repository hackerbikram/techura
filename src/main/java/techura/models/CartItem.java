// models/CartItem.java
package techura.models;

public class CartItem {
    public Product product;
    public int quantity;
    public double total;

    public CartItem(Product product, int quantity,double total) {
        this.product = product;
        this.quantity = quantity;
        this.total=total;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}