package entity;

//Employee class extending Person
import java.util.Date;

public class Employee extends Person {
 private int employeeID;
 private String officeAddress;
 private Date employeeStartDate;

 // Constructor
 public Employee(int employeeID, String name, String phoneNumber, 
                 String officeAddress, String email, Date employeeStartDate) {
     super(name, phoneNumber, email);
     this.employeeID = employeeID;
     this.officeAddress = officeAddress;
     this.employeeStartDate = employeeStartDate;
 }

 // Getters and Setters
 public int getEmployeeID() {
     return employeeID;
 }

 public void setEmployeeID(int employeeID) {
     this.employeeID = employeeID;
 }

 public String getOfficeAddress() {
     return officeAddress;
 }

 public void setOfficeAddress(String officeAddress) {
     this.officeAddress = officeAddress;
 }

 public Date getEmployeeStartDate() {
     return employeeStartDate;
 }

 public void setEmployeeStartDate(Date employeeStartDate) {
     this.employeeStartDate = employeeStartDate;
 }

 // Override display method
 @Override
 public void displayInfo() {
     super.displayInfo();
     System.out.println("Employee ID: " + employeeID);
     System.out.println("Office Address: " + officeAddress);
     System.out.println("Employee Start Date: " + employeeStartDate);
 }
}
