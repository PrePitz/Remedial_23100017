package com.mycompany.mavenpoject20;

public class Product {
    private int id;
    private String code;
    private String name;
    private String category;
    private int price;
    private int stock;
    private int sales;

    public Product(int id, String code, String name, String category, int price, int stock) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.sales = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public int getSales() { return sales; }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(int price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setSales(int sales) { this.sales = sales; }

    public void recordSale(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
            sales += quantity;
        }
    }
    @Override
public String toString() {
    return name + " (Rp" + price + ") - Stok: " + stock;
}
}