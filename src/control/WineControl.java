package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import entity.Consts;
import entity.Wine;
import enums.SweetnessLevel;

public class WineControl {
    private static WineControl _instance;

    private WineControl() {
    }

    public static WineControl getInstance() {
        if (_instance == null)
            _instance = new WineControl();
        return _instance;
    }

    /**
     * Fetches all wines from the database.
     * @return ArrayList of Wine objects.
     */
    public ArrayList<Wine> getWines() {
        ArrayList<Wine> results = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINES);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                   
                   results.add(new Wine(
                           rs.getInt(i++),        
                           rs.getString(i++),     
                           rs.getInt(i++),        
                           rs.getString(i++),    
                           rs.getDouble(i++),     
                           SweetnessLevel.valueOf(rs.getString(i++).replace("-", "_").toUpperCase()), 
                           rs.getInt(i++),        
                           ManufacturerControl.getInstance().getManufacturerById(rs.getInt(i++)), 
                           rs.getString(i++),
                           WineTypeControl.getInstance().getWineTypeById(rs.getInt(i++)))) ;
                    
                    
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
     * Adds a new wine to the database.
     * @param wine The Wine object to add.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean addWine(Wine wine) {
    	if (!isWineUnique(wine.getCatalogNumber(), wine.getManufacturer().getManufacturerId(),wine.getWineId())) {
            JOptionPane.showMessageDialog(null, "A wine with the same catalog number and manufacturer already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
    	}
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_WINE)) {

                int i = 1;
                stmt.setString(i++, wine.getName());
                stmt.setInt(i++, wine.getProductionYear());
                stmt.setString(i++, wine.getDescription());
                stmt.setDouble(i++, wine.getPricePerBottle());
                stmt.setString(i++, wine.getSweetnessLevel().name().replace("_", "-").toLowerCase());
                stmt.setInt(i++, wine.getCatalogNumber());
                stmt.setInt(i++, wine.getManufacturer().getManufacturerId());
                stmt.setString(i++, wine.getPhotoPath());
                stmt.setInt(i++, wine.getWineType().getWineTypeID());
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
     * Updates an existing wine in the database.
     * @param wine The Wine object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateWine(Wine wine) {
    	 if (!isWineUnique(wine.getCatalogNumber(), wine.getManufacturer().getManufacturerId(),wine.getWineId())) {
    	        JOptionPane.showMessageDialog(null, "A wine with the same catalog number and manufacturer already exists.", "Error", JOptionPane.ERROR_MESSAGE);
    	        return false;
    	    }
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_WINE)) {

                int i = 1;
                stmt.setString(i++, wine.getName());
                stmt.setInt(i++, wine.getProductionYear());
                stmt.setString(i++, wine.getDescription());
                stmt.setDouble(i++, wine.getPricePerBottle());
                stmt.setString(i++, wine.getSweetnessLevel().name().replace("_", "-").toLowerCase());
                stmt.setInt(i++, wine.getCatalogNumber());
                stmt.setInt(i++, wine.getManufacturer().getManufacturerId());
                stmt.setString(i++, wine.getPhotoPath());
                stmt.setInt(i++, wine.getWineType().getWineTypeID());
                stmt.setInt(i++, wine.getWineId());
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
     * Deletes a wine from the database by ID.
     * @param wineId The ID of the wine to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteWine(int wineId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_WINE)) {

                stmt.setInt(1, wineId);
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
     * Fetches all wines from the database.according to manufacutrerID
     * @return ArrayList of Wine objects.
     */

    public ArrayList<Wine> getWinesByManufacturerId(int manufacturerId) {
        ArrayList<Wine> wines = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINES_BY_MANUFACTURER)) {
                stmt.setInt(1, manufacturerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int i = 1;
                   
                    wines.add(new Wine(
                        rs.getInt(i++),        
                        rs.getString(i++),     
                        rs.getInt(i++),        
                        rs.getString(i++),     
                        rs.getDouble(i++),     
                        SweetnessLevel.valueOf(rs.getString(i++).replace("-", "_").toUpperCase()),
                        rs.getInt(i++),       
                        ManufacturerControl.getInstance().getManufacturerById(rs.getInt(i++)),         
                        rs.getString(i++),
                        WineTypeControl.getInstance().getWineTypeById(rs.getInt(i++))

                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wines;
    }
    /**
     * Fetches a specific wine from the database
     * @return A wine Object
     */

    public Wine getWineById(int wineId) {
        Wine wine = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINE_BY_ID)) {

                stmt.setInt(1, wineId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int i = 1;
                    wine = new Wine(
                        rs.getInt(i++),       
                        rs.getString(i++),     
                        rs.getInt(i++),        
                        rs.getString(i++),     
                        rs.getDouble(i++),     
                        SweetnessLevel.valueOf(rs.getString(i++).replace("-", "_").toUpperCase()), 
                        rs.getInt(i++),        
                        ManufacturerControl.getInstance().getManufacturerById(rs.getInt(i++)), 
                        rs.getString(i++),
                        WineTypeControl.getInstance().getWineTypeById(rs.getInt(i++))

                        
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wine;
    }
    /**
     * Fetches all Catalog numbers and manufacturer pairs from the database.
     * @return a hashset of all the pairs.
     */
    public boolean isWineUnique(int catalogNumber, int manufacturerId, int wineId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM TblWine WHERE CatalogNumber = ? AND ManufacturerID = ? AND WineID <> ?")) {

                stmt.setInt(1, catalogNumber);
                stmt.setInt(2, manufacturerId);
                stmt.setInt(3, wineId); 

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) == 0;                 }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
  
//fetches wine by winetype id 
	public ArrayList<Wine> getWinesByWineTypeId(int wineTypeId) {
		ArrayList<Wine> wines = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINES_BY_WINETYPE)) {
				stmt.setInt(1, wineTypeId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					int i = 1;
					wines.add(new Wine(rs.getInt(i++), rs.getString(i++), rs.getInt(i++), rs.getString(i++),
							rs.getDouble(i++),
							SweetnessLevel.valueOf(rs.getString(i++).replace("-", "_").toUpperCase()), rs.getInt(i++),
							ManufacturerControl.getInstance().getManufacturerById(rs.getInt(i++)), rs.getString(i++),
							WineTypeControl.getInstance().getWineTypeById(rs.getInt(i++))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wines;
	}
	
	public static Object[][] getAvailableWines() {
        ArrayList<Object[]> results = new ArrayList<>();
        for (Wine wine : WineControl.getInstance().getWines()) { // Use your existing method
            results.add(new Object[]{wine.getWineId(), wine.getName(), wine.getPricePerBottle()});
        }
        return results.toArray(new Object[0][0]);
    }
	



}
