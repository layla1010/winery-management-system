package boundary;

import entity.Customer;
import control.PersonControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class FrmAddCustomer extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField nameTextField;
    private JTextField phoneTextField;
    private JTextField emailTextField;
    private JTextField addressTextField;
    private JDateChooser firstContactDateChooser;

    /**
     * Create the panel.
     */
    public FrmAddCustomer() {
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
        

        JLabel addCustomerLabel = new JLabel("Add Customer");
        addCustomerLabel.setBounds(193, 11, 209, 33);
        addCustomerLabel.setBackground(new Color(255, 255, 255));
        addCustomerLabel.setForeground(new Color(235, 174, 63));
        addCustomerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(addCustomerLabel);

        // Another Separator for Top Padding
        

        // Labels and Fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(235, 174, 63));
        nameLabel.setBounds(80, 110, 70, 19);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(158, 110, 263, 20);
        nameTextField.setBackground(new Color(217, 166, 165));
        nameTextField.setForeground(Color.BLACK);
        nameTextField.setColumns(10);
        panel.add(nameTextField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(new Color(235, 174, 63));
        phoneLabel.setBounds(80, 135, 70, 19);        phoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(phoneLabel);

        phoneTextField = new JTextField();
        phoneTextField.setBounds(158, 135, 263, 20);
        phoneTextField.setBackground(new Color(217, 166, 165));
        phoneTextField.setForeground(Color.BLACK);
        phoneTextField.setColumns(10);
        panel.add(phoneTextField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(new Color(235, 174, 63));
        emailLabel.setBounds(80, 160, 70, 19);   
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setBounds(158, 160, 263, 20);
        emailTextField.setBackground(new Color(217, 166, 165));
        emailTextField.setForeground(Color.BLACK);
        emailTextField.setColumns(10);
        panel.add(emailTextField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(new Color(235, 174, 63));
        addressLabel.setBounds(80, 215, 70, 19);        addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(addressLabel);

        addressTextField = new JTextField();
        addressTextField.setBounds(158, 215, 263, 20);
        addressTextField.setBackground(new Color(217, 166, 165));
        addressTextField.setForeground(Color.BLACK);
        addressTextField.setColumns(10);
        panel.add(addressTextField);

        JLabel dateLabel = new JLabel("First Contact Date:");
        dateLabel.setForeground(new Color(235, 174, 63));
        dateLabel.setBounds(30, 270, 130, 19);        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(dateLabel);

        firstContactDateChooser = new JDateChooser();
        firstContactDateChooser.setBounds(158, 270, 263, 20);
        firstContactDateChooser.setBackground(new Color(217, 166, 165));
        firstContactDateChooser.setDateFormatString("yyyy-MM-dd");
        panel.add(firstContactDateChooser);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(new Color(194, 65, 78));
        btnSubmit.setBounds(212, 336, 178, 33);
        panel.add(btnSubmit);

        // Action Listener for Submit Button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomerToDatabase();
            }
        });
    }

    private void addCustomerToDatabase() {
        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String address = addressTextField.getText().trim();
        Date firstContactDate = firstContactDateChooser.getDate();
        
        try {
            // Validate fields
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || firstContactDate == null) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            // Validate phone contains only numbers
            if (!phone.matches("\\d+")) {
                throw new IllegalArgumentException("Phone must contain only numbers.");
            }

            // Validate first contact date is not later than today
            Date today = new Date();
            if (firstContactDate.after(today)) {
                throw new IllegalArgumentException("First contact date cannot be in the future.");
            }

            // Create Customer Object
            Customer newCustomer = new Customer(0, name, phone, address, email, firstContactDate);

            // Add Customer to Database
            boolean success = PersonControl.getInstance().addCustomer(newCustomer);
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields(); // Clear the form after successful submission
            } else {
                JOptionPane.showMessageDialog(this, "Error adding customer.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearFields() {
        nameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
        addressTextField.setText("");
        firstContactDateChooser.setDate(null);
    }
}
