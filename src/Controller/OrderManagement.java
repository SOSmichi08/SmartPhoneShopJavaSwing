package Controller;

import Model.Customer;
import Model.Order;
import Model.Smartphone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseAccess;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderManagement extends JFrame {
    private JTextField orderIdField, customerEmailField;
    private JTextArea itemsField;
    private MongoCollection<Document> orderCollection;
    private MongoCollection<Document> customerCollection;
    private MongoCollection<Document> smartphoneCollection;

    public OrderManagement() {
        setTitle("Order Management");
        setSize(800, 400);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        MongoDatabase db = DatabaseAccess.getDatabase();
        orderCollection = db.getCollection("orders");
        customerCollection = db.getCollection("customers");
        smartphoneCollection = db.getCollection("smartphones");

        // Row 1 - Order ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Order ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        orderIdField = new JTextField(20);
        add(orderIdField, gbc);

        // Row 2 - Customer Email
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Customer Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        customerEmailField = new JTextField(20);
        add(customerEmailField, gbc);

        // Row 3 - Smartphone Models
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Smartphone Models (comma-separated):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        itemsField = new JTextArea(3, 20);
        add(new JScrollPane(itemsField), gbc);

        // Row 4 - Buttons (spanning two columns)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton addButton = new JButton("Create Order");
        JButton updateButton = new JButton("Update Order");
        JButton showButton = new JButton("Show Orders");
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Please fill in the fields and click 'Add Smartphone' to add a new smartphone.\n" +
                "To update an order, enter the Order Id and all the other attributes you want to change and click 'Update Order'.\n" +
                "To delete an order, click 'Show Orders' and select a row to delete.\n" +
                "Click 'Help' to see this message again."));

        addButton.addActionListener(e -> createOrder());
        updateButton.addActionListener(e -> updateOrder());
        showButton.addActionListener(e -> showOrders());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(showButton);
        buttonPanel.add(helpButton);


        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createOrder() {
        String email = customerEmailField.getText().trim();
        Document customerDoc = customerCollection.find(new Document("email", email)).first();

        if (customerDoc == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        List<Smartphone> items = new ArrayList<>();
        double totalPrice = 0.0;
        String[] models = itemsField.getText().split(",");

        for (String model : models) {
            Document phoneDoc = smartphoneCollection.find(new Document("model", model.trim())).first();
            if (phoneDoc != null) {
                Smartphone phone = Smartphone.fromDocument(phoneDoc);
                items.add(phone);
                totalPrice += phone.getPriceInCHF();
            }
        }

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid smartphones found!");
            return;
        }

        Customer customer = Customer.fromDocument(customerDoc);
        String orderId = orderIdField.getText().trim().toString();
        Order order = new Order(orderId, customer, items, totalPrice);

        orderCollection.insertOne(order.toDocument());
        JOptionPane.showMessageDialog(this, "Order created successfully!");
    }

    private void updateOrder() {
        String orderId = orderIdField.getText().trim();
        if (orderId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Order-ID to update.");
            return;
        }

        Document existingOrder = orderCollection.find(Filters.eq("orderId", orderId)).first();
        if (existingOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }

        Document updateFields = new Document();

        String email = customerEmailField.getText().trim();
        if (!email.isEmpty()) {
            Document customerDoc = customerCollection.find(new Document("email", email)).first();
            if (customerDoc != null) {
                updateFields.append("customer", customerDoc);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found!");
                return;
            }
        }

        String[] models = itemsField.getText().split(",");
        List<Document> updatedItems = new ArrayList<>();
        double totalPrice = 0.0;

        if (models.length > 0 && !models[0].trim().isEmpty()) {
            for (String model : models) {
                Document phoneDoc = smartphoneCollection.find(new Document("model", model.trim())).first();
                if (phoneDoc != null) {
                    updatedItems.add(phoneDoc);
                    totalPrice += phoneDoc.getDouble("price");
                }
            }
            if (!updatedItems.isEmpty()) {
                updateFields.append("items", updatedItems);
                updateFields.append("totalPrice", totalPrice);
            } else {
                JOptionPane.showMessageDialog(this, "No valid smartphones found!");
                return;
            }
        }

        if (!updateFields.isEmpty()) {
            orderCollection.updateOne(Filters.eq("orderId", orderId), new Document("$set", updateFields));
            JOptionPane.showMessageDialog(this, "Order updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "No new values were inputted. Nothing was updated.");
        }
    }


    private void deleteOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Order ID to delete.");
            return;
        }

        Document existingOrder = orderCollection.find(Filters.eq("orderId", orderId)).first();
        if (existingOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }

        orderCollection.deleteOne(Filters.eq("orderId", orderId));
        JOptionPane.showMessageDialog(this, "Order deleted successfully!");
        showOrders();
    }

    private void showOrders() {
        JDialog dialog = new JDialog(this, "Orders", true);
        dialog.setSize(800, 400);
        dialog.setLayout(new BorderLayout());
        setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Order ID", "Customer Email", "Total Price", "Smartphones"};
        List<String[]> data = new ArrayList<>();

        for (Document doc : orderCollection.find()) {
            Order order = Order.fromDocument(doc);
            data.add(new String[]{
                    order.getOrderId(),
                    order.getCustomer().getEmail(),
                    String.format("%.2f", order.getTotalPrice()),
                    order.getSmartphoneListAsString()
            });
        }

        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);
        table.setDefaultEditor(Object.class, null); // read only table
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Delete Order Button
        JButton deleteButton = new JButton("Delete Order");
        deleteButton.setEnabled(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

                deleteButton.setEnabled(row >= 0);

                // Double-click event for cell selection
                if (e.getClickCount() == 2 && row >= 0 && column >= 0) {
                    String cellValue = (String) table.getValueAt(row, column); // Get value of clicked cell

                    // Display the clicked cell value in a popup window
                    JOptionPane.showMessageDialog(dialog, "Selected cell value: " + cellValue,
                            "Cell Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String orderId = (String) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to delete this order?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteOrder(orderId);
                    dialog.dispose();
                    showOrders();
                }
            }
        });

        dialog.setVisible(true);
    }

}
