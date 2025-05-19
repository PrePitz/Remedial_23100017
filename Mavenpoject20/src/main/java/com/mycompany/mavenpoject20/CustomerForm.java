package com.mycompany.mavenpoject20;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class CustomerForm extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, phoneField, addressField;
    private JButton addButton, updateButton, deleteButton;
    private List<Customer> customers;
    private int selectedRow = -1;

    public CustomerForm() {
        this.customers = new ArrayList<>();
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setTitle("Kelola Pelanggan");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(new JLabel("Kode Pelanggan:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Nama Pelanggan:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Telepon:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Alamat:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");

        addButton.addActionListener(this::addCustomer);
        updateButton.addActionListener(this::updateCustomer);
        deleteButton.addActionListener(this::deleteCustomer);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Telepon", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromTable(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(customerTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadSampleData() {
        addCustomer("C001", "John Doe", "08123456789", "Jl. Contoh No. 123");
        addCustomer("C002", "Jane Smith", "08987654321", "Jl. Sample No. 456");
    }

    private void addCustomer(String code, String name, String phone, String address) {
        int newId = customers.isEmpty() ? 1 : customers.get(customers.size()-1).getId() + 1;
        Customer customer = new Customer(newId, code, name, phone, address);
        customers.add(customer);
        refreshTable();
    }

    private void addCustomer(ActionEvent e) {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dan nama pelanggan tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate customer code
            for (Customer c : customers) {
                if (c.getCode().equalsIgnoreCase(code)) {
                    JOptionPane.showMessageDialog(this, "Pelanggan dengan kode ini sudah ada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            addCustomer(code, name, phone, address);
            clearForm();
            JOptionPane.showMessageDialog(this, "Pelanggan berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer(ActionEvent e) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan yang akan diupdate", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            Customer customer = customers.get(selectedRow);

            // Check if code is changed to another existing code
            if (!customer.getCode().equals(code)) {
                for (Customer c : customers) {
                    if (c != customer && c.getCode().equalsIgnoreCase(code)) {
                        JOptionPane.showMessageDialog(this, 
                            "Kode pelanggan sudah digunakan oleh pelanggan lain", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Update customer properties
            customer.setCode(code);
            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);

            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, 
                "Pelanggan berhasil diupdate", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Terjadi kesalahan: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer(ActionEvent e) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan yang akan dihapus", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Apakah Anda yakin ingin menghapus pelanggan ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Customer deletedCustomer = customers.remove(selectedRow);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, 
                "Pelanggan " + deletedCustomer.getName() + " berhasil dihapus", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateFormFromTable(int row) {
        Customer customer = customers.get(row);
        codeField.setText(customer.getCode());
        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        customerTable.clearSelection();
        selectedRow = -1;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                customer.getCode(),
                customer.getName(),
                customer.getPhone(),
                customer.getAddress()
            });
        }
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
}