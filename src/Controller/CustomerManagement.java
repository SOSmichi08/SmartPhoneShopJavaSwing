package Controller;

import Model.Customer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseConnection;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import java.text.ParseException;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerManagement extends JFrame {
    private JTextField emailField, firstNameField, lastNameField, addressField, formOfAdressField, usernameField, passwordField, phoneNumberPrivateField, phoneNumberMobileField;
    private JFormattedTextField dateOfBirthField;
    private MongoCollection<Document> customerCollection;

    public CustomerManagement() {
        setTitle("Customer Management");
        setSize(1400, 600);
        setLayout(new GridLayout(6, 2));

        MongoDatabase db = DatabaseConnection.getDatabase();
        customerCollection = db.getCollection("customers");

        add(new JLabel("Customer Form of Address:"));
        formOfAdressField = new JTextField();
        add(formOfAdressField);

        add(new JLabel("Customer Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Customer Password:"));
        passwordField = new JTextField();
        add(passwordField);

        add(new JLabel("Customer Date of Birth:"));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dateOfBirthField = new JFormattedTextField(df);
        dateOfBirthField.setColumns(10);
        add(dateOfBirthField);

        add(new JLabel("Customer Private Phone Number:"));
        phoneNumberPrivateField = new JTextField();
        add(phoneNumberPrivateField);

        add(new JLabel("Customer Mobile Phone Number:"));
        phoneNumberMobileField = new JTextField();
        add(phoneNumberMobileField);

        add(new JLabel("Customer Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Customer First Name:"));
        firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Customer Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Customer Address:"));
        addressField = new JTextField();
        add(addressField);

        JButton addButton = new JButton("Create Customer");
        addButton.addActionListener(e -> createCustomer());
        add(addButton);

        JButton updateButton = new JButton("Update Customer");
        updateButton.addActionListener(e -> updateCustomer());
        add(updateButton);

        JButton showButton = new JButton("Show Customers");
        showButton.addActionListener(e -> showCustomers());
        add(showButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createCustomer() {
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String formOfAddress = formOfAdressField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String phoneNumberPrivate = phoneNumberPrivateField.getText().trim();
        String phoneNumberMobile = phoneNumberMobileField.getText().trim();

        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || formOfAddress.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumberPrivate.isEmpty() || phoneNumberMobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        Date birthday = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            String dateText = dateOfBirthField.getText();
            if (!dateText.isEmpty()) {
                birthday = df.parse(dateText);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.");
            return;
        }

        if (birthday == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date of birth.");
            return;
        }

        Document existingCustomer = customerCollection.find(Filters.eq("email", email)).first();
        if (existingCustomer != null) {
            JOptionPane.showMessageDialog(this, "Customer already exists.");
            return;
        }

        // Fix: Import java.util.Date and use it to create a new Date object
        Customer customer = new Customer(formOfAddress, firstName, lastName, email, address, username, password, birthday, phoneNumberPrivate, phoneNumberMobile);
        customerCollection.insertOne(customer.toDocument());
        JOptionPane.showMessageDialog(this, "Customer created successfully!");
    }


    private void updateCustomer() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email to update.");
            return;
        }

        Document existingCustomer = customerCollection.find(Filters.eq("email", email)).first();
        if (existingCustomer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String formOfAddress = formOfAdressField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String phoneNumberPrivate = phoneNumberPrivateField.getText().trim();
        String phoneNumberMobile = phoneNumberMobileField.getText().trim();
        String emailNew = emailField.getText().trim();
        Date birthday = dateOfBirthField.getText().trim().isEmpty() ? null : new Date();

        if (firstName.isEmpty() && lastName.isEmpty() && address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing to update.");
            return;
        }

        List<Bson> updates = new ArrayList<>();
        if (!firstName.isEmpty()) updates.add(Updates.set("firstName", firstName));
        if (!lastName.isEmpty()) updates.add(Updates.set("lastName", lastName));
        if (!address.isEmpty()) updates.add(Updates.set("address", address));
        if (!formOfAddress.isEmpty()) updates.add(Updates.set("formOfAddress", formOfAddress));
        if (!username.isEmpty()) updates.add(Updates.set("username", username));
        if (!password.isEmpty()) updates.add(Updates.set("password", password));
        if (birthday != null) updates.add(Updates.set("DateOfBirth", birthday));
        if (!phoneNumberPrivate.isEmpty()) updates.add(Updates.set("phoneNumberPrivate", phoneNumberPrivate));
        if (!phoneNumberMobile.isEmpty()) updates.add(Updates.set("phoneNumberMobile", phoneNumberMobile));
        if (!emailNew.isEmpty() &&!emailNew.equals(email)) {
            Document existingCustomerNew = customerCollection.find(Filters.eq("email", emailNew)).first();
            if (existingCustomerNew != null) {
                JOptionPane.showMessageDialog(this, "Customer with new email already exists.");
                return;
            }
            updates.add(Updates.set("email", emailNew));
        }

        customerCollection.updateOne(Filters.eq("email", email), Updates.combine(updates));
        JOptionPane.showMessageDialog(this, "Customer updated successfully!");
    }

    private void deleteCustomer(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }

        email = email.trim();
        System.out.println("Attempting to delete customer with email: " + email);
        Document existingCustomer = customerCollection.find(Filters.eq("email", email)).first();
        if (existingCustomer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
            return;
        }

        customerCollection.deleteOne(Filters.eq("email", email));
        JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
        dispose();
        showCustomers();
    }


    private void showCustomers() {
        JDialog dialog = new JDialog(this, "Customers", true);
        dialog.setSize(900, 400);
        dialog.setLayout(new BorderLayout());

        String[] columnNames = {"First Name", "Email", "Address", "Last Name", "Personal Phone", "Mobile Phone", "Date of Birth", "Username", "Password", "Form of Adress"};
        List<String[]> data = new ArrayList<>();
        for (Document doc : customerCollection.find()) {
            Customer customer = Customer.fromDocument(doc);
            data.add(new String[] {
                    customer.getFormOfAddress(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getAddress(),
                    customer.getUsername(),
                    String.valueOf(customer.getDateOfBirth()),
                    customer.getPhoneNumberPrivate(),
                    customer.getPhoneNumberMobile(),
                    customer.getPassword()
            });
        }

        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);

        // Mouse listener for handling row selection
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String email = (String) table.getValueAt(row, 0);
                    email = email.trim();
                    System.out.println("Email to delete: " + email);
                    deleteCustomer(email);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setVisible(true);
    }


}
