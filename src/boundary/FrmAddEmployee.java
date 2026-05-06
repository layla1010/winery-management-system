package boundary;

import entity.Employee;
import entity.User;
import control.PersonControl;
import control.UserControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class FrmAddEmployee extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField nameTextField;
    private JTextField phoneTextField;
    private JTextField emailTextField;
    private JTextField addressTextField;
    private JDateChooser workStartDateChooser;
    private JTextField tfUserName;
    private JTextField tfPassword;
    private JComboBox<String> cbUserRole;

    /**
     * Create the panel.
     */
    public FrmAddEmployee() {
        this.setPreferredSize(new Dimension(614, 503));
        this.setBackground(new Color(105, 20, 56));
        setLayout(null);

        // Extra Panel for Better UI
        JPanel panel = new JPanel();
        panel.setBackground(new Color(105, 20, 56));
        panel.setBounds(10, 28, 593, 433);
        panel.setLayout(null);
        add(panel);

        // Separators for Styling
        
        JLabel addEmployeeLabel = new JLabel("Add Employee");
        addEmployeeLabel.setBounds(193, 11, 209, 33);
        addEmployeeLabel.setBackground(new Color(255, 255, 255));
        addEmployeeLabel.setForeground(new Color(235, 174, 63));
        addEmployeeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(addEmployeeLabel);

        // Another Separator for Top Padding
        

        // Labels and Fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(235, 174, 63));
        nameLabel.setBounds(30, 110, 120, 19);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(158, 111, 263, 20);
        nameTextField.setBackground(new Color(217, 166, 165));
        nameTextField.setForeground(Color.BLACK);
        nameTextField.setColumns(10);
        panel.add(nameTextField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(new Color(235, 174, 63));
        phoneLabel.setBounds(30, 139, 120, 19);
        phoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(phoneLabel);

        phoneTextField = new JTextField();
        phoneTextField.setBounds(158, 140, 263, 20);
        phoneTextField.setBackground(new Color(217, 166, 165));
        phoneTextField.setForeground(Color.BLACK);
        phoneTextField.setColumns(10);
        panel.add(phoneTextField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(new Color(235, 174, 63));
        emailLabel.setBounds(30, 170, 120, 19);
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setBounds(158, 171, 263, 20);
        emailTextField.setBackground(new Color(217, 166, 165));
        emailTextField.setForeground(Color.BLACK);
        emailTextField.setColumns(10);
        panel.add(emailTextField);

        JLabel addressLabel = new JLabel("Office Address:");
        addressLabel.setForeground(new Color(235, 174, 63));
        addressLabel.setBounds(30, 201, 123, 19);
        addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(addressLabel);

        addressTextField = new JTextField();
        addressTextField.setBounds(158, 202, 263, 20);
        addressTextField.setBackground(new Color(217, 166, 165));
        addressTextField.setForeground(Color.BLACK);
        addressTextField.setColumns(10);
        panel.add(addressTextField);

        JLabel dateLabel = new JLabel("WorkStartDate:");
        dateLabel.setForeground(new Color(235, 174, 63));
        dateLabel.setBounds(30, 306, 123, 19);
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(dateLabel);

        workStartDateChooser = new JDateChooser();
        workStartDateChooser.setBounds(158, 305, 263, 20);
        workStartDateChooser.setBackground(new Color(217, 166, 165));
        workStartDateChooser.setForeground(new Color(217, 166, 165));        
        workStartDateChooser.setDateFormatString("yyyy-MM-dd");
        panel.add(workStartDateChooser);

        // User Account Info Fields
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(new Color(235, 174, 63));
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUsername.setBounds(30, 246, 102, 19);
        panel.add(lblUsername);

        tfUserName = new JTextField();
        tfUserName.setForeground(Color.BLACK);
        tfUserName.setColumns(10);
        tfUserName.setBackground(new Color(217, 166, 165));
        tfUserName.setBounds(158, 243, 102, 20);
        panel.add(tfUserName);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setForeground(new Color(235, 174, 63));
        lblRole.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblRole.setBounds(287, 246, 38, 19);
        panel.add(lblRole);

        // Populate ComboBox with SALES and MARKETING (skip ADMIN)
        cbUserRole = new JComboBox<>();
        cbUserRole.setBounds(335, 242, 86, 22);
        cbUserRole.setBackground(new Color(217, 166, 165));
        cbUserRole.addItem("SALES");
        cbUserRole.addItem("MARKETING");
        panel.add(cbUserRole);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(new Color(235, 174, 63));
        lblPassword.setBackground(new Color(217, 166, 165));
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPassword.setBounds(30, 274, 102, 19);
        panel.add(lblPassword);

        tfPassword = new JTextField();
        tfPassword.setForeground(Color.BLACK);
        tfPassword.setColumns(10);
        tfPassword.setBackground(new Color(217, 166, 165));
        tfPassword.setBounds(158, 274, 263, 20);
        panel.add(tfPassword);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(new Color(194, 65, 78));
        btnSubmit.setBounds(212, 336, 178, 33);
        panel.add(btnSubmit);

        // Action Listener for Submit Button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployeeAndUser();
            }
        });
    }

    private void addEmployeeAndUser() {
        try {
            String name = nameTextField.getText().trim();
            String phone = phoneTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String address = addressTextField.getText().trim();
            Date workStartDate = workStartDateChooser.getDate();
            String username = tfUserName.getText().trim();
            String password = tfPassword.getText().trim();
            String role = (String) cbUserRole.getSelectedItem();

            if (name.isEmpty()) throw new IllegalArgumentException("Name field cannot be empty.");
            if (phone.isEmpty()) throw new IllegalArgumentException("Phone field cannot be empty.");
            if (email.isEmpty()) throw new IllegalArgumentException("Email field cannot be empty.");
            if (address.isEmpty()) throw new IllegalArgumentException("Address field cannot be empty.");
            if (workStartDate == null) throw new IllegalArgumentException("Work Start Date must be selected.");
            if (username.isEmpty()) throw new IllegalArgumentException("Username field cannot be empty.");
            if (password.isEmpty()) throw new IllegalArgumentException("Password field cannot be empty.");
            if (role == null) throw new IllegalArgumentException("Role must be selected.");
            if (!phone.matches("\\d+")) {
                throw new IllegalArgumentException("Phone must contain only numbers.");
            }
            if (workStartDate.after(new Date())) {
                throw new IllegalArgumentException("First contact date cannot be in the future.");
            }
            

            // Create new Employee object
            Employee newEmployee = new Employee(0, name, phone, address, email, workStartDate);

            // Add Employee to database
            boolean empSuccess = PersonControl.getInstance().addEmployee(newEmployee);
            if (empSuccess) {
                int empId = newEmployee.getEmployeeID();

                if (empId == 0) {
                    Employee empFromDb = PersonControl.getInstance().getEmployeeByEmail(email);
                    if (empFromDb != null) {
                        empId = empFromDb.getEmployeeID();
                        newEmployee.setEmployeeID(empId);
                    }
                }
                if (empId == 0) throw new IllegalArgumentException("Employee added but failed to retrieve new Employee ID.");

                // Create corresponding User record
                User newUser = new User(0, empId, username, password, role);
                boolean userSuccess = UserControl.getInstance().addUser(newUser);
                if (userSuccess) {
                    JOptionPane.showMessageDialog(this, "Employee and user account added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    throw new IllegalArgumentException("Employee added but failed to create user account.");
                }
            } else {
                throw new IllegalArgumentException("Error adding Employee.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        nameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
        addressTextField.setText("");
        workStartDateChooser.setDate(null);
        tfUserName.setText("");
        tfPassword.setText("");
        cbUserRole.setSelectedIndex(0);
    }
}
