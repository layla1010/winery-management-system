package entity;

import java.util.Date;
import java.util.HashMap;

import enums.CurrentStatus;
import enums.Role;

	public class RegularOrder extends Order {
	    private HashMap<Customer, Role> customers; 

	    public RegularOrder(int order_ID, Date order_Date, CurrentStatus currentStatus, Date shipment_Date,
	                        Employee employee_ID, HashMap<Customer, Role> customers) {
	        super(order_ID, order_Date, shipment_Date, employee_ID, currentStatus);
	        this.customers = customers != null ? customers : new HashMap<>();
	    }

	    public HashMap<Customer, Role> getCustomers() {
	        return customers;
	    }
	    
	    public void setCustomers (HashMap<Customer, Role> customers) {
	    	this.customers=customers;
	    }

	    public void addCustomer(Customer customer, Role role) {
	        this.customers.put(customer, role);
	    }

	    public void removeCustomer(Customer customer) {
	        this.customers.remove(customer);
	    }

	    @Override
	    public void displayInfo() {
	        super.displayInfo();
	        System.out.println("Customers in Order:");
	        for (Customer c : customers.keySet()) {
	            System.out.println("  - " + c + " (Role: " + customers.get(c) + ")");
	        }
	    }
	}


