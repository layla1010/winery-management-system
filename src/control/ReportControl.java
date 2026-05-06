
package control;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import javax.swing.JOptionPane;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.*;
import enums.*;


public class ReportControl {
    private static ReportControl _instance;

    public ReportControl() {}

    public static ReportControl getInstance() {
        if (_instance == null)
            _instance = new ReportControl();
        return _instance;
    }
  
   

    /**
     * Fetches all occasions from the database.
     * @return ArrayList of Occasion objects.
     */
    public ArrayList<Occasion> getAllOccasions() {
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
     * Fetches all dishes from the database.
     * @return ArrayList of Dish objects.
     */
    public ArrayList<Dish> getAllDishes() {
        ArrayList<Dish> dishes = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblDish");
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int dishId = rs.getInt("DishID");
                    String dishName = rs.getString("DishName");

                    // Create Dish object without recipe links
                    Dish dish = new Dish(dishId, dishName);
                    dishes.add(dish);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }
    public int getUnlinkedWineTypesCount() {
        // Get all wine types from your WineTypeControl
        ArrayList<WineType> allWineTypes = WineTypeControl.getInstance().getWineTypes();
        int count = 0;
        for (WineType wt : allWineTypes) {
            // You can either:
            // 1. Use getLinkedOccasionsByWineTypeId() and getLinkedDishesByWineTypeId() if available
            ArrayList<Occasion> occasions = getLinkedOccasionsByWineTypeId(wt.getWineTypeID());
            ArrayList<Dish> dishes = getLinkedDishesByWineTypeId(wt.getWineTypeID());
            if (occasions.isEmpty() && dishes.isEmpty()) {
                count++;
            }
            
            // 2. Alternatively, if you want to check using your isWineTypeRelatedToOccasion() and isWineTypeRelatedToDish() methods,
            // you would need to iterate over all possible occasion IDs and dish IDs (which is less efficient).
        }
        return count;
    }

    /**
     * Fetches occasions associated with a specific wine type.
     * @param wineTypeId The ID of the wine type.
     * @return ArrayList of Occasion objects.
     */
    public ArrayList<Occasion> getOccasionsByWineTypeId(int wineTypeId) {
        ArrayList<Occasion> occasions = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_OCCASIONS_BY_WINETYPE)) {
                stmt.setInt(1, wineTypeId);
                ResultSet rs = stmt.executeQuery();
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
     * Fetches dishes associated with a specific wine type.
     * @param wineTypeId The ID of the wine type.
     * @return ArrayList of Dish objects.
     */
    public ArrayList<Dish> getDishesByWineTypeId(int wineTypeId) {
        ArrayList<Dish> dishes = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_DISHES_BY_WINETYPE)) {
                stmt.setInt(1, wineTypeId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int dishId = rs.getInt("DishID");
                    String dishName = rs.getString("DishName");

                    // Create Dish object without recipe links
                    Dish dish = new Dish(dishId, dishName);
                    dishes.add(dish);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    /**
     * Checks if a wine type is related to a specific occasion.
     * @param wineTypeId The ID of the wine type.
     * @param occasionId The ID of the occasion.
     * @return true if related, false otherwise.
     */
    public boolean isWineTypeRelatedToOccasion(int wineTypeId, int occasionId) {
        boolean isRelated = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblOccasionPerWineType WHERE WineTypeID = ? AND OccasionID = ?")) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, occasionId);
                ResultSet rs = stmt.executeQuery();
                isRelated = rs.next();  // If a row exists, the WineType is related to the Occasion
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return isRelated;
    }

    /**
     * Checks if a wine type is related to a specific dish.
     * @param wineTypeId The ID of the wine type.
     * @param dishId The ID of the dish.
     * @return true if related, false otherwise.
     */
    public boolean isWineTypeRelatedToDish(int wineTypeId, int dishId) {
        boolean isRelated = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TblWineTypeDish WHERE WineTypeID = ? AND DishID = ?")) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, dishId);
                ResultSet rs = stmt.executeQuery();
                isRelated = rs.next();  // If a row exists, the WineType is related to the Dish
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return isRelated;
    }
    /***********************************
     * OccasionPerWineType Methods
     ***********************************/

    /**
     * Inserts a record into TblOccasionPerWineType linking a WineType to an Occasion.
     * @param wineTypeId  The ID of the WineType
     * @param occasionId  The ID of the Occasion
     * @return true if insertion succeeds, false otherwise
     */
    public boolean addOccasionPerWineType(int wineTypeId, int occasionId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_OCCASION_PER_WINETYPE)) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, occasionId);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a record from TblOccasionPerWineType linking a WineType to an Occasion.
     * @param wineTypeId  The ID of the WineType
     * @param occasionId  The ID of the Occasion
     * @return true if deletion succeeds, false otherwise
     */
    public boolean deleteOccasionPerWineType(int wineTypeId, int occasionId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_OCCASION_PER_WINETYPE)) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, occasionId);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /***********************************
     * WineTypeDish Methods
     ***********************************/

    /**
     * Inserts a record into TblWineTypeDish linking a WineType to a Dish.
     * @param wineTypeId  The ID of the WineType
     * @param dishId      The ID of the Dish
     * @return true if insertion succeeds, false otherwise
     */
    public boolean addWineTypeDish(int wineTypeId, int dishId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_INS_WINETYPE_DISH)) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, dishId);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a record from TblWineTypeDish linking a WineType to a Dish.
     * @param wineTypeId  The ID of the WineType
     * @param dishId      The ID of the Dish
     * @return true if deletion succeeds, false otherwise
     */
    public boolean deleteWineTypeDish(int wineTypeId, int dishId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_DEL_WINETYPE_DISH)) {

                stmt.setInt(1, wineTypeId);
                stmt.setInt(2, dishId);
                stmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Fetches all occasions linked to the given WineType.
     * @param wineTypeId The ID of the WineType.
     * @return ArrayList of Occasion objects.
     */
    public ArrayList<Occasion> getLinkedOccasionsByWineTypeId(int wineTypeId) {
        ArrayList<Occasion> occasions = new ArrayList<>();
        String sql = "SELECT o.OccasionID, o.OccasionName, o.Season, o.Location, o.Description " +
                     "FROM TblOccasion o " +
                     "JOIN TblOccasionPerWineType opwt ON o.OccasionID = opwt.OccasionID " +
                     "WHERE opwt.WineTypeID = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, wineTypeId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int occasionId = rs.getInt("OccasionID");
                    String occasionName = rs.getString("OccasionName");
                    String seasonStr = rs.getString("Season");
                    String locationStr = rs.getString("Location");
                    String description = rs.getString("Description");
                    
                    // Assuming Season and Location are enums:
                    Occasion occasion = new Occasion(
                            Season.valueOf(seasonStr.toUpperCase()),
                            Location.valueOf(locationStr.toUpperCase()),
                            occasionId,
                            description,
                            occasionName);
                    occasions.add(occasion);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return occasions;
    }
    /**
     * Fetches all dishes linked to the given WineType.
     * @param wineTypeId The ID of the WineType.
     * @return ArrayList of Dish objects.
     */
    public ArrayList<Dish> getLinkedDishesByWineTypeId(int wineTypeId) {
        ArrayList<Dish> dishes = new ArrayList<>();
        String sql = "SELECT d.DishID, d.DishName " +
                     "FROM TblDish d " +
                     "JOIN TblWineTypeDish wtd ON d.DishID = wtd.DishID " +
                     "WHERE wtd.WineTypeID = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, wineTypeId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int dishId = rs.getInt("DishID");
                    String dishName = rs.getString("DishName");
                    dishes.add(new Dish(dishId, dishName));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }
    public void generateInventoryReport() {
        ArrayList<Inventory> inventoryList = InventoryControl.getInstance().getInventory();
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File("CurrentInventoryReport.json"), inventoryList);
            System.out.println("Inventory report generated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scheduleWeeklyReport() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = computeInitialDelayForNextWeekend();
        scheduler.scheduleAtFixedRate(this::generateInventoryReport, initialDelay, 7, TimeUnit.DAYS);
    }

    private long computeInitialDelayForNextWeekend() {
        Calendar nextSaturday = Calendar.getInstance();
        nextSaturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        nextSaturday.set(Calendar.HOUR_OF_DAY, 9);  // 9 AM
        nextSaturday.set(Calendar.MINUTE, 0);
        nextSaturday.set(Calendar.SECOND, 0);
        long delay = nextSaturday.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) delay += TimeUnit.DAYS.toMillis(7); // Next Saturday if past this Saturday
        return delay;
    }

    public void importGefenXML(File xmlFile) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            GefenData data = xmlMapper.readValue(xmlFile, GefenData.class);

            for (GefenData.ManufacturerXML manu : data.manufacturers) {
                Manufacturer manufacturer = new Manufacturer(
                    0, // AutoNumber
                    manu.fullName,
                    manu.phoneNumber,
                    manu.address,
                    manu.email
                );

                ManufacturerControl.getInstance().addManufacturer(manufacturer);

                Manufacturer insertedManufacturer = ManufacturerControl.getInstance()
                                                    .getManufacturers()
                                                    .stream()
                                                    .filter(m -> m.getName().equals(manu.fullName))
                                                    .findFirst().orElse(null);

                if (insertedManufacturer == null) continue; // safety check

                for (GefenData.WineXML wineXML : manu.wines) {
                    Wine wine = new Wine(
                        0, // AutoNumber
                        wineXML.wineName,
                        wineXML.productionYear,
                        wineXML.description,
                        wineXML.pricePerBottle,
                        SweetnessLevel.valueOf(wineXML.sweetnessLevel.replace("-", "_").toUpperCase()),
                        wineXML.catalogNumber,
                        insertedManufacturer,
                        wineXML.photo,
                        WineTypeControl.getInstance().getWineTypeById(wineXML.wineTypeID)
                    );

                    WineControl.getInstance().addWine(wine);
                }
            }

            JOptionPane.showMessageDialog(null, "Gefen XML import completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "XML import failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void exportToXML(File xmlFile) {
        try {
            ArrayList<Manufacturer> manufacturers = ManufacturerControl.getInstance().getManufacturers();
            GefenData gefenData = new GefenData();
            gefenData.manufacturers = new ArrayList<>();

            for (Manufacturer manufacturer : manufacturers) {
                GefenData.ManufacturerXML manuXML = new GefenData.ManufacturerXML();
                manuXML.fullName = manufacturer.getName();
                manuXML.phoneNumber = manufacturer.getPhoneNumber();
                manuXML.address = manufacturer.getAddress();
                manuXML.email = manufacturer.getEmail();

                ArrayList<Wine> wines = WineControl.getInstance().getWinesByManufacturerId(manufacturer.getManufacturerId());
                manuXML.wines = new ArrayList<>();

                for (Wine wine : wines) {
                    GefenData.WineXML wineXML = new GefenData.WineXML();
                    wineXML.wineName = wine.getName();
                    wineXML.productionYear = wine.getProductionYear();
                    wineXML.description = wine.getDescription();
                    wineXML.pricePerBottle = wine.getPricePerBottle();
                    wineXML.sweetnessLevel = wine.getSweetnessLevel().toString().replace("_", "-").toLowerCase();
                    wineXML.catalogNumber = wine.getCatalogNumber();
                    wineXML.photo = wine.getPhotoPath();
                    wineXML.wineTypeID = wine.getWineType().getWineTypeID();

                    manuXML.wines.add(wineXML);
                }

                gefenData.manufacturers.add(manuXML);
            }

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(xmlFile, gefenData);

            JOptionPane.showMessageDialog(null, "XML Export Successful!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "XML Export Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public ArrayList<Object[]> getUnproductiveEmployeeReportData(java.util.Date startDate, java.util.Date endDate) {
        ArrayList<Object[]> reportData = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_EMPLOYEE_UNPRODUCTIVE)) {

                // Set the parameters for the stored procedure
                stmt.setDate(1, new java.sql.Date(startDate.getTime()));
                stmt.setDate(2, new java.sql.Date(endDate.getTime()));
                //System.out.println("Start Date: " + new java.sql.Date(startDate.getTime()));
                //System.out.println("End Date: " + new java.sql.Date(endDate.getTime()));

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Assuming your stored procedure returns the following columns:
                    // EmployeeID, employeeName, phoneNumber, officeAddress, email,
                    // employeeStartDate, CountOfUrgentOrders, CountOfRegularOrders, TotalOrders, Unproductive
                    Object[] row = new Object[10];
                    row[0] = rs.getInt("EmployeeID");
                    row[1] = rs.getString("employeeName");
                    row[2] = rs.getString("phoneNumber");
                    row[3] = rs.getString("officeAddress");
                    row[4] = rs.getString("email");
                    row[5] = rs.getDate("employeeStartDate");
                    row[6] = rs.getInt("CountOfUrgentOrders");
                    row[7] = rs.getInt("CountOfRegularOrders");
                    row[8] = rs.getInt("TotalOrders");
                    row[9] = rs.getString("Unproductive");
                    reportData.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportData;
    }
    public void generateCSVReport(ArrayList<Object[]> reportData, String filePath) {
        // Define CSV header corresponding to your report columns
        String[] headers = {"EmployeeID", "Employee Name", "Phone", "Office Address", "Email", 
                            "Start Date", "Urgent Orders", "Regular Orders", "Total Orders", "Unproductive"};
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header row
            writer.write(String.join(",", headers));
            writer.newLine();
            
            // Write each data row
            for (Object[] row : reportData) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    Object value = row[i];
                    // Enclose cell in quotes if it contains a comma or special characters
                    String cell = (value != null) ? value.toString() : "";
                    if (cell.contains(",") || cell.contains("\"") || cell.contains("\n")) {
                        cell = cell.replace("\"", "\"\"");
                        cell = "\"" + cell + "\"";
                    }
                    sb.append(cell);
                    if (i < row.length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.flush();
            System.out.println("CSV report generated successfully at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  

}