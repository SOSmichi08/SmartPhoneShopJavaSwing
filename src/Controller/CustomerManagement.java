package Controller;

import Model.Customer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbConnection.DatabaseAccess;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import java.text.ParseException;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


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
        setLayout(new GridLayout(6, 2,5,5));

        MongoDatabase db = DatabaseAccess.getDatabase();
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

        add(new JLabel("Customer Date of Birth (dd/MM/yyyy):"));
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

        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Please fill in the fields and click 'Add Smartphone' to add a new smartphone.\n" +
                "To update a customer, first enter the customer's email, then enter all the attributes of the customer syou want to change and click 'Update Customer'.\n" +
                "To delete a customer, click 'Show Customers' and select a row to delete.\n" +
                "Click 'Help' to see this message again."));
        add(helpButton);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    //MongoClient mongoClient = MongoClients.create(uri);//fix this !!! pojo is an alternative to get connections automatically and not manually instead of the "fromDocument" method.
    //MongoDatabase database = mongoClient.getDatabase("smartphone_shop").withCodecRegistry(pojoCodecRegistry);
   // MongoCollection<Customer> collection = database.getCollection("flowers", Customer.class);

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

        Customer customer = new Customer(formOfAddress, firstName, lastName, email, address, username, birthday, phoneNumberPrivate, phoneNumberMobile, password);
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

        Date birthday = null;
        try {
            String dateText = dateOfBirthField.getText().trim();
            if (!dateText.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                birthday = df.parse(dateText);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.");
            return;
        }

        if (firstName.isEmpty() && lastName.isEmpty() && address.isEmpty() &&
                formOfAddress.isEmpty() && username.isEmpty() && password.isEmpty() &&
                phoneNumberPrivate.isEmpty() && phoneNumberMobile.isEmpty() &&
                birthday == null && emailNew.equals(email)) {
            JOptionPane.showMessageDialog(this, "Nothing to update. Please enter the specifications you want to change.");
            return;
        }

        if (emailNew.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email to update.");
            return;
        }

        List<Bson> updates = new ArrayList<>();
        if (!firstName.isEmpty()) updates.add(Updates.set("firstName", firstName));
        if (!lastName.isEmpty()) updates.add(Updates.set("lastName", lastName));
        if (!address.isEmpty()) updates.add(Updates.set("address", address));
        if (!formOfAddress.isEmpty()) updates.add(Updates.set("formOfAdress", formOfAddress));
        if (!username.isEmpty()) updates.add(Updates.set("username", username));
        if (!password.isEmpty()) updates.add(Updates.set("password", password));
        if (birthday != null) updates.add(Updates.set("dateOfBirth", birthday));
        if (!phoneNumberPrivate.isEmpty()) updates.add(Updates.set("phoneNumber Private", phoneNumberPrivate));
        if (!phoneNumberMobile.isEmpty()) updates.add(Updates.set("phoneNumber Mobile", phoneNumberMobile));

        if (!emailNew.isEmpty() && !emailNew.equals(email)) {
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
        dialog.setSize(1300, 400);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Select a row and click 'Delete Customer' to remove.");
        dialog.add(label, BorderLayout.NORTH);

        String[] columnNames = {
                "Form of Address", "First Name", "Last Name", "Email", "Address",
                "Username", "Date of Birth", "Personal Phone", "Mobile Phone", "Password"
        };
        List<String[]> data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Document doc : customerCollection.find()) {
            Customer customer = Customer.fromDocument(doc);
            String formattedDate = (customer.getDateOfBirth() != null) ? dateFormat.format(customer.getDateOfBirth()) : "N/A";

            data.add(new String[] {
                    customer.getFormOfAddress(), customer.getFirstName(), customer.getLastName(),
                    customer.getEmail(), customer.getAddress(), customer.getUsername(),
                    formattedDate, customer.getPhoneNumberPrivate(), customer.getPhoneNumberMobile(),
                    customer.getPassword()
            });
        }

        String[][] dataArray = data.toArray(new String[0][]);
        JTable table = new JTable(dataArray, columnNames);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Create delete button
        JButton deleteButton = new JButton("Delete Customer");
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
                JOptionPane.showMessageDialog(dialog, "Please select a customer to delete.");
                return;
            }


            String email = (String) table.getValueAt(selectedRow, 3); // Get email from the selected row
            email = email.trim();
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete this customer?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteCustomer(email);
                dialog.dispose();
                showCustomers(); // Refresh table after deletion
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

}
