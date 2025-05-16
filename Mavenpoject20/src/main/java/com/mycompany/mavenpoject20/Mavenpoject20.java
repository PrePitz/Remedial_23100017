package com.mycompany.mavenpoject20;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Mavenpoject20 extends JFrame implements Runnable {
    private JLabel bannerLabel;
    private JButton manageProductsButton, sellProductsButton;
    private ProductForm productForm;
    private volatile boolean running;
    private String bannerText;
    private Thread animationThread;

    public Mavenpoject20() {
        initializeUI();
        startBannerAnimation();
    }

    private void initializeUI() {
        setTitle("Aplikasi Kasir");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Banner
        bannerLabel = new JLabel("", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bannerLabel.setForeground(Color.RED);
        mainPanel.add(bannerLabel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        manageProductsButton = new JButton("Kelola Produk");
        sellProductsButton = new JButton("Jual Produk");

        manageProductsButton.addActionListener(e -> openProductManagement());
        sellProductsButton.addActionListener(e -> openSalesForm());

        buttonPanel.add(manageProductsButton);
        buttonPanel.add(sellProductsButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateBanner();
    }

    public void updateBanner() {
        if (productForm == null || productForm.getProducts().isEmpty()) {
            bannerText = " Tidak ada produk yang tersedia";
        } else {
            StringBuilder sb = new StringBuilder("Menu tersedia: ");
            for (Product product : productForm.getProducts()) {
                if (product.getStock() > 0) {
                    sb.append(product.getName())
                      .append(" (Rp").append(product.getPrice())
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
                
                Thread.sleep(200); // Animation speed
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

    private void openSalesForm() {
        if (productForm == null || !productForm.isVisible()) {
            JOptionPane.showMessageDialog(this, 
                "Buka menu Kelola Produk terlebih dahulu", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        new SalesForm(productForm).setVisible(true);
    }

    @Override
    public void dispose() {
        stopBannerAnimation();
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Mavenpoject20 app = new Mavenpoject20();
            app.setVisible(true);
        });
    }
    public void resetBannerAnimation() {
    stopBannerAnimation();
    updateBanner();
    startBannerAnimation();
}

public void updateBannerText(String text) {
    this.bannerText = text;
}
}