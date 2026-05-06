package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import entity.Customer;
import control.OrderControl;
import control.PersonControl;
import control.WineControl;

public class FrmAddOrder extends JFrame {
    private JPanel panelDynamic, panelCustomerSelection;
    private JComboBox<String> comboBoxOrderType;
    // Two buttons: one for regular and one for urgent orders.
    private JButton btnAddCustomersRegular;
    
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JTextField txtSearch;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtEmployeeId;
    private JDateChooser orderDate;
    private JDateChooser shipDate;
    private JLabel lblOrderIdPreview;
    private JDateChooser expectedDeliveryDate;
    private JComboBox<Integer> priorityLevel;

    public FrmAddOrder() {
        getContentPane().setBackground(new Color(105, 20, 56));
        setTitle("Add Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;

       

        // Panel for dynamic header fields (dates, employee id, etc.)
        panelDynamic = new JPanel(new GridBagLayout());
        panelDynamic.setBackground(new Color(105,20,56));
        gbc.gridy = 1;
        add(panelDynamic, gbc);

        // Initialize customer table first.
        initializeCustomerTable();

        // Create both buttons.
        btnAddCustomersRegular = new JButton("Add Customers to Order");
        btnAddCustomersRegular.setBackground(new Color(194,65,78));
        btnAddCustomersRegular.addActionListener(e -> openWineSelectionDialog());

    

        // Add both buttons (one will be hidden based on selection)
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnAddCustomersRegular, gbc);

        // Initialize dynamic fields (including urgent order fields if selected).
        initializeDynamicFields();

        // Set listener for order type changes.
        
            initializeDynamicFields();
            updateButtonVisibility();
        
        refreshCustomerTable();
        setupSearchFunctionality();

        // For regular orders, the button is already wired to openWineSelectionDialog.
        // For urgent orders, the separate button's action listener calls openWineSelectionDialogUrgent.
    }

    // Updates the visibility of the two buttons based on the comboBox selection.
    private void updateButtonVisibility() {
        
            btnAddCustomersRegular.setVisible(true);
        
    }

    // Helper method to initialize customer table components.
    private void initializeCustomerTable() {
        panelCustomerSelection = new JPanel(new BorderLayout());
        panelCustomerSelection.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(217,166,165)),
                "Customer List",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                new Color(234,171,55)
        ));
        panelCustomerSelection.setPreferredSize(new Dimension(600,250));
        panelCustomerSelection.setBackground(new Color(105,20,56));

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Address", "Email", "First Contact"}, 0);
        customerTable = new JTable(tableModel);
        customerTable.setForeground(new Color(234, 171, 55));
        customerTable.setBackground(new Color(69, 16, 32));
        customerTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
	            @Override
	            public Component getTableCellRendererComponent(JTable table, Object value,
	                                                           boolean isSelected, boolean hasFocus, int row, int column) {
	                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                c.setBackground(new Color(234, 171, 55));
	                c.setForeground(new Color(69, 16, 32));
	                return c;
	            }
	        });
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);
        scrollPane = new JScrollPane(customerTable);
        panelCustomerSelection.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(105,20,56));
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setForeground(new Color(234,171,55));
        searchPanel.add(lblSearch);
        txtSearch = new JTextField(15);
        txtSearch.setBackground(new Color(217,166,165));
        txtSearch.setForeground(Color.BLACK);
        searchPanel.add(txtSearch);
        panelCustomerSelection.add(searchPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(panelCustomerSelection, gbc);
    }

    private void initializeDynamicFields() {
        panelDynamic.removeAll();
        panelDynamic.setLayout(new GridBagLayout());

        // Create a panel for Order Details
        JPanel panelOrderDetails = new JPanel(new GridBagLayout());
        panelOrderDetails.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(217, 166, 165)), 
                "Order Details",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                new Color(234, 171, 55)
        ));
        panelOrderDetails.setBackground(new Color(105, 20, 56));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Order ID
        lblOrderIdPreview = getNextOrderIdLabel();
        addLabelAndComponent(panelOrderDetails, gbc, "Order ID:", lblOrderIdPreview, row++);

        // Background color for all input fields
        Color inputBackground = new Color(217, 166, 165);

        // Date
        orderDate = new JDateChooser();
        orderDate.setPreferredSize(new Dimension(180, 25));
        ((JTextField) orderDate.getDateEditor().getUiComponent()).setBackground(inputBackground); // Apply background
        addLabelAndComponent(panelOrderDetails, gbc, "Date:", orderDate, row++);

        // Shipment Date
        shipDate = new JDateChooser();
        shipDate.setPreferredSize(new Dimension(180, 25));
        ((JTextField) shipDate.getDateEditor().getUiComponent()).setBackground(inputBackground); // Apply background
        addLabelAndComponent(panelOrderDetails, gbc, "Shipment Date:", shipDate, row++);

        // Employee ID
        txtEmployeeId = new JTextField(15);
        txtEmployeeId.setPreferredSize(new Dimension(180, 25));
        txtEmployeeId.setBackground(inputBackground);
        addLabelAndComponent(panelOrderDetails, gbc, "Employee ID:", txtEmployeeId, row++);

        // Add the panelOrderDetails to panelDynamic
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelDynamic.add(panelOrderDetails, gbc);

        panelDynamic.revalidate();
        panelDynamic.repaint();
    }

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String label, Component comp, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(217, 166, 165));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    private JLabel getNextOrderIdLabel() {
        int nextId = OrderControl.getInstance().getNextOrderIdPreview();
        lblOrderIdPreview = new JLabel(String.valueOf(nextId));
        lblOrderIdPreview.setForeground(Color.BLACK);
        return lblOrderIdPreview;
    }

    private void refreshCustomerTable() {
        List<Customer> customers = PersonControl.getInstance().getCustomers();
        tableModel.setRowCount(0);
        for(Customer c : customers){
            tableModel.addRow(new Object[]{c.getCustomerID(), c.getName(), c.getPhoneNumber(), c.getDeliveryAddress(), c.getEmail(), c.getFirstContactDate()});
        }
    }

    private void setupSearchFunctionality() {
        txtSearch.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e) {
                String searchText = txtSearch.getText().trim();
                rowSorter.setRowFilter(searchText.isEmpty() ? null : RowFilter.regexFilter("(?i)" + searchText));
            }
        });
    }

    // For regular orders (multiple customer selection)
    private void openWineSelectionDialog() {
        int[] selectedRows = customerTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one customer.");
            return;
        }
        Object[][] selectedCustomers = new Object[selectedRows.length][2];
        for (int i = 0; i < selectedRows.length; i++) {
            int modelRow = customerTable.convertRowIndexToModel(selectedRows[i]);
            selectedCustomers[i][0] = tableModel.getValueAt(modelRow, 0);
            selectedCustomers[i][1] = tableModel.getValueAt(modelRow, 1);
        }
        Object[][] availableWines = WineControl.getAvailableWines();
        // For regular orders, we open the wine selection dialog without urgent order parameters.
        FrmWineForCustomer wineDialog = new FrmWineForCustomer(
            this,
            selectedCustomers,
            availableWines,
            orderDate.getDate(),
            shipDate.getDate(),
            txtEmployeeId.getText().trim()
        );
        wineDialog.setVisible(true);
    }

  

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new FrmAddOrder().setVisible(true));
    }
}
