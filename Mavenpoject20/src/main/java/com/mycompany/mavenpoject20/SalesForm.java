package com.mycompany.mavenpoject20;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SalesForm extends JFrame {
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private ProductForm productForm;

    public SalesForm(ProductForm productForm) {
        this.productForm = productForm;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Penjualan Produk");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Product selection
        mainPanel.add(new JLabel("Pilih Produk:"));
        productComboBox = new JComboBox<>();
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    Product product = (Product) value;
                    setText(product.getName() + " (Stok: " + product.getStock() + ")");
                }
                return this;
            }
        });
        refreshProductList();
        mainPanel.add(productComboBox);

        // Quantity input
        mainPanel.add(new JLabel("Jumlah:"));
        quantityField = new JTextField();
        mainPanel.add(quantityField);

        // Sell button
        JButton sellButton = new JButton("Jual");
        sellButton.addActionListener(this::processSale);
        mainPanel.add(new JLabel());
        mainPanel.add(sellButton);

        add(mainPanel);
    }

    private void refreshProductList() {
        productComboBox.removeAllItems();
        for (Product product : productForm.getProducts()) {
            if (product.getStock() > 0) {
                productComboBox.addItem(product);
            }
        }
    }

    private void processSale(ActionEvent e) {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, 
                "Tidak ada produk yang dipilih", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Jumlah harus lebih dari 0", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantity > selectedProduct.getStock()) {
                JOptionPane.showMessageDialog(this, 
                    "Stok tidak mencukupi! Stok tersedia: " + selectedProduct.getStock(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Process the sale
            productForm.updateProductStock(selectedProduct.getCode(), quantity);
            refreshProductList();
            quantityField.setText("");
            
            JOptionPane.showMessageDialog(this, 
                "Berhasil menjual " + quantity + " " + selectedProduct.getName(), 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Masukkan jumlah yang valid", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}