package entity;

import java.net.URLDecoder;


/**
 * http://www.javapractices.com/topic/TopicAction.do?Id=2
 */
public final class Consts {
	private Consts() {
		throw new AssertionError();
	}

	protected static final String DB_FILEPATH = getDBPath();
	public static final String CONN_STR = "jdbc:ucanaccess://" + DB_FILEPATH ;

	/*----------------------------------------- MANUFACTURERS QUERIES -------------------------------------*/
	public static final String SQL_SEL_MANUFACTURERS = "SELECT * FROM TblManufacturer";
	public static final String SQL_INS_MANUFACTURER = "{ call qryInsManufacturer(?,?,?,?) }";
	public static final String SQL_UPD_MANUFACTURER = "{ call qryUpdManufacturer(?,?,?,?,?) }";
	public static final String SQL_DEL_MANUFACTURER = "{ call qryDelManufacturer(?) }";
	public static final String SQL_SEL_MANUFACTURER_BY_ID = "SELECT * FROM TblManufacturer WHERE UniqueAutomaticNumber = ?";

	/*----------------------------------------- WINES QUERIES --------------------------------------------*/
	public static final String SQL_SEL_WINE_BY_ID = "SELECT * FROM TblWine WHERE WineID = ?";

	public static final String SQL_SEL_WINES = "SELECT * FROM TblWine";
	public static final String SQL_INS_WINE = "{ call qryInsWine(?,?,?,?,?,?,?,?,?) }";
	public static final String SQL_UPD_WINE = "{ call qryUpdWine(?,?,?,?,?,?,?,?,?,?) }";
	public static final String SQL_DEL_WINE = "{ call qryDelWine(?) }";
	public static final String SQL_SEL_WINES_BY_MANUFACTURER = "{ call qryShowWineByManufacturer(?) }"; 
	public static final String SQL_SEL_CATALOG_MANUFACTURER_PAIRS = "SELECT * FROM qryGetAllCatalogManufacturer";
	public static final String SQL_SEL_WINES_BY_MANUFACTURERS = "{ call qryShowWinesByManufacturers }"; 
	public static final String SQL_SEL_WINES_BY_WINETYPE =
		    "SELECT * FROM TblWine WHERE WineTypeID = ? ";

	/*----------------------------------------- WINE TYPE QUERIES -----------------------------------------*/
	public static final String SQL_SEL_WINETYPES = "SELECT * FROM TblWineType";
	public static final String SQL_INS_WINETYPE = "{ call qryInsWineType(?,?) }";
	public static final String SQL_DEL_WINETYPE = "{ call qryDelWineType(?) }";
	public static final String SQL_UPD_WINETYPE = "{ call qryUpdWineType(?,?,?) }";
	public static final String SQL_SEL_WINETYPE_BY_ID = "SELECT * FROM TblWineType WHERE WineTypeID = ?";
	public static final String SQL_SEL_WINETYPE_BY_NAME = "SELECT * FROM TblWineType WHERE WineTypeName = ?";
	/*----------------------------------------- OCCASION QUERIES -----------------------------------------*/
	public static final String SQL_SEL_OCCASIONS = "SELECT * FROM TblOccasion";
	public static final String SQL_SEL_OCCASIONS_BY_WINETYPE = "{ call qrySelWineTypeOccasion(?) }";
	public static final String SQL_SEL_OCCASIONS_WINETYPES = "SELECT * FROM TblOccasionPerWineType";
	public static final String SQL_INS_OCCASION = "{ call qryInsOccasion(?,?,?,?) }";
	public static final String SQL_DEL_OCCASION = "{ call qryDelOccasion(?) }";
	public static final String SQL_UPD_OCCASION = "{ call qryUpdOccasion(?,?,?,?,?) }";
	public static final String SQL_INS_OCCASION_PER_WINETYPE = 
		    "INSERT INTO TblOccasionPerWineType (WineTypeID, OccasionID) VALUES (?, ?)";
		public static final String SQL_DEL_OCCASION_PER_WINETYPE = 
		    "DELETE FROM TblOccasionPerWineType WHERE WineTypeID = ? AND OccasionID = ?";
	/*----------------------------------------- DISH QUERIES --------------------------------------------*/
	public static final String SQL_SEL_DISHES = "SELECT * FROM TblDish";
	public static final String SQL_SEL_DISHES_BY_WINETYPE = "{ call qrySelWineTypeDish(?) }";
	public static final String SQL_SEL_DISHES_WINETYPES = "SELECT * FROM TblWineTypeDish";
	public static final String SQL_INS_DISH = "{ call qryInsDish(?) }";
	public static final String SQL_DEL_DISH = "{ call qryDelDish(?) }";
	public static final String SQL_UPD_DISH = "{ call qryUpdDish(?,?) }";
	public static final String SQL_INS_DISHLINK = "{ call qryInsDishLinks(?,?) }";
	public static final String SQL_DEL_DISHLINK = "{ call qryDelDishLink(?) }";
	public static final String SQL_UPD_DISHLINK = "{ call qryUpdDishLinks(?,?,?) }";
	public static final String SQL_INS_WINETYPE_DISH = 
		    "INSERT INTO TblWineTypeDish (WineTypeID, DishID) VALUES (?, ?)";
		public static final String SQL_DEL_WINETYPE_DISH = 
		    "DELETE FROM TblWineTypeDish WHERE WineTypeID = ? AND DishID = ?";
	/*----------------------------------------- PERSON QUERIES -----------------------------------------*/
	public static final String SQL_SEL_CUSTOMERS = "SELECT * FROM TblCustomer";
	public static final String SQL_SEL_EMPLOYEES = 
		    "SELECT employeeID, employeeName, phoneNumber, officeAddress, email, employeeStartDate FROM TblEmployee";

	public static final String SQL_INS_CUSTOMER = "{ call qryInsCustomer(?,?,?,?,?) }";
	public static final String SQL_UPD_CUSTOMER = "{ call qryUpdCustomer(?,?,?,?,?,?) }";
	public static final String SQL_INS_EMPLOYEE = "{ call qryInsEmp(?,?,?,?,?) }";
	public static final String SQL_UPD_EMPLOYEE= "{ call qryUpdEmp(?,?,?,?,?,?) }";
	public static final String SQL_DEL_CUSTOMER = "{ call qryDelCustomer(?) }";
	public static final String SQL_DEL_EMPLOYEE = "{ call qryDelEmp(?) }";

	public static final String SQL_SEL_EMPLOYEE_BY_ID = "SELECT * FROM TblEmployee WHERE EmployeeID = ?";
	public static final String SQL_SEL_CUSTOMER_BY_ID = "SELECT * FROM TblCustomer WHERE CustomerID = ?";
	public static final String SQL_SEL_EMPLOYEE_UNPRODUCTIVE = "{call qryUnproductiveEmployees(?,?)}";
	/*----------------------------------------- Inventory QUERIES -----------------------------------------*/
	public static final String SQL_INS_INVENTORY = "{ call qryInsInventory(?,?,?) }";
	public static final String SQL_UPD_INVENTORY = "{ call qryUpdInventory(?,?,?,?) }";
	public static final String SQL_INS_lOCATION = "{ call qryInsLocation(?) }";
	public static final String SQL_UPD_lOCATION= "{ call qryUpdLocation(?,?) }";
	public static final String SQL_DEL_INVENTORY = "{ call qryDelInventory(?) }";
	public static final String SQL_DEL_lOCATION = "{ call qryDelLocation(?) }";
	public static final String SQL_SEL_INVENTORY = "SELECT * FROM TblInventory";
	public static final String SQL_SEL_lOCATION = "SELECT * FROM TblLocation";
	public static final String SQL_SEL_INVENTORY_INFO = "SELECT * FROM qryInventoryInfo";
	public static final String SQL_SEL_OVERALL_STOCK = "SELECT * FROM qryOverallStock";
	/*----------------------------------------- Order QUERIES -----------------------------------------*/
    public static final String SQL_INS_ORDER = "{ call qryInsOrder(?,?,?,?) }";
    public static final String SQL_UPD_ORDER =
    	    "UPDATE TblOrder SET OrderDate = ?, ShipmentDate = ?, EmployeeID = ?, CurrentStatus = ? WHERE OrderID = ?";

    public static final String SQL_DEL_ORDER = "{ call qryDelOrder(?) }";
    public static final String SQL_SEL_ORDER = "SELECT * FROM TblOrder";
	public static final String SQL_SEL_ORDER_BY_ID = "SELECT * FROM TblOrder WHERE OrderID = ?";

    
	/*----------------------------------------- Urgent Order QUERIES -----------------------------------------*/
    public static final String SQL_INS_URGENT_ORDER = "INSERT INTO TblUrgentOrder ([OrderID],[ExpectedDeliveryTime], [Customer], [PriorityLevel]) VALUES (?,?, ?, ?)";
    public static final String SQL_UPD_URGENT_ORDER = "{ call qryUpdUrgentOrder(?,?,?,?) }";
    public static final String SQL_DEL_URGENT_ORDER = "{ call qryDelUrgentOrder(?) }";
    public static final String SQL_SEL_URGENT_ORDER = "SELECT * FROM TblUrgentOrder";
	public static final String SQL_SEL_URGENT_ORDER_BY_ID = "SELECT * FROM TblUrgentOrder WHERE OrderID = ?";
	/*----------------------------------------- Regular Order QUERIES -----------------------------------------*/
    public static final String SQL_INS_REGULAR_ORDER = "INSERT INTO TblRegularOrder ([OrderCustomersID], [Role]) VALUES (?, ?)";
    public static final String SQL_UPD_REGULAR_ORDER = "{ call qryUpdRegOrder(?,?,?) }";
    public static final String SQL_DEL_REGULAR_ORDER = "{ call qryDelRegOrder(?) }";
    public static final String SQL_SEL_REGULAR_ORDER = "SELECT * FROM TblRegularOrder";
    public static final String SQL_INS_REGULAR_ORDER_CUSTOMER = "INSERT INTO TblRegularOrder (RegularOrderID, CustomerID, Role) VALUES (?, ?, ?)";
	public static final String SQL_SEL_REGULAR_ORDER_BY_ID = "SELECT * FROM TblRegularOrder WHERE RegularOrderID = ?";

    
	/*----------------------------------------- Regular Order Details QUERIES -----------------------------------------*/
    public static final String SQL_INS_REGULAR_ORDER_DETAILS = "{ call qryInsRegularOrderDet(?,?,?) }";
    public static final String SQL_UPD_REGULAR_ORDER_DETAILS = "{ call qryUpdRegOrderDet(?,?,?) }";
    public static final String SQL_DEL_REGULAR_ORDER_DETAILS = "{ call qryDelRegOrderDet(?) }";
    public static final String SQL_SEL_REGULAR_ORDER_DETAILS = "SELECT * FROM TblRegularOrderDetails WHERE OrderID = ?";
    public static final String SQL_UPD_REGPRICE = "{ call qryUpdRegPrice() }";

	
    /*----------------------------------------- USER QUERIES -----------------------------------------*/
    public static final String SQL_INS_USER = "{ call qryInsUser(?,?,?,?) }";
	public static final String SQL_UPD_USER= "{ call qryUpdUser(?,?,?,?,?) }";
	public static final String SQL_DEL_USER = "{ call qryDelUser(?) }";
	public static final String SQL_SEL_USERS = "SELECT * FROM TblUser";
	public static final String SQL_SEL_USER_BY_CREDENTIALS =
    	    "SELECT * FROM TblUser WHERE [Username] = ? AND [Password] = ?";
	
	
	/**
	 * find the correct path of the DB file
	 * @return the path of the DB file (from eclipse or with runnable file)
	 */
	private static String getDBPath() {
		try {
			String path = Consts.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path, "UTF-8");
			// System.out.println(decoded) ;
			if (decoded.contains(".jar")) {
				decoded = decoded.substring(0, decoded.lastIndexOf('/'));
				return decoded + "/database/CheersSystemDatabase.accdb";
			} else {
				//System.out.println(decoded.substring(0, decoded.lastIndexOf('/'))+ "/entity/CheersSystemDatabase.accdb");
				decoded = decoded.substring(0, decoded.lastIndexOf("bin/"));
			//	System.out.println(decoded);
				return decoded + "/src/entity/CheersSystemDatabase.accdb";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}




}
