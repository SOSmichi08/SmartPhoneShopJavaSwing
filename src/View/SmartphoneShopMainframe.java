package View;

import Controller.CustomerManagement;
import Controller.OrderManagement;
import Controller.SmartphoneManagement;

import javax.swing.*;
import java.awt.*;

public class SmartphoneShopMainframe extends JFrame {
    private JButton manageSmartphonesButton;
    private JButton manageCustomersButton;
    private JButton manageOrdersButton;

    public SmartphoneShopMainframe() {
        setTitle("Smartphone Shop Admin");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        manageSmartphonesButton = new JButton("Manage Smartphones");
        manageCustomersButton = new JButton("Manage Customers");
        manageOrdersButton = new JButton("Manage Orders");

        add(manageSmartphonesButton);
        add(manageCustomersButton);
        add(manageOrdersButton);

        manageSmartphonesButton.addActionListener(e -> new SmartphoneManagement());
        manageCustomersButton.addActionListener(e -> new CustomerManagement());
        manageOrdersButton.addActionListener(e -> new OrderManagement());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartphoneShopMainframe app = new SmartphoneShopMainframe();
            app.setVisible(true);
        });
    }
}
