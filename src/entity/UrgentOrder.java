package entity;

import java.util.Date;

import enums.CurrentStatus;

public class UrgentOrder extends Order{
	
	private Date expectedDeliveryDate;
	private Customer customer;
	private int priorityLevel;
	
	
	public UrgentOrder(int order_ID, Date order_Date, CurrentStatus currentStatus, Date shipment_Date,
			Employee employee_ID, Date expectedDeliveryDate, Customer customer, int priorityLevel) {
		super(order_ID, order_Date, shipment_Date, employee_ID, currentStatus);
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.customer = customer;
		this.priorityLevel = priorityLevel;
	}


	public Date getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}


	public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}


	public int getPriorityLevel() {
		return priorityLevel;
	}


	public void setPriorityLevel(int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
	@Override
	 public void displayInfo() {
	     super.displayInfo();
	     System.out.println("Expected Delivery Date: " + expectedDeliveryDate);
	     System.out.println("Customer: " + customer);
	     System.out.println("Priority Level: " + priorityLevel);
	 }


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	

}
