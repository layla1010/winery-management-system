package boundary;


import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Button;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import control.*;
import entity.User;
import java.awt.Font;


public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField passwordField;
    private JTextField textField;
    private ImageIcon backgroundIcon; // Store the background image once

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginPage frame = new LoginPage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 828, 581);

        // Load the background image once
       backgroundIcon = new ImageIcon(getClass().getResource("/vecteezy_enjoying-white-wine-at-sunset-in-a-vineyard-setting-with_54417713.jpeg"));

        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        contentPane.setForeground(new Color(64, 128, 128));
        contentPane.setBackground(new Color(64, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 0, true), new LineBorder(new Color(0, 0, 0), 0, true)));
        panel.setBackground(new Color(0, 0, 0, 100));
        panel.setBounds(98, 101, 304, 280);
        contentPane.add(panel);

        passwordField = new JPasswordField();
        passwordField.setForeground(new Color(255, 255, 255));
        passwordField.setCaretColor(new Color(0, 0, 0));
        passwordField.setBackground(new Color(100, 0, 2));
        passwordField.setBounds(25, 135, 201, 28);
        panel.add(passwordField);

        textField = new JTextField();
        textField.setForeground(new Color(255, 255, 255));
        textField.setBackground(new Color(100, 0, 2));
        textField.setColumns(10);
        textField.setBounds(25, 81, 201, 28);
        panel.add(textField);

        JLabel lblNewLabel = new JLabel("Enter UserName:");
        lblNewLabel.setForeground(new Color(154, 154, 154));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(25, 50, 201, 28);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Enter Password:");
        lblNewLabel_1.setForeground(new Color(154, 154, 154));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(25, 106, 201, 28);
        panel.add(lblNewLabel_1);

        Button button = new Button("Login");
        button.setForeground(new Color(192, 192, 192));
        button.setBackground(new Color(126, 35, 14));
        button.setBounds(25, 198, 201, 37);
        panel.add(button);
        
     // Add a checkbox to show/hide the password
        JCheckBox showPasswordCheckBox = new JCheckBox("");
        showPasswordCheckBox.setBounds(25, 170, 21, 20);
        showPasswordCheckBox.setBackground(new Color(0, 0, 0, 0));
        showPasswordCheckBox.setForeground(Color.WHITE);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('*'); // Hide password
                }
            }
        });
        panel.add(showPasswordCheckBox);
        
        JLabel lblShowPass = new JLabel("Show Password");
        lblShowPass.setForeground(new Color(154, 154, 154));
        lblShowPass.setBounds(52, 170, 109, 22);
        panel.add(lblShowPass);
        
        JLabel lblNewLabel_2 = new JLabel("Cheers System");
        lblNewLabel_2.setForeground(new Color(255, 214, 196));
        lblNewLabel_2.setFont(new Font("Bauhaus 93", Font.PLAIN, 45));
        lblNewLabel_2.setBounds(469, 45, 321, 75);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNewLabel_2_1 = new JLabel("Cheers System");
        lblNewLabel_2_1.setForeground(new Color(100, 0, 0));
        lblNewLabel_2_1.setFont(new Font("Bauhaus 93", Font.PLAIN, 45));
        lblNewLabel_2_1.setBounds(469, 31, 321, 75);
        contentPane.add(lblNewLabel_2_1);

        // Add action listener for the login button
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    /**
     * Handle the login logic.
     */
    private void handleLogin() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());
        
        // Authenticate user using UserControl
        User user = UserControl.getInstance().authenticateUser(username, password);
        
        if (user != null) { 
            // Create the main layout frame with the authenticated user
            RootLayout rt = new RootLayout(user);
            rt.setVisible(true);
            dispose();  // Close the login window
        } else {
            // Login failed: display an error message.
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

}