/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.util.ArrayList;
import java.util.List;

public class ProductForm extends JFrame {
    public JTable drinkTable;
    public DefaultTableModel tableModel;
    public JTextField codeField;
    public JTextField nameField;
    public JComboBox<String> categoryField;
    public JTextField priceField;
    public JTextField stockField;
    public JButton saveButton;
    public JButton add;
    public JButton edit;
    public JButton delete;
    
    public ProductForm() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
        products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));
        
        setTitle("WK. Cuan | Stok Barang");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        nameField = new JTextField(10);
        
        priceField = new JTextField(10);
        // Panel form pemesanan
        JPanel formPanel = new JPanel();
        
        formPanel.add(new JLabel("Kode Barang"));
        codeField = new JTextField();
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField();
        formPanel.add(stockField);
        
        saveButton = new JButton("Simpan");
        formPanel.add(saveButton);
        
        add = new JButton("Tambah");
        formPanel.add(add);
        
        edit = new JButton("Edit");
        formPanel.add(edit);
        
        delete = new JButton("Hapus");
        formPanel.add(delete);
        
        tableModel = new DefaultTableModel (new String[]{"Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0);
        drinkTable = new JTable(tableModel);
        Mavenproject3(products);
        
         add.addActionListener(e -> {
            String name = nameField.getText();
            try {
                double price = Double.parseDouble(priceField.getText());
                Product product = new Product();
                products.add(product);
                tableModel.addRow(new Object[]{name, price});
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
            }
        });
        

        
        edit.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                String newName = nameField.getText();
                try {
                    double newPrice = Double.parseDouble(priceField.getText());
                    tableModel.setValueAt(newName, selectedRow, 0);
                    tableModel.setValueAt(newPrice, selectedRow, 1);
                    nameField.setText("");
                    priceField.setText("");
                }catch (NumberFormatException ex) {
            }}
        });
        panel.add(add);
        panel.add(delete);
        panel.add(edit);
        
        frame.add(new JScrollPane(tableModel), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
        }
    
    private void Mavenproject3(List<Product> productList) {
        for (Product products : productList) {
            tableModel.addRow(new Object[]{
                products.getCode(), products.getName(), products.getCategory(), products.getPrice(), products.getStock()});
                     
      
        }
    }
}