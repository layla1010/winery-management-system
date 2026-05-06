package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import entity.Consts;
import entity.Customer;
import entity.Employee;
import entity.Manufacturer;
import entity.Order;
import enums.CurrentStatus;

public class PersonControl {
    private static PersonControl _instance;

    private PersonControl() {
    }

    public static PersonControl getInstance() {
        if (_instance == null)
            _instance = new PersonControl();
        return _instance;
    }

    /**
     * Fetches all customers from the database.
     * @return ArrayList of Customer objects.
     */
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> results = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_CUSTOMERS);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                    results.add(new Customer(
                        rs.getInt(i++),    
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getDate(i++)    
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Fetches all employees from the database.
     * @return ArrayList of Employee objects.
     */
    public ArrayList<Employee> getEmployees() {
        ArrayList<Employee> results = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_EMPLOYEES);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                    results.add(new Employee(
                        rs.getInt(i++),    
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getString(i++), 
                        rs.getDate(i++)    
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Adds a new customer to the database.
     * @param customer The Customer object to add.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean addCustomer(Customer customer) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_CUSTOMER)) {

                int i = 1;
                stmt.setString(i++, customer.getName());
                stmt.setString(i++, customer.getPhoneNumber());
                stmt.setString(i++, customer.getDeliveryAddress());
                stmt.setString(i++, customer.getEmail());
                stmt.setDate(i++, new java.sql.Date(customer.getFirstContactDate().getTime()));

                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a new employee to the database.
     * @param employee The Employee object to add.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean addEmployee(Employee employee) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_EMPLOYEE)) {

                int i = 1;
                stmt.setString(i++, employee.getName());
                stmt.setString(i++, employee.getPhoneNumber());
                stmt.setString(i++, employee.getOfficeAddress());
                stmt.setString(i++, employee.getEmail());
                stmt.setDate(i++, new java.sql.Date(employee.getEmployeeStartDate().getTime()));

                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing customer in the database.
     * @param customer The Customer object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateCustomer(Customer customer) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall("UPDATE TblCustomer SET CustomerName = ?, PhoneNumber = ?, DeliveryAddress = ?, Email = ?, FirstContactDate = ? WHERE CustomerID = ?")) {

                int i = 1;
               
                stmt.setString(i++, customer.getName());
                stmt.setString(i++, customer.getPhoneNumber());
                stmt.setString(i++, customer.getDeliveryAddress());
                stmt.setString(i++, customer.getEmail());
                stmt.setDate(i++, new java.sql.Date(customer.getFirstContactDate().getTime()));
                stmt.setInt(i++, customer.getCustomerID());
                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing employee in the database.
     * @param employee The Employee object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateEmployee(Employee employee) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_EMPLOYEE)) {

                int i = 1;
                stmt.setString(i++, employee.getName());  // Name
                stmt.setString(i++, employee.getPhoneNumber()); // Phone
                stmt.setString(i++, employee.getOfficeAddress()); // Office
                stmt.setString(i++, employee.getEmail()); // Email
                
                // **Fix: Ensure Date is set before Employee ID**
                stmt.setDate(i++, new java.sql.Date(employee.getEmployeeStartDate().getTime())); 

                stmt.setInt(i++, employee.getEmployeeID()); // Employee ID should be last (WHERE condition)
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a customer from the database by ID.
     * @param customerID The ID of the customer to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteCustomer(int customerID) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall(Consts.SQL_DEL_CUSTOMER)) {

                stmt.setInt(1, customerID);
                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an employee from the database by ID.
     * @param employeeID The ID of the employee to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteEmployee(int employeeID) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement stmt = conn.prepareCall(Consts.SQL_DEL_EMPLOYEE)) {

                stmt.setInt(1, employeeID);
                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_EMPLOYEE_BY_ID)) {

                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int i = 1;
                    employee = new Employee(
                    		rs.getInt(i++),    
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getDate(i++)          
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return employee;
    }

	public Customer getCustomerById(int customerId) {
		Customer customer = null;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_CUSTOMER_BY_ID)) {

                stmt.setInt(1, customerId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int i = 1;
                    customer = new Customer(
                    		rs.getInt(i++),    
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getString(i++), 
                            rs.getDate(i++)          
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customer;
    }

	public Employee getEmployeeByEmail(String email) {
	    Employee employee = null;
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblEmployee WHERE Email = ?")) {
	             
	             stmt.setString(1, email);
	             ResultSet rs = stmt.executeQuery();
	             if (rs.next()){
	                int i = 1;
	                employee = new Employee(
	                    rs.getInt(i++),     // EmployeeID
	                    rs.getString(i++),  // Name
	                    rs.getString(i++),  // PhoneNumber
	                    rs.getString(i++),  // OfficeAddress
	                    rs.getString(i++),  // Email
	                    rs.getDate(i++)     // EmployeeStartDate
	                );
	             }
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return employee;
	}
	public ArrayList<Order> getOrdersByEmployee(int employeeId) {
	    ArrayList<Order> orders = new ArrayList<>();
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        String sql = "SELECT * FROM TblOrder WHERE EmployeeID = ?";
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, employeeId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    int i = 1;
	                    Order order = new Order(
	                        rs.getInt(i++),            // OrderID
	                        rs.getDate(i++),           // Order Date
	                        rs.getDate(i++),           // Shipment Date
	                        PersonControl.getInstance().getEmployeeById(rs.getInt(i++)),  // Employee
	                        CurrentStatus.valueOf(rs.getString(i++).replace("-", "_").toUpperCase())  // Current Status
	                    );
	                    orders.add(order);
	                }
	            }
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return orders;
	}


	}
    

