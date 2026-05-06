package entity;

//Base class: Person
public class Person {
 private String name;
 private String phoneNumber;
 private String email;

 // Constructor
 public Person(String name, String phoneNumber, String email) {
     this.name = name;
     this.phoneNumber = phoneNumber;
     this.email = email;
 }

 // Getters and Setters
 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

 public String getPhoneNumber() {
     return phoneNumber;
 }

 public void setPhoneNumber(String phoneNumber) {
     this.phoneNumber = phoneNumber;
 }

 public String getEmail() {
     return email;
 }

 public void setEmail(String email) {
     this.email = email;
 }

 // Display method
 public void displayInfo() {
     System.out.println("Name: " + name);
     System.out.println("Phone Number: " + phoneNumber);
     System.out.println("Email: " + email);
 }
}
