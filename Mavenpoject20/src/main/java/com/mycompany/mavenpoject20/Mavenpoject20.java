package com.mycompany.mavenpoject20;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Mavenpoject20 extends JFrame implements Runnable {
    private JLabel bannerLabel;
    private JButton manageProductsButton, sellProductsButton, manageCustomersButton;
    private ProductForm productForm;
    private CustomerForm customerForm;
    private volatile boolean running;
    private String bannerText;
    private Thread animationThread;
    private TransactionForm transactionForm;
  

    public Mavenpoject20() {
        initializeUI();
        startBannerAnimation();
    }

    private void initializeUI() {
        setTitle("Aplikasi Kasir - Toko Minuman");
        setSize(900, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Banner
        bannerLabel = new JLabel("", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bannerLabel.setForeground(new Color(0, 100, 0)); // Warna hijau tua
        mainPanel.add(bannerLabel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Products Button
        manageProductsButton = new JButton("Kelola Produk");
        manageProductsButton.setFont(new Font("Arial", Font.BOLD, 14));
        manageProductsButton.setBackground(new Color(70, 130, 180)); // Steel blue
        manageProductsButton.setForeground(Color.WHITE);
        manageProductsButton.addActionListener(e -> openProductManagement());

        // Sales Button
        sellProductsButton = new JButton("Jual Produk");
        sellProductsButton.setFont(new Font("Arial", Font.BOLD, 14));
        sellProductsButton.setBackground(new Color(34, 139, 34)); // Forest green
        sellProductsButton.setForeground(Color.WHITE);
        sellProductsButton.addActionListener(e -> openTransactionForm());

        // Customers Button
        manageCustomersButton = new JButton("Kelola Pelanggan");
        manageCustomersButton.setFont(new Font("Arial", Font.BOLD, 14));
        manageCustomersButton.setBackground(new Color(147, 112, 219)); // Medium purple
        manageCustomersButton.setForeground(Color.WHITE);
        manageCustomersButton.addActionListener(e -> openCustomerManagement());

        buttonPanel.add(manageProductsButton);
        buttonPanel.add(sellProductsButton);
        buttonPanel.add(manageCustomersButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateBanner();
    }

    public void updateBanner() {
        if (productForm == null || productForm.getProducts().isEmpty()) {
            bannerText = " SELAMAT DATANG DI TOKO MINUMAN KAMI - Tidak ada produk yang tersedia saat ini ";
        } else {
            StringBuilder sb = new StringBuilder(" PROMO HARI INI: ");
            for (Product product : productForm.getProducts()) {
                if (product.getStock() > 0) {
                    sb.append(product.getName())
                      .append(" (Rp").append(String.format("%,d", product.getPrice()))
                      .append(") - Stok: ").append(product.getStock())
                      .append(" | ");
                }
            }
            bannerText = sb.length() > 4 ? sb.substring(0, sb.length() - 3) : sb.toString();
        }
    }

    private void startBannerAnimation() {
        running = true;
        animationThread = new Thread(this);
        animationThread.start();
    }

    private void stopBannerAnimation() {
        running = false;
        if (animationThread != null) {
            animationThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Shift text to create scrolling effect
                if (bannerText != null && bannerText.length() > 0) {
                    bannerText = bannerText.substring(1) + bannerText.charAt(0);
                }
                
                SwingUtilities.invokeLater(() -> {
                    bannerLabel.setText(bannerText);
                });
                
                Thread.sleep(150); // Faster animation speed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void openProductManagement() {
        if (productForm == null || !productForm.isVisible()) {
            productForm = new ProductForm(this);
            productForm.setVisible(true);
        } else {
            productForm.toFront();
        }
    }

    private void openCustomerManagement() {
        if (customerForm == null || !customerForm.isVisible()) {
            customerForm = new CustomerForm();
            customerForm.setVisible(true);
        } else {
            customerForm.toFront();
        }
    }

private void openTransactionForm() {
    if (productForm == null || productForm.getProducts().isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Tidak ada produk yang tersedia. Silakan tambahkan produk terlebih dahulu.", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    if (customerForm == null) {
        customerForm = new CustomerForm();
    }
    
    if (transactionForm == null || !transactionForm.isVisible()) {
        transactionForm = new TransactionForm(productForm, customerForm);
        transactionForm.setVisible(true);
    } else {
        transactionForm.toFront();
    }
}


    @Override
    public void dispose() {
        stopBannerAnimation();
        if (productForm != null) productForm.dispose();
        if (customerForm != null) customerForm.dispose();
        super.dispose();
    }

    public void resetBannerAnimation() {
        stopBannerAnimation();
        updateBanner();
        startBannerAnimation();
    }

    public void updateBannerText(String text) {
        this.bannerText = text;
    }

    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Mavenpoject20 app = new Mavenpoject20();
            app.setVisible(true);
        });
    }
}