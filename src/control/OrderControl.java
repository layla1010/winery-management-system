package control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;

import entity.Consts;
import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.RegularOrder;
import entity.UrgentOrder;
import enums.CurrentStatus;
import enums.Role;

public class OrderControl {

	private static OrderControl _instance;

	private OrderControl() {
	}

	public static OrderControl getInstance() {
		if (_instance == null)
			_instance = new OrderControl();
		return _instance;
	}

	// ----------------------- ORDER RETRIEVAL METHODS ---------------------------

	// Returns all orders (basic order data)
	public ArrayList<Order> getOrders() {
		ArrayList<Order> results = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_ORDER);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new Order(
							rs.getInt(i++),
							rs.getDate(i++),
							rs.getDate(i++),
							PersonControl.getInstance().getEmployeeById(rs.getInt(i++)),
							CurrentStatus.valueOf(rs.getString(i++).replace("-", "_").toUpperCase())
							));
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	// Returns an order by its ID
	public Order getOrderByID(int orderID) {
		Order order = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_ORDER_BY_ID)) {
				stmt.setInt(1, orderID);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					int i = 1;
					order = new Order(
							rs.getInt(i++),
							rs.getDate(i++),
							rs.getDate(i++),
							PersonControl.getInstance().getEmployeeById(rs.getInt(i++)),
							CurrentStatus.valueOf(rs.getString(i++).replace("-", "_").toUpperCase())
							);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	// Returns all urgent orders (each urgent order has only one customer)
	public ArrayList<UrgentOrder> getUrgentOrders() {
		ArrayList<UrgentOrder> results = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_URGENT_ORDER);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					int orderId = rs.getInt(i++);
					Date expectedDeliveryDate = rs.getDate(i++);
					Customer customer = PersonControl.getInstance().getCustomerById(rs.getInt(i++));
					int priorityLevel = rs.getInt(i++);
					Order baseOrder = getOrderByID(orderId);
					if (baseOrder != null) {
						results.add(new UrgentOrder(
								orderId, 
								baseOrder.getOrder_Date(), 
								baseOrder.getCurrentStatus(),
								baseOrder.getShipment_Date(), 
								baseOrder.getEmployee(),
								expectedDeliveryDate, 
								customer,
								priorityLevel
								));
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	// Returns all regular (joint) orders. The SQL query should return rows containing OrderID, CustomerID, and Role.
	public ArrayList<RegularOrder> getRegularOrders() {
		ArrayList<RegularOrder> results = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_REGULAR_ORDER);
					ResultSet rs = stmt.executeQuery()) {
				HashMap<Integer, RegularOrder> orderMap = new HashMap<>();
				while (rs.next()) {
					int i = 1;
					int orderId = rs.getInt(i++);
					Customer customer = PersonControl.getInstance().getCustomerById(rs.getInt(i++));
					Role role = Role.valueOf(rs.getString(i++).replace("-", "_").toUpperCase());
					if (orderMap.containsKey(orderId)) {
						orderMap.get(orderId).addCustomer(customer, role);
					} else {
						Order baseOrder = getOrderByID(orderId);
						if (baseOrder != null) {
							HashMap<Customer, Role> customers = new HashMap<>();
							customers.put(customer, role);
							RegularOrder regularOrder = new RegularOrder(
									orderId, 
									baseOrder.getOrder_Date(), 
									baseOrder.getCurrentStatus(),
									baseOrder.getShipment_Date(), 
									baseOrder.getEmployee(), 
									customers
									);
							orderMap.put(orderId, regularOrder);
						}
					}
				}
				results.addAll(orderMap.values());
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	// ----------------------- ORDER DETAIL METHODS ---------------------------

	// Retrieves the regular order details: for a given regular order,
	// returns a list of Object arrays where each row contains:
	// { DetailID, RegularOrderID, CustomerID, WineID, Quantity }
	public ArrayList<Object[]> getRegularOrderDetails(int orderId) {
	    ArrayList<Object[]> details = new ArrayList<>();
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        String sql = "SELECT OrderID, OrderCustomersID, WineID, Quantity, Price " +
	                     "FROM TblRegularOrderDetails " +
	                     "WHERE OrderID = ?";
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, orderId);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                Object[] row = new Object[5];
	                row[0] = rs.getInt("OrderID");           // Order ID
	                row[1] = rs.getInt("OrderCustomersID");    // Customer ID (or detail identifier)
	                row[2] = rs.getInt("WineID");              // Wine ID
	                row[3] = rs.getInt("Quantity");            // Quantity
	                row[4] = rs.getDouble("Price");            // Price
	                details.add(row);
	            }
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return details;
	}

	public boolean updateRegularOrderDetail(int orderId, int customerId, int wineId, int newQuantity) {
		// Retrieve the price per bottle from TblWine
		double pricePerBottle = getPricePerBottle(wineId); // Calculate the new total price
		double newPrice = newQuantity * pricePerBottle; // Call the helper method to update the order detail record 
		return updateRegularOrderDetail(orderId, customerId, wineId, newQuantity, newPrice); }


	public double getPricePerBottle(int wineId) {
		double price = 0.0; String sql = "SELECT PricePerBottle FROM TblWine WHERE WineID = ?";
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); 
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(sql))
			{ 
				stmt.setInt(1, wineId); ResultSet rs = stmt.executeQuery();
				if (rs.next()) { price = rs.getDouble("PricePerBottle"); 
				} 
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return price; 
	}

	public boolean updateRegularOrderDetail(int orderId, int customerId, int wineId, int newQuantity, double newPrice) 
	{ String updateSql = "UPDATE TblRegularOrderDetails SET Quantity = ?, Price = ? " + "WHERE OrderID = ? AND OrderCustomersID = ? AND WineID = ?";
	try { Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
			PreparedStatement stmt = conn.prepareStatement(updateSql)) {
		stmt.setInt(1, newQuantity); stmt.setDouble(2, newPrice);
		stmt.setInt(3, orderId); stmt.setInt(4, customerId);
		stmt.setInt(5, wineId); 
		int rows = stmt.executeUpdate(); return rows > 0;
		}
	} catch (Exception e) {
		e.printStackTrace();
		} 
	return false; }



	// Inserts a new detail row into TblRegularOrderDetails.
	public boolean addRegularOrderDetail(int orderId, int customerId, int wineId, int quantity, double totalPrice) {
	    String sql = "INSERT INTO TblRegularOrderDetails (OrderID, OrderCustomersID, WineID, Quantity, Price) VALUES (?, ?, ?, ?, ?)";
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, orderId);
	            stmt.setInt(2, customerId);
	            stmt.setInt(3, wineId);
	            stmt.setInt(4, quantity);
	            stmt.setDouble(5, totalPrice);
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Inserts a new parent row into TblRegularOrder.
	// Note: Your table has columns RegularOrderID, CustomerID, and Role.
	public boolean addCustomerToRegularOrder(int orderId, int customerId, String role) {
	    String sql = "INSERT INTO TblRegularOrder (RegularOrderID, CustomerID, Role) VALUES (?, ?, ?)";
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, orderId);
	            stmt.setInt(2, customerId);
	            stmt.setString(3, role);  // e.g., "ADDITIONAL"
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	public boolean addCustomerToRegularOrder(int orderId, int customerId, int wineId, int quantity, double totalPrice) {
	    Connection conn = null;
	    PreparedStatement checkOrderStmt = null;
	    PreparedStatement checkDetailStmt = null;
	    PreparedStatement insertParentStmt = null;
	    PreparedStatement insertDetailStmt = null;
	    PreparedStatement updateDetailStmt = null;
	    boolean success = false;

	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        conn = DriverManager.getConnection(Consts.CONN_STR);
	        conn.setAutoCommit(false); 

	       
	        String checkOrderSql = "SELECT COUNT(*) FROM TblRegularOrder WHERE RegularOrderID = ? AND CustomerID = ?";
	        checkOrderStmt = conn.prepareStatement(checkOrderSql);
	        checkOrderStmt.setInt(1, orderId);
	        checkOrderStmt.setInt(2, customerId);
	        ResultSet rsOrder = checkOrderStmt.executeQuery();
	        boolean customerExists = false;
	        if (rsOrder.next() && rsOrder.getInt(1) > 0) {
	            customerExists = true;  
	        }
  if (!customerExists) {
	            String insertParentSql = "INSERT INTO TblRegularOrder (RegularOrderID, CustomerID, Role) VALUES (?, ?, 'ADDITIONAL')";
	            insertParentStmt = conn.prepareStatement(insertParentSql);
	            insertParentStmt.setInt(1, orderId);
	            insertParentStmt.setInt(2, customerId);
	            insertParentStmt.executeUpdate();
	        }

	      
	        String checkDetailSql = "SELECT Quantity FROM TblRegularOrderDetails WHERE OrderID = ? AND OrderCustomersID = ? AND WineID = ?";
	        checkDetailStmt = conn.prepareStatement(checkDetailSql);
	        checkDetailStmt.setInt(1, orderId);
	        checkDetailStmt.setInt(2, customerId);
	        checkDetailStmt.setInt(3, wineId);
	        ResultSet rsDetail = checkDetailStmt.executeQuery();

	        if (rsDetail.next()) {
	         
	            int existingQuantity = rsDetail.getInt(1);
	            int newQuantity = existingQuantity + quantity;
	            double newTotalPrice = newQuantity * (totalPrice / quantity); 

	            String updateDetailSql = "UPDATE TblRegularOrderDetails SET Quantity = ?, Price = ? WHERE OrderID = ? AND OrderCustomersID = ? AND WineID = ?";
	            updateDetailStmt = conn.prepareStatement(updateDetailSql);
	            updateDetailStmt.setInt(1, newQuantity);
	            updateDetailStmt.setDouble(2, newTotalPrice);
	            updateDetailStmt.setInt(3, orderId);
	            updateDetailStmt.setInt(4, customerId);
	            updateDetailStmt.setInt(5, wineId);
	            updateDetailStmt.executeUpdate();
	        } else {
	           
	            String insertDetailSql = "INSERT INTO TblRegularOrderDetails (OrderID, OrderCustomersID, WineID, Quantity, Price) VALUES (?, ?, ?, ?, ?)";
	            insertDetailStmt = conn.prepareStatement(insertDetailSql);
	            insertDetailStmt.setInt(1, orderId);
	            insertDetailStmt.setInt(2, customerId);
	            insertDetailStmt.setInt(3, wineId);
	            insertDetailStmt.setInt(4, quantity);
	            insertDetailStmt.setDouble(5, totalPrice);
	            insertDetailStmt.executeUpdate();
	        }

	        conn.commit(); 
	        success = true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) {
	                conn.rollback();  
	            }
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	    } finally {
	        // 🔻 Close resources safely
	        try {
	            if (checkOrderStmt != null) checkOrderStmt.close();
	            if (checkDetailStmt != null) checkDetailStmt.close();
	            if (insertParentStmt != null) insertParentStmt.close();
	            if (insertDetailStmt != null) insertDetailStmt.close();
	            if (updateDetailStmt != null) updateDetailStmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return success;
	}

	public boolean deleteRegularOrderDetail(int orderId, int customerId, int wineId) {
		String sql = "DELETE FROM TblRegularOrderDetails WHERE OrderID = ? AND OrderCustomersID = ? AND WineID = ?";
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, orderId);
				stmt.setInt(2, customerId);
				stmt.setInt(3, wineId);
				stmt.executeUpdate();
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ----------------------- CUSTOMER REMOVAL METHODS ---------------------------

	// Removes a customer from a joint (regular) order.
	// If the removed customer is the main customer, then either reassign a new main (if available)
	// or delete the entire order if no alternative exists.
	// Removes a customer from a joint (regular) order.
	// If the removed customer is the main customer, then either reassign a new main (if available)
	// or delete the entire order if no alternative exists.
	public boolean removeCustomerFromRegularOrder(int orderId, int customerId) {
	    // Retrieve the regular order by filtering the list.
	    RegularOrder regOrder = getRegularOrders().stream()
	            .filter(o -> o.getOrder_ID() == orderId)
	            .findFirst().orElse(null);
	    if (regOrder == null) {
	        System.out.println("Order not found.");
	        return false;
	    }

	    // Get the target customer object.
	    Customer targetCustomer = PersonControl.getInstance().getCustomerById(customerId);
	    // Use the regOrder's customer mapping to check role.
	    Role roleToRemove = regOrder.getCustomers().get(targetCustomer);
	    if (roleToRemove == null) {
	        System.out.println("Customer not in order.");
	        return false;
	    }

	    if (roleToRemove == Role.MAIN) {
	        // If the target customer is the MAIN customer,
	        // try to promote an additional customer to MAIN.
	        Customer newMain = regOrder.getCustomers().entrySet().stream()
	                .filter(e -> e.getKey().getCustomerID() != customerId && e.getValue() == Role.ADDITIONAL)
	                .map(e -> e.getKey())
	                .findFirst().orElse(null);
	        if (newMain != null) {
	            // Promote the additional customer to MAIN.
	            try {
	                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	                String sql = "UPDATE TblRegularOrder SET Role = 'MAIN' WHERE RegularOrderID = ? AND CustomerID = ?";
	                try (PreparedStatement stmt = DriverManager.getConnection(Consts.CONN_STR)
	                        .prepareStatement(sql)) {
	                    stmt.setInt(1, orderId);
	                    stmt.setInt(2, newMain.getCustomerID());
	                    stmt.executeUpdate();
	                }
	            } catch (ClassNotFoundException | SQLException e) {
	                e.printStackTrace();
	                return false;
	            }
	            // Remove all order detail records for the target customer.
	            boolean detailsRemoved = removeCustomerDetails(orderId, customerId);
	            // Also remove the customer mapping row from TblRegularOrder.
	            boolean mappingRemoved = deleteCustomerFromRegularOrderMapping(orderId, customerId);
	            return detailsRemoved && mappingRemoved;
	        } else {
	            // No alternative customer available, so delete the entire order.
	            return deleteOrder(orderId);
	        }
	    } else {
	        // If the target customer is not the MAIN, just remove their detail records.
	        boolean detailsRemoved = removeCustomerDetails(orderId, customerId);
	        boolean mappingRemoved = deleteCustomerFromRegularOrderMapping(orderId, customerId);
	        return detailsRemoved && mappingRemoved;
	    }
	}

	// Helper method to remove all detail records for a given customer from an order.
	private boolean removeCustomerDetails(int orderId, int customerId) {
	    ArrayList<Object[]> details = getRegularOrderDetails(orderId);
	    boolean success = true;
	    // The getRegularOrderDetails method returns:
	    // row[0]: OrderID, row[1]: OrderCustomersID, row[2]: WineID, row[3]: Quantity, row[4]: Price
	    for (Object[] row : details) {
	        int custId = (int) row[1];  // CustomerID is at index 1
	        int wineId = (int) row[2];  // WineID is at index 2
	        if (custId == customerId) {
	            success &= deleteRegularOrderDetail(orderId, customerId, wineId);
	        }
	    }
	    return success;
	}

	// New helper method to remove the customer mapping row from TblRegularOrder.
	public boolean deleteCustomerFromRegularOrderMapping(int orderId, int customerId) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        String sql = "DELETE FROM TblRegularOrder WHERE RegularOrderID = ? AND CustomerID = ?";
	        try (PreparedStatement stmt = DriverManager.getConnection(Consts.CONN_STR)
	                .prepareStatement(sql)) {
	            stmt.setInt(1, orderId);
	            stmt.setInt(2, customerId);
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	// Delete a single order by ID (for both urgent and regular orders)
	public boolean deleteOrder(int orderId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_ORDER)) {
				stmt.setInt(1, orderId);
				stmt.executeUpdate();
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	// ----------------------- DELETE ALL ORDERS ---------------------------

	// Deletes all orders and the dependent records in regular and urgent order tables
	public boolean deleteAllOrders() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					Statement stmt = conn.createStatement()) {
				// Delete from the dependent tables first (if no cascading)
				stmt.executeUpdate("DELETE FROM TblRegularOrderDetails");
				stmt.executeUpdate("DELETE FROM TblRegularOrder");
				stmt.executeUpdate("DELETE FROM TblUrgentOrder");
				stmt.executeUpdate("DELETE FROM TblOrder");
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ----------------------- INVENTORY UPDATES ---------------------------

	// Updates the order (using stored procedure) and then adjusts inventory based on the order status.
	// For example, when an order is submitted (DISPATCHED/IN_PROCESS/DELIVERED) we reduce stock,
	// and when an order is canceled, we add the quantities back.
	public boolean updateOrder(Order order) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_ORDER)) {
	            int i = 1;
	            stmt.setDate(i++, new java.sql.Date(order.getOrder_Date().getTime()));
	            stmt.setDate(i++, new java.sql.Date(order.getShipment_Date().getTime())); 
	            stmt.setInt(i++, order.getEmployee().getEmployeeID());
	            stmt.setString(i++, order.getCurrentStatus().name().replace("_", "-").toLowerCase());
	            stmt.setInt(i++, order.getOrder_ID());
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean updateOrderStatus(int orderId, CurrentStatus newStatus) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(
	                 "UPDATE TblOrder SET CurrentStatus = ? WHERE OrderID = ?")) {
	             
	     
	            stmt.setString(1, newStatus.name().replace("_", "-").toLowerCase());
	            stmt.setInt(2, orderId);
	            int rows = stmt.executeUpdate();
	            
	            if (rows > 0) {
	                // Retrieve the updated order so we can update inventory accordingly.
	                Order order = getOrderByID(orderId);
	                if (order != null) {
	                    // Ensure the order object now has the new status.
	                    order.setCurrentStatus(newStatus);
	                    
	                }
	                return true;
	            }
	            return false;
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public HashMap<Integer, Integer> aggregateWineQuantities(int orderId) {
	    HashMap<Integer, Integer> wineQuantities = new HashMap<>();
	    ArrayList<Object[]> details = getRegularOrderDetails(orderId);
	    // Assuming the details array is structured as:
	    // index 0: OrderID, index 1: OrderCustomersID, index 2: WineID, index 3: Quantity, index 4: Price
	    for (Object[] row : details) {
	        int wineId = (int) row[2];
	        int quantity = (int) row[3];
	        wineQuantities.put(wineId, wineQuantities.getOrDefault(wineId, 0) + quantity);
	    }
	    return wineQuantities;
	}
	public HashMap<Integer, Integer> aggregateWineQuantitiesUrgent(int orderId) {
	    HashMap<Integer, Integer> wineQuantities = new HashMap<>();
	    ArrayList<Object[]> details = getUrgentOrderDetails(orderId);
	    // Assuming the details array is structured as:
	    // index 0: OrderID, index 1: OrderCustomersID, index 2: WineID, index 3: Quantity, index 4: Price
	    for (Object[] row : details) {
	        int wineId = (int) row[2];
	        int quantity = (int) row[3];
	        wineQuantities.put(wineId, wineQuantities.getOrDefault(wineId, 0) + quantity);
	    }
	    return wineQuantities;
	}
	public boolean updateOrderStatusWithInventoryUpdate(int orderId, CurrentStatus newStatus) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(
	                 "UPDATE TblOrder SET CurrentStatus = ? WHERE OrderID = ?")) {
	            
	            String statusString = newStatus.name().replace("_", "-").toLowerCase();
	            stmt.setString(1, statusString);
	            stmt.setInt(2, orderId);
	            int rows = stmt.executeUpdate();
	            
	            if (rows > 0) {
	                // Only update inventory for statuses that need a stock reduction.
	                if (
	                    newStatus == CurrentStatus.IN_PROCESS ) 
	                {
	                    // Aggregate the required wine quantities from the order details.
	                    HashMap<Integer, Integer> wineQuantities = aggregateWineQuantities(orderId);
	                    boolean inventoryUpdated = InventoryControl.getInstance().reduceStockForWines(wineQuantities);
	                    
	                    if (!inventoryUpdated) {
	                    	
	                        return false;
	                    }
	                }
	                return true;
	            }
	            return false;
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean updateOrderStatusWithInventoryUpdateUrgent(int orderId, CurrentStatus newStatus) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(
	                 "UPDATE TblOrder SET CurrentStatus = ? WHERE OrderID = ?")) {
	            
	            String statusString = newStatus.name().replace("_", "-").toLowerCase();
	            stmt.setString(1, statusString);
	            stmt.setInt(2, orderId);
	            int rows = stmt.executeUpdate();
	            
	            if (rows > 0) {
	                // Only update inventory for statuses that need a stock reduction.
	            	 if (
	 	                    newStatus == CurrentStatus.IN_PROCESS ) 
	 	                {
	 	                 
	                    HashMap<Integer, Integer> wineQuantities = aggregateWineQuantitiesUrgent(orderId);
	                    boolean inventoryUpdated = InventoryControl.getInstance().reduceStockForWines(wineQuantities);
	                    
	                    if (!inventoryUpdated) {
	                        System.err.println("Inventory update failed for order " + orderId);
	                        // Optionally, you could revert the status update here.
	                        return false;
	                    }
	                }
	                return true;
	            }
	            return false;
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	
	// ----------------------- ADDING NEW ORDERS ---------------------------

	// Adds a new regular (joint) order.
	// Inserts into TblOrder first and then into TblRegularOrder (or TblRegularOrderCustomer) for the customer-role mapping.
	public boolean addRegularOrder(RegularOrder regularOrder) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			int orderId = -1;
			try (PreparedStatement stmtOrder = DriverManager.getConnection(Consts.CONN_STR)
					.prepareStatement(Consts.SQL_INS_ORDER, Statement.RETURN_GENERATED_KEYS)) {
				int i = 1;
				stmtOrder.setDate(i++, new java.sql.Date(regularOrder.getOrder_Date().getTime()));
				stmtOrder.setDate(i++, new java.sql.Date(regularOrder.getShipment_Date().getTime()));
				stmtOrder.setInt(i++, regularOrder.getEmployee().getEmployeeID());
				stmtOrder.setString(i++, regularOrder.getCurrentStatus().toString());
				stmtOrder.executeUpdate();
				ResultSet rs = stmtOrder.getGeneratedKeys();
				if (rs.next()) {
					orderId = rs.getInt(1);
				}
			}
			if (orderId == -1) {
				throw new SQLException("Failed to retrieve Order ID");
			}
			// Insert each customer-role mapping into TblRegularOrder (or using the SQL_INS_REGULAR_ORDER_CUSTOMER query)
			try (PreparedStatement stmtRegOrder = DriverManager.getConnection(Consts.CONN_STR)
					.prepareStatement(Consts.SQL_INS_REGULAR_ORDER_CUSTOMER)) {
				for (Customer customer : regularOrder.getCustomers().keySet()) {
					int i = 1;
					stmtRegOrder.setInt(i++, orderId);
					stmtRegOrder.setInt(i++, customer.getCustomerID());
					stmtRegOrder.setString(i++, regularOrder.getCustomers().get(customer).toString());
					stmtRegOrder.addBatch();
				}
				stmtRegOrder.executeBatch();
			}
			// Optionally, if you have details for wine purchases per customer, insert them here using SQL_INS_REGULAR_ORDER_DETAILS.
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// A basic addOrder method for orders that do not require extra details.
	public boolean addOrder(Order order) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (CallableStatement stmt = DriverManager.getConnection(Consts.CONN_STR)
					.prepareCall(Consts.SQL_INS_ORDER)) {
				int i = 1;
				stmt.setDate(i++, new java.sql.Date(order.getOrder_Date().getTime()));
				stmt.setDate(i++, new java.sql.Date(order.getShipment_Date().getTime()));
				stmt.setInt(i++, order.getEmployee().getEmployeeID());
				stmt.setString(i++, order.getCurrentStatus().name().replace("_", "-").toLowerCase());
				stmt.executeUpdate();
				
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public int getNextOrderIdPreview() {
		int nextId = 1;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement("SELECT MAX(OrderID) FROM TblOrder");
					ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					nextId = rs.getInt(1) + 1;
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return nextId;
	}

	public boolean addUrgentOrderDetail(int orderId, int wineId, int quantity, double totalPrice) {
		String sql = "INSERT INTO TblUrgentOrderDetails ([OrderID], [WineID], [Quantity], [Price]) VALUES (?,?,?,?)";        try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, orderId);
				stmt.setInt(2, wineId);
				stmt.setInt(3, quantity);
				stmt.setDouble(4, totalPrice);
				stmt.executeUpdate();
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Adds a new urgent order.
	
	public boolean updateUrgentOrder(UrgentOrder urgentOrder) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR)) {
	            conn.setAutoCommit(false);  // Start transaction

	            // Update base order fields in TblOrder
	            String sqlOrder = "UPDATE TblOrder SET OrderDate = ?, ShipmentDate = ?, EmployeeID = ?, CurrentStatus = ? WHERE OrderID = ?";
	            try (PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder)) {
	                stmtOrder.setDate(1, new java.sql.Date(urgentOrder.getOrder_Date().getTime()));
	                stmtOrder.setDate(2, new java.sql.Date(urgentOrder.getShipment_Date().getTime()));
	                stmtOrder.setInt(3, urgentOrder.getEmployee().getEmployeeID());
	                stmtOrder.setString(4, urgentOrder.getCurrentStatus().name().replace("_", "-").toLowerCase());
	                stmtOrder.setInt(5, urgentOrder.getOrder_ID());
	                stmtOrder.executeUpdate();
	            }

	            // Update urgent-specific fields in TblUrgentOrder
	            String sqlUrgent = "UPDATE TblUrgentOrder SET ExpectedDeliveryTime = ?, Customer = ?, PriorityLevel = ? WHERE OrderID = ?";
	            try (PreparedStatement stmtUrg = conn.prepareStatement(sqlUrgent)) {
	                stmtUrg.setDate(1, new java.sql.Date(urgentOrder.getExpectedDeliveryDate().getTime()));
	                stmtUrg.setInt(2, urgentOrder.getCustomer().getCustomerID());
	                stmtUrg.setInt(3, urgentOrder.getPriorityLevel());
	                stmtUrg.setInt(4, urgentOrder.getOrder_ID());
	                stmtUrg.executeUpdate();
	            }

	            conn.commit();
	            return true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public int addUrgentOrder(UrgentOrder urgentOrder) {
	    int orderId = -1; // Default value for Order ID

	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR)) {
	            conn.setAutoCommit(false); // Start transaction

	            // Step 1: Insert into TblOrder
	            try (PreparedStatement stmtOrder = conn.prepareStatement(
	                    Consts.SQL_INS_ORDER, Statement.RETURN_GENERATED_KEYS)) {
	                int i = 1;
	                stmtOrder.setDate(i++, new java.sql.Date(urgentOrder.getOrder_Date().getTime()));
	                stmtOrder.setDate(i++, new java.sql.Date(urgentOrder.getShipment_Date().getTime()));
	                stmtOrder.setInt(i++, urgentOrder.getEmployee().getEmployeeID());
	                stmtOrder.setString(i++, urgentOrder.getCurrentStatus().toString());
	                stmtOrder.executeUpdate();

	                // Retrieve generated OrderID and update urgentOrder object.
	                try (ResultSet rs = stmtOrder.getGeneratedKeys()) {
	                    if (rs.next()) {
	                        orderId = rs.getInt(1);
	                        urgentOrder.setOrder_ID(orderId);
	                    }
	                }
	            }

	            if (orderId == -1) {
	                conn.rollback();
	                throw new SQLException("Failed to retrieve generated Order ID.");
	            }

	            // Step 2: Insert into TblUrgentOrder using the generated orderId
	            try (PreparedStatement stmtUrgOrder = conn.prepareStatement(Consts.SQL_INS_URGENT_ORDER)) {
	                int i = 1;
	                stmtUrgOrder.setInt(i++, orderId);  
	                stmtUrgOrder.setDate(i++, new java.sql.Date(urgentOrder.getExpectedDeliveryDate().getTime()));
	                stmtUrgOrder.setInt(i++, urgentOrder.getCustomer().getCustomerID());
	                stmtUrgOrder.setInt(i++, urgentOrder.getPriorityLevel());
	                stmtUrgOrder.executeUpdate();
	            }

	            conn.commit();
	            return orderId;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return -1;
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}


	public UrgentOrder getUrgentOrderByID(int orderID) {
	    UrgentOrder order = null;
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR)) {
	            // First: Retrieve the base order data from TblOrder
	            try (PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_ORDER_BY_ID)) {
	                stmt.setInt(1, orderID);
	                try (ResultSet rs = stmt.executeQuery()) {
	                    if (rs.next()) {
	                        int retrievedOrderId = rs.getInt(1);
	                        Date orderDate = rs.getDate(2);
	                        Date shipmentDate = rs.getDate(3);
	                        int employeeId = rs.getInt(4);
	                        CurrentStatus status = CurrentStatus.valueOf(rs.getString(5).replace("-", "_").toUpperCase());
	                        Employee employee = PersonControl.getInstance().getEmployeeById(employeeId);

	                        // Now: Retrieve the urgent order details from TblUrgentOrder
	                        String sqlUrgentQuery = "SELECT ExpectedDeliveryTime, Customer, PriorityLevel FROM TblUrgentOrder WHERE OrderID = ?";
	                        try (PreparedStatement stmtUrg = conn.prepareStatement(sqlUrgentQuery)) {
	                            stmtUrg.setInt(1, orderID);
	                            try (ResultSet rsUrg = stmtUrg.executeQuery()) {
	                                if (rsUrg.next()) {
	                                    Date expectedDeliveryDate = rsUrg.getDate("ExpectedDeliveryTime");
	                                    int customerId = rsUrg.getInt("Customer");
	                                    int priorityLevel = rsUrg.getInt("PriorityLevel");
	                                    Customer customer = PersonControl.getInstance().getCustomerById(customerId);

	                                    // Create the UrgentOrder object with all retrieved data
	                                    order = new UrgentOrder(retrievedOrderId, orderDate, status, shipmentDate, employee,
	                                            expectedDeliveryDate, customer, priorityLevel);
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return order;
	}


	public RegularOrder getRegularOrderByID(int orderID) {
		RegularOrder order = null;

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_ORDER_BY_ID)) {

				stmt.setInt(1, orderID);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					int i = 1;
					int retrievedOrderId = rs.getInt(i++);
					Date orderDate = rs.getDate(i++);
					Date shipmentDate = rs.getDate(i++);
					int employeeId = rs.getInt(i++);
					CurrentStatus status = CurrentStatus.valueOf(rs.getString(i++).replace("-", "_").toUpperCase());

					// Retrieve employee object
					Employee employee = PersonControl.getInstance().getEmployeeById(employeeId);

					// Create a HashMap to store customers and their roles
					HashMap<Customer, Role> customers = new HashMap<>();

					// Retrieve customers for this order
					String sqlCustomerQuery = "SELECT CustomerID, Role FROM TblRegularOrder WHERE RegularOrderID = ?";
					try (PreparedStatement stmtCustomers = conn.prepareStatement(sqlCustomerQuery)) {
						stmtCustomers.setInt(1, orderID);
						ResultSet rsCustomers = stmtCustomers.executeQuery();

						while (rsCustomers.next()) {
							int customerId = rsCustomers.getInt(1);
							String roleString = rsCustomers.getString(2);
							Role role = Role.valueOf(roleString.replace("-", "_").toUpperCase());

							Customer customer = PersonControl.getInstance().getCustomerById(customerId);
							if (customer != null) {
								customers.put(customer, role);
							}
						}
					}

					// Construct the RegularOrder object with retrieved data
					order = new RegularOrder(retrievedOrderId, orderDate, status, shipmentDate, employee, customers);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return order;
	}

	

	public int insertRegularOrder(RegularOrder regularOrder) {
		int orderId = -1;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (PreparedStatement stmtOrder = DriverManager.getConnection(Consts.CONN_STR)
					.prepareStatement(Consts.SQL_INS_ORDER, Statement.RETURN_GENERATED_KEYS)) {
				int i = 1;
				stmtOrder.setDate(i++, new java.sql.Date(regularOrder.getOrder_Date().getTime()));
				stmtOrder.setDate(i++, new java.sql.Date(regularOrder.getShipment_Date().getTime()));
				stmtOrder.setInt(i++, regularOrder.getEmployee().getEmployeeID());
				stmtOrder.setString(i++, regularOrder.getCurrentStatus().toString());
				stmtOrder.executeUpdate();
				ResultSet rs = stmtOrder.getGeneratedKeys();
				if (rs.next()) {
					orderId = rs.getInt(1);
				}
			}
			if (orderId != -1) {
				// Insert the customer-role mapping into TblRegularOrder (or via your stored query)
				try (PreparedStatement stmtRegOrder = DriverManager.getConnection(Consts.CONN_STR)
						.prepareStatement(Consts.SQL_INS_REGULAR_ORDER_CUSTOMER)) {
					for (Customer customer : regularOrder.getCustomers().keySet()) {
						int i = 1;
						stmtRegOrder.setInt(i++, orderId);
						stmtRegOrder.setInt(i++, customer.getCustomerID());
						stmtRegOrder.setString(i++, regularOrder.getCustomers().get(customer).toString());
						stmtRegOrder.addBatch();
					}
					stmtRegOrder.executeBatch();
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return orderId;
	}
	public boolean updateMainCustomer(int orderId, int newMainCustomerId) {
	    // Update the main customer for a given RegularOrder.
	    // Assumes that the row with Role 'MAIN' already exists and has RegularOrderID as primary key.
	    String sql = "UPDATE TblRegularOrder SET CustomerID = ? WHERE RegularOrderID = ? AND Role = 'MAIN'";
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, newMainCustomerId);
	            stmt.setInt(2, orderId);
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	public boolean swapMainCustomer(int orderId, int newMainCustomerId) {
	    // Retrieve the existing regular order first.
	    RegularOrder regOrder = getRegularOrderByID(orderId);
	    if (regOrder == null) return false;
	    
	    int currentMainCustomerId = -1;
	    for (Customer c : regOrder.getCustomers().keySet()) {
	        if (regOrder.getCustomers().get(c) == Role.MAIN) {
	            currentMainCustomerId = c.getCustomerID();
	            break;
	        }
	    }
	    if (currentMainCustomerId == -1) {
	        // No main customer exists, so nothing to swap.
	        return false;
	    }
	    // If the new main is already the current main, no swap is needed.
	    if (currentMainCustomerId == newMainCustomerId) return true;
	    
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR)) {
	            conn.setAutoCommit(false);
	            // Update the row that currently has the new main customer's ID to be MAIN.
	            // This assumes that a row with that customer already exists in this order as an additional.
	            String sqlSetNewMain = "UPDATE TblRegularOrder SET Role = 'MAIN' WHERE RegularOrderID = ? AND CustomerID = ?";
	            try (PreparedStatement stmtNew = conn.prepareStatement(sqlSetNewMain)) {
	                stmtNew.setInt(1, orderId);
	                stmtNew.setInt(2, newMainCustomerId);
	                int rowsNew = stmtNew.executeUpdate();
	                if (rowsNew == 0) {
	                    // If the new main customer is not yet mapped in this order,
	                    // you may choose to insert a new row.
	                    // For example:
	                    String sqlInsert = "INSERT INTO TblRegularOrder (RegularOrderID, CustomerID, Role) VALUES (?, ?, 'MAIN')";
	                    try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
	                        stmtInsert.setInt(1, orderId);
	                        stmtInsert.setInt(2, newMainCustomerId);
	                        stmtInsert.executeUpdate();
	                    }
	                }
	            }
	            // Update the row that currently is MAIN to become ADDITIONAL.
	            String sqlSetOldMain = "UPDATE TblRegularOrder SET Role = 'ADDITIONAL' WHERE RegularOrderID = ? AND CustomerID = ?";
	            try (PreparedStatement stmtOld = conn.prepareStatement(sqlSetOldMain)) {
	                stmtOld.setInt(1, orderId);
	                stmtOld.setInt(2, currentMainCustomerId);
	                stmtOld.executeUpdate();
	            }
	            conn.commit();
	            return true;
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}
	// In OrderControl.java

	// Updates the detail record for an urgent order when quantity is changed.
	// It recalculates the total price based on the new quantity.
	public boolean updateUrgentOrderDetail(int orderId, int wineId, int newQuantity) {
	    // Retrieve the price per bottle using the existing helper method
	    double pricePerBottle = getPricePerBottle(wineId);
	    double newPrice = newQuantity * pricePerBottle;
	    
	    String sql = "UPDATE TblUrgentOrderDetails SET Quantity = ?, Price = ? WHERE OrderID = ? AND WineID = ?";
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, newQuantity);
	            stmt.setDouble(2, newPrice);
	            stmt.setInt(3, orderId);
	            stmt.setInt(4, wineId);
	            int rowsUpdated = stmt.executeUpdate();
	            return rowsUpdated > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	

	// Deletes a detail record for an urgent order from TblUrgentOrderDetails.
	public boolean deleteUrgentOrderDetail(int orderId, int wineId) {
	    String sql = "DELETE FROM TblUrgentOrderDetails WHERE OrderID = ? AND WineID = ?";
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, orderId);
	            stmt.setInt(2, wineId);
	            int rowsDeleted = stmt.executeUpdate();
	            return rowsDeleted > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	// Retrieves urgent order details from TblUrgentOrderDetails.
	// Assumes the table has columns: OrderID, WineID, Quantity, Price.
	public ArrayList<Object[]> getUrgentOrderDetails(int orderId) {
	    ArrayList<Object[]> details = new ArrayList<>();
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement("SELECT OrderID, WineID, Quantity, Price FROM TblUrgentOrderDetails WHERE OrderID = ?")) {
	            stmt.setInt(1, orderId);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                Object[] row = new Object[5];
	                row[0] = rs.getInt("OrderID");
	                row[1] = 0; // Placeholder for CustomerID; UI will fill this from UrgentOrder.getCustomer()
	                row[2] = rs.getInt("WineID");
	                row[3] = rs.getInt("Quantity");
	                row[4] = rs.getDouble("Price");
	                details.add(row);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return details;
	}

	



}
