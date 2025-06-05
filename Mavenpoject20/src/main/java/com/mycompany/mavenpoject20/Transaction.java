package com.mycompany.mavenpoject20;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    private int id;
    private String transactionCode;
    private Date transactionDate;
    private Customer customer;
    private List<TransactionItem> items;
    private int totalAmount;
    private int paymentAmount;
    private int change;

    public Transaction(int id, String transactionCode, Customer customer) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.transactionDate = new Date();
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
        this.paymentAmount = 0;
        this.change = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getTransactionCode() { return transactionCode; }
    public Date getTransactionDate() { return transactionDate; }
    public Customer getCustomer() { return customer; }
    public List<TransactionItem> getItems() { return items; }
    public int getTotalAmount() { return totalAmount; }
    public int getPaymentAmount() { return paymentAmount; }
    public int getChange() { return change; }

    // Methods
    public void addItem(Product product, int quantity) {
        // Check if product already exists in transaction
        for (TransactionItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotal();
                return;
            }
        }
        
        // Add new item
        items.add(new TransactionItem(product, quantity));
        calculateTotal();
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProduct().getId() == productId);
        calculateTotal();
    }

    public void updateQuantity(int productId, int newQuantity) {
        for (TransactionItem item : items) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(newQuantity);
                break;
            }
        }
        calculateTotal();
    }

    public void setPayment(int paymentAmount) {
        this.paymentAmount = paymentAmount;
        this.change = paymentAmount - totalAmount;
    }

    private void calculateTotal() {
        totalAmount = 0;
        for (TransactionItem item : items) {
            totalAmount += item.getSubtotal();
        }
        change = paymentAmount - totalAmount;
    }

    public boolean processPayment(ProductForm productForm) {
        if (paymentAmount < totalAmount) {
            return false;
        }

        // Update product stocks
        for (TransactionItem item : items) {
            productForm.updateProductStock(
                item.getProduct().getCode(), 
                item.getQuantity()
            );
        }

        return true;
    }

    @Override
    public String toString() {
        return "Transaksi #" + transactionCode + " - Total: Rp" + String.format("%,d", totalAmount);
    }
}