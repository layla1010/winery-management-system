package entity;

//Customer class extending Person
import java.util.Date;

public class Customer extends Person {
 private int customerID;
 private String deliveryAddress;
 private Date firstContactDate;

 // Constructor
 public Customer(int customerID, String name, String phoneNumber, 
                 String deliveryAddress,String email,  Date firstContactDate) {
     super(name, phoneNumber, email);
     this.customerID = customerID;
     this.deliveryAddress = deliveryAddress;
     this.firstContactDate = firstContactDate;
 }

 // Getters and Setters
 public int getCustomerID() {
     return customerID;
 }

 public void setCustomerID(int customerID) {
     this.customerID = customerID;
 }

 public String getDeliveryAddress() {
     return deliveryAddress;
 }

 public void setDeliveryAddress(String deliveryAddress) {
     this.deliveryAddress = deliveryAddress;
 }

 public Date getFirstContactDate() {
     return firstContactDate;
 }

 public void setFirstContactDate(Date firstContactDate) {
     this.firstContactDate = firstContactDate;
 }

 // Override display method
 @Override
 public void displayInfo() {
     super.displayInfo();
     System.out.println("Customer ID: " + customerID);
     System.out.println("Delivery Address: " + deliveryAddress);
     System.out.println("First Contact Date: " + firstContactDate);
 }
 @Override
 public String toString() {
     return this.getName();
 }
 @Override
 public boolean equals(Object o) {
     if (this == o) return true;
     if (o == null || getClass() != o.getClass()) return false;
     Customer customer = (Customer) o;
     return customerID == customer.customerID;
 }

 @Override
 public int hashCode() {
     return Integer.hashCode(customerID);
 }

}

