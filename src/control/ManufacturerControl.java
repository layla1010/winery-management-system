package control;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entity.Consts;
import entity.Manufacturer;


public class ManufacturerControl {
    private static ManufacturerControl _instance;

    private ManufacturerControl() {
    }

    public static ManufacturerControl getInstance() {
        if (_instance == null)
            _instance = new ManufacturerControl();
        return _instance;
    }

    /**
     * Fetches all manufacturers from the database.
     * @return ArrayList of Manufacturer objects.
     */
    public ArrayList<Manufacturer> getManufacturers() {
        ArrayList<Manufacturer> results = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_MANUFACTURERS);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                    results.add(new Manufacturer(
                        rs.getInt(i++),    
                        rs.getString(i++),  
                        rs.getString(i++), 
                        rs.getString(i++),  
                        rs.getString(i++)   
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Adds a new manufacturer to the database.
     * @param manufacturer The Manufacturer object to add.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean addManufacturer(Manufacturer manufacturer) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_MANUFACTURER)) {

                int i = 1;
                stmt.setString(i++, manufacturer.getName());
                stmt.setString(i++, manufacturer.getAddress());
                stmt.setString(i++, manufacturer.getPhoneNumber());
                stmt.setString(i++, manufacturer.getEmail());
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
     * Updates an existing manufacturer in the database.
     * @param manufacturer The Manufacturer object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateManufacturer(Manufacturer manufacturer) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_MANUFACTURER)) {

                int i = 1;
                stmt.setString(i++, manufacturer.getName());
                stmt.setString(i++, manufacturer.getAddress());
                stmt.setString(i++, manufacturer.getPhoneNumber());
                stmt.setString(i++, manufacturer.getEmail());
                stmt.setInt(i++, manufacturer.getManufacturerId());
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
     * Deletes a manufacturer from the database by ID.
     * @param manufacturerId The ID of the manufacturer to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteManufacturer(int manufacturerId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_MANUFACTURER)) {

                stmt.setInt(1, manufacturerId);
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
     * Fetches a manufacturer by its ID from the database.
     * @param manufacturerId The ID of the manufacturer to fetch.
     * @return Manufacturer object if found, null otherwise.
     */
    public Manufacturer getManufacturerById(int manufacturerId) {
        Manufacturer manufacturer = null;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_MANUFACTURER_BY_ID)) {

                stmt.setInt(1, manufacturerId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int i = 1;
                    manufacturer = new Manufacturer(
                        rs.getInt(i++),       
                        rs.getString(i++),     
                        rs.getString(i++),    
                        rs.getString(i++),    
                        rs.getString(i++)      
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return manufacturer;
    }
    

}
