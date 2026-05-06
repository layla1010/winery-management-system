package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import control.PersonControl;
import entity.Employee;

public class FrmEmployee extends JPanel {  // Change extends RootLayout to extends JPanel
    private JPanel sideMenu, contentPanel;
    private CardLayout cardLayout;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private ArrayList<Employee> employees;
    private JTextField searchField;

    // Remove the main method from FrmEmployee

    public FrmEmployee() {
        // Initialize this panel instead of setting up a JFrame.
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 550));
        this.setBackground(new Color(69, 16, 32));

        // Sidebar Menu
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(105, 20, 56));
        sideMenu.setPreferredSize(new Dimension(200, 0));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("Perform an Operation", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(217, 166, 165));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));

        JButton btnView = createMenuButton("View Employees", "📋");
        JButton btnAdd = createMenuButton("Add Employee", "➕");
        JButton btnUpdate = createMenuButton("Update Employee", "✏️");
        JButton btnRemove = createMenuButton("Remove Employee", "❌");

        sideMenu.add(lblTitle);
        sideMenu.add(Box.createVerticalStrut(20));
        sideMenu.add(btnView);
        sideMenu.add(btnAdd);
        sideMenu.add(btnUpdate);
        sideMenu.add(btnRemove);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(69, 16, 32));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(null);

        JLabel lblManage = new JLabel("  Manage Employees", SwingConstants.LEFT);
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
                filterEmployeeTable(searchText);
            } else {
                refreshEmployeeTable();
            }
        });

        // Content Panel with CardLayout (if you plan multiple subviews within employee management)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Employee Table Panel
        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Office", "Email", "Start Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionBackground(new Color(194, 65, 78));
        employeeTable.setBackground(new Color(105, 20, 56));
        employeeTable.setForeground(new Color(234, 171, 55));

        employeeTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55)); 
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        employeeTable.setRowHeight(20);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(viewPanel, "View");
        // Add additional panels like FrmAddEmployee as needed:
        FrmAddEmployee addEmployeePanel = new FrmAddEmployee();
        contentPanel.add(addEmployeePanel, "AddEmployee");

        refreshEmployeeTable();

        btnView.addActionListener(e -> {
            refreshEmployeeTable();
            cardLayout.show(contentPanel, "View");
        });

        btnAdd.addActionListener(e -> cardLayout.show(contentPanel, "AddEmployee"));

        btnUpdate.addActionListener(e -> {
            if (employeeTable.isEditing()) {
                employeeTable.getCellEditor().stopCellEditing();
            }
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                String phone = (String) tableModel.getValueAt(selectedRow, 2);
                String office = (String) tableModel.getValueAt(selectedRow, 3);
                String email = (String) tableModel.getValueAt(selectedRow, 4);
                java.util.Date startDate = (java.util.Date) tableModel.getValueAt(selectedRow, 5);
                Employee updatedEmployee = new Employee(employeeId, name, phone, office, email, startDate);
                if (PersonControl.getInstance().updateEmployee(updatedEmployee)) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                    refreshEmployeeTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating employee.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            }
        });

        btnRemove.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
                if (PersonControl.getInstance().deleteEmployee(employeeId)) {
                    JOptionPane.showMessageDialog(this, "Employee removed successfully!");
                    refreshEmployeeTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error removing employee.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to remove.");
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

    private void refreshEmployeeTable() {
        employees = PersonControl.getInstance().getEmployees();
        tableModel.setRowCount(0);
        for (Employee e : employees) {
            tableModel.addRow(new Object[]{e.getEmployeeID(), e.getName(), e.getPhoneNumber(), e.getOfficeAddress(), e.getEmail(), e.getEmployeeStartDate()});
        }
    }

    private void filterEmployeeTable(String search) {
        tableModel.setRowCount(0);
        for (Employee e : employees) {
            if (e.getName().toLowerCase().contains(search.toLowerCase()) ||
                e.getPhoneNumber().contains(search) ||
                e.getEmail().toLowerCase().contains(search.toLowerCase())) {
                tableModel.addRow(new Object[]{e.getEmployeeID(), e.getName(), e.getPhoneNumber(), e.getOfficeAddress(), e.getEmail(), e.getEmployeeStartDate()});
            }
        }
    }
}
