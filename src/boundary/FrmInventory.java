package boundary;

import control.InventoryControl;
import entity.Consts;
import entity.Locationn;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrmInventory extends JPanel {

    // UI Components
    private JLabel lblTitle;
    private JSeparator separator;
    private JPanel pnlModStock, pnlAddLoc, pnlDeleteLoc;
    private JComboBox<String> suppCombo1, cbDeleteLocation;
    private JLabel lblWineID1, lblQuantity1;
    private JTextField txtWineID1, txtQuantity1;
    private JButton btnAdd1, btnDelete1, btnClear1;
    private JButton btnRefresh;
    private JTextField txtSearch;
    private JLabel lblSearch;
    private JTextField tfAddLc;
    private JButton btnAddLocation, btnDelocation;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JTextField tfOverallStock;

    public FrmInventory() {
        // Set the preferred size and layout for this panel
        setPreferredSize(new Dimension(900, 600));
        setBackground(new Color(105, 20, 56));
        setLayout(null);
        
        // Initialize components
        initComponents();
        populateTable(); // Load inventory data into JTable on startup
        loadLocations(); // Load location list into dropdown
    }

    private void initComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(69, 16, 32));
        titlePanel.setBounds(0, 0, 847, 33);
        lblTitle = new JLabel("Manage Inventory And Stock", SwingConstants.LEFT);
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(69, 16, 32));
        lblSearch = new JLabel("Search:");
        lblSearch.setForeground(new Color(234, 171, 55));
        txtSearch = new JTextField(15);
        txtSearch.setBackground(new Color(217, 166, 165));
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(194, 65, 78));
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnRefresh);
        titlePanel.add(searchPanel, BorderLayout.EAST);
        add(titlePanel);

        // Separator (if needed)
        separator = new JSeparator();
        separator.setBounds(0, 33, 847, 2);
        add(separator);

        // Right Panel (Contains Input Forms)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(105, 20, 56));
        rightPanel.setBounds(576, 43, 271, 504);
        rightPanel.setLayout(null);
        add(rightPanel);

        // Inventory Management Panel
        pnlModStock = new JPanel();
        pnlModStock.setBackground(new Color(105, 20, 56));
        pnlModStock.setBounds(10, 0, 258, 172);
        pnlModStock.setBorder(BorderFactory.createTitledBorder("Enter Product Details"));
        ((TitledBorder) pnlModStock.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlModStock.setLayout(null);
        lblWineID1 = new JLabel("WineID:");
        lblWineID1.setForeground(new Color(234, 171, 55));
        lblWineID1.setBounds(10, 22, 46, 20);
        txtWineID1 = new JTextField();
        txtWineID1.setBackground(new Color(217, 166, 165));
        txtWineID1.setBounds(88, 22, 140, 20);
        lblQuantity1 = new JLabel("Quantity:");
        lblQuantity1.setForeground(new Color(234, 171, 55));
        lblQuantity1.setBounds(10, 51, 68, 20);
        txtQuantity1 = new JTextField();
        txtQuantity1.setBackground(new Color(217, 166, 165));
        txtQuantity1.setBounds(88, 51, 140, 20);
        suppCombo1 = new JComboBox<>(new String[]{"Select A Location"});
        suppCombo1.setBackground(new Color(194, 65, 78));
        suppCombo1.setBounds(88, 82, 140, 20);
        btnAdd1 = new JButton("Add");
        btnAdd1.setBackground(new Color(194, 65, 78));
        btnAdd1.setBounds(10, 104, 116, 22);
        btnDelete1 = new JButton("Delete");
        btnDelete1.setBackground(new Color(194, 65, 78));
        btnDelete1.setBounds(129, 104, 116, 23);
        btnClear1 = new JButton("Clear");
        btnClear1.setBackground(new Color(194, 65, 78));
        btnClear1.setBounds(10, 138, 235, 23);
        pnlModStock.add(lblWineID1);
        pnlModStock.add(txtWineID1);
        pnlModStock.add(lblQuantity1);
        pnlModStock.add(txtQuantity1);
        pnlModStock.add(suppCombo1);
        pnlModStock.add(btnAdd1);
        pnlModStock.add(btnDelete1);
        pnlModStock.add(btnClear1);
        rightPanel.add(pnlModStock);

        // Add Location Panel
        pnlAddLoc = new JPanel();
        pnlAddLoc.setBackground(new Color(105, 20, 56));
        pnlAddLoc.setBounds(10, 183, 258, 90);
        pnlAddLoc.setBorder(BorderFactory.createTitledBorder("Add Location"));
        ((TitledBorder) pnlAddLoc.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlAddLoc.setLayout(null);
        JLabel lblLocationName = new JLabel("Name:");
        lblLocationName.setForeground(new Color(234, 171, 55));
        lblLocationName.setBounds(10, 22, 68, 20);
        pnlAddLoc.add(lblLocationName);
        tfAddLc = new JTextField();
        tfAddLc.setBackground(new Color(217, 166, 165));
        tfAddLc.setBounds(88, 22, 140, 20);
        pnlAddLoc.add(tfAddLc);
        btnAddLocation = new JButton("Add Location");
        btnAddLocation.setBackground(new Color(194, 65, 78));
        btnAddLocation.setBounds(10, 53, 235, 23);
        pnlAddLoc.add(btnAddLocation);
        rightPanel.add(pnlAddLoc);

        // Delete Location Panel
        pnlDeleteLoc = new JPanel();
        pnlDeleteLoc.setBackground(new Color(105, 20, 56));
        pnlDeleteLoc.setBounds(10, 294, 258, 90);
        pnlDeleteLoc.setBorder(BorderFactory.createTitledBorder("Delete Location"));
        ((TitledBorder) pnlDeleteLoc.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlDeleteLoc.setLayout(null);
        JLabel lblLocationName_1 = new JLabel("Name:");
        lblLocationName_1.setForeground(new Color(234, 171, 55));
        lblLocationName_1.setBounds(10, 22, 68, 20);
        pnlDeleteLoc.add(lblLocationName_1);
        cbDeleteLocation = new JComboBox<>(new String[]{"Select A Location"});
        cbDeleteLocation.setBackground(new Color(194, 65, 78));
        cbDeleteLocation.setBounds(88, 22, 140, 20);
        pnlDeleteLoc.add(cbDeleteLocation);
        btnDelocation = new JButton("Delete Location");
        btnDelocation.setBackground(new Color(194, 65, 78));
        btnDelocation.setBounds(10, 53, 235, 23);
        pnlDeleteLoc.add(btnDelocation);
        rightPanel.add(pnlDeleteLoc);

        tfOverallStock = new JTextField();
        tfOverallStock.setBackground(new Color(217, 166, 165));
        tfOverallStock.setBounds(101, 407, 140, 20);
        rightPanel.add(tfOverallStock);
        tfOverallStock.setColumns(10);

        JButton btnNewButton = new JButton("Generate Inventory Report");
        btnNewButton.setBackground(new Color(194, 65, 78));
        btnNewButton.setBounds(20, 449, 230, 23);
        rightPanel.add(btnNewButton);

        JLabel lblStock = new JLabel("Overall Stock");
        lblStock.setForeground(new Color(234, 171, 55));
        lblStock.setBounds(20, 410, 82, 20);
        rightPanel.add(lblStock);

        // Inventory Table
        String[] columnNames = {"WineID", "Wine Name", "Year", "Manufacturer", "Location", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setBackground(new Color(105, 20, 56));
        inventoryTable.setForeground(new Color(234, 171, 55));

        inventoryTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground((new Color(234, 171, 55)));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBounds(10, 43, 556, 504);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        add(scrollPane);

        // Button Listeners
        btnAdd1.addActionListener(e -> addInventory());
        btnDelete1.addActionListener(e -> deleteInventory());
        btnAddLocation.addActionListener(e -> addLocation());
        btnDelocation.addActionListener(e -> deleteLocation());
        btnRefresh.addActionListener(e -> populateTable());
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchTable(); }
        });
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        ArrayList<Object[]> inventoryData = InventoryControl.getInstance().getInventoryInfo();
        for (Object[] row : inventoryData) {
            tableModel.addRow(row);
        }
        int totalStock = InventoryControl.getInstance().getTotalStock();
        tfOverallStock.setText(String.valueOf(totalStock));
    }

    private void addInventory() {
        try {
            int wineID = Integer.parseInt(txtWineID1.getText());
            int quantity = Integer.parseInt(txtQuantity1.getText());
            int locationID = suppCombo1.getSelectedIndex(); 
            
            
            
            
            boolean success = InventoryControl.getInstance().addInventory(wineID, locationID, quantity);
            if (success) {
                JOptionPane.showMessageDialog(this, "Inventory added successfully!");
                populateTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add inventory.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.");
        }
    }

    private int getLocationIDFromName(String locationName) {
        ArrayList<Locationn> locations = InventoryControl.getInstance().getLocations();
        for (Locationn loc : locations) {
            if (loc.getLocationName().equals(locationName)) {
                return loc.getLocationUniqueNumber();
            }
        }
        return -1;
    }

    private void deleteInventory() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to delete.");
            return;
        }
        int wineID = (int) tableModel.getValueAt(selectedRow, 0);
        String locationName = (String) tableModel.getValueAt(selectedRow, 4);
        int locationID = getLocationIDFromName(locationName);
        if (locationID == -1) {
            JOptionPane.showMessageDialog(this, "Error: Location not found.");
            return;
        }
        int inventoryID = InventoryControl.getInstance().getInventoryID(wineID, locationID);
        if (inventoryID == -1) {
            JOptionPane.showMessageDialog(this, "Error: Inventory record not found.");
            return;
        }
        boolean success = InventoryControl.getInstance().deleteInventory(inventoryID);
        if (success) {
            JOptionPane.showMessageDialog(this, "Inventory deleted successfully!");
            populateTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete inventory.");
        }
    }

    private void addLocation() {
        String locationName = tfAddLc.getText().trim();
        if (locationName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid location name.");
            return;
        }
        int newLocationID = cbDeleteLocation.getItemCount(); 
        boolean success = InventoryControl.getInstance().addLocation(newLocationID, locationName);
        if (success) {
            JOptionPane.showMessageDialog(this, "Location added successfully!");
            loadLocations();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add location.");
        }
    }

    private void deleteLocation() {
        int selectedIndex = cbDeleteLocation.getSelectedIndex();
        if (selectedIndex <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a location to delete.");
            return;
        }
        String locationName = cbDeleteLocation.getSelectedItem().toString();
        ArrayList<Locationn> locations = InventoryControl.getInstance().getLocations();
        for (Locationn loc : locations) {
            if (loc.getLocationName().equals(locationName)) {
                boolean success = InventoryControl.getInstance().removeLocation(loc.getLocationUniqueNumber());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Location deleted successfully!");
                    loadLocations();
                    populateTable();  
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete location.");
                }
                return;
            }
        }
    }

    private void loadLocations() {
        cbDeleteLocation.removeAllItems();
        cbDeleteLocation.addItem("Select A Location");
        suppCombo1.removeAllItems();
        suppCombo1.addItem("Select A Location");
        for (Locationn loc : InventoryControl.getInstance().getLocations()) {
            cbDeleteLocation.addItem(loc.getLocationName());
            suppCombo1.addItem(loc.getLocationName());
        }
    }

    private void clearFields() {
        txtWineID1.setText("");
        txtQuantity1.setText("");
        suppCombo1.setSelectedIndex(0);
        tfAddLc.setText("");
        cbDeleteLocation.setSelectedIndex(0);
        txtSearch.setText("");
    }

    private void searchTable() {
        String text = txtSearch.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    
}
