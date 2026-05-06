package entity;

import java.util.Date;

import enums.CurrentStatus;

public class Order {

	private int order_ID;
	private Date order_Date;
	private CurrentStatus currentStatus;
	private Date shipment_Date;
	private Employee employee;
	
	
	public Order(int order_ID, Date order_Date, Date shipment_Date, Employee employee, CurrentStatus currentStatus) {
		super();
		this.order_ID = order_ID;
		this.order_Date = order_Date;
		this.currentStatus = currentStatus;
		this.shipment_Date = shipment_Date;
		this.employee = employee;
	}


	public int getOrder_ID() {
		return order_ID;
	}


	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}


	public Date getOrder_Date() {
		return order_Date;
	}


	public void setOrder_Date(Date order_Date) {
		this.order_Date = order_Date;
	}


	public CurrentStatus getCurrentStatus() {
		return currentStatus;
	}


	public void setCurrentStatus(CurrentStatus currentStatus) {
		this.currentStatus = currentStatus;
	}


	public Date getShipment_Date() {
		return shipment_Date;
	}


	public void setShipment_Date(Date shipment_Date) {
		this.shipment_Date = shipment_Date;
	}


	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee_ID(Employee employee) {
		this.employee = employee;
	}
	
	 public void displayInfo() {
	     System.out.println("ID: " + order_ID);
	     System.out.println("Order Date: " + order_Date);
	     System.out.println("Current Status: " + currentStatus);
	     System.out.println("Shipment Date: " + shipment_Date);
	     System.out.println("Employee: " + employee);
	 }
	
	
}
