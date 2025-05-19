package com.mycompany.mavenpoject20;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionForm extends JFrame {
    private JComboBox<Product> productComboBox;
    private JComboBox<Customer> customerComboBox;
    private JTextField quantityField, paymentField, changeField;
    private JTextArea itemsArea;
    private JButton addButton, removeButton, processButton;
    private ProductForm productForm;
    private CustomerForm customerForm;
    private Transaction currentTransaction;
    private DefaultListModel<TransactionItem> itemsListModel;
    private JList<TransactionItem> itemsList;

    public TransactionForm(ProductForm productForm, CustomerForm customerForm) {
        this.productForm = productForm;
        this.customerForm = customerForm;
        this.currentTransaction = new Transaction(1, generateTransactionCode(), null);
        initializeUI();
    }

    private String generateTransactionCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "TRX-" + sdf.format(new Date());
    }

    private void initializeUI() {
        setTitle("Transaksi Penjualan");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Transaction info panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.add(new JLabel("Kode Transaksi:"));
        JLabel codeLabel = new JLabel(currentTransaction.getTransactionCode());
        infoPanel.add(codeLabel);

        infoPanel.add(new JLabel("Tanggal:"));
        JLabel dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(currentTransaction.getTransactionDate()));
        infoPanel.add(dateLabel);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Center panel with form and items
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Customer selection
        formPanel.add(new JLabel("Pelanggan:"));
        customerComboBox = new JComboBox<>();
        customerComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer customer = (Customer) value;
                    setText(customer.getName() + " (" + customer.getCode() + ")");
                }
                return this;
            }
        });
        refreshCustomerList();
        formPanel.add(customerComboBox);

        // Product selection
        formPanel.add(new JLabel("Produk:"));
        productComboBox = new JComboBox<>();
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    Product product = (Product) value;
                    setText(product.getName() + " (Stok: " + product.getStock() + ") - Rp" + String.format("%,d", product.getPrice()));
                }
                return this;
            }
        });
        refreshProductList();
        formPanel.add(productComboBox);

        // Quantity input
        formPanel.add(new JLabel("Jumlah:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        // Add/Remove buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        addButton = new JButton("Tambah");
        addButton.addActionListener(this::addItem);
        buttonPanel.add(addButton);

        removeButton = new JButton("Hapus");
        removeButton.addActionListener(this::removeItem);
        buttonPanel.add(removeButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);
        centerPanel.add(formPanel);

        // Items list
        itemsListModel = new DefaultListModel<>();
        itemsList = new JList<>(itemsListModel);
        itemsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TransactionItem) {
                    TransactionItem item = (TransactionItem) value;
                    setText(item.toString());
                }
                return this;
            }
        });

        centerPanel.add(new JScrollPane(itemsList));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Payment panel
        JPanel paymentPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        paymentPanel.add(new JLabel("Total Belanja:"));
        JLabel totalLabel = new JLabel("Rp0");
        paymentPanel.add(totalLabel);

        paymentPanel.add(new JLabel("Pembayaran:"));
        paymentField = new JTextField();
        paymentField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePayment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePayment(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePayment(); }
            
            private void updatePayment() {
                try {
                    int payment = Integer.parseInt(paymentField.getText());
                    currentTransaction.setPayment(payment);
                    changeField.setText(String.format("Rp%,d", currentTransaction.getChange()));
                    totalLabel.setText(String.format("Rp%,d", currentTransaction.getTotalAmount()));
                } catch (NumberFormatException ex) {
                    changeField.setText("Rp0");
                }
            }
        });
        paymentPanel.add(paymentField);

        paymentPanel.add(new JLabel("Kembalian:"));
        changeField = new JTextField("Rp0");
        changeField.setEditable(false);
        paymentPanel.add(changeField);

        processButton = new JButton("Proses Transaksi");
        processButton.addActionListener(this::processTransaction);
        paymentPanel.add(processButton);

        mainPanel.add(paymentPanel, BorderLayout.SOUTH);

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

    private void refreshCustomerList() {
        customerComboBox.removeAllItems();
        customerComboBox.addItem(null); // Optional customer
        for (Customer customer : customerForm.getCustomers()) {
            customerComboBox.addItem(customer);
        }
    }

    private void refreshItemsList() {
        itemsListModel.clear();
        for (TransactionItem item : currentTransaction.getItems()) {
            itemsListModel.addElement(item);
        }
    }

    private void addItem(ActionEvent e) {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            showError("Silakan pilih produk terlebih dahulu");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showError("Jumlah harus lebih dari 0");
                return;
            }

            if (quantity > selectedProduct.getStock()) {
                showError("Stok tidak mencukupi! Stok tersedia: " + selectedProduct.getStock());
                return;
            }

            currentTransaction.addItem(selectedProduct, quantity);
            refreshItemsList();
            quantityField.setText("");
        } catch (NumberFormatException ex) {
            showError("Masukkan jumlah yang valid");
        }
    }

    private void removeItem(ActionEvent e) {
        int selectedIndex = itemsList.getSelectedIndex();
        if (selectedIndex == -1) {
            showError("Pilih item yang akan dihapus");
            return;
        }

        TransactionItem selectedItem = itemsListModel.get(selectedIndex);
        currentTransaction.removeItem(selectedItem.getProduct().getId());
        refreshItemsList();
    }

    private void processTransaction(ActionEvent e) {
        if (currentTransaction.getItems().isEmpty()) {
            showError("Tidak ada item dalam transaksi");
            return;
        }

        try {
            int payment = Integer.parseInt(paymentField.getText());
            if (payment < currentTransaction.getTotalAmount()) {
                showError("Pembayaran kurang dari total belanja");
                return;
            }

            // Set customer
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            currentTransaction.setPayment(payment);

            // Process transaction
            if (currentTransaction.processPayment(productForm)) {
                // Save transaction to database or history here
                JOptionPane.showMessageDialog(this, 
                    "Transaksi berhasil!\nKode: " + currentTransaction.getTransactionCode() + 
                    "\nTotal: Rp" + String.format("%,d", currentTransaction.getTotalAmount()) +
                    "\nKembalian: Rp" + String.format("%,d", currentTransaction.getChange()),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

                // Reset form for new transaction
                currentTransaction = new Transaction(
                    currentTransaction.getId() + 1, 
                    generateTransactionCode(), 
                    null
                );
                refreshProductList();
                refreshItemsList();
                paymentField.setText("");
            }
        } catch (NumberFormatException ex) {
            showError("Masukkan jumlah pembayaran yang valid");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}