package boundary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import control.WineControl;
import control.WineTypeControl;
import entity.Wine;
import entity.WineType;
import util.JTextFieldLimiter;

public class FrmWineType extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // Data + State
    private ArrayList<WineType> wineTypeArray;
    private Integer currentWineType; // 1-based index in wineTypeArray
    private boolean inAddMode;
    
    // Main GUI Components
    private JPanel contentPane;
    
    private JTextField tfWineTypeID;
    private JTextField tfSerialNumber;
    private JTextField tfName;
    private JTextField tfNavigation;
    private JTextField tfSearch;
    
    private JButton btnFirst   = new JButton("|<");
    private JButton btnPrev    = new JButton("<<");
    private JButton btnNext    = new JButton(">>");
    private JButton btnLast    = new JButton("|>");
    private JButton btnSave    = new JButton("Save");
    private JButton btnAdd     = new JButton("Add New");
    private JButton btnRemove  = new JButton("Delete");
    private String userRole;

    // Table for the Wines associated with this WineType
    private JTable wineTable;
    private DefaultTableModel wineTableModel;
    
    public FrmWineType(String userRole) {
    	this.userRole=userRole;
        // Set this panel's layout and background
        setLayout(new BorderLayout());
        setBackground(new Color(105, 20, 56));
        
        // Create and configure the content pane
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(202, 187, 149));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        initComponents();
        fetchAndRefresh();
        createEvents();
       
        add(contentPane, BorderLayout.CENTER);
        customizeForRole();
    }
    
    /**
     * Initialize the entire UI.
     */
    private void initComponents() {
        // ---------------------------
        // 1) Panel for WineType details (smaller height)
        // ---------------------------
        JPanel pnlWineTypeDetails = new JPanel();
        pnlWineTypeDetails.setBackground(new Color(105, 20, 56));
        pnlWineTypeDetails.setBorder(new TitledBorder(
            new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(234, 171, 55)),
            "WineType Details",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            null,
            new Color(234, 171, 55)
        ));
        // Set a smaller preferred height for details panel
        pnlWineTypeDetails.setPreferredSize(new Dimension(0, 150));
        
        // Initialize text fields
        tfWineTypeID = new JTextField();
        tfWineTypeID.setBackground(new Color(217, 166, 165));
        tfWineTypeID.setEditable(false);
        tfWineTypeID.setColumns(10);
        
        tfSerialNumber = new JTextField();
        tfSerialNumber.setBackground(new Color(217, 166, 165));
        tfSerialNumber.setColumns(10);
        tfSerialNumber.setDocument(new JTextFieldLimiter(20));
        
        tfName = new JTextField();
        tfName.setBackground(new Color(217, 166, 165));
        tfName.setColumns(10);
        tfName.setDocument(new JTextFieldLimiter(100));
        
        // Labels
        JLabel lblWineTypeID   = new JLabel("WineType ID:");
        lblWineTypeID.setForeground(new Color(234, 171, 55));

        JLabel lblSerialNumber = new JLabel("Serial Number:");
        lblSerialNumber.setForeground(new Color(234, 171, 55));

        JLabel lblName         = new JLabel("Name:");
        lblName.setForeground(new Color(234, 171, 55));

        
        // Layout using GroupLayout
        GroupLayout gl_pnlWineTypeDetails = new GroupLayout(pnlWineTypeDetails);
        pnlWineTypeDetails.setLayout(gl_pnlWineTypeDetails);
        gl_pnlWineTypeDetails.setAutoCreateGaps(true);
        gl_pnlWineTypeDetails.setAutoCreateContainerGaps(true);
        gl_pnlWineTypeDetails.setHorizontalGroup(
            gl_pnlWineTypeDetails.createSequentialGroup()
                .addGroup(gl_pnlWineTypeDetails.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblWineTypeID)
                    .addComponent(lblSerialNumber)
                    .addComponent(lblName))
                .addGroup(gl_pnlWineTypeDetails.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(tfWineTypeID)
                    .addComponent(tfSerialNumber)
                    .addComponent(tfName))
        );
        gl_pnlWineTypeDetails.setVerticalGroup(
            gl_pnlWineTypeDetails.createSequentialGroup()
                .addGroup(gl_pnlWineTypeDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWineTypeID)
                    .addComponent(tfWineTypeID))
                .addGroup(gl_pnlWineTypeDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSerialNumber)
                    .addComponent(tfSerialNumber))
                .addGroup(gl_pnlWineTypeDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(tfName))
        );
        
        // ---------------------------
        // 2) Panel for navigation and action buttons
        // ---------------------------
        JPanel pnlActionBtn = new JPanel();
        pnlActionBtn.setBackground(new Color(105, 20, 56));
        pnlActionBtn.setLayout(null);
        btnFirst.setBounds(5, 11, 48, 23);
        
        // Navigation buttons
        btnFirst.setPreferredSize(new Dimension(48, 23));
        btnFirst.setForeground(new Color(202, 187, 149));
        btnFirst.setBorder(null);
        btnFirst.setBackground(new Color(194, 65, 78));
        pnlActionBtn.add(btnFirst);
        btnPrev.setBounds(58, 11, 50, 23);
        
        btnPrev.setPreferredSize(new Dimension(50, 23));
        btnPrev.setBorder(null);
        btnPrev.setBackground(new Color(194, 65, 78));
        pnlActionBtn.add(btnPrev);
        
        tfNavigation = new JTextField();
        tfNavigation.setBounds(113, 12, 76, 21);
        tfNavigation.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfNavigation.setColumns(9);
        tfNavigation.setEditable(false);
        pnlActionBtn.add(tfNavigation);
        btnNext.setBounds(199, 11, 50, 23);
        
        btnNext.setPreferredSize(new Dimension(50, 23));
        btnNext.setBackground(new Color(194, 65, 78));
        btnNext.setBorder(null);
        pnlActionBtn.add(btnNext);
        btnLast.setBounds(259, 11, 48, 23);
        
        btnLast.setPreferredSize(new Dimension(48, 23));
        btnLast.setForeground(new Color(202, 187, 149));
        btnLast.setBackground(new Color(194, 65, 78));
        btnLast.setBorder(null);
        pnlActionBtn.add(btnLast);
        
        // Search TextField
        tfSearch = new JTextField();
        tfSearch.setBounds(318, 11, 114, 36);
        tfSearch.setColumns(8);
        tfSearch.setBackground(new Color(217, 166, 165));
        tfSearch.setBorder(BorderFactory.createTitledBorder("Search"));
        tfSearch.setToolTipText("Search by Name or Serial Number...");
        pnlActionBtn.add(tfSearch);
        btnSave.setBounds(442, 11, 84, 23);
        
        // Data manipulation buttons
        btnSave.setPreferredSize(new Dimension(56, 23));
        btnSave.setBackground(new Color(194, 65, 78));
        btnSave.setBorder(null);
        pnlActionBtn.add(btnSave);
        btnAdd.setBounds(536, 11, 68, 23);
        
        btnAdd.setPreferredSize(new Dimension(70, 23));
        btnAdd.setBackground(new Color(194, 65, 78));
        btnAdd.setBorder(null);
        pnlActionBtn.add(btnAdd);
        btnRemove.setBounds(614, 11, 61, 23);
        
        btnRemove.setPreferredSize(new Dimension(61, 23));
        btnRemove.setBackground(new Color(194, 65, 78));
        btnRemove.setBorder(null);
        pnlActionBtn.add(btnRemove);
        
        // ---------------------------
        // 3) Panel for the table ("Wines by WineType") - smaller table height
        // ---------------------------
        JPanel pnlDetails = new JPanel(new BorderLayout());
        pnlDetails.setBackground(new Color(105, 20, 56));
        pnlDetails.setBorder(new TitledBorder(
            new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(234, 171, 55)),
            "Wines by WineType",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            null,
            new Color(234, 171, 55)
        ));
        // Set a smaller preferred height for the details (table) panel
        pnlDetails.setPreferredSize(new Dimension(10, 350));
        
        // Build the table model
        wineTableModel = new DefaultTableModel(
            new String[] {
                "Wine ID", "Name", "Production Year", "Price", "Description", "Sweetness Level", "Catalog Number", "Photo"
            },
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        wineTable = new JTable(wineTableModel);
        wineTable.setBackground(new Color(105, 20, 56));
        wineTable.setForeground(new Color(234, 171, 55));
        wineTable.setSelectionBackground(new Color(193, 21, 26));

        
        // Header styling
        wineTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(wineTable);
        scrollPane.setBackground(new Color(105, 20, 56));
        scrollPane.getViewport().setBackground(new Color(105, 20, 56));
        // Set a preferred size to make the table area smaller
        scrollPane.setPreferredSize(new Dimension(0, 150));
        pnlDetails.add(scrollPane, BorderLayout.CENTER);
        
        // ---------------------------
        // Add sub-panels to the main content pane
        // ---------------------------
        contentPane.add(pnlWineTypeDetails, BorderLayout.NORTH);
        contentPane.add(pnlActionBtn, BorderLayout.CENTER);
        contentPane.add(pnlDetails, BorderLayout.SOUTH);
        
    }
    
    /**
     * Set up event listeners for buttons and fields.
     */
    private void createEvents() {
        // Navigation
        btnFirst.addActionListener(e -> btnFirstOnClick());
        btnPrev.addActionListener(e -> btnPrevOnClick());
        btnNext.addActionListener(e -> btnNextOnClick());
        btnLast.addActionListener(e -> btnLastOnClick());
        
        // Save / Add / Remove
        btnSave.addActionListener(e -> btnSaveOnClick());
        btnAdd.addActionListener(e -> btnAddOnClick());
        btnRemove.addActionListener(e -> btnRemoveOnClick());
        
        // Enable Save on text changes
        addChangeListeners(tfSerialNumber);
        addChangeListeners(tfName);
        
        // Live Search
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = tfSearch.getText().trim().toLowerCase();
                performSearch(query);
            }
        });
        
        // Double-click on table row to open FrmWine details
        wineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = wineTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int wineId = (int) wineTableModel.getValueAt(selectedRow, 0);
                        openWineDetails(wineId);
                    }
                }
            }
        });
    }
    
    /**
     * Open the FrmWine for the double-clicked record.
     */
    private void openWineDetails(int wineId) {
        SwingUtilities.invokeLater(() -> {
            FrmWine wineFrame = new FrmWine(userRole);
            wineFrame.setVisible(true);
            wineFrame.displayWineById(wineId);
        });
    }
    
    /**
     * Add DocumentListeners to re-enable the Save button when text changes.
     */
    private void addChangeListeners(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { enableSaveButton(); }
            @Override public void removeUpdate(DocumentEvent e) { enableSaveButton(); }
            @Override public void changedUpdate(DocumentEvent e) { enableSaveButton(); }
        });
    }
    
    /**
     * Fetch data and refresh UI.
     */
    private void fetchAndRefresh() {
        wineTypeArray = WineTypeControl.getInstance().getWineTypes();
        currentWineType = (!wineTypeArray.isEmpty()) ? 1 : null;
        inAddMode = (currentWineType == null);
        refreshControls();
    }
    
    /**
     * Refresh UI controls.
     */
    private void refreshControls() {
        refreshNavigation();
        refreshWineTypeFields();
        refreshDataButtons();
        
        if (!inAddMode && currentWineType != null) {
            WineType wt = wineTypeArray.get(currentWineType - 1);
            refreshWineTableForWineType(wt);
        } else {
            wineTableModel.setRowCount(0);
        }
    }
    
    /**
     * Refresh navigation label and button states.
     */
    private void refreshNavigation() {
        tfNavigation.setText(
            (!inAddMode)
                ? (currentWineType + " of " + wineTypeArray.size())
                : ((wineTypeArray.size() + 1) + " of " + (wineTypeArray.size() + 1))
        );
        
        btnFirst.setEnabled(currentWineType != null && currentWineType > 1);
        btnPrev.setEnabled(currentWineType != null && currentWineType > 1);
        btnNext.setEnabled(currentWineType != null && currentWineType < wineTypeArray.size());
        btnLast.setEnabled(currentWineType != null && currentWineType < wineTypeArray.size());
    }
    
    /**
     * Load current WineType's data into fields.
     */
    private void refreshWineTypeFields() {
        WineType wt = (!inAddMode && currentWineType != null)
                ? wineTypeArray.get(currentWineType - 1)
                : null;
        tfWineTypeID.setText((wt != null) ? String.valueOf(wt.getWineTypeID()) : "(NEW)");
        tfSerialNumber.setText((wt != null) ? String.valueOf(wt.getWineTypeSerialNumber()) : "");
        tfName.setText((wt != null) ? wt.getName() : "");
    }
    
    /**
     * Enable or disable Save, Add, Remove buttons.
     */
    private void refreshDataButtons() {
        boolean hasRecord = (currentWineType != null && currentWineType > 0);
        btnSave.setEnabled(inAddMode || hasRecord);
        btnAdd.setEnabled(!inAddMode);
        btnRemove.setEnabled(!inAddMode && hasRecord);
        if("Sales".equalsIgnoreCase(userRole)) {
            btnAdd.setEnabled(false);
            btnSave.setEnabled(false);
            btnRemove.setEnabled(false);}
    }
    
    
    /**
     * Populate the table based on the given WineType.
     */
    private void refreshWineTableForWineType(WineType wineType) {
        wineTableModel.setRowCount(0);
        if (wineType == null) return;
        
        ArrayList<Wine> wines = WineControl.getInstance().getWinesByWineTypeId(wineType.getWineTypeID());
        for (Wine w : wines) {
            wineTableModel.addRow(new Object[] {
                w.getWineId(),
                w.getName(),
                w.getProductionYear(),
                w.getPricePerBottle(),
                w.getDescription(),
                w.getSweetnessLevel(),
                w.getCatalogNumber(),
                w.getPhotoPath()
            });
        }
    }
    
    // ---- Navigation Handlers ----
    private void btnFirstOnClick() {
        currentWineType = 1;
        inAddMode = false;
        refreshControls();
    }
    
    private void btnPrevOnClick() {
        if (currentWineType > 1) {
            currentWineType--;
        }
        inAddMode = false;
        refreshControls();
    }
    
    private void btnNextOnClick() {
        if (currentWineType < wineTypeArray.size()) {
            currentWineType++;
        }
        inAddMode = false;
        refreshControls();
    }
    
    private void btnLastOnClick() {
        currentWineType = wineTypeArray.size();
        inAddMode = false;
        refreshControls();
    }
    
    /**
     * Search by partial name or serial number.
     */
    private void performSearch(String query) {
        if (query.isEmpty()) {
            fetchAndRefresh();
            return;
        }
        ArrayList<WineType> fullList = WineTypeControl.getInstance().getWineTypes();
        ArrayList<WineType> filtered = new ArrayList<>();
        
        for (WineType wt : fullList) {
            String nameLower = (wt.getName() != null) ? wt.getName().toLowerCase() : "";
            String serialText = String.valueOf(wt.getWineTypeSerialNumber());
            
            if (nameLower.contains(query) || serialText.contains(query)) {
                filtered.add(wt);
            }
        }
        
        if (!filtered.isEmpty()) {
            this.wineTypeArray = filtered;
            currentWineType = 1;
            inAddMode = false;
            refreshControls();
        } else {
            JOptionPane.showMessageDialog(this, "No results found for \"" + query + "\"");
            tfSearch.setText("");
        }
    }
    
    // ---- Button Clicks for Data ----
    private void btnSaveOnClick() {
        try {
            if (tfSerialNumber.getText().isEmpty() || tfName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int wineTypeID = inAddMode ? 0 : Integer.parseInt(tfWineTypeID.getText().replace("(NEW)", "0"));
            int serialNum = Integer.parseInt(tfSerialNumber.getText());
            
            WineType wt = new WineType(wineTypeID, serialNum, tfName.getText());
            boolean success;
            if (inAddMode) {
                success = WineTypeControl.getInstance().addWineType(wt);
                if (success) {
                    JOptionPane.showMessageDialog(this, "WineType added successfully.");
                }
            } else {
                success = WineTypeControl.getInstance().updateWineType(wt);
                if (success) {
                    JOptionPane.showMessageDialog(this, "WineType updated successfully.");
                }
            }
            
            if (success) {
                fetchAndRefresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save WineType.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Serial Number (and ID if needed).", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void btnAddOnClick() {
        inAddMode = true;
        currentWineType = wineTypeArray.size();
        refreshControls();
    }
    
    private void btnRemoveOnClick() {
        if (currentWineType != null) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this WineType?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                int wineTypeId = Integer.parseInt(tfWineTypeID.getText().replace("(NEW)", "0"));
                boolean success = WineTypeControl.getInstance().deleteWineType(wineTypeId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "WineType deleted successfully.");
                    fetchAndRefresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete WineType.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
  
    private void enableSaveButton() {
        btnSave.setEnabled(true);
    }
    private void customizeForRole() {
        
		if("Sales".equalsIgnoreCase(userRole)) {
        	  btnAdd.setEnabled(false);
        	  btnSave.setEnabled(false);
              btnRemove.setEnabled(false);
        }
        
            
        }
}
