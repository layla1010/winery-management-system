package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.User;
import entity.Consts;
import entity.Employee;

public class UserControl {
    private static UserControl _instance;

    private UserControl() {}

    public static UserControl getInstance() {
        if (_instance == null)
            _instance = new UserControl();
        return _instance;
    }

    /**
     * Fetches all users from the database (TblUser).
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_USERS);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int i = 1;
                    int userId = rs.getInt(i++);
                    int employeeId = rs.getInt(i++);
                    String username = rs.getString(i++);
                    String password = rs.getString(i++);
                    String userRole = rs.getString(i++);
                    
                    // Create the User object
                    User user = new User(userId, employeeId, username, password, userRole);
                    users.add(user);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Inserts a new user into the database via qryInsUser.
     */
    public boolean addUser(User user) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement cstmt = conn.prepareCall(Consts.SQL_INS_USER)) {
                
                // Make sure the parameter order matches your Access query
                // For example: qryInsUser(EmployeeID, Username, [Password], UserRole)
                cstmt.setInt(1, user.getEmployeeId());
                cstmt.setString(2, user.getUsername());
                cstmt.setString(3, user.getPassword());
                cstmt.setString(4, user.getUserRole());

                cstmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing user via qryUpdUser.
     */
    public boolean updateUser(User user) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement cstmt = conn.prepareCall(Consts.SQL_UPD_USER)) {
                
                // Again, match parameter order:
                // qryUpdUser(EmployeeID, Username, [Password], UserRole, UserID)
                cstmt.setInt(1, user.getEmployeeId());
                cstmt.setString(2, user.getUsername());
                cstmt.setString(3, user.getPassword());
                cstmt.setString(4, user.getUserRole());
                cstmt.setInt(5, user.getUserId());

                cstmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a user via qryDelUser.
     */
    public boolean deleteUser(int userId) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 CallableStatement cstmt = conn.prepareCall(Consts.SQL_DEL_USER)) {

                cstmt.setInt(1, userId);
                cstmt.executeUpdate();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public User authenticateUser(String username, String password) {
        User foundUser = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_USER_BY_CREDENTIALS)) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int i = 1;
                        int userId = rs.getInt(i++);
                        int employeeId = rs.getInt(i++);
                        String userNameDB = rs.getString(i++);
                        String passwordDB = rs.getString(i++);
                        String userRole = rs.getString(i++);
                        
                        // Create the User object if found
                        foundUser = new User(userId, employeeId, userNameDB, passwordDB, userRole);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return foundUser;
    }
    public Employee getEmployeeNameByUsername(String username) {
    	Employee employee = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement("SELECT EmployeeId FROM TblUser WHERE Username = ?")) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int employeeId = rs.getInt("EmployeeID");
                        // Use PersonControl to get the Employee object
                        employee = PersonControl.getInstance().getEmployeeById(employeeId);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

}
