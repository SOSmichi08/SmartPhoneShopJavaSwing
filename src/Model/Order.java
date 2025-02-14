package Model;

import org.bson.Document;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private String orderId;
    private Customer customer;
    private List<Smartphone> items;
    private double totalPrice;

    public Order(String orderId, Customer customer, List<Smartphone> items, double totalPrice) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.totalPrice = items.stream().mapToDouble(Smartphone::getPriceInCHF).sum();
    }

    //object to document method
    public Document toDocument() {
        return new Document("orderId", orderId)  // Ensuring ID remains unchanged
                .append("customer", customer.toDocument())
                .append("items", items.stream().map(Smartphone::toDocument).collect(Collectors.toList()))
                .append("totalPrice", totalPrice);
    }

    //document to object method
    public static Order fromDocument(Document doc) {
        Customer customer = Customer.fromDocument((Document) doc.get("customer"));
        List<Smartphone> items = ((List<Document>) doc.get("items"))
                .stream().map(Smartphone::fromDocument).collect(Collectors.toList());

        return new Order(doc.getString("orderId"), customer, items, doc.getDouble("totalPrice"));
    }

    //getters
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<Smartphone> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }

    public String getSmartphoneListAsString() {
        return items.stream()
                .map(Smartphone::getModel)  //gets smartphone models
                .collect(Collectors.joining(", "));
    }
}
