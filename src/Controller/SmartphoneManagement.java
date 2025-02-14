package Controller;

import Model.Smartphone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseAccess;
import org.bson.Document;

import javax.swing.*;
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

        //create and add fields
        add(new JLabel("Brand:"));
        brandField = new JTextField();
        brandField.setPreferredSize(textFieldSize);
        add(brandField);

        add(new JLabel("Model:"));
        modelField = new JTextField();
        modelField.setPreferredSize(textFieldSize);
        add(modelField);

        add(new JLabel("Price (in CHF):"));
        priceField = new JTextField();
        priceField.setPreferredSize(textFieldSize);
        add(priceField);

        add(new JLabel("Storage (in GB):"));
        storageField = new JTextField();
        storageField.setPreferredSize(textFieldSize);
        add(storageField);

        add(new JLabel("RAM (in GB):"));
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

        add(new JLabel("Battery Capacity (in mAh)"));
        batteryCapacityField = new JTextField();
        batteryCapacityField.setPreferredSize(textFieldSize);
        add(batteryCapacityField);

        add(new JLabel("Connectivity (e.g. NFC, Bluetooth, USB-C)"));
        connectivityField = new JTextField();
        connectivityField.setPreferredSize(textFieldSize);
        add(connectivityField);

        add(new JLabel("Mobile Data Standard (e.g. 4G, 5G)"));
        mobiledataStandardField = new JTextField();
        mobiledataStandardField.setPreferredSize(textFieldSize);
        add(mobiledataStandardField);


        //create and add buttons
        JButton addButton = new JButton("Add Smartphone");
        addButton.addActionListener(e -> addSmartphone());
        add(addButton);

        JButton updateButton = new JButton("Update Smartphone");
        updateButton.addActionListener(e -> updateSmartphone());
        add(updateButton);

        JButton showButton = new JButton("Show Smartphones");
        showButton.addActionListener(e -> showSmartphones());
        add(showButton);

        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Please fill in the fields and click 'Add Smartphone' to add a new smartphone.\n" +
                "To update a smartphone, first enter the model name, then all the attributes you want to change (model name is required and click 'Update Smartphone'.\n" +
                "To delete a smartphone, click 'Show Smartphones' and select a row to delete.\n" +
                "Click 'Help' to see this message again."));
        add(helpButton);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    //add a smartphone to db
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
        if(smartphoneCollection.find(new Document("model", smartphone.getModel())).first() != null) {
            JOptionPane.showMessageDialog(this, "Smartphone already exists!");
            return;
        }
        if(smartphone.getModel().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a model name.");
            return;
        }
        smartphoneCollection.insertOne(smartphone.toDocument());
        JOptionPane.showMessageDialog(this, "Smartphone added!");
    }

    //update smartphone by model name
    private void updateSmartphone() {
        String model = modelField.getText();
        if (model == null || model.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a smartphone model to update.");
            return;
        }

        Document query = new Document("model", model);
        Document existingPhone = smartphoneCollection.find(query).first();

        if (existingPhone == null) {
            JOptionPane.showMessageDialog(this, "Smartphone not found!");
            return;
        }

        Document updateFields = new Document();
        //keeps old data for attributes if no new data is available
        if (!brandField.getText().trim().isEmpty())
            updateFields.append("brand", brandField.getText());

        if (!priceField.getText().trim().isEmpty())
            updateFields.append("price", Double.parseDouble(priceField.getText()));

        if (!ramField.getText().trim().isEmpty())
            updateFields.append("ram", Integer.parseInt(ramField.getText()));

        if (!storageField.getText().trim().isEmpty())
            updateFields.append("storage", Integer.parseInt(storageField.getText()));

        if (!osField.getText().trim().isEmpty())
            updateFields.append("os", osField.getText());

        if (!osVersionField.getText().trim().isEmpty())
            updateFields.append("osVersion", osVersionField.getText());

        if (!pixelResolutionField.getText().trim().isEmpty())
            updateFields.append("pixelResolution", pixelResolutionField.getText());

        if (!screenSizeInInchesField.getText().trim().isEmpty())
            updateFields.append("screenSizeInInches", screenSizeInInchesField.getText());

        if (!numberOfCoresField.getText().trim().isEmpty())
            updateFields.append("numberOfCores", Integer.parseInt(numberOfCoresField.getText()));

        if (!batteryCapacityField.getText().trim().isEmpty())
            updateFields.append("batteryCapacity", batteryCapacityField.getText());

        if (!connectivityField.getText().trim().isEmpty())
            updateFields.append("connectivity", connectivityField.getText());

        if (!mobiledataStandardField.getText().trim().isEmpty())
            updateFields.append("mobiledataStandard", mobiledataStandardField.getText());

        if (!updateFields.isEmpty()) {
            Document updateOperation = new Document("$set", updateFields);
            smartphoneCollection.updateOne(query, updateOperation);
            JOptionPane.showMessageDialog(this, "Smartphone updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "No new values were inputted. Nothing was updated.");
        }
    }

    //delete smartphone by model name
    private void deleteSmartphone(String model) {
        if (model == null || model.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Model to delete.");
            return;
        }
        smartphoneCollection.deleteOne(new Document("model", model));
        JOptionPane.showMessageDialog(this, "Smartphone deleted!");
    }
    //show all smartphones in a table. User can delete a smartphone by selecting a row and clicking 'Delete Smartphone'. When he double-clicks on a cell its full content is displayed in a dialog.
    private void showSmartphones() {
        JDialog dialog = new JDialog(this, "Smartphones", true);
        dialog.setSize(1300, 400);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Select a row and click 'Delete Smartphone' to remove. Double click a cell to view its full content.", SwingConstants.CENTER);
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
        // make table read-only
        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Delete Button
        JButton deleteButton = new JButton("Delete Smartphone");
        deleteButton.setEnabled(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

                deleteButton.setEnabled(row >= 0);

                // Double-click event for cell selection
                if (e.getClickCount() == 2 && row >= 0 && column >= 0) {
                    String cellValue = (String) table.getValueAt(row, column); // Get value of clicked cell

                    // Display  clicked cell value popup
                    JOptionPane.showMessageDialog(dialog, "Selected cell value: " + cellValue, "Cell Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a smartphone to delete.");
                return;
            }
            // Model from selected row
            String model = (String) table.getValueAt(selectedRow, 1);
            model = model.trim();
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete this smartphone?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteSmartphone(model);
                dialog.dispose();
                showSmartphones();
            }
        });
        //add delete button and bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


}
