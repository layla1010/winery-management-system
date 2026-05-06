package boundary;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import control.OrderControl;
import control.PersonControl;
import entity.Employee;
import entity.Order;
import entity.Customer;
import entity.RegularOrder;
import enums.CurrentStatus;

public class FrmAllOrders extends JPanel {

    // UI Components for the orders table (big table)
    private JLabel lblTitle;
    private JSeparator separator;
    private JPanel pnlOperations, pnlAddOrder;
    private JButton btnDeleteOrder;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JScrollPane scrollPane;
    private JButton btnUpdateOrder;
    
    // Control for shipment/expected delivery date (for updating order)
    private JDateChooser dcShipmentDate;
    
    // Text field for entering a new Employee ID
    private JTextField tfEmployeeID;
    
    // Row sorter for search functionality on the orders table.
    private TableRowSorter<DefaultTableModel> ordersRowSorter;
    
    // Panels for displaying order details and employee information
    private JPanel pnlDetails;
    private JTextField tfTotalItems;
    private JTextField tfPrice;
    private JTextField tfTotalCustomers;
    private JLabel lblTotalItems;
    private JLabel lblTotalPrice;
    private JLabel lblTotalCustomers;
    
    private JPanel pnlEmployeeDetails;
    private JLabel lblId;
    private JTextField tfID;
    private JLabel lblName;
    private JTextField tfName;
    private JLabel lblPhone;
    private JTextField tfPhone;
    private JLabel lblAddress;
    private JTextField tfAddress;
    private JLabel lblEmail;
    private JTextField tfEmail;
    private JButton btnContact;
    
    // Search field and refresh button in the title panel.
    private JLabel lblSearch;
    private JTextField textField;
    private JButton btnRefresh;
    
    // Flag to avoid firing events when programmatically updating controls.
    private boolean updatingCombos = false;

    public FrmAllOrders() {
        // Set the preferred size and layout for this panel
        setPreferredSize(new Dimension(900, 600));
        setBackground(new Color(105, 20, 56));
        setLayout(null);

        initComponents();
        populateOrdersTable(); // Load orders on startup
        attachOrderSelectionListener();
    }

    private void initComponents() {
        // ----- Title Panel -----
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(69, 16, 32));
        titlePanel.setBounds(0, 0, 878, 33);
        titlePanel.setLayout(null);
        lblTitle = new JLabel("Manage Orders", SwingConstants.LEFT);
        lblTitle.setBounds(0, 0, 211, 33);
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        titlePanel.add(lblTitle);
        add(titlePanel);
        
        // ----- Search Components in Title Panel -----
        lblSearch = new JLabel("Search:");
        lblSearch.setForeground(new Color(234, 171, 55));
        lblSearch.setBounds(586, 12, 55, 14);
        titlePanel.add(lblSearch);
        
        textField = new JTextField(15);
        textField.setText("");
        textField.setBackground(new Color(217, 166, 165));
        textField.setBounds(636, 9, 126, 20);
        titlePanel.add(textField);
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(194, 65, 78));
        btnRefresh.setBounds(778, 8, 90, 23);
        titlePanel.add(btnRefresh);
        
        // Add key listener to search field so that it filters the orders table.
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textField.getText().trim();
                if (text.length() == 0) {
                    ordersRowSorter.setRowFilter(null);
                } else {
                    ordersRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        // Attach action listener to refresh button.
        btnRefresh.addActionListener(e -> {
            textField.setText("");
            ordersRowSorter.setRowFilter(null);
            populateOrdersTable();
        });

        // Separator
        separator = new JSeparator();
        separator.setBounds(0, 33, 878, 2);
        add(separator);

        // ----- Right Panel for Order Actions, Details, and Employee Info -----
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(105, 20, 56));
        rightPanel.setBounds(518, 43, 360, 517);
        rightPanel.setLayout(null);
        add(rightPanel);

        // ----- Order Actions Panel -----
        JPanel pnlOperations = new JPanel();
        pnlOperations.setBackground(new Color(69, 16, 32));
        pnlOperations.setBounds(0, 0, 350, 211);
        pnlOperations.setBorder(BorderFactory.createTitledBorder("Order Actions"));
        ((TitledBorder) pnlOperations.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlOperations.setLayout(null);

      
        // Shipment date chooser for updating the order’s ship date
        dcShipmentDate = new JDateChooser();
        dcShipmentDate.setBounds(109, 50, 221, 29);
        // Shipment date chooser for updating the order’s ship date
        dcShipmentDate = new JDateChooser();
        dcShipmentDate.setBackground(new Color(217, 166, 165));
        dcShipmentDate.setBounds(109, 47, 221, 29);

        pnlOperations.add(dcShipmentDate);
        JLabel lblShipDate = new JLabel("Ship Date");
        lblShipDate.setForeground(new Color(234, 171, 55));
        lblShipDate.setBounds(20, 62, 79, 14);
        pnlOperations.add(lblShipDate);

        // Text field for entering new Employee ID
        tfEmployeeID = new JTextField();
        tfEmployeeID.setBackground(new Color(217, 166, 165));
        tfEmployeeID.setBounds(109, 116, 221, 29);
        pnlOperations.add(tfEmployeeID);
        JLabel lblEmployeeID = new JLabel("Emp ID");
        lblEmployeeID.setForeground(new Color(234, 171, 55));
        lblEmployeeID.setBounds(20, 121, 59, 14);
        pnlOperations.add(lblEmployeeID);

        // Order action buttons: Delete and Update
        btnDeleteOrder = new JButton("Delete Order");
        btnDeleteOrder.setBackground(new Color(194, 65, 78));
        btnDeleteOrder.setBounds(20, 171, 150, 29);
        pnlOperations.add(btnDeleteOrder);
        btnUpdateOrder = new JButton("Update Order");
        btnUpdateOrder.setBackground(new Color(194, 65, 78));
        btnUpdateOrder.setBounds(180, 171, 150, 29);
        pnlOperations.add(btnUpdateOrder);
        rightPanel.add(pnlOperations);

        // ----- Add Panel: Both Add buttons open FrmAddOrder -----
        pnlAddOrder = new JPanel();
        pnlAddOrder.setBounds(0, 234, 350, 87);
        pnlAddOrder.setBackground(new Color(105, 20, 56));
        pnlAddOrder.setBorder(BorderFactory.createTitledBorder("Add Orders"));
        ((TitledBorder) pnlAddOrder.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlAddOrder.setLayout(null);
        rightPanel.add(pnlAddOrder);
        
        JButton btnAddUrgentOrder = new JButton("Add Urgent Order");
        btnAddUrgentOrder.setBounds(181, 24, 159, 39);
        btnAddUrgentOrder.setBackground(new Color(194, 65, 78));
        pnlAddOrder.add(btnAddUrgentOrder);
        btnAddUrgentOrder.addActionListener(e -> {
            // Open FrmAddOrder JFrame
            new FrmAddUrgentOrder().setVisible(true);
        });
        
        JButton btnAddRegularOrder = new JButton("Add Regular Order");
        btnAddRegularOrder.setBackground(new Color(194, 65, 78));
        btnAddRegularOrder.setBounds(10, 24, 159, 39);
        pnlAddOrder.add(btnAddRegularOrder);
        btnAddRegularOrder.addActionListener(e -> {
            // Open FrmAddOrder JFrame
            new FrmAddOrder().setVisible(true);
        });
        
        // ----- Details Panel: Shows order detail totals -----
        pnlDetails = new JPanel();
        pnlDetails.setLayout(null);
        pnlDetails.setBorder(BorderFactory.createTitledBorder("Order Details"));
        ((TitledBorder) pnlDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlDetails.setBackground(new Color(69, 16, 32));
        pnlDetails.setBounds(0, 332, 350, 174);
        rightPanel.add(pnlDetails);
        
        tfTotalItems = new JTextField();
        tfTotalItems.setBackground(new Color(217, 166, 165));
        tfTotalItems.setEditable(false);
        tfTotalItems.setBounds(177, 31, 150, 20);
        pnlDetails.add(tfTotalItems);
        
        tfPrice = new JTextField();
        tfPrice.setBackground(new Color(217, 166, 165));
        tfPrice.setEditable(false);
        tfPrice.setBounds(177, 72, 150, 20);
        pnlDetails.add(tfPrice);
        
        tfTotalCustomers = new JTextField();
        tfTotalCustomers.setBackground(new Color(217, 166, 165));
        tfTotalCustomers.setEditable(false);
        tfTotalCustomers.setBounds(177, 110, 150, 20);
        pnlDetails.add(tfTotalCustomers);
        
        lblTotalItems = new JLabel("Total Items");
        lblTotalItems.setForeground(new Color(234, 171, 55));
        lblTotalItems.setBounds(33, 37, 79, 14);
        pnlDetails.add(lblTotalItems);
        
        lblTotalPrice = new JLabel("Total Price");
        lblTotalPrice.setForeground(new Color(234, 171, 55));
        lblTotalPrice.setBounds(33, 78, 79, 14);
        pnlDetails.add(lblTotalPrice);
        
        lblTotalCustomers = new JLabel("Total Customers");
        lblTotalCustomers.setForeground(new Color(234, 171, 55));
        lblTotalCustomers.setBounds(33, 116, 79, 14);
        pnlDetails.add(lblTotalCustomers);

        // ----- Employee Details Panel -----
        pnlEmployeeDetails = new JPanel();
        pnlEmployeeDetails.setLayout(null);
        pnlEmployeeDetails.setBorder(BorderFactory.createTitledBorder("Assigned Employee Details"));
        ((TitledBorder) pnlEmployeeDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlEmployeeDetails.setBackground(new Color(69, 16, 32));
        pnlEmployeeDetails.setBounds(0, 374, 498, 186);
        add(pnlEmployeeDetails);
        
        lblId = new JLabel("ID:");
        lblId.setForeground(new Color(234, 171, 55));
        lblId.setBounds(20, 30, 50, 14);
        pnlEmployeeDetails.add(lblId);
        
        tfID = new JTextField();
        tfID.setBackground(new Color(217, 166, 165));
        tfID.setEditable(false);
        tfID.setBounds(80, 27, 70, 20);
        pnlEmployeeDetails.add(tfID);
        
        lblName = new JLabel("Name:");
        lblName.setForeground(new Color(234, 171, 55));
        lblName.setBounds(20, 60, 50, 14);
        pnlEmployeeDetails.add(lblName);
        
        tfName = new JTextField();
        tfName.setBackground(new Color(217, 166, 165));
        tfName.setEditable(false);
        tfName.setBounds(80, 57, 150, 20);
        pnlEmployeeDetails.add(tfName);
        
        lblPhone = new JLabel("Phone:");
        lblPhone.setForeground(new Color(234, 171, 55));
        lblPhone.setBounds(20, 90, 50, 14);
        pnlEmployeeDetails.add(lblPhone);
        
        tfPhone = new JTextField();
        tfPhone.setBackground(new Color(217, 166, 165));
        tfPhone.setEditable(false);
        tfPhone.setBounds(80, 87, 150, 20);
        pnlEmployeeDetails.add(tfPhone);
        
        lblAddress = new JLabel("Address:");
        lblAddress.setForeground(new Color(234, 171, 55));
        lblAddress.setBounds(250, 60, 60, 14);
        pnlEmployeeDetails.add(lblAddress);
        
        tfAddress = new JTextField();
        tfAddress.setBackground(new Color(217, 166, 165));
        tfAddress.setEditable(false);
        tfAddress.setBounds(320, 57, 150, 20);
        pnlEmployeeDetails.add(tfAddress);
        
        lblEmail = new JLabel("Email:");
        lblEmail.setForeground(new Color(234, 171, 55));
        lblEmail.setBounds(250, 90, 50, 14);
        pnlEmployeeDetails.add(lblEmail);
        
        tfEmail = new JTextField();
        tfEmail.setBackground(new Color(217, 166, 165));
        tfEmail.setEditable(false);
        tfEmail.setBounds(320, 87, 150, 20);
        pnlEmployeeDetails.add(tfEmail);
        
        btnContact = new JButton("Contact");
        btnContact.setBackground(new Color(194, 65, 78));
        btnContact.setBounds(341, 139, 129, 32);
        pnlEmployeeDetails.add(btnContact);

        // ----- Orders Table (big table) -----
        String[] columnNames = {"OrderID", "Employee", "Order Date", "Ship Date", "Status"};
        ordersTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setBackground(new Color(69, 16, 32));
        ordersTable.setForeground(new Color(234, 171, 55));
        ordersTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBounds(10, 43, 498, 314);
        scrollPane.setBackground(new Color(202, 167, 75));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        add(scrollPane);
        
        // Set up row sorter for orders table.
        ordersRowSorter = new TableRowSorter<>(ordersTableModel);
        ordersTable.setRowSorter(ordersRowSorter);

        // Big table button actions
        btnDeleteOrder.addActionListener(e -> deleteSelectedOrder());
        btnUpdateOrder.addActionListener(e -> updateSelectedOrder());
        
        // Wire the Contact button to open the email client.
        btnContact.addActionListener(e -> {
            try {
                String email = tfEmail.getText();
                if (email != null && !email.isEmpty()) {
                    Desktop.getDesktop().mail(new URI("mailto:" + email));
                } else {
                    JOptionPane.showMessageDialog(this, "No email address provided.");
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening email client.");
            }
        });
    }
    
    // Populate the orders table using OrderControl.getOrders()
    private void populateOrdersTable() {
        ordersTableModel.setRowCount(0);
        ArrayList<Order> orders = OrderControl.getInstance().getOrders();
        for (Order order : orders) {
            String employeeName = (order.getEmployee() != null) ? order.getEmployee().getName() : "N/A";
            ordersTableModel.addRow(new Object[]{
                order.getOrder_ID(),
                employeeName,
                order.getOrder_Date(),
                order.getShipment_Date(),
                order.getCurrentStatus()
            });
        }
    }

    // Attach a selection listener to load order details and employee info when a row is selected.
    private void attachOrderSelectionListener() {
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    Order order = OrderControl.getInstance().getOrderByID(orderId);
                    if (order != null) {
                        updatingCombos = true;
                        dcShipmentDate.setDate(order.getShipment_Date());
                        if (order.getEmployee() != null)
                            tfEmployeeID.setText(String.valueOf(order.getEmployee().getEmployeeID()));
                        else
                            tfEmployeeID.setText("");
                        // Load employee details
                        loadEmployeeDetails(order.getEmployee());
                        // Load order detail totals (aggregates from both regular and urgent details)
                        loadOrderDetailsForSelectedOrder(order);
                        updatingCombos = false;
                    }
                }
            }
        });
    }

    // Loads order details (totals) into pnlDetails based on the selected order.
    private void loadOrderDetailsForSelectedOrder(Order order) {
        // Retrieve detail records from both regular and urgent tables.
        ArrayList<Object[]> regDetails = OrderControl.getInstance().getRegularOrderDetails(order.getOrder_ID());
        ArrayList<Object[]> urgDetails = OrderControl.getInstance().getUrgentOrderDetails(order.getOrder_ID());
        
        ArrayList<Object[]> details = new ArrayList<>();
        if(regDetails != null) {
            details.addAll(regDetails);
        }
        if(urgDetails != null) {
            details.addAll(urgDetails);
        }
        
        int totalItems = 0;
        double totalPrice = 0.0;
        Set<Integer> uniqueCustomers = new HashSet<>();
        
        for (Object[] row : details) {
            try {
                // Expected row: {OrderID, CustomerID, WineID, Quantity, Price}
                int quantity = (Integer) row[3];
                double price = (Double) row[4];
                int custId = (Integer) row[1];
                totalItems += quantity;
                totalPrice += price;
                uniqueCustomers.add(custId);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        tfTotalItems.setText(String.valueOf(totalItems));
        tfPrice.setText(String.format("%.2f", totalPrice));
        tfTotalCustomers.setText(String.valueOf(uniqueCustomers.size()));
    }

    // Loads employee details into pnlEmployeeDetails.
    private void loadEmployeeDetails(Employee employee) {
        if (employee != null) {
            tfID.setText(String.valueOf(employee.getEmployeeID()));
            tfName.setText(employee.getName());
            tfPhone.setText(employee.getPhoneNumber());
            tfAddress.setText(employee.getOfficeAddress());
            tfEmail.setText(employee.getEmail());
        } else {
            tfID.setText("");
            tfName.setText("");
            tfPhone.setText("");
            tfAddress.setText("");
            tfEmail.setText("");
        }
    }

    // Deletes the selected order.
    private void deleteSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete order " + orderId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = OrderControl.getInstance().deleteOrder(orderId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Order deleted successfully.");
                populateOrdersTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting order.");
            }
        }
    }

    // Updates the selected order with new values from the controls.
    private void updateSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
        Order order = OrderControl.getInstance().getOrderByID(orderId);
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        // Read new values from controls.
        
        Date newShipDate = dcShipmentDate.getDate();
        int newEmpId;
        try {
            newEmpId = Integer.parseInt(tfEmployeeID.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID. Please enter a valid number.");
            return;
        }
        // Retrieve the employee using the provided ID.
        Employee newEmployee = PersonControl.getInstance().getEmployeeById(newEmpId);
        if (newEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }
        // Update the order object.
        
        order.setShipment_Date(newShipDate);
        order.setEmployee_ID(newEmployee);
        
        // Call the update method in OrderControl (ensure it is implemented to update TblOrder correctly)
        boolean updated = OrderControl.getInstance().updateOrder(order);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Order updated successfully.");
            populateOrdersTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating order.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new FrmAllOrders());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
