package com.mycompany.mavenpoject20;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ProductForm extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, priceField, stockField;
    private JComboBox<String> categoryField;
    private JButton addButton, updateButton, deleteButton;
    private List<Product> products;
    private int selectedRow = -1;
    private Mavenpoject20 mainApp;

    public ProductForm(Mavenpoject20 mainApp) {
        this.mainApp = mainApp;
        this.products = new ArrayList<>();
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setTitle("Kelola Produk");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(new JLabel("Kode Produk:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Nama Produk:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Tea", "Juice", "Soda"});
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Harga:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stok:"));
        stockField = new JTextField();
        formPanel.add(stockField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");

        addButton.addActionListener(this::addProduct);
        updateButton.addActionListener(this::updateProduct);
        deleteButton.addActionListener(this::deleteProduct);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga", "Stok", "Terjual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromTable(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadSampleData() {
        addProduct("P001", "Americano", "Coffee", 18000, 50);
        addProduct("P002", "Green Tea", "Tea", 15000, 30);
    }

    private void addProduct(String code, String name, String category, int price, int stock) {
        int newId = products.isEmpty() ? 1 : products.get(products.size()-1).getId() + 1;
        Product product = new Product(newId, code, name, category, price, stock);
        products.add(product);
        refreshTable();
        mainApp.updateBanner();
    }

    private void addProduct(ActionEvent e) {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String category = (String) categoryField.getSelectedItem();
            int price = Integer.parseInt(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dan nama produk tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (price <= 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Harga dan stok harus bernilai positif", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate product code
            for (Product p : products) {
                if (p.getCode().equalsIgnoreCase(code)) {
                    JOptionPane.showMessageDialog(this, "Produk dengan kode ini sudah ada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            addProduct(code, name, category, price, stock);
            clearForm();
            JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void updateProduct(ActionEvent e) {
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih produk yang akan diupdate", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String category = (String) categoryField.getSelectedItem();
        int price = Integer.parseInt(priceField.getText().trim());
        int stock = Integer.parseInt(stockField.getText().trim());

        Product product = products.get(selectedRow);

        // Check if code is changed to another existing code
        if (!product.getCode().equals(code)) {
            for (Product p : products) {
                if (p != product && p.getCode().equalsIgnoreCase(code)) {
                    JOptionPane.showMessageDialog(this, 
                        "Kode produk sudah digunakan oleh produk lain", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // Update product properties
        product.setCode(code);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);

        refreshTable();
        clearForm();
        mainApp.updateBanner();
        JOptionPane.showMessageDialog(this, 
            "Produk berhasil diupdate", 
            "Sukses", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, 
            "Harga dan stok harus berupa angka", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void deleteProduct(ActionEvent e) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan dihapus", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Apakah Anda yakin ingin menghapus produk ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Product deletedProduct = products.remove(selectedRow);
            refreshTable();
            clearForm();
            mainApp.updateBanner();
            JOptionPane.showMessageDialog(this, 
                "Produk " + deletedProduct.getName() + " berhasil dihapus", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateFormFromTable(int row) {
        Product product = products.get(row);
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        categoryField.setSelectedItem(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
        productTable.clearSelection();
        selectedRow = -1;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getCode(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getSales()
            });
        }
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void updateProductStock(String productCode, int quantitySold) {
    for (Product product : products) {
        if (product.getCode().equals(productCode)) {
            product.recordSale(quantitySold);
            break;
        }
    }
    refreshTable();
    mainApp.updateBanner(); // This triggers the banner update
    mainApp.resetBannerAnimation(); // Add this method to Mavenpoject20
    }
    
}