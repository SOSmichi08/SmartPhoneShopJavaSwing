package View;

import Controller.CustomerManagement;
import Controller.OrderManagement;
import Controller.SmartphoneManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;

public class SmartphoneShopMainframe extends JFrame {
    private JButton manageSmartphonesButton;
    private JButton manageCustomersButton;
    private JButton manageOrdersButton;

    // Mainframe for Smartphone Shop
    public SmartphoneShopMainframe() {
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

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPanel);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(0, 122, 255));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Smartphone Shop Admin", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(timeLabel, BorderLayout.EAST);
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        ActionListener timerListener = e -> {
            Date date = new Date();
            String time = timeFormat.format(date);
            timeLabel.setText(time);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        manageSmartphonesButton = createStyledButton("Manage Smartphones");
        manageCustomersButton = createStyledButton("Manage Customers");
        manageOrdersButton = createStyledButton("Manage Orders");

        buttonPanel.add(manageSmartphonesButton);
        buttonPanel.add(manageCustomersButton);
        buttonPanel.add(manageOrdersButton);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

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
        SwingUtilities.invokeLater(SmartphoneShopMainframe::new);
    }
}
