package entity;

public class User {
    private int userId;       // AutoNumber PK from TblUser
    private int employeeId;   // FK referencing TblEmployee.EmployeeID
    private String username;
    private String password;  // For homework/demo only; in production, store a hash instead.
    private String userRole;  // e.g., "Sales", "Marketing", "Admin", etc.

    public User(int userId, int employeeId, String username, String password, String userRole) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
