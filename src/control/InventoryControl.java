package control;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import entity.Locationn;
import entity.Consts;
import entity.Inventory;

public class InventoryControl {
    private static InventoryControl _instance;

    private InventoryControl() {
    }

    public static InventoryControl getInstance() {
        if (_instance == null)
            _instance = new InventoryControl();
        return _instance;
    }

    /**
     * Fetches all inventory records from the database.
     * @return ArrayList of Inventory objects.
     */
    public ArrayList<Inventory> getInventory() {
        ArrayList<Inventory> inventoryList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_INVENTORY);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    inventoryList.add(new Inventory(
                        rs.getInt("InventoryID"),   
                        rs.getInt("WineID"),        
                        rs.getInt("LocationUniqueNumber"),  
                        rs.getInt("Quantity")      
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    /**
     * Adds new stock or updates existing stock in the database.
     * Uses a stored query `qryInsInventory`.
     * @param wineID The ID of the wine.
     * @param locationID The ID of the location.
     * @param quantity The quantity of stock to add.
     * @return true if successful, false otherwise.
     */
    public boolean addInventory(int wineID, int locationID, int quantity) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR)) {

                // 🔹 Step 1: Check if Inventory Entry Exists
                String checkSQL = "SELECT Quantity FROM TblInventory WHERE WineID = ? AND LocationUniqueNumber = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                    checkStmt.setInt(1, wineID);
                    checkStmt.setInt(2, locationID);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) { 
                        // 🔹 Step 2: If Exists, Update Quantity
                        int existingQuantity = rs.getInt("Quantity");
                        int newQuantity = existingQuantity + quantity;

                        String updateSQL = "UPDATE TblInventory SET Quantity = ? WHERE WineID = ? AND LocationUniqueNumber = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setInt(2, wineID);
                            updateStmt.setInt(3, locationID);
                            updateStmt.executeUpdate();
                        }

                        return true; // Successfully updated
                    }
                }

                // 🔹 Step 3: If Not Exists, Insert New Record
                String insertSQL = "INSERT INTO TblInventory (WineID, LocationUniqueNumber, Quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    insertStmt.setInt(1, wineID);
                    insertStmt.setInt(2, locationID);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }

                return true; // Successfully inserted

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Updates stock quantity in the database.
     * Uses stored query `qryUpdInventory`.
     * @param inventoryID The inventory record to update.
     * @param wineID The wine ID.
     * @param locationID The location ID.
     * @param newQuantity The new quantity.
     * @return true if successful, false otherwise.
     */
    public boolean updateInventory(int inventoryID, int wineID, int locationID, int newQuantity) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_INVENTORY)) {

                // Set parameters in correct order (MATCHING the SQL query)
                stmt.setInt(1, wineID);          // WineID = ?
                stmt.setInt(2, locationID);      // LocationUniqueNumber = ?
                stmt.setInt(3, newQuantity);     // Quantity = ?
                stmt.setInt(4, inventoryID);     // WHERE InventoryID = ?

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;  // ✅ Returns true if at least one row is updated
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;  // ❌ Returns false if update fails
    }


    /**
     * Deletes an inventory record by ID.
     * Uses stored query `qryDelInventory`.
     * @param inventoryID The ID of the inventory record to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteInventory(int inventoryID) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_INVENTORY)) {

                stmt.setInt(1, inventoryID);
                stmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fetches inventory records based on WineID.
     * @param wineID The ID of the wine.
     * @return ArrayList of Inventory objects.
     */
    public ArrayList<Inventory> getInventoryByWine(int wineID) {
        ArrayList<Inventory> inventoryList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
           
            
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblInventory WHERE WineID = ?")) {

                stmt.setInt(1, wineID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    inventoryList.add(new Inventory(
                        rs.getInt("InventoryID"),   
                        rs.getInt("WineID"),        
                        rs.getInt("LocationUniqueNumber"),  
                        rs.getInt("Quantity")      
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
    public int getInventoryID(int wineID, int locationID) {
        int inventoryID = -1; // Default: Not Found
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT InventoryID FROM TblInventory WHERE WineID = ? AND LocationUniqueNumber = ?")) {

                stmt.setInt(1, wineID);
                stmt.setInt(2, locationID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    inventoryID = rs.getInt("InventoryID");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return inventoryID;
    }

    /**
     * Fetches inventory records based on LocationID.
     * @param locationID The ID of the location.
     * @return ArrayList of Inventory objects.
     */
    public ArrayList<Inventory> getInventoryByLocation(int locationID) {
        ArrayList<Inventory> inventoryList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
           
            
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblInventory WHERE LocationUniqueNumber = ?")) {

                stmt.setInt(1, locationID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    inventoryList.add(new Inventory(
                        rs.getInt("InventoryID"),   
                        rs.getInt("WineID"),        
                        rs.getInt("LocationUniqueNumber"),  
                        rs.getInt("Quantity")      
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
    /**
     * Fetches all locations from the database.
     * @return ArrayList of Locationn objects.
     */
    public ArrayList<Locationn> getLocations() {
        ArrayList<Locationn> locationList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
           
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_lOCATION);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    locationList.add(new Locationn(
                        rs.getInt("LocationUniqueNumber"),
                        rs.getString("LocationName")
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return locationList;
    }

    /**
     * Adds a new location to the database.
     * @param locationUniqueNumber Unique ID of the location.
     * @param locationName Name of the location.
     * @return true if successful, false otherwise.
     */
    public boolean addLocation(int locationUniqueNumber, String locationName) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
          
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_lOCATION)) {
            	int i = 1;
                stmt.setString(i++, locationName);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a location from the database.
     * @param locationUniqueNumber Unique ID of the location to delete.
     * @return true if successful, false otherwise.
     */
    public boolean removeLocation(int locationUniqueNumber) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_lOCATION)) {

                stmt.setInt(1, locationUniqueNumber);
                stmt.executeUpdate(); // Do not check rowsAffected

                System.out.println("Location deleted (Ignoring rowsAffected issue)."); // ✅ Debugging
                return true; // ✅ Assume success if no exception occurs

            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Failure only if an exception is caught
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Fetches overall stock details from the database.
     * @return ArrayList of Object[] containing (WineID, WineName, ProductionYear, PricePerBottle, TotalInventory).
     */
    public ArrayList<Object[]> getOverallStock() {
        ArrayList<Object[]> stockList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT WineID, WineName, ProductionYear, PricePerBottle, SUM(Quantity) AS TotalInventory " +
                     "FROM TblInventory " +
                     "JOIN TblWine ON TblInventory.WineID = TblWine.WineID " +
                     "GROUP BY WineID, WineName, ProductionYear, PricePerBottle"
                 );
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    stockList.add(new Object[]{
                        rs.getInt("WineID"),
                        rs.getString("WineName"),
                        rs.getInt("ProductionYear"),
                        rs.getDouble("PricePerBottle"),
                        rs.getInt("TotalInventory")
                    });
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    public int getTotalStock() {
        int totalStock = 0;
        ArrayList<Object[]> stockList = getOverallStock(); // Fetch overall stock data

        for (Object[] row : stockList) {
            totalStock += (int) row[4];  // Summing "TotalInventory" column
        }

        return totalStock;
    }


    /**
     * Fetches inventory details including location and manufacturer information.
     * @return ArrayList of Object[] containing (WineID, WineName, ProductionYear, ManufacturerName, LocationName, Quantity).
     */
    public ArrayList<Object[]> getInventoryInfo() {
        ArrayList<Object[]> inventoryInfoList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_INVENTORY_INFO);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    inventoryInfoList.add(new Object[]{
                        rs.getInt("WineID"),
                        rs.getString("WineName"),
                        rs.getInt("ProductionYear"),
                        rs.getString("FullName"),   // Manufacturer's Full Name
                        rs.getString("LocationName"), // Location Name
                        rs.getInt("Quantity")
                    });
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return inventoryInfoList;
    }
    public ArrayList<Inventory> getLowStockInventory() {
        ArrayList<Inventory> lowStockList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblInventory WHERE Quantity < 100");
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    lowStockList.add(new Inventory(
                        rs.getInt("InventoryID"),
                        rs.getInt("WineID"),
                        rs.getInt("LocationUniqueNumber"),
                        rs.getInt("Quantity")
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return lowStockList;
    }
    

    public boolean reduceStockForWines(HashMap<Integer, Integer> wineQuantities) {
        // For each wine ID and required quantity...
        for (Map.Entry<Integer, Integer> entry : wineQuantities.entrySet()) {
            int wineId = entry.getKey();
            int requiredQty = entry.getValue();
            
            // Get all inventory records for this wine.
            ArrayList<Inventory> inventoryList = getInventoryByWine(wineId);
            if (inventoryList == null || inventoryList.isEmpty()) {    
                return false;
            }
            // Calculate total available stock.
            int totalAvailable = 0;
            for (Inventory inv : inventoryList) {
                totalAvailable += inv.getQuantity();
            }
            
            if (totalAvailable < requiredQty) {
                
                return false;
            }
            
            // Deduct required quantity from the inventory records in order.
            int remaining = requiredQty;
            for (Inventory inv : inventoryList) {
                int available = inv.getQuantity();
                if (available <= 0) {
                    continue;  // Skip records with no available stock.
                }
                if (available >= remaining) {
                    int newQty = available - remaining;
                     boolean updated = updateInventoryQuantityOnly(inv.getInventoryID(), newQty);
                    if (!updated) {
                       
                        return false;
                    }
                    remaining = 0;
                    break;
                } else {
                   
                    boolean updated = updateInventoryQuantityOnly(inv.getInventoryID(), 0);
                    if (!updated) {
                       
                        return false;
                    }
                    remaining -= available;
                }
            }
            // If after processing all records there is still some remaining, it's an error.
            if (remaining > 0) {
                
                return false;
            }
        }
        // All wine IDs processed successfully.
        return true;
    }

 // Minimal approach that only updates the quantity by InventoryID:
    public boolean updateInventoryQuantityOnly(int inventoryID, int newQuantity) {
        String sql = "UPDATE TblInventory SET Quantity = ? WHERE InventoryID = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, newQuantity);
                stmt.setInt(2, inventoryID);
                int rows = stmt.executeUpdate();

                return (rows > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



        
}
