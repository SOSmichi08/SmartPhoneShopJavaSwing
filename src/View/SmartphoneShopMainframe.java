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
        // Set title and window close operation
        setTitle("Smartphone Shop Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 350));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPanel);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 122, 255));
        JLabel titleLabel = new JLabel("Smartphone Shop Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(titleLabel);



        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        //Buttons
        manageSmartphonesButton = createStyledButton("Manage Smartphones");

        manageCustomersButton = createStyledButton("Manage Customers");

        manageOrdersButton = createStyledButton("Manage Orders");

        // Adding buttons to panel
        buttonPanel.add(manageSmartphonesButton);
        buttonPanel.add(manageCustomersButton);
        buttonPanel.add(manageOrdersButton);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    //create uniformly styled buttons method
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(new Color(0, 122, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
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
