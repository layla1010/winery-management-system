package boundary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import control.WineTypeControl;
import entity.Occasion;
import enums.Location;
import enums.Season;
import util.JTextFieldLimiter;

public class FrmOccasion extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Common colors and fonts
    private final Color pink = new Color(194, 65, 78);

    private final Color BG_PANEL = new Color(105, 20, 56);
    private final Color BG_FIELD = new Color(217, 166, 165);
    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 12);
    private final Color COLOR_BORDER_TEXT = new Color(234, 171, 55);
    
    // Data and state variables
    private ArrayList<Occasion> occasionArray;
    private Integer currentOccasion;
    private boolean inAddMode;
    
    // GUI Components
    private JTextField tfOccasionID, tfOccasionName, tfNavigation, tfSearch;
    private JComboBox<Season> cbSeason;
    private JComboBox<Location> cbLocation;
    private JTextArea taDescription;
    private JButton btnFirst, btnPrev, btnNext, btnLast, btnSave, btnAdd, btnRemove;
    
    public FrmOccasion() {
        setLayout(null); // absolute layout for the main panel
        setBackground(new Color(105, 20, 56));
        // Set a preferred size (you can change this later)
        setPreferredSize(new Dimension(550, 360));
        
        // Initialize buttons
        btnFirst = createNavButton("|<", pink);
        btnPrev = createNavButton("<<", pink);
        btnNext = createNavButton(">>", pink);
        btnLast = createNavButton(">|", pink);
        btnSave = createButton("Save", pink);
        btnAdd = createButton("Add New", pink);
        btnRemove = createButton("Delete", pink);
        
        initUI();
        fetchAndRefresh();
        setupEventHandlers();
    }
    
    private void initUI() {
        // === DETAILS PANEL ===
        JPanel pnlDetails = new JPanel(null);
        pnlDetails.setBackground(BG_PANEL);
        pnlDetails.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(            new Color(234, 171, 55)
),
            "Occasion Details",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            FONT_LABEL,
            new Color(234, 171, 55)
        ));
        pnlDetails.setBounds(10, 10, 670, 150);        
        // Occasion ID row
        JLabel lblOccasionID = new JLabel("Occasion ID:");
        lblOccasionID.setForeground(new Color(234, 171, 55));
        lblOccasionID.setBounds(10, 20, 80, 20);
        pnlDetails.add(lblOccasionID);
        tfOccasionID = createTextField(false);
        tfOccasionID.setBounds(100, 20, 550, 20);
        pnlDetails.add(tfOccasionID);
        
        // Name row
        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(new Color(234, 171, 55));
        lblName.setBounds(10, 50, 80, 20);
        pnlDetails.add(lblName);
        tfOccasionName = createTextField(true);
        tfOccasionName.setBounds(100, 50, 550, 20);
        pnlDetails.add(tfOccasionName);
        
        // Season row
        JLabel lblSeason = new JLabel("Season:");
        lblSeason.setForeground(new Color(234, 171, 55));
        lblSeason.setBounds(10, 80, 80, 20);
        pnlDetails.add(lblSeason);
        cbSeason = new JComboBox<>(Season.values());
        cbSeason.setBackground(BG_FIELD);
        cbSeason.setBounds(100, 80, 550, 20);
        pnlDetails.add(cbSeason);
        
        // Location row
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setForeground(new Color(234, 171, 55));
        lblLocation.setBounds(10, 110, 80, 20);
        pnlDetails.add(lblLocation);
        cbLocation = new JComboBox<>(Location.values());
        cbLocation.setBackground(BG_FIELD);
        cbLocation.setBounds(100, 110, 550, 20);
        pnlDetails.add(cbLocation);
        
        add(pnlDetails);
        
        // === DESCRIPTION PANEL ===
        JPanel pnlDescription = new JPanel(null);
        pnlDescription.setBackground(BG_PANEL);
        pnlDescription.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(234, 171, 55)),
            "Description",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            FONT_LABEL,
            COLOR_BORDER_TEXT
        ));
        pnlDescription.setBounds(10, 220, 670, 320);
        
        taDescription = new JTextArea();
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        taDescription.setBackground(BG_FIELD);
        JScrollPane spDescription = new JScrollPane(taDescription);
        spDescription.setBounds(10, 20, 640, 280);
        pnlDescription.add(spDescription);
        
        add(pnlDescription);
        
        // === BUTTON PANEL ===
        JPanel pnlButtons = new JPanel(null);
        pnlButtons.setBackground(BG_PANEL);
        pnlButtons.setBounds(290, 10, 120, 130);
        
        btnAdd.setBounds(10, 10, 100, 30);
        pnlButtons.add(btnAdd);
        btnRemove.setBounds(10, 50, 100, 30);
        pnlButtons.add(btnRemove);
        btnSave.setBounds(10, 90, 100, 30);
        pnlButtons.add(btnSave);
        
        add(pnlButtons);
        
        // === NAVIGATION PANEL ===
        
        
        tfSearch = new JTextField();
        tfSearch.setToolTipText("Search occasions...");
        tfSearch.setPreferredSize(new Dimension(100, 25));
        
        btnFirst.setBounds(170, 10, 50, 25);
        btnPrev.setBounds(230, 10, 50, 25);
        
        tfNavigation = new JTextField();
        tfNavigation.setEditable(false);
        tfNavigation.setHorizontalAlignment(JTextField.CENTER);
        tfNavigation.setBounds(290, 10, 80, 25);
        
        btnNext.setBounds(380, 10, 50, 25);
        btnLast.setBounds(440, 10, 50, 25);
        
        
;        
        

		JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlControls.setBackground(BG_PANEL);
		pnlControls.setBounds(10, 170, 670, 50); 
		
		pnlControls.add(btnFirst);
		pnlControls.add(btnNext);
		pnlControls.add(btnAdd);
		pnlControls.add(btnSave);
		pnlControls.add(btnRemove);
		pnlControls.add(tfNavigation);
		pnlControls.add(tfSearch);
		pnlControls.add(btnPrev);
		pnlControls.add(btnLast);

		
		add(pnlControls);        

        
    }
    
    // Helper method to create a text field
    private JTextField createTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setEditable(editable);
        tf.setBackground(BG_FIELD);
        if (editable) tf.setDocument(new JTextFieldLimiter(100));
        return tf;
    }
    
    // Helper methods to create buttons
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return btn;
    }
    
    private JButton createNavButton(String text, Color bg) {
        return createButton(text, bg);
    }
    
    // Setup event handlers
    private void setupEventHandlers() {
        btnFirst.addActionListener(e -> { 
            if (!occasionArray.isEmpty()) { 
                currentOccasion = 1; 
                refreshUI(); 
            }
        });
        btnLast.addActionListener(e -> { 
            if (!occasionArray.isEmpty()) { 
                currentOccasion = occasionArray.size(); 
                refreshUI(); 
            }
        });
        btnPrev.addActionListener(e -> navigate(-1));
        btnNext.addActionListener(e -> navigate(1));
        btnSave.addActionListener(e -> saveOccasion());
        btnAdd.addActionListener(e -> addNewOccasion());
        btnRemove.addActionListener(e -> deleteOccasion());
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchOccasions(tfSearch.getText().trim().toLowerCase());
            }
        });
    }
    
    private void navigate(int step) {
        if (currentOccasion != null) {
            int newIndex = currentOccasion + step;
            if (newIndex >= 1 && newIndex <= occasionArray.size()) {
                currentOccasion = newIndex;
                refreshUI();
            }
        }
    }
    
    private void fetchAndRefresh() {
        occasionArray = WineTypeControl.getInstance().getOccasions();
        currentOccasion = occasionArray.isEmpty() ? null : 1;
        inAddMode = (currentOccasion == null);
        refreshUI();
    }
    
    private void refreshUI() {
        refreshNavigation();
        refreshFormFields();
        refreshButtonStates();
    }
    
    private void refreshNavigation() {
        if (occasionArray.isEmpty()) {
            tfNavigation.setText("0 of 0");
        } else {
            String navText = inAddMode 
                ? (occasionArray.size() + 1) + " of " + (occasionArray.size() + 1)
                : currentOccasion + " of " + occasionArray.size();
            tfNavigation.setText(navText);
            btnFirst.setEnabled(!inAddMode && currentOccasion > 1);
            btnPrev.setEnabled(!inAddMode && currentOccasion > 1);
            btnNext.setEnabled(!inAddMode && currentOccasion < occasionArray.size());
            btnLast.setEnabled(!inAddMode && currentOccasion < occasionArray.size());
        }
    }
    
    private void refreshFormFields() {
        if (inAddMode || occasionArray.isEmpty()) {
            tfOccasionID.setText("(NEW)");
            tfOccasionName.setText("");
            cbSeason.setSelectedIndex(0);
            cbLocation.setSelectedIndex(0);
            taDescription.setText("");
        } else {
            Occasion current = occasionArray.get(currentOccasion - 1);
            tfOccasionID.setText(String.valueOf(current.getOccasionId()));
            tfOccasionName.setText(current.getOccasionName());
            cbSeason.setSelectedItem(current.getSeason());
            cbLocation.setSelectedItem(current.getLocation());
            taDescription.setText(current.getDescription());
        }
    }
    
    private void refreshButtonStates() {
        btnSave.setEnabled(true);
        btnAdd.setEnabled(!inAddMode);
        btnRemove.setEnabled(!inAddMode && !occasionArray.isEmpty());
    }
    
    private void saveOccasion() {
        if (!validateForm()) return;
        Occasion occasion = new Occasion(
            (Season) cbSeason.getSelectedItem(),
            (Location) cbLocation.getSelectedItem(),
            inAddMode ? 0 : occasionArray.get(currentOccasion - 1).getOccasionId(),
            taDescription.getText(),
            tfOccasionName.getText()
        );
        boolean success = inAddMode 
            ? WineTypeControl.getInstance().addOccasion(occasion)
            : WineTypeControl.getInstance().updateOccasion(occasion);
        if (success) {
            JOptionPane.showMessageDialog(this, "Operation successful", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            fetchAndRefresh();
        } else {
            showError("Operation failed");
        }
    }
    
    private boolean validateForm() {
        if (tfOccasionName.getText().trim().isEmpty()) {
            showError("Name is required");
            return false;
        }
        return true;
    }
    
    private void addNewOccasion() {
        inAddMode = true;
        currentOccasion = occasionArray.size() + 1;
        refreshUI();
    }
    
    private void deleteOccasion() {
        if (inAddMode || occasionArray.isEmpty()) return;
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this occasion?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean success = WineTypeControl.getInstance()
                .deleteOccasion(occasionArray.get(currentOccasion - 1).getOccasionId());
            if (success) {
                fetchAndRefresh();
            } else {
                showError("Deletion failed");
            }
        }
    }
    
    private void searchOccasions(String query) {
        if (query.isEmpty()) {
            fetchAndRefresh();
            return;
        }
        ArrayList<Occasion> results = new ArrayList<>();
        for (Occasion o : WineTypeControl.getInstance().getOccasions()) {
            if (matchesSearch(o, query)) results.add(o);
        }
        if (!results.isEmpty()) {
            occasionArray = results;
            currentOccasion = 1;
            refreshUI();
        } else {
            showError("No matches found for: " + query);
            tfSearch.setText("");
        }
    }
    
    private boolean matchesSearch(Occasion occasion, String query) {
        return occasion.getOccasionName().toLowerCase().contains(query) ||
               occasion.getDescription().toLowerCase().contains(query) ||
               occasion.getSeason().toString().toLowerCase().contains(query) ||
               occasion.getLocation().toString().toLowerCase().contains(query);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
