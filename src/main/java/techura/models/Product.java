package techura.models;

import javafx.beans.property.*;

public class Product {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty date;
    private final DoubleProperty price;
    private final StringProperty type;
    private final IntegerProperty stock;

    public Product(String id, String name, String date, double price, String type, int stock) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.price = new SimpleDoubleProperty(price);
        this.type = new SimpleStringProperty(type);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // ✅ GETTER METHODS to extract real values
    public String getId() { return id.get(); }
    public void setId(String id){this.id.set(id);}

    public String getName() { return name.get(); }
    public void setName(String name){this.name.set(name);}

    public String getDate() { return date.get(); }
    public void  setDate(String date){this.date.set(date);}

    public double getPrice() { return price.get(); }
    public void setPrice(double price){this.price.set(price);}

    public String getType() { return type.get(); }
    public void setType(String type){this.type.set(type);}

    public int getStock() { return stock.get(); }
    public void setStock(int stock){this.stock.set(stock);}

    // ✅ Use real values in CSV
    public String toCSV() {
        return getId() + "," + getName() + "," + getDate() + "," + getPrice() + "," + getType() + "," + getStock();
    }
}