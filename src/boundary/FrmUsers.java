package boundary;

import entity.User;
import entity.Employee;
import control.UserControl;
import control.PersonControl;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FrmUsers extends JPanel {
    private JPanel sideMenu, contentPanel;
    private CardLayout cardLayout;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ArrayList<User> users;
    private JTextField searchField;
    
    public FrmUsers() {
        // Use JPanel settings instead of JFrame methods
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 550));
        
        // Sidebar Menu
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(105, 20, 56));
        sideMenu.setPreferredSize(new Dimension(200, 0));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        JLabel lblTitle = new JLabel("Select an Operation", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(217, 166, 165));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        
        JButton btnView = createMenuButton("View Users", "📋");
        JButton btnUpdate = createMenuButton("Update User", "✏️");
        JButton btnRemove = createMenuButton("Delete User", "❌");
        
        sideMenu.add(lblTitle);
        sideMenu.add(Box.createVerticalStrut(20));
        sideMenu.add(btnView);
        sideMenu.add(btnUpdate);
        sideMenu.add(btnRemove);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(69, 16, 32));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(null);
        
        JLabel lblManage = new JLabel("  Manage Users", SwingConstants.LEFT);
        lblManage.setBounds(52, 0, 202, 80);
        lblManage.setForeground(new Color(234, 171, 55));
        lblManage.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        headerPanel.add(lblManage);
        
        searchField = new JTextField(15);
        searchField.setBackground(new Color(217, 166, 165));
        searchField.setBounds(403, 41, 184, 28);
        headerPanel.add(searchField);
        
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(194, 65, 78));
        btnSearch.setBorder(null);
        btnSearch.setBounds(592, 41, 98, 26);
        headerPanel.add(btnSearch);
        btnSearch.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                filterUserTable(searchText);
            } else {
                refreshUserTable();
            }
        });
        
        // Content Panel with CardLayout (for future extensions)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // User Table Panel
        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{"User ID", "Employee ID", "Employee Name", "Username", "Password", "Role"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow inline editing for Username, Password, and Role
                return column >= 3;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionBackground(new Color(0, 128, 128));
        userTable.setBackground(new Color(105, 20, 56));
        userTable.setForeground(new Color(234, 171, 55));
        userTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        userTable.setRowHeight(20);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getColumnModel().getColumn(0).setPreferredWidth(20);  // User ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(20);  // Employee ID
        userTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Employee Name
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Username
        userTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Password
        userTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Role
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBackground(new Color(202, 167, 75));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(viewPanel, "View");
        
        refreshUserTable();
        
        // Side menu actions
        btnView.addActionListener(e -> {
            refreshUserTable();
            cardLayout.show(contentPanel, "View");
        });
        
        btnUpdate.addActionListener(e -> {
            if (userTable.isEditing()) {
                userTable.getCellEditor().stopCellEditing();
            }
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                int employeeId = (int) tableModel.getValueAt(selectedRow, 1);
                String username = (String) tableModel.getValueAt(selectedRow, 3);
                String password = (String) tableModel.getValueAt(selectedRow, 4);
                String role = (String) tableModel.getValueAt(selectedRow, 5);
                User updatedUser = new User(userId, employeeId, username, password, role);
                boolean success = UserControl.getInstance().updateUser(updatedUser);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User updated successfully!");
                    refreshUserTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating user.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to update.");
            }
        });
        
        btnRemove.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    boolean success = UserControl.getInstance().deleteUser(userId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "User removed successfully!");
                        refreshUserTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error removing user.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to remove.");
            }
        });
        
        add(sideMenu, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JButton createMenuButton(String text, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setMaximumSize(new Dimension(180, 60));
        button.setBackground(new Color(105, 20, 56));
        button.setForeground(new Color(235, 174, 63));
        button.setFocusPainted(false);
        return button;
    }
    
    private void refreshUserTable() {
        users = UserControl.getInstance().getAllUsers();
        tableModel.setRowCount(0);
        for (User u : users) {
            Employee emp = PersonControl.getInstance().getEmployeeById(u.getEmployeeId());
            String empName = (emp != null) ? emp.getName() : "";
            tableModel.addRow(new Object[]{
                u.getUserId(), 
                u.getEmployeeId(), 
                empName, 
                u.getUsername(), 
                u.getPassword(), 
                u.getUserRole()
            });
        }
    }
    
    private void filterUserTable(String search) {
        tableModel.setRowCount(0);
        for (User u : users) {
            Employee emp = PersonControl.getInstance().getEmployeeById(u.getEmployeeId());
            String empName = (emp != null) ? emp.getName() : "";
            if (u.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                empName.toLowerCase().contains(search.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    u.getUserId(), 
                    u.getEmployeeId(), 
                    empName, 
                    u.getUsername(), 
                    u.getPassword(), 
                    u.getUserRole()
                });
            }
        }
    }
}
