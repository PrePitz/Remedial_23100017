package com.mycompany.mavenpoject20;

public class TransactionItem {
    private Product product;
    private int quantity;

    public TransactionItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public int getSubtotal() { return product.getPrice() * quantity; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " = Rp" + String.format("%,d", getSubtotal());
    }
}