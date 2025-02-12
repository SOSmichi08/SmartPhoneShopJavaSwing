package Controller;

import Model.Smartphone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseAccess;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SmartphoneManagement extends JFrame {
    private JTextField brandField, modelField, priceField, storageField, ramField, osField, osVersionField, pixelResolutionField, screenSizeInInchesField , numberOfCoresField, batteryCapacityField, connectivityField, mobiledataStandardField;
    private MongoCollection<Document> smartphoneCollection;

    public SmartphoneManagement() {
        setTitle("Smartphone Management");
        setSize(700, 500);
        setLayout(new GridLayout(9, 2,5,5));


        MongoDatabase db = DatabaseAccess.getDatabase();
        smartphoneCollection = db.getCollection("smartphones");
        Dimension textFieldSize = new Dimension(50, 10);

        add(new JLabel("Brand:"));
        brandField = new JTextField();
        brandField.setPreferredSize(textFieldSize);
        add(brandField);

        add(new JLabel("Model:"));
        modelField = new JTextField();
        modelField.setPreferredSize(textFieldSize);
        add(modelField);

        add(new JLabel("Price:"));
        priceField = new JTextField();
        priceField.setPreferredSize(textFieldSize);
        add(priceField);

        add(new JLabel("Storage:"));
        storageField = new JTextField();
        storageField.setPreferredSize(textFieldSize);
        add(storageField);

        add(new JLabel("RAM:"));
        ramField = new JTextField();
        ramField.setPreferredSize(textFieldSize);
        add(ramField);

        add(new JLabel("OS:"));
        osField = new JTextField();
        osField.setPreferredSize(textFieldSize);
        add(osField);

        add(new JLabel("OS Version"));
        osVersionField = new JTextField();
        osVersionField.setPreferredSize(textFieldSize);
        add(osVersionField);

        add(new JLabel("Pixel Resolution"));
        pixelResolutionField = new JTextField();
        pixelResolutionField.setPreferredSize(textFieldSize);
        add(pixelResolutionField);

        add(new JLabel("Screen Size in Inches"));
        screenSizeInInchesField = new JTextField();
        screenSizeInInchesField.setPreferredSize(textFieldSize);
        add(screenSizeInInchesField);

        add(new JLabel("Number of Cores"));
        numberOfCoresField = new JTextField();
        numberOfCoresField.setPreferredSize(textFieldSize);
        add(numberOfCoresField);

        add(new JLabel("Battery Capacity"));
        batteryCapacityField = new JTextField();
        batteryCapacityField.setPreferredSize(textFieldSize);
        add(batteryCapacityField);

        add(new JLabel("Connectivity"));
        connectivityField = new JTextField();
        connectivityField.setPreferredSize(textFieldSize);
        add(connectivityField);

        add(new JLabel("Mobile Data Standard"));
        mobiledataStandardField = new JTextField();
        mobiledataStandardField.setPreferredSize(textFieldSize);
        add(mobiledataStandardField);

        JButton addButton = new JButton("Add Smartphone");
        addButton.addActionListener(e -> addSmartphone());
        add(addButton);

        JButton updateButton = new JButton("Update Smartphone");
        updateButton.addActionListener(e -> updateSmartphone());
        add(updateButton);

        JButton showButton = new JButton("Show Smartphones");
        showButton.addActionListener(e -> showSmartphones());
        add(showButton);

        pack();
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

    private void deleteSmartphone(String model) {
        if (model == null || model.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Model to delete.");
            return;
        }
        smartphoneCollection.deleteOne(new Document("model", model));
        JOptionPane.showMessageDialog(this, "Smartphone deleted!");
    }

    private void showSmartphones() {
        JDialog dialog = new JDialog(this, "Smartphones", true);
        dialog.setSize(1200, 400);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Select a row and click 'Delete Smartphone' to remove.");
        dialog.add(label, BorderLayout.NORTH);

        String[] columnNames = {
                "Brand", "Model", "Price (CHF)", "Storage (GB)", "RAM (GB)", "OS", "OS Version",
                "Pixel Resolution", "Screen Size", "Cores", "Battery", "Connectivity", "Data Standard"
        };

        List<String[]> data = new ArrayList<>();

        for (Document doc : smartphoneCollection.find()) {
            Smartphone phone = Smartphone.fromDocument(doc);

            data.add(new String[]{
                    phone.getBrand(), phone.getModel(), String.valueOf(phone.getPriceInCHF()),
                    String.valueOf(phone.getStorageInGB()), String.valueOf(phone.getRamInGB()),
                    phone.getOs(), phone.getOSVersion(), phone.getPixelresolution(),
                    phone.getScreenSizeInInches(), String.valueOf(phone.getNumberOfCores()),
                    phone.getBatteryCapacity(), phone.getConnectivity(), phone.getMobiledataStandard()
            });
        }

        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);
        table.setDefaultEditor(Object.class, null); // To make the table read-only
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Delete Button
        JButton deleteButton = new JButton("Delete Smartphone");
        deleteButton.setEnabled(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                deleteButton.setEnabled(row >= 0);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a smartphone to delete.");
                return;
            }

            String model = (String) table.getValueAt(selectedRow, 1); // Get model from selected row
            model = model.trim();
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete this smartphone?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteSmartphone(model);
                dialog.dispose();
                showSmartphones(); // Refresh table after deletion
            }
        });


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

}
