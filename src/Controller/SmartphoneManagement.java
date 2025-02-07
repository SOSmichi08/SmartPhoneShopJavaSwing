package Controller;

import Model.Smartphone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SmartphoneManagement extends JFrame {
    private JTextField brandField, modelField, priceField, storageField, ramField, osField, osVersionField, pixelResolutionField, screenSizeInInchesField , numberOfCoresField, batteryCapacityField, connectivityField, mobiledataStandardField;
    private MongoCollection<Document> smartphoneCollection;

    public SmartphoneManagement() {
        setTitle("Smartphone Management");
        setSize(400, 500);
        setLayout(new GridLayout(9, 2));

        MongoDatabase db = DatabaseConnection.getDatabase();
        smartphoneCollection = db.getCollection("smartphones");

        add(new JLabel("Brand:"));
        brandField = new JTextField();
        add(brandField);

        add(new JLabel("Model:"));
        modelField = new JTextField();
        add(modelField);

        add(new JLabel("Price:"));
        priceField = new JTextField();
        add(priceField);

        add(new JLabel("Storage:"));
        storageField = new JTextField();
        add(storageField);

        add(new JLabel("RAM:"));
        ramField = new JTextField();
        add(ramField);

        add(new JLabel("OS:"));
        osField = new JTextField();
        add(osField);

        add(new JLabel("OS Version"));
        osVersionField = new JTextField();
        add(osVersionField);

        add(new JLabel("Pixel Resolution"));
        pixelResolutionField = new JTextField();
        add(pixelResolutionField);

        add(new JLabel("Screen Size in Inches"));
        screenSizeInInchesField = new JTextField();
        add(screenSizeInInchesField);

        add(new JLabel("Number of Cores"));
        numberOfCoresField = new JTextField();
        add(numberOfCoresField);

        add(new JLabel("Battery Capacity"));
        batteryCapacityField = new JTextField();
        add(batteryCapacityField);

        add(new JLabel("Connectivity"));
        connectivityField = new JTextField();
        add(connectivityField);

        add(new JLabel("Mobile Data Standard"));
        mobiledataStandardField = new JTextField();
        add(mobiledataStandardField);

        JButton addButton = new JButton("Add Smartphone");
        addButton.addActionListener(e -> addSmartphone());
        add(addButton);

        JButton updateButton = new JButton("Update Smartphone");
        updateButton.addActionListener(e -> updateSmartphone());
        add(updateButton);

        JButton deleteButton = new JButton("Delete Smartphone");
        deleteButton.addActionListener(e -> deleteSmartphone());
        add(deleteButton);

        JButton showButton = new JButton("Show All Smartphones");
        showButton.addActionListener(e -> showSmartphones());
        add(showButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addSmartphone() {
        Smartphone smartphone = new Smartphone(
                brandField.getText(),
                modelField.getText(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(ramField.getText()),
                screenSizeInInchesField.getText(),
                Integer.parseInt(storageField.getText()),
                osField.getText(),
                osVersionField.getText(),
                pixelResolutionField.getText(),
                Integer.parseInt(numberOfCoresField.getText()),
                batteryCapacityField.getText(),
                connectivityField.getText(),
                mobiledataStandardField.getText()
        );
        smartphoneCollection.insertOne(smartphone.toDocument());
        JOptionPane.showMessageDialog(this, "Smartphone added!");
    }

    private void updateSmartphone() {
        String model = modelField.getText();
        Document query = new Document("model", model);
        Document existingPhone = smartphoneCollection.find(query).first();

        if (existingPhone == null) {
            JOptionPane.showMessageDialog(this, "Smartphone not found!");
            return;
        }

        Smartphone updatedPhone = new Smartphone(
                brandField.getText(),
                modelField.getText(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(ramField.getText()),
                screenSizeInInchesField.getText(),
                Integer.parseInt(storageField.getText()),
                osField.getText(),
                osVersionField.getText(),
                pixelResolutionField.getText(),
                Integer.parseInt(numberOfCoresField.getText()),
                batteryCapacityField.getText(),
                connectivityField.getText(),
                mobiledataStandardField.getText()
        );

        smartphoneCollection.replaceOne(query, updatedPhone.toDocument());
        JOptionPane.showMessageDialog(this, "Smartphone updated!");
    }

    private void deleteSmartphone() {
        String model = modelField.getText();
        smartphoneCollection.deleteOne(new Document("model", model));
        JOptionPane.showMessageDialog(this, "Smartphone deleted!");
    }

    private void showSmartphones() {
        JFrame displayFrame = new JFrame("Smartphone List");
        displayFrame.setSize(800, 400);

        String[] columnNames = {"Brand", "Model", "Price (CHF)", "Storage (GB)", "RAM (GB)", "OS", "OS Version", "Pixel Resolution", "Number of Cores", "Battery Capacity", "Connectivity", "Mobile Data Standard"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        List<Document> documents = smartphoneCollection.find().into(new ArrayList<>());
        for (Document doc : documents) {
            Smartphone phone = Smartphone.fromDocument(doc);
            Object[] row = {
                    phone.getBrand(),
                    phone.getModel(),
                    phone.getPriceInCHF(),
                    phone.getStorageInGB(),
                    phone.getRamInGB(),
                    phone.getOs()
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        displayFrame.add(scrollPane);
        displayFrame.setVisible(true);
    }
}
