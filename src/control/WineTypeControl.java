package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entity.*;
import enums.*;

public class WineTypeControl {
    private static WineTypeControl _instance;

    
    private WineTypeControl() {
    }

    public static WineTypeControl getInstance() {
        if (_instance == null)
            _instance = new WineTypeControl();
        return _instance;
    }

    /**
     * Fetches all wine types from the database.
     * @return ArrayList of WineType objects.
     */
    public ArrayList<WineType> getWineTypes() {
        ArrayList<WineType> wineTypes = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINETYPES);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                    wineTypes.add(new WineType(
                        rs.getInt(i++),            
                        rs.getInt(i++),            
                        rs.getString(i++)          
                    ));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return wineTypes;
    }

    /**
     * Adds a new wine type to the database.
     * @param wineType The WineType object to add.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean addWineType(WineType wineType) {

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_WINETYPE)) {

                int i = 1;
                stmt.setInt(i++, wineType.getWineTypeSerialNumber());
                stmt.setString(i++, wineType.getName());
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing wine type in the database.
     * @param wineType The WineType object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateWineType(WineType wineType) {
  

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_WINETYPE)) {

                int i = 1;
                stmt.setInt(i++, wineType.getWineTypeSerialNumber());
                stmt.setString(i++, wineType.getName());
                stmt.setInt(i++, wineType.getWineTypeID());
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a wine type from the database by ID.
     * @param wineTypeId The ID of the wine type to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteWineType(int wineTypeId) {

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_WINETYPE)) {

                stmt.setInt(1, wineTypeId);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fetches a specific wine type by ID.
     * @param wineTypeId The ID of the wine type to fetch.
     * @return WineType object if found, null otherwise.
     */
    public WineType getWineTypeById(int wineTypeId) {
        WineType wineType = null;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINETYPE_BY_ID)) {

                stmt.setInt(1, wineTypeId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int i = 1;
                    wineType = new WineType(
                        rs.getInt(i++),           
                        rs.getInt(i++),            
                        rs.getString(i++)        
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return wineType;
    }

	/**
	 * Fetches a specific wine type by name.
	 * 
	 * @param wineTypeName The name of the wine type to fetch.
	 * @return WineType object if found, null otherwise.
	 */
        public int getWineTypeIDByName(String wineTypeName) {
        	int wineTypeID = -1;
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
						PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_WINETYPE_BY_NAME)) {
					stmt.setString(1, wineTypeName);
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						wineTypeID = rs.getInt(1);
					}
					
				}
				
			} 
			catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			return wineTypeID;
        }
        /*----------------------------------------- DISH METHODS --------------------------------------------*/

        /**
         * Fetches all dishes from the database.
         * @return List of Dish objects.
         */
        public ArrayList<Dish> getDishes() {
        	ArrayList<Dish> dishes = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_DISHES);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    dishes.add(new Dish(rs.getInt("DishID"), rs.getString("DishName")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dishes;
        }

        /**
         * Adds a new dish.
         * @param dish The Dish object to add.
         * @return true if the operation is successful.
         */
        public boolean addDish(Dish dish) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_DISH)) {

                stmt.setString(1, dish.getName());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Updates a dish.
         * @param dish The updated Dish object.
         * @return true if the update is successful.
         */
        public boolean updateDish(Dish dish) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_DISH)) {

                stmt.setString(1, dish.getName());
                stmt.setInt(2, dish.getDishID());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Deletes a dish.
         * @param dishID The dish ID to delete.
         * @return true if deletion is successful.
         */
        public boolean deleteDish(int dishID) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_DISH)) {

                stmt.setInt(1, dishID);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /*----------------------------------------- DISH LINKS METHODS --------------------------------------------*/

        /**
         * Fetches all links for a given dish.
         * @param dishID The ID of the dish.
         * @return List of DishLink objects.
         */
        public ArrayList<DishLink> getDishLinksByDishID(int dishID) {
        	ArrayList<DishLink> links = new ArrayList<>();

            String query = "SELECT * FROM TblDishLinks WHERE DishID = ?";

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, dishID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    links.add(new DishLink(
                        rs.getInt("LinkID"), 
                        rs.getInt("DishID"), 
                        rs.getString("Link")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return links;
        }

        /**
         * Adds a new DishLink.
         * @param dishLink The DishLink object to add.
         * @return true if successful.
         */
        public boolean addDishLink(DishLink dishLink) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_DISHLINK)) {

                stmt.setInt(1, dishLink.getDishID());  // Include dishID
                stmt.setString(2, dishLink.getLink());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Updates a DishLink.
         * @param dishLink The updated DishLink object.
         * @return true if the update is successful.
         */
        public boolean updateDishLink(DishLink dishLink) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_UPD_DISHLINK)) {

                stmt.setInt(1, dishLink.getDishID());  // Include dishID
                stmt.setString(2, dishLink.getLink());
                stmt.setInt(3, dishLink.getLinkID());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Deletes a DishLink.
         * @param linkID The link ID to delete.
         * @return true if deletion is successful.
         */
        public boolean deleteDishLink(int linkID) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_DISHLINK)) {

                stmt.setInt(1, linkID);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }


        /*----------------------------------------- OCCASION METHODS --------------------------------------------*/

        /**
         * Fetches all occasions from the database.
         * @return List of Occasion objects.
         */
        public ArrayList<Occasion> getOccasions() {
            ArrayList<Occasion> occasions = new ArrayList<>();
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                     PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_OCCASIONS);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        occasions.add(new Occasion(
                            Season.valueOf(rs.getString("Season").toUpperCase()),
                            Location.valueOf(rs.getString("Location").toUpperCase()),
                            rs.getInt("OccasionID"),
                            rs.getString("Description"),rs.getString("OccasionName")
                        ));
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return occasions;
        }


        /**
         * Adds a new Occasion.
         * @param occasion The Occasion object to add.
         * @return true if successful.
         */
        public boolean addOccasion(Occasion occasion) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_OCCASION)) {
            	 stmt.setString(1, occasion.getOccasionName());
            	stmt.setString(2, occasion.getSeason().name().toUpperCase());  
                stmt.setString(3, occasion.getLocation().name().toUpperCase());
                stmt.setString(4, occasion.getDescription());
               
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Updates an Occasion.
         * @param occasion The updated Occasion object.
         * @return true if the update is successful.
         */
        public boolean updateOccasion(Occasion occasion) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE TblOccasion SET  OccasionName = ?, [Season] = ?, Location = ?, Description = ? WHERE OccasionID = ?")) {
            	stmt.setString(1, occasion.getOccasionName());
            	stmt.setString(2, occasion.getSeason().name().toUpperCase());  
                stmt.setString(3, occasion.getLocation().name().toUpperCase());
                stmt.setString(4, occasion.getDescription());
                
                stmt.setInt(5, occasion.getOccasionId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Deletes an Occasion.
         * @param occasionID The occasion ID to delete.
         * @return true if deletion is successful.
         */
        public boolean deleteOccasion(int occasionID) {
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_OCCASION)) {

                stmt.setInt(1, occasionID);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

		
       
    }
