package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import control.InventoryControl;
import control.PersonControl;
import control.ReportControl;
import control.UserControl;
import entity.Customer;
import entity.Employee;
import entity.Order;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.ArrayList;

public class FrmLandPageMarketing extends JPanel {

    private JLabel lblGreeting;
    private JTable tblInventory;
    private JLabel lblTotalStock;
    private JLabel lblNeedLink;
    
    public FrmLandPageMarketing(String username) {
        setSize(900, 700);
        setLayout(null);
        setBackground(new Color(105, 20, 56));

        // Retrieve employee
        Employee employee = UserControl.getInstance().getEmployeeNameByUsername(username);
        String welcomeText = (employee != null) ? "Welcome Back " + employee.getName() : "Welcome Back " + username;
        lblGreeting = new JLabel(welcomeText);
        lblGreeting.setForeground(new Color(234, 171, 55));
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 20));
        lblGreeting.setBounds(20, 20, 300, 30);
        add(lblGreeting);

        // Panel for Inlinked WineTypes (restored as unlinkpnl)
        JPanel pnlWineTypes = new JPanel(null);
        pnlWineTypes.setBackground(new Color(69, 16, 32));
        pnlWineTypes.setBorder(BorderFactory.createTitledBorder("Inlinked WineTypes"));
        ((TitledBorder) pnlWineTypes.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlWineTypes.setBounds(50, 129, 273, 120);
        add(pnlWineTypes);
        
        int unlinkedCount = ReportControl.getInstance().getUnlinkedWineTypesCount();
        lblNeedLink = new JLabel();
        lblNeedLink.setForeground(new Color(234, 171, 55));
        lblNeedLink.setBounds(133, 50, 107, 30);
        lblNeedLink.setText("Need Linking: " + unlinkedCount);
        pnlWineTypes.add(lblNeedLink);
        
        // Use setIcon with getResource() to load the task icon
        JLabel iconTypes = new JLabel();
        iconTypes.setIcon(new ImageIcon(getClass().getResource("/boundary/images/task.png")));
        iconTypes.setForeground(new Color(234, 171, 55));
        iconTypes.setBounds(10, 28, 64, 64);
        pnlWineTypes.add(iconTypes);
        
        // Panel for Wines in Stock with JTable and Total Stock Label
        JPanel pnlStock = new JPanel(null);
        pnlStock.setBackground(new Color(69, 16, 32));
        pnlStock.setBorder(BorderFactory.createTitledBorder("Wines in Stock"));
        ((TitledBorder) pnlStock.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlStock.setBounds(563, 129, 273, 120);
        add(pnlStock);
        
        DefaultTableModel inventoryModel = new DefaultTableModel(
                new Object[]{"Wine Name", "Production Year", "Location", "Quantity"}, 0);
        
        lblTotalStock = new JLabel("Total Wines: 0");
        lblTotalStock.setForeground(new Color(234, 171, 55));
        lblTotalStock.setBounds(132, 55, 108, 25);
        pnlStock.add(lblTotalStock);
        
        // Use setIcon with getResource() to load the Wines icon
        JLabel iconWines = new JLabel();
        iconWines.setIcon(new ImageIcon(getClass().getResource("/boundary/images/Wines.png")));
        iconWines.setForeground(new Color(234, 171, 55));
        iconWines.setBounds(10, 28, 64, 64);
        pnlStock.add(iconWines);
        
        DefaultTableModel ordersModel = new DefaultTableModel(
                new Object[]{"Order ID", "Order Date", "Shipment Date", "Employee", "Status"}, 0);
        
        JLabel lblOrders = new JLabel("Low stock under 200");
        lblOrders.setForeground(new Color(234, 171, 55));
        lblOrders.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblOrders.setBounds(50, 260, 211, 28);
        add(lblOrders);
        
        // Populate orders table for the employee if available
        if (employee != null) {
            ArrayList<Order> orders = PersonControl.getInstance().getOrdersByEmployee(employee.getEmployeeID());
            for (Order order : orders) {
                ordersModel.addRow(new Object[]{
                    order.getOrder_ID(),
                    order.getOrder_Date(),
                    order.getShipment_Date(),
                    order.getEmployee().getName(),
                    order.getCurrentStatus()
                });
            }
        }
        
        // Contact Panel
        JPanel pnlContact = new JPanel(null);
        pnlContact.setBackground(new Color(69, 16, 32));
        pnlContact.setBorder(BorderFactory.createTitledBorder("Contact"));
        ((TitledBorder) pnlContact.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlContact.setBounds(50, 465, 786, 81);
        add(pnlContact);
        
        JButton btnSendEmail = new JButton("Send Email");
        btnSendEmail.setBounds(478, 25, 195, 30);
        btnSendEmail.setBackground(new Color(194, 65, 78));
        pnlContact.add(btnSendEmail);
        
        JComboBox comboBox = new JComboBox();
        comboBox.setBackground(new Color(194, 65, 78));
        comboBox.setBounds(86, 25, 213, 30);
        pnlContact.add(comboBox);
        
        // Table for inventory info (columns: Wine Name, Production Year, Location Name, Quantity)
        JScrollPane stockScrollPane = new JScrollPane();
        stockScrollPane.setBounds(50, 299, 786, 155);
        add(stockScrollPane);
        tblInventory = new JTable(inventoryModel);
        stockScrollPane.setViewportView(tblInventory);
        stockScrollPane.getViewport().setBackground(new Color(69, 16, 32));
        
        tblInventory.setForeground(new Color(234, 171, 55));
        tblInventory.setBackground(new Color(105, 20, 56));
        tblInventory.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        tblInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        stockScrollPane.setViewportView(tblInventory);
        
        ArrayList<Customer> customers = PersonControl.getInstance().getCustomers();
        for (Customer cust : customers) {
            comboBox.addItem(cust);
        }
        
        // Email button action listener
        btnSendEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer selectedCustomer = (Customer) comboBox.getSelectedItem();
                if (selectedCustomer != null) {
                    String email = selectedCustomer.getEmail();
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:" + email));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error launching email client: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer.");
                }
            }
        });
        
        // Populate the inventory table and update total stock label
        populateInventoryTable();
        updateTotalStockLabel();
    }
    
    // Populate the inventory table using InventoryControl.getInventoryInfo()
    private void populateInventoryTable() {
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.setRowCount(0); // Clear table

        // Assuming getInventoryInfo() returns ArrayList<Object[]> where columns are:
        // [WineID, WineName, ProductionYear, ManufacturerName, LocationName, Quantity]
        ArrayList<Object[]> invInfo = InventoryControl.getInstance().getInventoryInfo();
        for (Object[] row : invInfo) {
            // Extract relevant columns: WineName, ProductionYear, LocationName, Quantity
            Object wineName = row[1];
            Object productionYear = row[2];
            Object locationName = row[4];
            Object quantityObj = row[5];
            
            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantityObj.toString());
            } catch (NumberFormatException ex) {
                continue; // Skip row if quantity cannot be parsed
            }
            
            // Only add rows where quantity is under 200
            if (quantity < 200) {
                model.addRow(new Object[]{wineName, productionYear, locationName, quantity});
            }
        }
    }

    // Update the total wines label using InventoryControl.getTotalStock()
    private void updateTotalStockLabel() {
        int totalStock = InventoryControl.getInstance().getTotalStock();
        lblTotalStock.setText("Total Wines: " + totalStock);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test FrmLandPageMarketing");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.getContentPane().add(new FrmLandPageMarketing("Admin"));
            testFrame.pack();
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        });
    }
}
