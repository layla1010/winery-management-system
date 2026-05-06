package boundary;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.ArrayList;
import control.OrderControl;
import control.PersonControl;
import entity.Customer;
import entity.UrgentOrder;
import enums.CurrentStatus;

public class FrmUrgentOrder extends JPanel {

    // UI Components for the orders table (big table)
    private JLabel lblTitle;
    private JSeparator separator;
    private JPanel pnlOperations, pnlUrgentOrderDetails;
    private JButton btnDeleteOrder;
    private JButton btnRefresh;
    private JTextField txtSearch;
    private JLabel lblSearch;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JScrollPane scrollPane;
    private JButton btnUpdateOrder;
    
    // UI Components for the details table (small table)
    private JTable detailsTable;
    private DefaultTableModel detailsTableModel;
    private JButton btnAddDetail;
    
    // Combo box for status
    private JComboBox<CurrentStatus> cbStatus;
    
    // Additional components for urgent orders:
    // Date chooser for DeliveryDate and a text field for PriorityLevel
    private JDateChooser dcDeliveryDate;
    private JTextField tfPriorityLevel;
    
    // Flag to avoid firing events when programmatically updating combo boxes.
    private boolean updatingCombos = false;
    
    // --- Customer Details Panel components (created once) ---
    private JPanel pnlCusDetails;
    private JTextField tfCustID, tfCustName, tfCustPhone, tfCustAddress, tfCustEmail;
    private JButton btnContact;
    
    // Row sorter for search functionality on the orders table.
    private TableRowSorter<DefaultTableModel> ordersRowSorter;

    public FrmUrgentOrder() {
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
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(69, 16, 32));
        titlePanel.setBounds(0, 0, 878, 33);
        lblTitle = new JLabel("Manage Urgent Orders", SwingConstants.LEFT);
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        // ----- Search Panel -----
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
        btnRefresh.addActionListener(e -> {
            txtSearch.setText(""); // clear search field
            populateOrdersTable();
        });
        searchPanel.add(btnRefresh);
        titlePanel.add(searchPanel, BorderLayout.EAST);
        add(titlePanel);

        // Add key listener to search field to filter orders table.
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = txtSearch.getText().trim();
                if(text.length() == 0) {
                    ordersRowSorter.setRowFilter(null);
                } else {
                    ordersRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // Separator
        separator = new JSeparator();
        separator.setBounds(0, 33, 878, 2);
        add(separator);

        // ----- Right Panel for order actions and detail actions -----
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(105, 20, 56));
        rightPanel.setBounds(518, 43, 360, 517);
        rightPanel.setLayout(null);
        add(rightPanel);

        // ----- Order Actions Panel -----
        pnlOperations = new JPanel();
        pnlOperations.setBounds(0, 0, 350, 168);
        pnlOperations.setBorder(BorderFactory.createTitledBorder("Order Actions"));
        ((TitledBorder) pnlOperations.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlOperations.setBackground(new Color(69, 16, 32));
        pnlOperations.setLayout(null);

        // Status combo box
        cbStatus = new JComboBox<>();
        cbStatus.setBackground(new Color(217, 166, 165));
        for (CurrentStatus s : CurrentStatus.values()) {
            cbStatus.addItem(s);
        }
        cbStatus.setBounds(109, 23, 221, 29);
        pnlOperations.add(cbStatus);
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setForeground(new Color(234, 171, 55));
        lblStatus.setBounds(20, 30, 59, 14);
        pnlOperations.add(lblStatus);

        // DeliveryDate Label and DateChooser
        JLabel lblDeliveryDate = new JLabel("DeliveryDate");
        lblDeliveryDate.setForeground(new Color(234, 171, 55));
        lblDeliveryDate.setBounds(20, 67, 79, 14);
        pnlOperations.add(lblDeliveryDate);
        dcDeliveryDate = new JDateChooser();
        dcDeliveryDate.setBackground(new Color(217, 166, 165));
        dcDeliveryDate.setBounds(109, 60, 221, 29);
        pnlOperations.add(dcDeliveryDate);

        // PriorityLevel Label and TextField
        JLabel lblPriorityLevel = new JLabel("PriorityLevel");
        lblPriorityLevel.setForeground(new Color(234, 171, 55));
        lblPriorityLevel.setBounds(20, 103, 79, 14);
        pnlOperations.add(lblPriorityLevel);
        tfPriorityLevel = new JTextField();
        tfPriorityLevel.setBackground(new Color(217, 166, 165));
        tfPriorityLevel.setBounds(109, 97, 221, 29);
        tfPriorityLevel.setColumns(10);
        pnlOperations.add(tfPriorityLevel);

        // Order action buttons
        btnDeleteOrder = new JButton("Delete Order");
        btnDeleteOrder.setBackground(new Color(194, 65, 78));
        btnDeleteOrder.setBounds(20, 128, 150, 29);
        pnlOperations.add(btnDeleteOrder);
        btnUpdateOrder = new JButton("Update Order");
        btnUpdateOrder.setBackground(new Color(194, 65, 78));
        btnUpdateOrder.setBounds(190, 128, 150, 29);
        pnlOperations.add(btnUpdateOrder);
        rightPanel.add(pnlOperations);

        // ----- Detail Actions Panel -----
        JPanel pnlUrgOrdDetailsOperations = new JPanel();
        pnlUrgOrdDetailsOperations.setLayout(null);
        pnlUrgOrdDetailsOperations.setBorder(BorderFactory.createTitledBorder("Update Detail"));
        pnlUrgOrdDetailsOperations.setBackground(new Color(105, 20, 56));
        pnlUrgOrdDetailsOperations.setBounds(0, 446, 350, 71);
        rightPanel.add(pnlUrgOrdDetailsOperations);

        JButton btnUpdateDetail = new JButton("Update");
        btnUpdateDetail.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnUpdateDetail.setBackground(new Color(194, 65, 78));
        btnUpdateDetail.setBounds(10, 21, 110, 23);
        pnlUrgOrdDetailsOperations.add(btnUpdateDetail);

        btnAddDetail = new JButton("Add");
        btnAddDetail.setBackground(new Color(194, 65, 78));
        btnAddDetail.setBounds(130, 21, 100, 23);
        btnAddDetail.addActionListener(e -> addDetail());
        pnlUrgOrdDetailsOperations.add(btnAddDetail);
        
        JButton btnDeleteDetail = new JButton("Delete");
        btnDeleteDetail.setBounds(240, 21, 100, 23);
        btnDeleteDetail.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnDeleteDetail.setBackground(new Color(194, 65, 78));
        btnDeleteDetail.addActionListener(e -> deleteDetailQuantity());
        pnlUrgOrdDetailsOperations.add(btnDeleteDetail);

        // ----- Urgent Order Details Panel (small table) -----
        pnlUrgOrderDetailsSetup(rightPanel);

        // ----- Orders Table (big table) -----
        // Now with six columns: OrderID, Customer, Order Date, Expected Delivery, Priority, Status
        String[] columnNames = {"OrderID", "Customer", "Order Date", "Expected Delivery", "Priority", "Status"};
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
        
        // Create and set up row sorter for orders table.
        ordersRowSorter = new TableRowSorter<>(ordersTableModel);
        ordersTable.setRowSorter(ordersRowSorter);

        // ----- Customer Details Panel (created once) -----
        pnlCusDetails = new JPanel();
        pnlCusDetails.setLayout(null);
        pnlCusDetails.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        ((TitledBorder) pnlCusDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlCusDetails.setBackground(new Color(69, 16, 32));
        pnlCusDetails.setBounds(10, 360, 498, 200);
        add(pnlCusDetails);

        // Labels and text fields for customer details
        JLabel lblId = new JLabel("ID:");
        lblId.setForeground(new Color(234, 171, 55));
        lblId.setBounds(20, 30, 50, 14);
        pnlCusDetails.add(lblId);
        tfCustID = new JTextField();
        tfCustID.setBackground(new Color(217, 166, 165));
        tfCustID.setEditable(false);
        tfCustID.setBounds(80, 27, 100, 20);
        pnlCusDetails.add(tfCustID);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(new Color(234, 171, 55));
        lblName.setBounds(20, 60, 50, 14);
        pnlCusDetails.add(lblName);
        tfCustName = new JTextField();
        tfCustName.setBackground(new Color(217, 166, 165));
        tfCustName.setEditable(false);
        tfCustName.setBounds(80, 57, 150, 20);
        pnlCusDetails.add(tfCustName);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setForeground(new Color(234, 171, 55));
        lblPhone.setBounds(20, 90, 50, 14);
        pnlCusDetails.add(lblPhone);
        tfCustPhone = new JTextField();
        tfCustPhone.setBackground(new Color(217, 166, 165));
        tfCustPhone.setEditable(false);
        tfCustPhone.setBounds(80, 87, 150, 20);
        pnlCusDetails.add(tfCustPhone);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setForeground(new Color(234, 171, 55));
        lblAddress.setBounds(250, 60, 60, 14);
        pnlCusDetails.add(lblAddress);
        tfCustAddress = new JTextField();
        tfCustAddress.setBackground(new Color(217, 166, 165));
        tfCustAddress.setEditable(false);
        tfCustAddress.setBounds(320, 57, 150, 20);
        pnlCusDetails.add(tfCustAddress);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(new Color(234, 171, 55));
        lblEmail.setBounds(250, 90, 50, 14);
        pnlCusDetails.add(lblEmail);
        tfCustEmail = new JTextField();
        tfCustEmail.setBackground(new Color(217, 166, 165));
        tfCustEmail.setEditable(false);
        tfCustEmail.setBounds(320, 87, 150, 20);
        pnlCusDetails.add(tfCustEmail);
        
        btnContact = new JButton("Contact");
        btnContact.setBackground(new Color(194, 65, 78));
        btnContact.setBounds(341, 157, 129, 32);
        // When Contact is clicked, open the default email client with the customer's email.
        btnContact.addActionListener(e -> {
            String email = tfCustEmail.getText().trim();
            if (!email.isEmpty()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.MAIL)) {
                        URI mailto = new URI("mailto:" + email);
                        desktop.mail(mailto);
                    } else {
                        JOptionPane.showMessageDialog(this, "Mail client not supported.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error opening mail client.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No email address available.");
            }
        });
        pnlCusDetails.add(btnContact);

        // ----- Table Selection Listener -----
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    UrgentOrder urgOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
                    if (urgOrder != null) {
                        updatingCombos = true;
                        cbStatus.setSelectedItem(urgOrder.getCurrentStatus());
                        dcDeliveryDate.setDate(urgOrder.getExpectedDeliveryDate());
                        tfPriorityLevel.setText(String.valueOf(urgOrder.getPriorityLevel()));
                        loadCustomerDetails(urgOrder.getCustomer());
                        updatingCombos = false;
                    }
                    loadOrderDetailsForSelectedOrder(urgOrder);
                }
            }
        });

        // Big table button actions
        btnDeleteOrder.addActionListener(e -> deleteSelectedOrder());
        btnUpdateDetail.addActionListener(e -> updateDetailQuantity());
        btnUpdateOrder.addActionListener(e -> updateSelectedUrgentOrder());
    }
    
    // Helper method to set up the Urgent Order Details Panel.
    private void pnlUrgOrderDetailsSetup(JPanel rightPanel) {
        pnlUrgentOrderDetails = new JPanel();
        pnlUrgentOrderDetails.setBounds(0, 168, 350, 277);
        pnlUrgentOrderDetails.setBackground(new Color(69, 16, 32));
        pnlUrgentOrderDetails.setBorder(BorderFactory.createTitledBorder("Order Details"));
        ((TitledBorder) pnlUrgentOrderDetails.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlUrgentOrderDetails.setLayout(null);
        rightPanel.add(pnlUrgentOrderDetails);
        
        JScrollPane detailsScrollPane = new JScrollPane();
        detailsScrollPane.setBounds(10, 18, 330, 248);
        detailsScrollPane.setBackground(new Color(69, 16, 32));
        detailsScrollPane.setForeground(new Color(234, 171, 55));
        detailsScrollPane.getViewport().setBackground(new Color(69, 16, 32));
        pnlUrgentOrderDetails.add(detailsScrollPane);
        
        detailsTableModel = new DefaultTableModel(
            new Object[]{"OrderID", "CustomerID", "WineID", "Quantity", "Price"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Allow editing quantity
            }
        };
        detailsTable = new JTable(detailsTableModel);
        detailsTable.setForeground(new Color(234, 171, 55));
        detailsTable.setBackground(new Color(105, 20, 56));
        detailsTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        detailsScrollPane.setViewportView(detailsTable);
    }

    // Populate the orders table with urgent orders.
    private void populateOrdersTable() {
        ordersTableModel.setRowCount(0);
        ArrayList<UrgentOrder> orders = OrderControl.getInstance().getUrgentOrders();
        for (UrgentOrder order : orders) {
            String customerName = (order.getCustomer() != null) ? order.getCustomer().getName() : "N/A";
            ordersTableModel.addRow(new Object[]{
                order.getOrder_ID(),
                customerName,
                order.getOrder_Date(),
                order.getExpectedDeliveryDate(),
                order.getPriorityLevel(),
                order.getCurrentStatus()
            });
        }
    }

    // Loads order details into the details table using getUrgentOrderDetails() from OrderControl.
    private void loadOrderDetailsForSelectedOrder(UrgentOrder urgOrder) {
        detailsTableModel.setRowCount(0);
        if (urgOrder != null) {
            int orderId = urgOrder.getOrder_ID();
            ArrayList<Object[]> details = OrderControl.getInstance().getUrgentOrderDetails(orderId);
            for (Object[] row : details) {
                // Fill in the CustomerID column with the order's customer id.
                row[1] = (urgOrder.getCustomer() != null) ? urgOrder.getCustomer().getCustomerID() : 0;
                detailsTableModel.addRow(new Object[]{ row[0], row[1], row[2], row[3], row[4] });
            }
        }
    }

    // Updates the customer details panel with the given customer.
    private void loadCustomerDetails(Customer customer) {
        if (customer != null) {
            tfCustID.setText(String.valueOf(customer.getCustomerID()));
            tfCustName.setText(customer.getName());
            tfCustPhone.setText(customer.getPhoneNumber());
            tfCustAddress.setText(customer.getDeliveryAddress());
            tfCustEmail.setText(customer.getEmail());
        } else {
            tfCustID.setText("");
            tfCustName.setText("");
            tfCustPhone.setText("");
            tfCustAddress.setText("");
            tfCustEmail.setText("");
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
                detailsTableModel.setRowCount(0);
                loadCustomerDetails(null);
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting order.");
            }
        }
    }

    // Deletes the selected detail record.
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
        
        UrgentOrder urgOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
        if (urgOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        // Only allow detail update if order status is PAID.
        if (!urgOrder.getCurrentStatus().equals(CurrentStatus.PAID)) {
            JOptionPane.showMessageDialog(this, "Detail updates are allowed only when the order status is PAID.",
                                          "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int wineId = (Integer) detailsTableModel.getValueAt(selectedDetailRow, 2);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this detail?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = OrderControl.getInstance().deleteUrgentOrderDetail(orderId, wineId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Detail deleted successfully.");
                int selectedRow = ordersTable.getSelectedRow();
                if(selectedRow != -1) {
                    int ordId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    UrgentOrder UpdOrder = OrderControl.getInstance().getUrgentOrderByID(ordId);
                    loadOrderDetailsForSelectedOrder(UpdOrder);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting detail.");
            }
        }
    }

    // Updates the selected detail record's quantity.
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
     // Retrieve the urgent order to check its status.
        int orderId = (Integer) ordersTableModel.getValueAt(selectedOrderRow, 0);
        UrgentOrder urgOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
        if (urgOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        // Only allow detail update if order status is PAID.
        if (!urgOrder.getCurrentStatus().equals(CurrentStatus.PAID)) {
            JOptionPane.showMessageDialog(this, "Detail updates are allowed only when the order status is PAID.",
                                          "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (detailsTable.isEditing()) {
            detailsTable.getCellEditor().stopCellEditing();
        }
        
        
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
            boolean success = OrderControl.getInstance().updateUrgentOrderDetail(orderId, wineId, newQuantity);
            if (success) {
                JOptionPane.showMessageDialog(this, "Quantity updated successfully.");
                int selectedRow = ordersTable.getSelectedRow();
                if(selectedRow != -1) {
                    int ordId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    UrgentOrder updatedOrder = OrderControl.getInstance().getUrgentOrderByID(ordId);
                    loadOrderDetailsForSelectedOrder(updatedOrder);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error updating quantity.");
            }
        }
    }

    // Adds a detail record.
    private void addDetail() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Please select an order first.");
            return;
        }
        int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);

        // For urgent orders, we already have a single customer.
        UrgentOrder urgOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
        if (urgOrder == null || urgOrder.getCustomer() == null) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "No customer found for this order.");
            return;
        }
        if (!urgOrder.getCurrentStatus().equals(CurrentStatus.PAID)) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, 
                "New items can be added only when the order status is PAID.",
                "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int customerId = urgOrder.getCustomer().getCustomerID();

        String wineIdStr = JOptionPane.showInputDialog(FrmUrgentOrder.this, "Enter Wine ID:");
        if (wineIdStr == null || wineIdStr.trim().isEmpty()) return;
        int wineId;
        try {
            wineId = Integer.parseInt(wineIdStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Invalid Wine ID.");
            return;
        }
        String quantityStr = JOptionPane.showInputDialog(FrmUrgentOrder.this, "Enter Quantity:");
        if (quantityStr == null || quantityStr.trim().isEmpty()) return;
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Invalid Quantity.");
            return;
        }
        double pricePerBottle = OrderControl.getInstance().getPricePerBottle(wineId);
        if (pricePerBottle == 0.0) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Unable to retrieve price for Wine ID: " + wineId);
            return;
        }
        double totalPrice = quantity * pricePerBottle;
        
        boolean success = OrderControl.getInstance().addUrgentOrderDetail(orderId, wineId, quantity, totalPrice);
        if (success) {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Detail added successfully.");
        } else {
            JOptionPane.showMessageDialog(FrmUrgentOrder.this, "Error adding order detail.");
        }
        int selectedOrdRow = ordersTable.getSelectedRow();
        if (selectedOrdRow != -1) {
            int ordId = (Integer) ordersTableModel.getValueAt(selectedOrdRow, 0);
            UrgentOrder updatedOrder = OrderControl.getInstance().getUrgentOrderByID(ordId);
            loadOrderDetailsForSelectedOrder(updatedOrder);
        }
    }

    // Call this method from the update button's action listener.
    private void updateSelectedUrgentOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }
        
        int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
        UrgentOrder urgentOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
        if (urgentOrder == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }
        
        // Read new values from controls.
        CurrentStatus newStatus = (CurrentStatus) cbStatus.getSelectedItem();
        OrderControl.getInstance().updateOrderStatusWithInventoryUpdateUrgent(orderId,newStatus);
        java.util.Date newExpectedDate = dcDeliveryDate.getDate();
        int newPriority;
        try {
            newPriority = Integer.parseInt(tfPriorityLevel.getText().trim());
            if (newPriority < 1 || newPriority > 5) {
                JOptionPane.showMessageDialog(this, "Priority level must be between 1 and 5.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid priority level. Please enter a number between 1 and 5.");
            return;
        }
        
        // Update the urgent order object with new values.
        urgentOrder.setCurrentStatus(newStatus);
        urgentOrder.setExpectedDeliveryDate(newExpectedDate);
        urgentOrder.setPriorityLevel(newPriority);
        
        // Call the control method to update in the database.
        boolean updated = OrderControl.getInstance().updateUrgentOrder(urgentOrder);
      
        if (updated) {
            JOptionPane.showMessageDialog(this, "Urgent order updated successfully.");
            populateOrdersTable(); // Refresh the table to show updated values.
        } else {
            JOptionPane.showMessageDialog(this, "Error updating urgent order.");
        }
    }

    // Attaches a selection listener to the orders table.
    private void attachOrderSelectionListener() {
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
                    UrgentOrder urgOrder = OrderControl.getInstance().getUrgentOrderByID(orderId);
                    if (urgOrder != null) {
                        updatingCombos = true;
                        cbStatus.setSelectedItem(urgOrder.getCurrentStatus());
                        dcDeliveryDate.setDate(urgOrder.getExpectedDeliveryDate());
                        tfPriorityLevel.setText(String.valueOf(urgOrder.getPriorityLevel()));
                        loadCustomerDetails(urgOrder.getCustomer());
                        updatingCombos = false;
                    }
                    loadOrderDetailsForSelectedOrder(urgOrder);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Urgent Order Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new FrmUrgentOrder());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
