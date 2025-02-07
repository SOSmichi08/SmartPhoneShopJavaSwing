package Controller;

import Model.Customer;
import Model.Order;
import Model.Smartphone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseConnection;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import javax.swing.*;
import java.awt.*;
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
        setSize(800, 600);
        setLayout(new GridLayout(7, 2));

        MongoDatabase db = DatabaseConnection.getDatabase();
        orderCollection = db.getCollection("orders");
        customerCollection = db.getCollection("customers");
        smartphoneCollection = db.getCollection("smartphones");

        add(new JLabel("Order ID:"));
        orderIdField = new JTextField();
        add(orderIdField);

        add(new JLabel("Customer Email:"));
        customerEmailField = new JTextField();
        add(customerEmailField);

        add(new JLabel("Smartphone Models (comma-separated):"));
        itemsField = new JTextArea(3, 20);
        add(new JScrollPane(itemsField));

        JButton addButton = new JButton("Create Order");
        addButton.addActionListener(e -> createOrder());
        add(addButton);

        JButton updateButton = new JButton("Update Order");
        updateButton.addActionListener(e -> updateOrder());
        add(updateButton);

        JButton deleteButton = new JButton("Delete Order");
        deleteButton.addActionListener(e -> deleteOrder());
        add(deleteButton);

        JButton showButton = new JButton("Show Orders");
        showButton.addActionListener(e -> showOrders());
        add(showButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createOrder() {
        String email = customerEmailField.getText();
        Document customerDoc = customerCollection.find(new Document("email", email)).first();

        if (customerDoc == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        List<Smartphone> items = new ArrayList<>();
        String[] models = itemsField.getText().split(",");

        for (String model : models) {
            Document phoneDoc = smartphoneCollection.find(new Document("model", model.trim())).first();
            if (phoneDoc != null) {
                items.add(Smartphone.fromDocument(phoneDoc));
            }
        }

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid smartphones found!");
            return;
        }

        Customer customer = Customer.fromDocument(customerDoc);
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, customer, items);

        orderCollection.insertOne(order.toDocument());
        JOptionPane.showMessageDialog(this, "Order created successfully!");
    }

    private void updateOrder() {
        String orderId = orderIdField.getText().trim();
        if (orderId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Order ID to update.");
            return;
        }

        Document existingOrder = orderCollection.find(Filters.eq("orderId", orderId)).first();
        if (existingOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }

        String email = customerEmailField.getText().trim();
        Document customerDoc = customerCollection.find(new Document("email", email)).first();
        if (customerDoc == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        List<Smartphone> items = new ArrayList<>();
        String[] models = itemsField.getText().split(",");

        for (String model : models) {
            Document phoneDoc = smartphoneCollection.find(new Document("model", model.trim())).first();
            if (phoneDoc != null) {
                items.add(Smartphone.fromDocument(phoneDoc));
            }
        }

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid smartphones found!");
            return;
        }

        Customer customer = Customer.fromDocument(customerDoc);
        Order updatedOrder = new Order(orderId, customer, items);

        orderCollection.updateOne(Filters.eq("orderId", orderId),
                Updates.combine(
                        Updates.set("customer", updatedOrder.getCustomer().toDocument()),
                        Updates.set("items", updatedOrder.getItems().stream().map(Smartphone::toDocument).toList()),
                        Updates.set("totalPrice", updatedOrder.getTotalPrice())
                ));

        JOptionPane.showMessageDialog(this, "Order updated successfully!");
    }

    private void deleteOrder() {
        String orderId = orderIdField.getText().trim();
        if (orderId.isEmpty()) {
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
    }

    private void showOrders() {
        JDialog dialog = new JDialog(this, "Orders", true);
        dialog.setSize(800, 400);
        dialog.setLayout(new BorderLayout());

        String[] columnNames = {"Order ID", "Customer Email", "Total Price", "Smartphones"};
        List<String[]> data = new ArrayList<>();

        for (Document doc : orderCollection.find()) {
            Order order = Order.fromDocument(doc);
            data.add(new String[]{
                    order.getOrderId(),
                    order.getCustomer().getEmail(),
                    String.valueOf(order.getTotalPrice()),
                    order.getSmartphoneListAsString()
            });
        }

        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
