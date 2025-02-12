package View;

import Controller.CustomerManagement;
import Controller.OrderManagement;
import Controller.SmartphoneManagement;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class SmartphoneShopMainframe extends JFrame {
    private JButton manageSmartphonesButton;
    private JButton manageCustomersButton;
    private JButton manageOrdersButton;

    public SmartphoneShopMainframe() {
        // Setting the title and window close operation
        setTitle("Smartphone Shop Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 350));
        setLocationRelativeTo(null); // Centering the window

        // Setting a modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main content panel with modern layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Adding padding
        setContentPane(contentPanel);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 122, 255));
        JLabel titleLabel = new JLabel("              Smartphone Shop Admin");// vorübergehende Lösung
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(titleLabel);



        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));// Light gray background

        // Manage Smartphones Button
        manageSmartphonesButton = createStyledButton("Manage Smartphones");

        // Manage Customers Button
        manageCustomersButton = createStyledButton("Manage Customers");

        // Manage Orders Button
        manageOrdersButton = createStyledButton("Manage Orders");

        // Adding buttons to the panel
        buttonPanel.add(manageSmartphonesButton);
        buttonPanel.add(manageCustomersButton);
        buttonPanel.add(manageOrdersButton);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        // Pack the window to preferred size and make it visible
        pack();
        setVisible(true);
    }

    // Method to create uniformly styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Subtle and modern font
        button.setBackground(new Color(255, 255, 255)); // White background
        button.setForeground(new Color(0, 122, 255)); // Subtle blue text
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230))); // Light gray border
        button.setPreferredSize(new Dimension(300, 40));
        button.addActionListener(e -> {
            if (text.equals("Manage Smartphones")) {
                new SmartphoneManagement();
            } else if (text.equals("Manage Customers")) {
                new CustomerManagement();
            } else if (text.equals("Manage Orders")) {
                new OrderManagement();
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartphoneShopMainframe app = new SmartphoneShopMainframe();
        });
    }
}
