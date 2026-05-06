package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import control.OrderControl;
import control.PersonControl;
import entity.Customer;
import entity.Employee;
import entity.RegularOrder;
import enums.CurrentStatus;
import enums.Role;

public class FrmWineForCustomer extends JDialog {
    private JTable customerTable, wineTable;
    private DefaultTableModel customerTableModel, wineTableModel;
    private JButton btnConfirm, btnSave;
    private JPanel winePanel;
    
    // Map of customerID to their wine selections.
    private Map<Integer, List<WineSelection>> customerWineSelection = new HashMap<>();
    
    // Order header details passed from FrmAddOrder.
    private Date orderDate;
    private Date shipmentDate;
    private String employeeIdText; // We will parse this into an int later.
    private Date expectedDeliveryDate;
    private Integer priorityLevel;
    
    public FrmWineForCustomer(JFrame parent, Object[][] selectedCustomers, Object[][] availableWines,
            Date orderDate, Date shipmentDate, String employeeIdText) {
        super(parent, "Select Wines for Customers", true);
        this.orderDate = orderDate;
        this.shipmentDate = shipmentDate;
        this.employeeIdText = employeeIdText;
        getContentPane().setBackground(new Color(105, 20, 56));

        // Make the whole dialog shorter.
        setSize(1000, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // --- Customer Table Panel ---
        String[] customerColumns = {"Customer ID", "Name"};
        customerTableModel = new DefaultTableModel(selectedCustomers, customerColumns);
        customerTable = new JTable(customerTableModel);
        // Set table cell colors.
        customerTable.setBackground(new Color(105, 20, 56));
        customerTable.setForeground(new Color(234, 171, 55));
        customerTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Set header colors (opposite of cell colors).
        customerTable.getTableHeader().setBackground(new Color(234, 171, 55));
        customerTable.getTableHeader().setForeground(new Color(105, 20, 56));
        
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        // Set the viewport background color.
        customerScrollPane.getViewport().setBackground(new Color(105, 20, 56));

        // Wrap the customer table in a panel with a titled border "Customer Details"
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        ((TitledBorder) customerPanel.getBorder()).setTitleColor(new Color(234, 171, 55));
        customerPanel.add(customerScrollPane, BorderLayout.CENTER);
        customerPanel.setBackground(new Color(69, 16, 32));
        // Set preferred size so that it is narrower and shorter.
        customerPanel.setPreferredSize(new Dimension(300, 250));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1; // make sure it takes one row (same height as wine panel)
        add(customerPanel, gbc);

        // --- Wine Table Panel ---
        winePanel = new JPanel(new BorderLayout());
        winePanel.setBorder(BorderFactory.createTitledBorder("Wine Selection"));
        ((TitledBorder) winePanel.getBorder()).setTitleColor(new Color(234, 171, 55));
        winePanel.setBackground(new Color(69, 16, 32));

        String[] wineColumns = {"Select", "Wine ID", "Wine Name", "Price per Bottle", "Quantity", "Total Price"};
        wineTableModel = new DefaultTableModel(wineColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                if (columnIndex == 4) return Integer.class;
                if (columnIndex == 5) return Double.class;
                return String.class;
            }
        };
        for (Object[] wine : availableWines) {
            // Assuming wine[2] is the price (double)
            wineTableModel.addRow(new Object[]{false, wine[0], wine[1], wine[2], 1, (double) wine[2]});
        }

        wineTable = new JTable(wineTableModel);
        // Set table cell colors.
        wineTable.setBackground(new Color(105, 20, 56));
        wineTable.setForeground(new Color(234, 171, 55));
        // Set header colors (opposite of cell colors).
        wineTable.getTableHeader().setBackground(new Color(234, 171, 55));
        wineTable.getTableHeader().setForeground(new Color(105, 20, 56));
        // Update total price when quantity changes.
        wineTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4) { // Quantity column
                int quantity = (int) wineTableModel.getValueAt(row, 4);
                double pricePerBottle = (double) wineTableModel.getValueAt(row, 3);
                double totalPrice = quantity * pricePerBottle;
                wineTableModel.setValueAt(totalPrice, row, 5);
            }
        });

        JScrollPane wineScrollPane = new JScrollPane(wineTable);
        // Set the viewport background color.
        wineScrollPane.getViewport().setBackground(new Color(105, 20, 56));
        winePanel.add(wineScrollPane, BorderLayout.CENTER);
        // Make the wine panel shorter (keeping the width as is).
        winePanel.setPreferredSize(new Dimension(600, 250));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        add(winePanel, gbc);

        // --- Save Button for wine selections ---
        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(194, 65, 78));
        btnSave.addActionListener(this::saveCurrentSelections);
        winePanel.add(btnSave, BorderLayout.SOUTH);

        // --- Confirm Button (spanning both columns) ---
        btnConfirm = new JButton("Confirm Selection");
        btnConfirm.setBackground(new Color(194, 65, 78));
        btnConfirm.addActionListener(this::confirmSelection);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(btnConfirm, gbc);

        // When customer selection changes, load saved selections.
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadCustomerSelections();
            }
        });

        if (customerTableModel.getRowCount() > 0) {
            customerTable.setRowSelectionInterval(0, 0);
        }
    }


 
    public FrmWineForCustomer(JFrame parent, Object[][] selectedCustomers, Object[][] availableWines,
            Date orderDate, Date shipmentDate, String employeeIdText,
            Date expectedDeliveryDate, Integer priorityLevel) {
super(parent, "Select Wines for Customers", true);
this.orderDate = orderDate;
this.shipmentDate = shipmentDate;
this.employeeIdText = employeeIdText;
// For urgent orders, store these values (if needed later)
this.expectedDeliveryDate = expectedDeliveryDate;
this.priorityLevel = priorityLevel;

setSize(1000, 800);
setLocationRelativeTo(parent);
setLayout(new GridBagLayout());

GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5);
gbc.fill = GridBagConstraints.BOTH;

// Customer List Table
String[] customerColumns = {"Customer ID", "Name"};
customerTableModel = new DefaultTableModel(selectedCustomers, customerColumns);
customerTable = new JTable(customerTableModel);
// For urgent orders, enforce single selection.
customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
JScrollPane customerScrollPane = new JScrollPane(customerTable);


gbc.gridx = 0;
gbc.gridy = 0;
gbc.gridheight = 2;
add(customerScrollPane, gbc);

// Wine Table Panel
winePanel = new JPanel(new BorderLayout());
winePanel.setBorder(BorderFactory.createTitledBorder("Wine Selection"));

String[] wineColumns = {"Select", "Wine ID", "Wine Name", "Price per Bottle", "Quantity", "Total Price"};
wineTableModel = new DefaultTableModel(wineColumns, 0) {
@Override
public Class<?> getColumnClass(int columnIndex) {
if (columnIndex == 0) return Boolean.class;
if (columnIndex == 4) return Integer.class;
if (columnIndex == 5) return Double.class;
return String.class;
}
};
for (Object[] wine : availableWines) {
// Assuming wine[2] is the price (double)
wineTableModel.addRow(new Object[]{false, wine[0], wine[1], wine[2], 1, (double) wine[2]});
}

wineTable = new JTable(wineTableModel);

// Update total price when quantity changes.
wineTable.getModel().addTableModelListener(e -> {
int row = e.getFirstRow();
int column = e.getColumn();
if (column == 4) { // Quantity column
int quantity = (int) wineTableModel.getValueAt(row, 4);
double pricePerBottle = (double) wineTableModel.getValueAt(row, 3);
double totalPrice = quantity * pricePerBottle;
wineTableModel.setValueAt(totalPrice, row, 5);
}
});

JScrollPane wineScrollPane = new JScrollPane(wineTable);
winePanel.add(wineScrollPane, BorderLayout.CENTER);

gbc.gridx = 1;
gbc.gridy = 0;
gbc.gridheight = 1;
add(winePanel, gbc);

// Note: We do NOT add the "Save" button in this constructor.

// Confirm Button remains.
btnConfirm = new JButton("Confirm Selection");
btnConfirm.setBackground(new Color(194, 65, 78));
btnConfirm.addActionListener(this::confirmSelection);

gbc.gridy = 1;
gbc.fill = GridBagConstraints.HORIZONTAL;
add(btnConfirm, gbc);

// When customer selection changes, load saved selections.
customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
@Override
public void valueChanged(ListSelectionEvent e) {
if (!e.getValueIsAdjusting()) {
loadCustomerSelections();
}
}
});

if (customerTableModel.getRowCount() > 0) {
customerTable.setRowSelectionInterval(0, 0);
}
}


	// Inner class to store wine selection details.
    public static class WineSelection {
        private int wineId;
        private int quantity;
        private double totalPrice;
 
        public WineSelection(int wineId, int quantity, double totalPrice) {
            this.wineId = wineId;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
 
        public int getWineId() {
            return wineId;
        }
 
        public int getQuantity() {
            return quantity;
        }
 
        public double getTotalPrice() {
            return totalPrice;
        }
    }
 
    private void saveCurrentSelections(ActionEvent e) {
        int selectedCustomerRow = customerTable.getSelectedRow();
        if(selectedCustomerRow == -1) return;
 
        int customerId = (int) customerTableModel.getValueAt(selectedCustomerRow, 0);
        List<WineSelection> selections = new ArrayList<>();
 
        for(int i = 0; i < wineTableModel.getRowCount(); i++){
            boolean isSelected = (Boolean) wineTableModel.getValueAt(i, 0);
            if(isSelected){
                int wineId = (int) wineTableModel.getValueAt(i, 1);
                int quantity = (int) wineTableModel.getValueAt(i, 4);
                double totalPrice = (double) wineTableModel.getValueAt(i, 5);
                selections.add(new WineSelection(wineId, quantity, totalPrice));
            }
        }
        customerWineSelection.put(customerId, selections);
        JOptionPane.showMessageDialog(this, "Selections saved for customer ID: " + customerId);
    }
 
    private void loadCustomerSelections() {
        int selectedCustomerRow = customerTable.getSelectedRow();
        if(selectedCustomerRow == -1) return;
 
        int customerId = (int) customerTableModel.getValueAt(selectedCustomerRow, 0);
        // Reset wine table selections.
        for(int i = 0; i < wineTableModel.getRowCount(); i++){
            wineTableModel.setValueAt(false, i, 0);
            wineTableModel.setValueAt(1, i, 4);
            wineTableModel.setValueAt((double) wineTableModel.getValueAt(i, 3), i, 5);
        }
        if(customerWineSelection.containsKey(customerId)){
            List<WineSelection> selections = customerWineSelection.get(customerId);
            for(int i = 0; i < wineTableModel.getRowCount(); i++){
                int wineId = (int) wineTableModel.getValueAt(i, 1);
                for(WineSelection ws : selections){
                    if(ws.getWineId() == wineId){
                        wineTableModel.setValueAt(true, i, 0);
                        wineTableModel.setValueAt(ws.getQuantity(), i, 4);
                        wineTableModel.setValueAt(ws.getTotalPrice(), i, 5);
                    }
                }
            }
        }
    }
 
    // Modified confirmSelection that now inserts the order.
    private void confirmSelection(ActionEvent e) {
        // Save current selections for current customer.
        saveCurrentSelections(e);

        // Validate header fields.
        if(orderDate == null || shipmentDate == null || employeeIdText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Order header details missing.");
            return;
        }
        int empId = 0;
        try {
            empId = Integer.parseInt(employeeIdText);
        } catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Invalid Employee ID.");
            return;
        }

        entity.Employee employee = PersonControl.getInstance().getEmployeeById(empId);
        if(employee == null){
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        // For demonstration purposes, build a customer mapping using saved selections.
        // This logic may vary based on your design.
        int rowCount = customerTableModel.getRowCount();
        if(rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No customers available.");
            return;
        }
        // Let's assume the last customer in the table is the main customer.
        int mainCustomerId = (int) customerTableModel.getValueAt(rowCount - 1, 0);
        HashMap<entity.Customer, enums.Role> customersMap = new HashMap<>();
        for(Integer custId : customerWineSelection.keySet()){
            entity.Customer cust = PersonControl.getInstance().getCustomerById(custId);
            if(cust != null){
                if(custId == mainCustomerId){
                    customersMap.put(cust, enums.Role.MAIN);
                } else {
                    customersMap.put(cust, enums.Role.ADDITIONAL);
                }
            }
        }

        // Branch based on the extra urgent order fields.
        // If expectedDeliveryDate and priorityLevel are non-null, assume it is an urgent order.
        if(this.expectedDeliveryDate != null && this.priorityLevel != null) {
            // Create an urgent order object.
            entity.UrgentOrder urgentOrder = new entity.UrgentOrder(mainCustomerId, expectedDeliveryDate, null, expectedDeliveryDate, employee, expectedDeliveryDate, null, mainCustomerId);
            urgentOrder.setOrder_Date(orderDate);
            urgentOrder.setShipment_Date(shipmentDate);
            urgentOrder.setEmployee_ID(employee);
            urgentOrder.setExpectedDeliveryDate(this.expectedDeliveryDate);
            urgentOrder.setPriorityLevel(this.priorityLevel);
            // For urgent orders, only one customer should be selected.
            int selectedRow = customerTable.getSelectedRow();
            if(selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer for the urgent order.");
                return;
            }
            int customerId = (int) customerTableModel.getValueAt(selectedRow, 0);
            entity.Customer selectedCustomer = PersonControl.getInstance().getCustomerById(customerId);
            if(selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Selected customer not found.");
                return;
            }
            urgentOrder.setCustomer(selectedCustomer);
            urgentOrder.setCurrentStatus(enums.CurrentStatus.IN_PROCESS);

            // Insert the urgent order.
            int orderInserted = OrderControl.getInstance().addUrgentOrder(urgentOrder);
            if(orderInserted==-1){
                JOptionPane.showMessageDialog(this, "Failed to insert urgent order.");
                return;
            }
            int orderId = urgentOrder.getOrder_ID();

            // Insert order details for the urgent order.
            // Since urgent orders allow only one customer, get that customer's wine selections.
            List<WineSelection> selections = customerWineSelection.get(customerId);
            if(selections == null || selections.isEmpty()){
                JOptionPane.showMessageDialog(this, "No wine selections found for the customer.");
                return;
            }
            boolean allInserted = true;
            for(WineSelection ws : selections){
                boolean success = OrderControl.getInstance().addUrgentOrderDetail(orderId, ws.getWineId(), ws.getQuantity(), ws.getTotalPrice());
                if(!success){
                    allInserted = false;
                    break;
                }
            }
     
            if(allInserted){
                JOptionPane.showMessageDialog(this, "Urgent order confirmed and saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error saving one or more urgent order details.");
            }
        } else {
            // Else, process as a regular order.
            entity.RegularOrder regularOrder = new entity.RegularOrder(
                    0,
                    orderDate,
                    enums.CurrentStatus.IN_PROCESS,  // forced status
                    shipmentDate,
                    employee,
                    customersMap
            );

            // Insert the regular order.
            int regOrderId = OrderControl.getInstance().insertRegularOrder(regularOrder);
            if(regOrderId == -1){
                JOptionPane.showMessageDialog(this, "Failed to insert regular order.");
                return;
            }

            // Insert order details for each saved wine selection.
            boolean allInserted = true;
            for(Map.Entry<Integer, List<WineSelection>> entry : customerWineSelection.entrySet()){
                int custKey = entry.getKey();
                for(WineSelection ws : entry.getValue()){
                    boolean success = OrderControl.getInstance().addRegularOrderDetail(
                            regOrderId, custKey, ws.getWineId(), ws.getQuantity(), ws.getTotalPrice());
                    if(!success){
                        allInserted = false;
                        break;
                    }
                }
                if(!allInserted) break;
            }

            if(allInserted){
                JOptionPane.showMessageDialog(this, "Regular order confirmed and details saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error saving one or more order details.");
            }
        }

        dispose();
    }

 
    public Map<Integer, List<WineSelection>> getCustomerWineSelection() {
        return customerWineSelection;
    }
}
