package com.mycompany.mavenpoject20;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistory extends JFrame {
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private List<Transaction> transactions;

    public TransactionHistory(List<Transaction> transactions) {
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Riwayat Transaksi");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        tableModel = new DefaultTableModel(
            new String[]{"Kode", "Tanggal", "Pelanggan", "Jumlah Item", "Total", "Pembayaran", "Kembalian"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton viewDetailsButton = new JButton("Lihat Detail");
        viewDetailsButton.addActionListener(e -> viewTransactionDetails());
        buttonPanel.add(viewDetailsButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (Transaction transaction : transactions) {
            tableModel.addRow(new Object[]{
                transaction.getTransactionCode(),
                dateFormat.format(transaction.getTransactionDate()),
                transaction.getCustomer() != null ? transaction.getCustomer().getName() : "Tanpa Pelanggan",
                transaction.getItems().size(),
                String.format("Rp%,d", transaction.getTotalAmount()),
                String.format("Rp%,d", transaction.getPaymentAmount()),
                String.format("Rp%,d", transaction.getChange())
            });
        }
    }

    private void viewTransactionDetails() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih transaksi terlebih dahulu", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transaction selectedTransaction = transactions.get(selectedRow);
        StringBuilder details = new StringBuilder();
        details.append("Kode Transaksi: ").append(selectedTransaction.getTransactionCode()).append("\n");
        details.append("Tanggal: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(selectedTransaction.getTransactionDate())).append("\n");
        
        if (selectedTransaction.getCustomer() != null) {
            details.append("Pelanggan: ").append(selectedTransaction.getCustomer().getName()).append("\n");
            details.append("Telepon: ").append(selectedTransaction.getCustomer().getPhone()).append("\n");
        }
        
        details.append("\nDaftar Produk:\n");
        for (TransactionItem item : selectedTransaction.getItems()) {
            details.append("- ").append(item.toString()).append("\n");
        }
        
        details.append("\nTotal Belanja: Rp").append(String.format("%,d", selectedTransaction.getTotalAmount())).append("\n");
        details.append("Pembayaran: Rp").append(String.format("%,d", selectedTransaction.getPaymentAmount())).append("\n");
        details.append("Kembalian: Rp").append(String.format("%,d", selectedTransaction.getChange()));

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        refreshTable();
    }
}