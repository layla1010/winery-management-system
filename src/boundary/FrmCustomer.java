package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import control.PersonControl;
import entity.Customer;

public class FrmCustomer extends JPanel {
    private JPanel sideMenu, contentPanel;
    private CardLayout cardLayout;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private ArrayList<Customer> customers;
    private JTextField searchField;
    private String user;
    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    public FrmCustomer(String Role) {
    	this.user=Role;
    	
        setSize(900, 550);
       
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 550));
        // Create Sidebar Menu (Dark Purple)
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(105, 20, 56)); // Dark Purple
        sideMenu.setPreferredSize(new Dimension(200, 0));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("Perform an Operation", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(217, 166, 165));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));

        JButton btnView = createMenuButton("View Customers", "📋");
         btnAdd = createMenuButton("Add Customer", "➕");
         btnUpdate = createMenuButton("Update Customer", "✏️");
         btnRemove = createMenuButton("Remove Customer", "❌");

        // Consolidated update listener for btnUpdate
        btnUpdate.addActionListener(e -> {
            // Commit any ongoing edits before processing the update
            if (customerTable.isEditing()) {
                customerTable.getCellEditor().stopCellEditing();
            }
            
            int selectedRow = customerTable.getSelectedRow();
            // If no row is selected but a cell is being edited, force selection.
            if (selectedRow == -1 && customerTable.getEditingRow() != -1) {
                selectedRow = customerTable.getEditingRow();
                customerTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
            
            // Debug: Print the selected row index.
            System.out.println("Selected Row: " + selectedRow);
            
            if (selectedRow != -1) {
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                String phone = (String) tableModel.getValueAt(selectedRow, 2);
                String address = (String) tableModel.getValueAt(selectedRow, 3);
                String email = (String) tableModel.getValueAt(selectedRow, 4);
                
                // Retrieve the date value safely
                Object dateObj = tableModel.getValueAt(selectedRow, 5);
                java.util.Date existingDate = null;
                if (dateObj instanceof java.util.Date) {
                    existingDate = (java.util.Date) dateObj;
                } else {
                    System.out.println("Date column data issue: " + dateObj);
                }
                
                Customer updatedCustomer = new Customer(customerId, name, phone, address, email, existingDate);
                
                // Update the customer using PersonControl
                if (PersonControl.getInstance().updateCustomer(updatedCustomer)) {
                    JOptionPane.showMessageDialog(this, "Customer updated successfully!");
                    refreshCustomerTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating customer.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to update.");
            }
        });

        sideMenu.add(lblTitle);
        sideMenu.add(Box.createVerticalStrut(20));
        sideMenu.add(btnView);
        sideMenu.add(btnAdd);
        sideMenu.add(btnUpdate);
        sideMenu.add(btnRemove);

        // Create Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Header Panel (Purple Background)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(69, 16, 32)); // Purple Theme
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(null);
        
        JLabel lblManage = new JLabel("  Manage Customers", SwingConstants.LEFT);
        lblManage.setBounds(52, 0, 202, 80);
        lblManage.setForeground(new Color(234, 171, 55));
        lblManage.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        headerPanel.add(lblManage);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBounds(700, 0, 184, 80);
        searchPanel.setOpaque(false);
        headerPanel.add(searchPanel);

        // Search field and button
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
                filterCustomerTable(searchText);
            } else {
                refreshCustomerTable();
            }
        });

        // View Customers Panel (JTable)
        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[] {
            "ID", "Name", "Phone", "Address", "Email", "First Contact"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Allow editing all columns except "ID"
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setSelectionBackground(new Color(69, 16, 32));
        customerTable.setBackground(new Color(105, 20, 56));
        customerTable.setForeground(new Color(234, 171, 55));
        customerTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        customerTable.setRowHeight(20);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // Add mouse listener to force row selection on click
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    customerTable.setRowSelectionInterval(row, row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBackground(new Color(69, 16, 32));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Panels to Content Panel
        contentPanel.add(viewPanel, "View");
        FrmAddCustomer addCustomerPanel = new FrmAddCustomer();
        contentPanel.add(addCustomerPanel, "AddCustomer");

        // Button actions
        btnAdd.addActionListener(e -> cardLayout.show(contentPanel, "AddCustomer"));
        btnView.addActionListener(e -> {
            refreshCustomerTable();
            cardLayout.show(contentPanel, "View");
        });
        btnRemove.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                if (PersonControl.getInstance().deleteCustomer(customerId)) {
                    JOptionPane.showMessageDialog(this, "Customer removed successfully!");
                    refreshCustomerTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error removing customer.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to remove.");
            }
        });

        // Add Components to Frame
        add(sideMenu, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        cardLayout.show(contentPanel, "View");
        refreshCustomerTable();
        setVisible(true);
        customizeForRole();
    }

    // Creates a sidebar button with adjusted height
    private JButton createMenuButton(String text, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setMaximumSize(new Dimension(180, 60));
        button.setBackground(new Color(105, 20, 56));
        button.setForeground(new Color(235, 174, 63));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshCustomerTable() {
        customers = PersonControl.getInstance().getCustomers();
        tableModel.setRowCount(0);
        for (Customer c : customers) {
            tableModel.addRow(new Object[] {
                c.getCustomerID(), c.getName(), c.getPhoneNumber(),
                c.getDeliveryAddress(), c.getEmail(), c.getFirstContactDate()
            });
        }
    }

    private void filterCustomerTable(String search) {
        tableModel.setRowCount(0); // Clear the table before adding filtered results
        for (Customer c : customers) {
            if (c.getName().toLowerCase().contains(search.toLowerCase()) ||
                c.getPhoneNumber().toLowerCase().contains(search.toLowerCase()) ||
                c.getEmail().toLowerCase().contains(search.toLowerCase()) ||
                c.getDeliveryAddress().toLowerCase().contains(search.toLowerCase())) {
                tableModel.addRow(new Object[] {
                    c.getCustomerID(), c.getName(), c.getPhoneNumber(),
                    c.getDeliveryAddress(), c.getEmail(), c.getFirstContactDate()
                });
            }
        }
    }
    private void customizeForRole() {
        if("Marketing".equalsIgnoreCase(user)) {
        	  btnAdd.setEnabled(false);
              btnUpdate.setEnabled(false);
              btnRemove.setEnabled(false);
        }
        
            
        }
    }
   

