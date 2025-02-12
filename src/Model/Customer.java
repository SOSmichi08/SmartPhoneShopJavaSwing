package Model;

import org.bson.Document;

import java.util.Date;

public class Customer {
    private String formOfAddress;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String username;
    private String password;
    private Date DateOfBirth;
    private String phoneNumberPrivate;
    private String phoneNumberMobile;

    public Customer(String formOfAddress, String firstName, String lastName, String email, String address, String username, Date dateOfBirth, String phoneNumberPrivate, String phoneNumberMobile, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.DateOfBirth = dateOfBirth;
        this.phoneNumberPrivate = phoneNumberPrivate;
        this.phoneNumberMobile = phoneNumberMobile;
        this.formOfAddress = formOfAddress;
    }

    public Document toDocument() {
        Document document = new Document("firstName", firstName)
                .append("email", email)
                .append("lastName", lastName)
                .append("address", address)
                .append("username", username)
                .append("password", password)
                .append("dateOfBirth", DateOfBirth)
                .append("phoneNumber Private", phoneNumberPrivate)
                .append("phoneNumber Mobile", phoneNumberMobile)
                .append("formOfAdress", formOfAddress);
        return document;
    }

    public String getAddress() {
        return address;
    }

    public String getFormOfAddress() {
        return formOfAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public String getPhoneNumberPrivate() {
        return phoneNumberPrivate;
    }

    public String getPhoneNumberMobile() {
        return phoneNumberMobile;
    }

    public static Customer fromDocument(Document doc) {
        return new Customer(
                doc.getString("formOfAdress"),
                doc.getString("firstName"),
                doc.getString("lastName"),
                doc.getString("email"),
                doc.getString("address"),
                doc.getString("username"),
                doc.getDate("dateOfBirth"),
                doc.getString("phoneNumber Private"),
                doc.getString("phoneNumber Mobile"),
                doc.getString("password"));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
