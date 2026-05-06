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
import control.ManufacturerControl;
import entity.Manufacturer;
import util.JTextFieldLimiter;

public class FrmManufacturer extends JPanel {
    private ArrayList<Manufacturer> manufacturerArray;
    private Integer currentManufacturer;
    private boolean inAddMode;

    // GUI Components
    private JPanel contentPane;
    private JTextField tfManufacturerID;
    private JTextField tfName;
    private JTextField tfAddress;
    private JTextField tfPhoneNumber;
    private JTextField tfEmail;
    private JTextField tfNavigation;
    private JButton btnFirst = new JButton("|<");
    private JButton btnPrev = new JButton("<<");
    private JButton btnNext = new JButton(">>");
    private JButton btnLast = new JButton("|>");
    private final JButton btnSave = new JButton("Save");
    private final JButton btnAdd = new JButton("Add New");
    private final JButton btnRemove = new JButton("Delete");
    private JTextField tfSearch; 

    // Panel to display manufacturer details
    private FrmManufacturerDetails manufacturerDetailsPanel;

    

    public FrmManufacturer() {
        // Instead of using JFrame methods, set the preferred size and layout for this panel.
        this.setBackground(new Color(81, 23, 48));
        setLayout(null);
        setPreferredSize(new Dimension(765, 600));
        initComponents();
        fetchAndRefresh();
        createEvents();
    }

    // Initializes components and lays them out inside a content pane.
    private void initComponents() {
        // Create a content pane to simulate the JFrame's content pane
        contentPane = new JPanel();
        contentPane.setBackground(new Color(105, 3, 56));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        add(contentPane, BorderLayout.CENTER);
        contentPane.setBounds(50, 10, 765, 600);

        // Panel for manufacturer details
        JPanel pnlManufacturerDetails = new JPanel();
        pnlManufacturerDetails.setForeground(new Color(128, 128, 128));
        pnlManufacturerDetails.setBackground(new Color(72, 2, 39));
        pnlManufacturerDetails.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
        		new Color(166, 84, 97), new Color(166, 84, 97)), 
                "Manufacturer Details", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(166, 84, 97)));
        pnlManufacturerDetails.setBounds(44, 20, 650, 200);
        contentPane.add(pnlManufacturerDetails);

        tfManufacturerID = new JTextField();
        tfManufacturerID.setBackground(new Color(166, 84, 97));
        tfManufacturerID.setEditable(false);
        tfManufacturerID.setColumns(10);

        tfName = new JTextField();
        tfName.setBackground(new Color(166, 84, 97));
        tfName.setColumns(10);
        tfName.setDocument(new JTextFieldLimiter(100));

        tfAddress = new JTextField();
        tfAddress.setBackground(new Color(166, 84, 97));
        tfAddress.setColumns(10);

        tfPhoneNumber = new JTextField();
        tfPhoneNumber.setBackground(new Color(166, 84, 97));
        tfPhoneNumber.setColumns(10);

        tfEmail = new JTextField();
        tfEmail.setBackground(new Color(166, 84, 97));
        tfEmail.setColumns(10);

        JLabel lblManufacturerID = new JLabel("Manufacturer ID:");
        lblManufacturerID.setForeground(new Color(166, 84, 97));
        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(new Color(166, 84, 97));
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setForeground(new Color(166, 84, 97));
        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setForeground(new Color(166, 84, 97));
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(new Color(166, 84, 97));

        GroupLayout gl_pnlManufacturerDetails = new GroupLayout(pnlManufacturerDetails);
        gl_pnlManufacturerDetails.setHorizontalGroup(
            gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_pnlManufacturerDetails.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblManufacturerID)
                    .addComponent(lblName)
                    .addComponent(lblAddress)
                    .addComponent(lblPhoneNumber)
                    .addComponent(lblEmail))
                .addGap(18)
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfManufacturerID)
                    .addComponent(tfName)
                    .addComponent(tfAddress)
                    .addComponent(tfPhoneNumber)
                    .addComponent(tfEmail, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_pnlManufacturerDetails.setVerticalGroup(
            gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_pnlManufacturerDetails.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblManufacturerID)
                    .addComponent(tfManufacturerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAddress)
                    .addComponent(tfAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhoneNumber)
                    .addComponent(tfPhoneNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_pnlManufacturerDetails.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlManufacturerDetails.setLayout(gl_pnlManufacturerDetails);

        // Action button panel
        JPanel pnlActionBtn = new JPanel();
        pnlActionBtn.setBackground(new Color(81, 23, 48));
        pnlActionBtn.setBounds(44, 231, 650, 42);
        contentPane.add(pnlActionBtn);
        pnlActionBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnFirst.setPreferredSize(new Dimension(48, 23));
        btnFirst.setMinimumSize(new Dimension(40, 23));
        btnFirst.setMaximumSize(new Dimension(60, 23));
        btnFirst.setForeground(new Color(202, 187, 149));
        btnFirst.setBorder(null);
        btnFirst.setBackground(new Color(194, 65, 78));
        pnlActionBtn.add(btnFirst);
        
        btnPrev.setPreferredSize(new Dimension(50, 23));
        btnPrev.setBorder(null);
        btnPrev.setBackground(new Color(194, 65, 78));
        pnlActionBtn.add(btnPrev);

        tfNavigation = new JTextField();
        tfNavigation.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfNavigation.setColumns(9);
        tfNavigation.setEditable(false);
        pnlActionBtn.add(tfNavigation);
        
        btnNext.setBackground(new Color(194, 65, 78));
        btnNext.setPreferredSize(new Dimension(50, 23));
        btnNext.setBorder(null);
        pnlActionBtn.add(btnNext);
        
        btnLast.setForeground(new Color(202, 187, 149));
        btnLast.setBackground(new Color(194, 65, 78));
        btnLast.setBorder(null);
        btnLast.setMaximumSize(new Dimension(48, 23));
        btnLast.setPreferredSize(new Dimension(48, 23));
        pnlActionBtn.add(btnLast);

        tfSearch = new JTextField();
        tfSearch.setBackground(new Color(217, 166, 165));
        tfSearch.setBorder(new TitledBorder(null, "Search", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        tfSearch.setColumns(8);
        tfSearch.setToolTipText("Search by Name, Address, Phone, or Email...");
        pnlActionBtn.add(tfSearch);
        
        btnSave.setBackground(new Color(169, 84, 84));
        btnSave.setPreferredSize(new Dimension(56, 23));
        btnSave.setBorder(null);
        pnlActionBtn.add(btnSave);
        
        btnAdd.setPreferredSize(new Dimension(70, 23));
        btnAdd.setBackground(new Color(196, 84, 84));
        btnAdd.setBorder(null);
        pnlActionBtn.add(btnAdd);
        
        btnRemove.setPreferredSize(new Dimension(61, 23));
        btnRemove.setBackground(new Color(196, 84, 84));
        btnRemove.setBorder(null);
        pnlActionBtn.add(btnRemove);

        // Manufacturer details panel
        manufacturerDetailsPanel = new FrmManufacturerDetails();
        manufacturerDetailsPanel.setBackground(new Color(179, 6, 97));
        manufacturerDetailsPanel.setBounds(20, 284, 696, 231);
        contentPane.add(manufacturerDetailsPanel);
    }

    private void createEvents() {
        btnFirst.addActionListener(e -> btnFirstOnClick());
        btnPrev.addActionListener(e -> btnPrevOnClick());
        btnNext.addActionListener(e -> btnNextOnClick());
        btnLast.addActionListener(e -> btnLastOnClick());
        btnSave.addActionListener(e -> btnSaveOnClick());
        btnAdd.addActionListener(e -> btnAddOnClick());
        btnRemove.addActionListener(e -> btnRemoveOnClick());
        addChangeListeners(tfName);
        addChangeListeners(tfAddress);
        addChangeListeners(tfPhoneNumber);
        addChangeListeners(tfEmail);
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = tfSearch.getText().trim().toLowerCase();
                performSearch(query);
            }
        });
    }

    private void addChangeListeners(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSaveButton();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSaveButton();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSaveButton();
            }
        });
    }

    private void fetchAndRefresh() {
        manufacturerArray = ManufacturerControl.getInstance().getManufacturers();
        currentManufacturer = (!manufacturerArray.isEmpty()) ? 1 : null;
        inAddMode = (currentManufacturer == null);
        refreshControls();
    }

    private void refreshControls() {
        refreshNavigation();
        refreshManufacturerFields();
        refreshDataButtons();

        if (currentManufacturer != null && !manufacturerArray.isEmpty()) {
            Manufacturer manufacturer = manufacturerArray.get(currentManufacturer - 1);
            manufacturerDetailsPanel.setManufacturer(manufacturer);
        }
    }

    private void refreshNavigation() {
        tfNavigation.setText((!inAddMode) ?
            currentManufacturer + " of " + manufacturerArray.size() :
            (manufacturerArray.size() + 1) + " of " + (manufacturerArray.size() + 1));

        btnFirst.setEnabled(currentManufacturer != null && currentManufacturer > 1);
        btnPrev.setEnabled(currentManufacturer != null && currentManufacturer > 1);
        btnNext.setEnabled(currentManufacturer != null && currentManufacturer < manufacturerArray.size());
        btnLast.setEnabled(currentManufacturer != null && currentManufacturer < manufacturerArray.size());
    }

    private void refreshManufacturerFields() {
        Manufacturer manufacturer = (!inAddMode) ? manufacturerArray.get(currentManufacturer - 1) : null;

        tfManufacturerID.setText((manufacturer != null) ? String.valueOf(manufacturer.getManufacturerId()) : "(NEW)");
        tfName.setText((manufacturer != null) ? manufacturer.getName() : "");
        tfAddress.setText((manufacturer != null) ? manufacturer.getAddress() : "");
        tfPhoneNumber.setText((manufacturer != null) ? manufacturer.getPhoneNumber() : "");
        tfEmail.setText((manufacturer != null) ? manufacturer.getEmail() : "");
    }

    private void refreshDataButtons() {
        btnSave.setEnabled(inAddMode);
        btnAdd.setEnabled(!inAddMode);
        btnRemove.setEnabled(!inAddMode);
    }

    private void btnFirstOnClick() {
        currentManufacturer = 1;
        refreshControls();
    }

    private void btnPrevOnClick() {
        if (currentManufacturer > 1) currentManufacturer--;
        refreshControls();
    }

    private void btnNextOnClick() {
        if (currentManufacturer < manufacturerArray.size()) currentManufacturer++;
        refreshControls();
    }

    private void btnLastOnClick() {
        currentManufacturer = manufacturerArray.size();
        refreshControls();
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            fetchAndRefresh();
            return;
        }
        ArrayList<Manufacturer> filteredManufacturers = new ArrayList<>();
        for (Manufacturer manufacturer : ManufacturerControl.getInstance().getManufacturers()) {
            if (manufacturer.getName().toLowerCase().contains(query) ||
                (manufacturer.getAddress() != null && manufacturer.getAddress().toLowerCase().contains(query)) ||
                (manufacturer.getPhoneNumber() != null && manufacturer.getPhoneNumber().toLowerCase().contains(query)) ||
                (manufacturer.getEmail() != null && manufacturer.getEmail().toLowerCase().contains(query))) {

                filteredManufacturers.add(manufacturer);
            }
        }
        if (!filteredManufacturers.isEmpty()) {
            manufacturerArray = filteredManufacturers;
            currentManufacturer = 1;
            refreshControls();
        } else {
            JOptionPane.showMessageDialog(this, "No results found for \"" + query + "\"");
            tfSearch.setText("");
        }
    }

    private void btnSaveOnClick() {
        try {
            if (tfName.getText().isEmpty() || tfAddress.getText().isEmpty() ||
                tfPhoneNumber.getText().isEmpty() || tfEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int manufacturerId = inAddMode ? 0 : Integer.parseInt(tfManufacturerID.getText());
            Manufacturer manufacturer = new Manufacturer(
                    manufacturerId,
                    tfName.getText(),
                    tfAddress.getText(),
                    tfPhoneNumber.getText(),
                    tfEmail.getText()
            );
            boolean success;
            if (inAddMode) {
                success = ManufacturerControl.getInstance().addManufacturer(manufacturer);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Manufacturer added successfully.");
                }
            } else {
                success = ManufacturerControl.getInstance().updateManufacturer(manufacturer);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Manufacturer updated successfully.");
                }
            }
            if (success) {
                fetchAndRefresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save manufacturer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for Manufacturer ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void enableSaveButton() {
        btnSave.setEnabled(true);
    }

    private void btnAddOnClick() {
        inAddMode = true;
        currentManufacturer = manufacturerArray.size();
        refreshControls();
    }

    private void btnRemoveOnClick() {
        if (currentManufacturer != null) {
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this manufacturer?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmation == JOptionPane.YES_OPTION) {
                int manufacturerId = Integer.parseInt(tfManufacturerID.getText());
                boolean success = ManufacturerControl.getInstance().deleteManufacturer(manufacturerId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Manufacturer deleted successfully.");
                    fetchAndRefresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete manufacturer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
