package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import control.OrderControl;
import control.PersonControl;
import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.RegularOrder;
import enums.CurrentStatus;
import enums.Role;

public class FrmRegularOrder extends JPanel {

    // UI Components for the orders table (big table)
    private JLabel lblTitle;
    private JSeparator separator;
    private JPanel pnlOrderDetails, pnlRegularOrdDetails, pnlDelCusInOrder;
    private JButton btnDeleteOrder;
    private JButton btnRefresh;
    private JTextField txtSearch;
    private JLabel lblSearch;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JScrollPane scrollPane;
    private JButton btnUpdateOrder;
 // Declare at the class level (before the constructor)
    private DefaultTableModel customerTableModel;
    private JTable customerTable;

    // UI Components for the details table (small table)
    private JTable detailsTable;
    private DefaultTableModel detailsTableModel;
    private JButton btnAddCusToOrder;
    
    // Combo boxes for status and main customer
    private JComboBox<CurrentStatus> cbStatus;
    private JComboBox<Customer> cbMainCustomer;
    
    // Flag to avoid firing events when programmatically updating combo boxes.
    private boolean updatingCombos = false;

    public FrmRegularOrder() {
        // Set the preferred size and layout for this panel
        setPreferredSize(new Dimension(900, 600));
        setBackground(new Color(105, 20, 56));
        setLayout(null);

        initComponents();
        populateOrdersTable(); // Load orders on startup
    }

    private void initComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(69, 16, 32));
        titlePanel.setBounds(0, 0, 878, 33);
        lblTitle = new JLabel("Manage Regular Orders", SwingConstants.LEFT);
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(69, 16, 32));
        lblSearch = new JLabel("Search:");
        lblSearch.setForeground(new Color(234, 171, 55));
        searchPanel.add(lblSearch);
        txtSearch = new JTextField(15);
        txtSearch.setBackground(new Color(217, 166, 165));
        searchPanel.add(txtSearch);
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(194, 65, 78));
        btnRefresh.addActionListener(e -> populateOrdersTable());
        searchPanel.add(btnRefresh);
        titlePanel.add(searchPanel, BorderLayout.EAST);
        add(titlePanel);

        // Separator
        separator = new JSeparator();
        separator.setBounds(0, 33, 847, 2);
        add(separator);

        // Right Panel for order actions and detail actions
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(105, 20, 56));
        rightPanel.setBounds(518, 43, 360, 517);
        rightPanel.setLayout(null);
        add(rightPanel);

        // Order Details Panel
        pnlOrderDetails = new JPanel();
        pnlOrderDetails.setBackground(new Color(69, 16, 32));
        pnlOrderDetails.setBounds(0, 0, 360, 137);
        pnlOrderDetails.setBorder(BorderFactory.createTitledBorder("Order Actions"));
        ((TitledBorder) pnlOrderDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlOrderDetails.setLayout(null);

        // Status combo box (populated at initialization)
        cbStatus = new JComboBox<>();
        cbStatus.setBackground(new Color(217, 166, 165));
        for (CurrentStatus s : CurrentStatus.values()) {
            cbStatus.addItem(s);
        }
        cbStatus.setBounds(109, 23, 221, 29);
        pnlOrderDetails.add(cbStatus);
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setForeground(new Color(234, 171, 55));
        lblStatus.setBounds(20, 30, 59, 14);
        pnlOrderDetails.add(lblStatus);

        // Main Customer combo box
        cbMainCustomer = new JComboBox<>();
        cbMainCustomer.setBackground(new Color(217, 166, 165));
        cbMainCustomer.setBounds(109, 63, 221, 23);
        pnlOrderDetails.add(cbMainCustomer);
        JLabel lblMainCustomer = new JLabel("Main Customer");
        lblMainCustomer.setForeground(new Color(234, 171, 55));
        lblMainCustomer.setBounds(20, 67, 79, 14);
        pnlOrderDetails.add(lblMainCustomer);

        // Order action buttons
        btnDeleteOrder = new JButton("Delete Order");
        btnDeleteOrder.setBackground(new Color(194, 65, 78));
        btnDeleteOrder.setBounds(10, 97, 162, 29);
        pnlOrderDetails.add(btnDeleteOrder);
        btnUpdateOrder = new JButton("Update Order");
        btnUpdateOrder.setBackground(new Color(194, 65, 78));
        btnUpdateOrder.setBounds(188, 97, 162, 29);
        pnlOrderDetails.add(btnUpdateOrder);
        rightPanel.add(pnlOrderDetails);

        // Detail Actions Panel
        pnlDelCusInOrder = new JPanel();
        pnlDelCusInOrder.setBackground(new Color(105, 20, 56));
        pnlDelCusInOrder.setBounds(162, 444, 198, 73);
        pnlDelCusInOrder.setBorder(BorderFactory.createTitledBorder("Detail Actions"));
        ((TitledBorder) pnlDelCusInOrder.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlDelCusInOrder.setLayout(null);
        rightPanel.add(pnlDelCusInOrder);

        JButton btnDeleteCustomer = new JButton("Customer");
        btnDeleteCustomer.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnDeleteCustomer.setBackground(new Color(194, 65, 78));
        btnDeleteCustomer.setBounds(10, 21, 79, 23);
        pnlDelCusInOrder.add(btnDeleteCustomer);

        JButton btnDeleteQuantity = new JButton("Quantity");
        btnDeleteQuantity.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnDeleteQuantity.setBackground(new Color(194, 65, 78));
        btnDeleteQuantity.setBounds(99, 21, 79, 23);
        pnlDelCusInOrder.add(btnDeleteQuantity);

        JPanel pnlUpdCusInOrder = new JPanel();
        pnlUpdCusInOrder.setLayout(null);
        pnlUpdCusInOrder.setBorder(BorderFactory.createTitledBorder("Update Detail"));
        pnlUpdCusInOrder.setBackground(new Color(105, 20, 56));
        pnlUpdCusInOrder.setBounds(0, 444, 162, 73);
        rightPanel.add(pnlUpdCusInOrder);

        JButton btnUpdateDetail = new JButton("Update");
        btnUpdateDetail.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnUpdateDetail.setBackground(new Color(194, 65, 78));
        btnUpdateDetail.setBounds(10, 21, 67, 23);
        pnlUpdCusInOrder.add(btnUpdateDetail);
  

        btnAddCusToOrder = new JButton("Add");
        btnAddCusToOrder.setBackground(new Color(194, 65, 78));
        btnAddCusToOrder.setBounds(85, 21, 67, 23);
        btnAddCusToOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Please select an order first.");
                    return;
                }
                int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);

                // 🔍 Ask for Customer ID
                String customerIdStr = JOptionPane.showInputDialog(FrmRegularOrder.this, "Enter Customer ID to add:");
                if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
                    return;
                }
                int customerId;
                try {
                    customerId = Integer.parseInt(customerIdStr.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Invalid Customer ID.");
                    return;
                }

                // 🔍 Ask for Wine ID
                String wineIdStr = JOptionPane.showInputDialog(FrmRegularOrder.this, "Enter Wine ID:");
                if (wineIdStr == null || wineIdStr.trim().isEmpty()) {
                    return;
                }
                int wineId;
                try {
                    wineId = Integer.parseInt(wineIdStr.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Invalid Wine ID.");
                    return;
                }

                // 🔍 Ask for Quantity
                String quantityStr = JOptionPane.showInputDialog(FrmRegularOrder.this, "Enter Quantity:");
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    return;
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Invalid Quantity.");
                    return;
                }

                // 📌 Retrieve Price Per Bottle
                double pricePerBottle = OrderControl.getInstance().getPricePerBottle(wineId);
                if (pricePerBottle == 0.0) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Unable to retrieve price for Wine ID: " + wineId);
                    return;
                }
                double totalPrice = quantity * pricePerBottle;

                // 📝 **Call the main method to handle everything in one transaction**
                boolean success = OrderControl.getInstance().addCustomerToRegularOrder(orderId, customerId, wineId, quantity, totalPrice);

                if (success) {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Customer added successfully to order.");
                } else {
                    JOptionPane.showMessageDialog(FrmRegularOrder.this, "Error adding order detail.");
                }

                // 🔄 Refresh order details
                loadOrderDetailsForSelectedOrder();
            }
        });

        pnlUpdCusInOrder.add(btnAddCusToOrder);

        // Regular Order Details Panel (Small table)
        pnlRegularOrdDetails = new JPanel();
        pnlRegularOrdDetails.setBounds(0, 145, 360, 288);
        pnlRegularOrdDetails.setBackground(new Color(69, 16, 32));
        pnlRegularOrdDetails.setBorder(BorderFactory.createTitledBorder("Order Details"));
        ((TitledBorder) pnlRegularOrdDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlRegularOrdDetails.setLayout(null);
        rightPanel.add(pnlRegularOrdDetails);

        JScrollPane detailsScrollPane = new JScrollPane();
        detailsScrollPane.setBounds(10, 18, 340, 259);
        pnlRegularOrdDetails.add(detailsScrollPane);

        detailsTableModel = new DefaultTableModel(
            new Object[]{"OrderID", "CustomerID", "WineID", "Quantity", "Price"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        detailsTable = new JTable(detailsTableModel);
        detailsTable.setBackground(new Color(105, 20, 56));
        detailsTable.setForeground(new Color(234, 171, 55));
        detailsTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        
        
        detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 43, 498, 314);
        scrollPane.setBackground(new Color(202, 167, 75));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        detailsScrollPane.setViewportView(detailsTable);
        detailsScrollPane.setBackground(new Color(234, 171, 55));

        String[] columnNames = {"OrderID", "Main Customer", "Order Date", "Status"};
        ordersTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setForeground(new Color(234, 171, 55));
        ordersTable.setBackground(new Color(105, 20, 56));
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
        scrollPane.setBounds(10, 43, 498, 306);
        scrollPane.setBackground(new Color(202, 167, 75));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));

        add(scrollPane);
        
        

        // When an order row is selected, update the combo boxes and load details.
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    RegularOrder regOrder = OrderControl.getInstance().getRegularOrderByID(orderId);
                    if (regOrder != null) {
                        updatingCombos = true;  // disable action listener triggers
                        cbStatus.setSelectedItem(regOrder.getCurrentStatus());
                        cbMainCustomer.removeAllItems();
                        HashMap<Customer, Role> custMap = regOrder.getCustomers();
                        for (Customer c : custMap.keySet()) {
                            cbMainCustomer.addItem(c);
                        }
                        for (Customer c : custMap.keySet()) {
                            if (custMap.get(c) == Role.MAIN) {
                                cbMainCustomer.setSelectedItem(c);
                                break;
                            }
                        }
                        updatingCombos = false;
                    }
                    loadOrderDetailsForSelectedOrder();
                }
            }
        });   // Customer Details Panel
        JPanel pnlCustomerDetails = new JPanel();
        pnlCustomerDetails.setSize(498, 200);
        pnlCustomerDetails.setLocation(10, 360);
        pnlCustomerDetails.setBackground(new Color(69, 16, 32));
        pnlCustomerDetails.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(217,166,165)),
            "Customers Details",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.PLAIN, 11),
            new Color(234,171,55)
        ));
      
        pnlCustomerDetails.setLayout(new BorderLayout());

        // Create Customer Table
        String[] customerColumns = {"Customer ID", "Name", "Phone", "Address", "Email"};
      customerTableModel = new DefaultTableModel(customerColumns, 0);
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadOrderDetailsForSelectedOrder();
                loadCustomersForSelectedOrder();  // Load customer data when an order is selected
            }
        });

        // Add to layout
        GridBagConstraints gbc = new GridBagConstraints();
      
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(pnlCustomerDetails, gbc);
        JTable customerTable = new JTable(customerTableModel);
        
        customerTable.setBackground(new Color(105, 20, 56));
        customerTable.setForeground(new Color(234, 171, 55));
        customerTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBounds(10, 43, 498, 314);
        customerScrollPane.setBackground(new Color(202, 167, 75));
        customerScrollPane.getViewport().setBackground(new Color(69, 16, 32));
        
        
        
        
        pnlCustomerDetails.add(customerScrollPane, BorderLayout.CENTER);

        // When the status combo box selection changes, update order status (unless we are updating programmatically)
        cbStatus.addActionListener(e -> {
            if (updatingCombos) return;
           
        });

        // When the main customer combo box selection changes, update mapping (unless we are updating programmatically)
        cbMainCustomer.addActionListener(e -> {
            if (updatingCombos) return;
           
        });

        // Big table button actions
        btnDeleteOrder.addActionListener(e -> deleteSelectedOrder());
        btnUpdateOrder.addActionListener(e -> updateSelectedOrder());
        // Small panel button actions
        btnDeleteCustomer.addActionListener(e -> deleteCustomerFromDetail());
        btnDeleteQuantity.addActionListener(e -> deleteDetailQuantity());
        btnUpdateDetail.addActionListener(e -> updateDetailQuantity());
    }

    // Populate the orders table with RegularOrders from the control layer.
    private void populateOrdersTable() {
        ordersTableModel.setRowCount(0);
        ArrayList<RegularOrder> orders = OrderControl.getInstance().getRegularOrders();
        for (RegularOrder order : orders) {
            String mainCustomerName = getMainCustomerName(order.getCustomers());
            ordersTableModel.addRow(new Object[]{
                order.getOrder_ID(),
                mainCustomerName,
                order.getOrder_Date(),
                order.getCurrentStatus()
            });
        }
    }

    // Utility: Get the MAIN customer's name from the order's customer-role map.
    private String getMainCustomerName(HashMap<?, Role> customers) {
        for (Object key : customers.keySet()) {
            if (key == null) continue;
            Role role = customers.get(key);
            if (role == Role.MAIN) {
                return ((Customer) key).getName();
            }
        }
        return "N/A";
    }

    // When an order row is selected, load its details into the details table.
    private void loadOrderDetailsForSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        detailsTableModel.setRowCount(0);
        if (selectedRow != -1) {
            int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
            ArrayList<Object[]> details = OrderControl.getInstance().getRegularOrderDetails(orderId);
            for (Object[] row : details) {
                detailsTableModel.addRow(new Object[]{ row[0], row[1], row[2], row[3], row[4] });
            }
        }
    }

    // Big Table: Delete selected order.
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
                detailsTableModel.setRowCount(0);
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting order.");
            }
        }
    }

    private void updateSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
        
        
        // Retrieve the urgent order to check its status.
        RegularOrder regOrder = OrderControl.getInstance().getRegularOrderByID(orderId);
        if (regOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        
        
        // Get the new status and new main customer from the visible combo boxes.
        CurrentStatus newStatus = (CurrentStatus) cbStatus.getSelectedItem();
        Customer newMainCustomer = (Customer) cbMainCustomer.getSelectedItem();
        
        boolean statusUpdated = true;
        
     // If the status has changed, update only the status using the new method.
        if (newStatus != null && newStatus != regOrder.getCurrentStatus()) {
            statusUpdated = OrderControl.getInstance().updateOrderStatusWithInventoryUpdate(orderId, newStatus);
        }
        // Instead of directly updating the main customer (which may cause a duplicate),
        // swap the roles between the current main and the new main customer.
        boolean swapUpdated = true;
        if (newMainCustomer != null) {
            swapUpdated = OrderControl.getInstance().swapMainCustomer(orderId, newMainCustomer.getCustomerID());
        }
        
        if (statusUpdated && swapUpdated) {
            JOptionPane.showMessageDialog(this, "Order updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Error updating order.");
        }
        
        populateOrdersTable();
        loadOrderDetailsForSelectedOrder();
    }


    // Small Panel: Delete customer from selected order detail.
    private void deleteCustomerFromDetail() {
        int selectedDetailRow = detailsTable.getSelectedRow();
        if (selectedDetailRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a detail row.");
            return;
        }
        int selectedOrderRow = ordersTable.getSelectedRow();
        if (selectedOrderRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedOrderRow, 0);
        int customerId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove customer " + customerId + " from order " + orderId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = OrderControl.getInstance().removeCustomerFromRegularOrder(orderId, customerId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer removed successfully.");
                loadOrderDetailsForSelectedOrder();
                populateOrdersTable();
                loadCustomersForSelectedOrder();
            } else {
                JOptionPane.showMessageDialog(this, "Error removing customer.");
            }
        }
    }

    // Small Panel: Delete a detail record.
    private void deleteDetailQuantity() {
        int selectedDetailRow = detailsTable.getSelectedRow();
        if (selectedDetailRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a detail row.");
            return;
        }
        int selectedOrderRow = ordersTable.getSelectedRow();
        if (selectedOrderRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedOrderRow, 0);
     
        // Retrieve the urgent order to check its status.
        RegularOrder regOrder = OrderControl.getInstance().getRegularOrderByID(orderId);
        if (regOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        // Allow deletion only if the order status is PAID.
        if (!regOrder.getCurrentStatus().equals(CurrentStatus.PAID)) {
            JOptionPane.showMessageDialog(this, "Details can be deleted only when the order status is PAID.",
                                          "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int customerId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 1);
        int wineId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 2);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this detail?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = OrderControl.getInstance().deleteRegularOrderDetail(orderId, customerId, wineId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Detail deleted successfully.");
                loadOrderDetailsForSelectedOrder();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting detail.");
            }
        }
    }
    private void updateDetailQuantity() {
        int selectedDetailRow = detailsTable.getSelectedRow();
        if (selectedDetailRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a detail row to update.");
            return;
        }
        int selectedOrderRow = ordersTable.getSelectedRow();
        if (selectedOrderRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first.");
            return;
        }
        
        // If the details table is editing, stop editing to commit changes.
        if (detailsTable.isEditing()) {
            detailsTable.getCellEditor().stopCellEditing();
        }
        
        int orderId = (Integer) ordersTableModel.getValueAt(selectedOrderRow, 0);
        
        // Retrieve the urgent order to check its status.
        RegularOrder regOrder = OrderControl.getInstance().getRegularOrderByID(orderId);
        if (regOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        // Allow deletion only if the order status is PAID.
        if (!regOrder.getCurrentStatus().equals(CurrentStatus.PAID)) {
            JOptionPane.showMessageDialog(this, "Details can be deleted only when the order status is PAID.",
                                          "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int customerId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 1);
        int wineId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 2);
        Object qtyObj = detailsTableModel.getValueAt(selectedDetailRow, 3);
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(qtyObj.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer for the quantity.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to update the quantity to " + newQuantity + "?", 
                "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = OrderControl.getInstance().updateRegularOrderDetail(orderId, customerId, wineId, newQuantity);
            if (success) {
                JOptionPane.showMessageDialog(this, "Quantity updated successfully.");
                loadOrderDetailsForSelectedOrder();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating quantity.");
            }
        }
        
    }
    private void loadCustomersForSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        customerTableModel.setRowCount(0); // Clear existing data
        
        if (selectedRow != -1) {
            int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
            RegularOrder regOrder = OrderControl.getInstance().getRegularOrderByID(orderId);
            
            if (regOrder != null) {
                for (Customer customer : regOrder.getCustomers().keySet()) {
                    customerTableModel.addRow(new Object[]{
                        customer.getCustomerID(),
                        customer.getName(),
                        customer.getPhoneNumber(),
                        customer.getDeliveryAddress(),
                        customer.getEmail()
                    });
                }
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Regular Order Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new FrmRegularOrder());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
