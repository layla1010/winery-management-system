package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import entity.*;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import control.ManufacturerControl;
import control.WineControl;
import control.WineTypeControl;
import entity.Manufacturer;
import entity.Wine;
import enums.SweetnessLevel;
import util.JTextFieldLimiter;

public class FrmWine extends JPanel {

    private static final long serialVersionUID = 1L;
    private ArrayList<Wine> wineArray;
    private Integer currentWine;
    private boolean inAddMode;

    // GUI Components
    private JPanel contentPane;
    private JTextField tfWineID;
    private JTextField tfWineName;
    private JTextField tfProductionYear;
    private JTextField tfPricePerBottle;
    private JTextField tfPhotoPath;
    private JTextField tfNavigation;
    private JComboBox<SweetnessLevel> cbSweetnessLevel;
    private JTextArea taDescription;
    private JButton btnFirst = new JButton("|<");
    private JButton btnPrev = new JButton("<<");
    private JButton btnNext = new JButton(">>");
    private JButton btnLast = new JButton(">|");
    private final JButton btnSave = new JButton("Save");
    private final JButton btnAdd = new JButton("Add New");
    private final JButton btnRemove = new JButton("Delete");
    private JTextField tfSearch;
    private JPanel pnlWineDetails = new JPanel();
    private JPanel pnlActionBtn;
    private JTextField tfManufacturer;
    private JLabel lblPhoto;
    private String userRole;
    JComboBox<WineType> cbWineType;
    private JTextField tfCatalogNumber;
	// Panel to display wine details
    public static void main(String[] args) {
    	String s="Admin";
        EventQueue.invokeLater(() -> {
            try {
            	
                FrmWine frame = new FrmWine(s);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FrmWine(String userRole) {
    	this.userRole=userRole;
        setPreferredSize(new Dimension(900, 700));
        setLayout(new BorderLayout());
        this.setBackground(new Color(105, 20, 56));
        initComponents();
        fetchAndRefresh();
        createEvents();
        customizeForRole();
    }
 // This method contain all the code for creating and
 		// initializing components
    private void initComponents() {
    	
        contentPane = new JPanel();
        contentPane.setBackground(new Color(105, 20, 56));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        add(contentPane, BorderLayout.CENTER);


        
       
        pnlWineDetails.setBackground(new Color(105, 20, 56));

        pnlWineDetails.setBorder(new TitledBorder(new LineBorder(new Color(234, 171, 55)), "Wine Details", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(234, 171, 55)));
        pnlWineDetails.setBounds(68, 33, 320, 390);
        contentPane.add(pnlWineDetails);

        tfWineID = new JTextField();
        tfWineID.setBackground(new Color(217, 166, 165));
        tfWineID.setEditable(false);
        tfWineID.setColumns(10);

        tfWineName = new JTextField();
        tfWineName.setBackground(new Color(217, 166, 165));
        tfWineName.setColumns(10);
        tfWineName.setDocument(new JTextFieldLimiter(100));

        tfProductionYear = new JTextField();
        tfProductionYear.setBackground(new Color(217, 166, 165));
        tfProductionYear.setColumns(10);

        tfSearch = new JTextField();
        tfSearch.setBounds(5, 5, 105, 36);
        tfSearch.setBackground(new Color(217, 166, 165));
        tfSearch.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Search", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        tfSearch.setForeground(new Color(128, 128, 128));
        tfSearch.setColumns(8);
        tfSearch.setToolTipText("Search wines...");

        tfPricePerBottle = new JTextField();
        tfPricePerBottle.setBackground(new Color(217, 166, 165));
        tfPricePerBottle.setColumns(10);

        cbSweetnessLevel = new JComboBox<>(SweetnessLevel.values());
        cbSweetnessLevel.setBackground(new Color(217, 166, 165));
        cbWineType=new JComboBox<>();
        populateWineTypes();

        tfManufacturer = new JTextField();
        tfManufacturer.setBackground(new Color(217, 166, 165));
        tfManufacturer.setText((String) null);
        tfManufacturer.setColumns(10);

        tfCatalogNumber = new JTextField();
        tfCatalogNumber.setBackground(new Color(217, 166, 165));
        tfCatalogNumber.setText((String) null);
        tfCatalogNumber.setColumns(10);

        tfPhotoPath = new JTextField();
        tfPhotoPath.setBackground(new Color(217, 166, 165));
        tfPhotoPath.setColumns(10);

        JLabel lblWineID = new JLabel("Wine ID:");
        lblWineID.setForeground(new Color(234, 171, 55));
        JLabel lblWineName = new JLabel("Wine Name:");
        lblWineName.setForeground(new Color(234, 171, 55));
        JLabel lblProductionYear = new JLabel("Production Year:");
        lblProductionYear.setForeground(new Color(234, 171, 55));
        JLabel lblPricePerBottle = new JLabel("Price Per Bottle:");
        lblPricePerBottle.setForeground(new Color(234, 171, 55));
        JLabel lblSweetnessLevel = new JLabel("Sweetness Level:");
        lblSweetnessLevel.setForeground(new Color(234, 171, 55));
        JLabel lblManufacturer = new JLabel("ManufacturerID:");
        lblManufacturer.setForeground(new Color(234, 171, 55));

        JLabel lblCatalogNumber = new JLabel("Catalog:");
        lblCatalogNumber.setForeground(new Color(234, 171, 55));

        JLabel lblPhotoPath = new JLabel("Photo Path:");
        lblPhotoPath.setForeground(new Color(234, 171, 55));

        
        
        cbWineType.setBackground(new Color(217, 166, 165));
        
        JLabel lblWineType = new JLabel("WineType:");
        lblWineType.setForeground(new Color(234, 171, 55));





        GroupLayout gl_pnlWineDetails = new GroupLayout(pnlWineDetails);
        gl_pnlWineDetails.setHorizontalGroup(
        	gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_pnlWineDetails.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblWineID)
        				.addComponent(lblWineName)
        				.addComponent(lblProductionYear)
        				.addComponent(lblPricePerBottle)
        				.addComponent(lblSweetnessLevel)
        				.addComponent(lblManufacturer)
        				.addComponent(lblCatalogNumber)
        				.addComponent(lblPhotoPath)
        				.addComponent(lblWineType, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(tfCatalogNumber, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(tfManufacturer, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(cbSweetnessLevel, 0, 88, Short.MAX_VALUE)
        					.addComponent(tfPricePerBottle, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(tfProductionYear, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(tfWineName, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(tfWineID, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        					.addComponent(tfPhotoPath))
        				.addComponent(cbWineType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(110, Short.MAX_VALUE))
        );
        gl_pnlWineDetails.setVerticalGroup(
        	gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_pnlWineDetails.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblWineID)
        				.addComponent(tfWineID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblWineName)
        				.addComponent(tfWineName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblProductionYear)
        				.addComponent(tfProductionYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblPricePerBottle)
        				.addComponent(tfPricePerBottle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblSweetnessLevel)
        				.addComponent(cbSweetnessLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(tfManufacturer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblManufacturer))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblCatalogNumber)
        				.addComponent(tfCatalogNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPhotoPath)
        				.addComponent(tfPhotoPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_pnlWineDetails.createParallelGroup(Alignment.BASELINE)
        				.addComponent(cbWineType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblWineType))
        			.addContainerGap(39, Short.MAX_VALUE))
        );
        pnlWineDetails.setLayout(gl_pnlWineDetails);

        pnlActionBtn = new JPanel();
        pnlActionBtn.setBackground(new Color(105, 20, 56));
        pnlActionBtn.setBounds(68, 434, 698, 48);
        contentPane.add(pnlActionBtn);
        pnlActionBtn.setLayout(null);
        pnlActionBtn.add(tfSearch);
        btnFirst.setBounds(114, 11, 48, 23);
        pnlActionBtn.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(234, 171, 55)));


        pnlActionBtn.add(btnFirst);
        btnFirst.setPreferredSize(new Dimension(48, 23));
        btnFirst.setMinimumSize(new Dimension(40, 23));
        btnFirst.setMaximumSize(new Dimension(60, 23));
        btnFirst.setForeground(new Color(202, 187, 149));
        btnFirst.setBorder(null);
        btnFirst.setBackground(new Color(194, 65, 78));
        btnPrev.setBounds(172, 11, 50, 23);

        pnlActionBtn.add(btnPrev);
        btnPrev.setPreferredSize(new Dimension(50, 23));
        btnPrev.setBorder(null);
        btnPrev.setBackground(new Color(194, 65, 78));

        tfNavigation = new JTextField();
        tfNavigation.setBounds(232, 11, 60, 21);
        tfNavigation.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfNavigation.setColumns(8);
        tfNavigation.setEditable(false);
        pnlActionBtn.add(tfNavigation);
        btnNext.setBounds(302, 11, 50, 23);

        pnlActionBtn.add(btnNext);
        btnNext.setBackground(new Color(194, 65, 78));
        btnNext.setPreferredSize(new Dimension(50, 23));
        btnNext.setBorder(null);
        btnLast.setBounds(362, 11, 48, 23);

        pnlActionBtn.add(btnLast);
        btnLast.setForeground(new Color(202, 187, 149));
        btnLast.setBackground(new Color(194, 65, 78));
        btnLast.setBorder(null);
        btnLast.setMaximumSize(new Dimension(48, 23));
        btnLast.setPreferredSize(new Dimension(48, 23));
        btnSave.setBounds(420, 11, 76, 23);

        pnlActionBtn.add(btnSave);
        btnSave.setBackground(new Color(194, 65, 78));
        btnSave.setPreferredSize(new Dimension(56, 23));
        btnSave.setBorder(null);
        btnAdd.setBounds(506, 11, 86, 23);

        pnlActionBtn.add(btnAdd);
        btnAdd.setPreferredSize(new Dimension(70, 23));
        btnAdd.setBackground(new Color(194, 65, 78));
        btnAdd.setBorder(null);
        btnRemove.setBounds(602, 11, 86, 23);

        pnlActionBtn.add(btnRemove);
        btnRemove.setPreferredSize(new Dimension(61, 23));
        btnRemove.setBackground(new Color(194, 65, 78));
        btnRemove.setBorder(null);

        JPanel pnlPhoto = new JPanel();
        pnlPhoto.setBackground(new Color(105, 20, 56));
        pnlPhoto.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(160, 160, 160)), "Photo", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(234, 171, 55)));
        pnlPhoto.setBounds(436, 33, 330, 230);
        contentPane.add(pnlPhoto);

        JPanel pnlDescription = new JPanel();
        pnlDescription.setBackground(new Color(105, 20, 56));
        pnlDescription.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(160, 160, 160)), "Description", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(234, 171, 55)));
        pnlDescription.setBounds(436, 273, 330, 149);
        contentPane.add(pnlDescription);
        pnlDescription.setLayout(new BorderLayout());

        taDescription = new JTextArea();
        taDescription.setBackground(new Color(105, 20, 56));
        taDescription.setForeground(new Color(234, 171, 55));
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        pnlDescription.add(new JScrollPane(taDescription), BorderLayout.CENTER);

        lblPhoto = new JLabel();
        lblPhoto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblPhoto.setPreferredSize(new Dimension(140, 165));
        pnlPhoto.add(lblPhoto);




    }
 // This method contain all the code for creating and
 	// initializing components
     private void createEvents() {
         btnFirst.addActionListener(e -> btnFirstOnClick());
         btnPrev.addActionListener(e -> btnPrevOnClick());
         btnNext.addActionListener(e -> btnNextOnClick());
         btnLast.addActionListener(e -> btnLastOnClick());
         btnSave.addActionListener(e -> btnSaveOnClick());
         btnAdd.addActionListener(e -> btnAddOnClick());
         btnRemove.addActionListener(e -> btnRemoveOnClick());
         addChangeListeners(tfPhotoPath);
         addChangeListeners(tfWineName);
         addChangeListeners(tfProductionYear);
         addChangeListeners(tfPricePerBottle);
         addChangeListeners(tfCatalogNumber);
         addChangeListeners(tfManufacturer);
         cbSweetnessLevel.addActionListener(e -> enableSaveButton());
         
         cbWineType.addActionListener(e -> enableSaveButton());

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
	// fetches wines, and refreshes controls.
    private void fetchAndRefresh() {
        wineArray = WineControl.getInstance().getWines();
        currentWine = (!wineArray.isEmpty()) ? 1 : null;
        inAddMode = (currentWine == null);
        refreshControls();
    }

    private void refreshControls() {
        refreshNavigation();
        refreshWineFields();
        refreshDataButtons();
    }

	// updates the navigation controls.
    private void refreshNavigation() {
        tfNavigation.setText((!inAddMode) ?
                "" + currentWine + " of " + wineArray.size() :
                "" + (wineArray.size() + 1) + " of " + (wineArray.size() + 1));

        btnFirst.setEnabled(currentWine != null && currentWine > 1);
        btnPrev.setEnabled(currentWine != null && currentWine > 1);
        btnNext.setEnabled(currentWine != null && currentWine < wineArray.size());
        btnLast.setEnabled(currentWine != null && currentWine < wineArray.size());
    }

 // updates the wines fields controls with a given wine information.
    private void refreshWineFields() {
        Wine wine = (!inAddMode) ? wineArray.get(currentWine - 1) : null;

        tfWineID.setText((wine != null) ? String.valueOf(wine.getWineId()) : "(NEW)");
        tfWineName.setText((wine != null) ? wine.getName() : null);
        tfProductionYear.setText((wine != null) ? String.valueOf(wine.getProductionYear()) : null);
        taDescription.setText((wine != null) ? wine.getDescription() : null);
        tfPricePerBottle.setText((wine != null) ? String.valueOf(wine.getPricePerBottle()) : null);
        cbSweetnessLevel.setSelectedItem((wine != null) ? SweetnessLevel.valueOf(String.valueOf(wine.getSweetnessLevel()).replaceAll("-", "_").toUpperCase()) : null);
        tfCatalogNumber.setText((wine != null) ? String.valueOf(wine.getCatalogNumber()) : null);
        tfManufacturer.setText((wine != null && wine.getManufacturer() != null) ? String.valueOf(wine.getManufacturer().getManufacturerId()) : null);
        tfPhotoPath.setText((wine != null) ? wine.getPhotoPath() : null);
        cbWineType.setSelectedItem((wine != null) ? wine.getWineType() : null);
        // Display the image
        if (wine != null) {
            SwingUtilities.invokeLater(() -> displayImage(wine.getPhotoPath()));
        } else {
            lblPhoto.setIcon(null);
            lblPhoto.setText("No Image Available");
        }
    }

//this method used to locate  wine from winetable in producer , to specific wine in wine form
    public void displayWineById(int wineId) {
        Wine wine = WineControl.getInstance().getWineById(wineId);
        if (wine != null) {
            wineArray = new ArrayList<>();
            wineArray.add(wine);
            currentWine = 1;
            refreshControls();
        } else {
            JOptionPane.showMessageDialog(this, "Wine not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //this method is to display images from database
    private void displayImage(String photoPath) {
        if (photoPath != null && !photoPath.isEmpty()) {
            
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/" + photoPath));

            if (lblPhoto.getWidth() > 0 && lblPhoto.getHeight() > 0) {
                Image image = imageIcon.getImage().getScaledInstance(
                    lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(image));
            } else {
                lblPhoto.setIcon(imageIcon);
            }
        } else {
            lblPhoto.setIcon(null);
            lblPhoto.setText("No Image Available");
        }
    }

    private void populateWineTypes() {
        ArrayList<WineType> wineTypes = WineTypeControl.getInstance().getWineTypes();
        for (WineType wineType : wineTypes) {
            cbWineType.addItem(wineType);
        }
    }




//toggles between enabled and disabled button
    private void enableSaveButton() {
        btnSave.setEnabled(true);
    }
	// Updates the various wines array manipulation buttons,
	// according to form state

    private void refreshDataButtons() {
        btnSave.setEnabled(inAddMode);
        btnAdd.setEnabled(!inAddMode);
        btnRemove.setEnabled(!inAddMode);
        if("Sales".equalsIgnoreCase(userRole)) {
            btnAdd.setEnabled(false);
            btnSave.setEnabled(false);
            btnRemove.setEnabled(false);}
    }
//navigation buttons
    private void btnFirstOnClick() {
        currentWine = 1;
        refreshControls();
    }
    private void btnPrevOnClick() {
        if (currentWine > 1) {
			currentWine--;
		}
        refreshControls();
    }

    private void btnNextOnClick() {
        if (currentWine < wineArray.size()) {
			currentWine++;
		}
        refreshControls();
    }

    private void btnLastOnClick() {
        currentWine = wineArray.size();
        refreshControls();
    }
    //looks for mathcing data in DB
    private void performSearch(String query) {
        if (query.isEmpty()) {
            fetchAndRefresh();
            return;
        }
        ArrayList<Wine> filteredWines = new ArrayList<>();
        for (Wine wine : WineControl.getInstance().getWines()) {
            if (wine.getName().toLowerCase().contains(query) ||
                String.valueOf(wine.getProductionYear()).contains(query) ||
                String.valueOf(wine.getCatalogNumber()).contains(query) ||
                String.valueOf(wine.getManufacturer().getManufacturerId()).contains(query) ||
                String.valueOf(wine.getPricePerBottle()).contains(query)) {

                filteredWines.add(wine);
            }
        }
        if (!filteredWines.isEmpty()) {
            wineArray = filteredWines;
            currentWine = 1;
            refreshControls();
        } else {
            JOptionPane.showMessageDialog(this, "No results found for \"" + query + "\"");
            tfSearch.setText("");
        }
    }
    /**
	 * Add a new wine OR Edit an existing wine (through Control package) to the DB.
	 */
    private void btnSaveOnClick() {
        try {

            if (tfWineName.getText().isEmpty() || tfProductionYear.getText().isEmpty() || tfPricePerBottle.getText().isEmpty() || tfPhotoPath.getText().isEmpty() || cbSweetnessLevel.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "All fields are required except description.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int productionYear = Integer.parseInt(tfProductionYear.getText());
            double pricePerBottle = Double.parseDouble(tfPricePerBottle.getText());
            int catalogNumber = Integer.parseInt(tfCatalogNumber.getText()); // Assuming there's a catalog number field

            Manufacturer selectedManufacturer = ManufacturerControl.getInstance().getManufacturerById(
                Integer.parseInt(tfManufacturer.getText())
            );

            if (selectedManufacturer == null) {
                JOptionPane.showMessageDialog(this, "Invalid manufacturer ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            WineType selectedWineType = (WineType) cbWineType.getSelectedItem();
            if (selectedWineType == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid wine type.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (inAddMode) {
                SweetnessLevel selectedSweetnessLevel = (SweetnessLevel) cbSweetnessLevel.getSelectedItem();
                Wine newWine = new Wine(
                    0,
                    tfWineName.getText(),
                    productionYear,
                    taDescription.getText(),
                    pricePerBottle,
                    selectedSweetnessLevel,
                    catalogNumber,
                    selectedManufacturer,
                    tfPhotoPath.getText(),
                    selectedWineType  
                );

                if (WineControl.getInstance().addWine(newWine)) {
                    JOptionPane.showMessageDialog(this, "Wine added successfully.");
                    fetchAndRefresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add wine.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Wine updatedWine = wineArray.get(currentWine - 1);
                updatedWine.setName(tfWineName.getText());
                updatedWine.setProductionYear(productionYear);
                updatedWine.setDescription(taDescription.getText());
                updatedWine.setPricePerBottle(pricePerBottle);
                updatedWine.setSweetnessLevel((SweetnessLevel) cbSweetnessLevel.getSelectedItem());
                updatedWine.setCatalogNumber(catalogNumber);
                updatedWine.setManufacturer(selectedManufacturer);
                updatedWine.setPhotoPath(tfPhotoPath.getText());
                updatedWine.setWineType(selectedWineType);
                if (WineControl.getInstance().updateWine(updatedWine)) {
                    JOptionPane.showMessageDialog(this, "Wine updated successfully.");
                    fetchAndRefresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update wine.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for production year, price, catalog number, and manufacturer ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

//allows to add new wine
    private void btnAddOnClick() {
        inAddMode = true;
        currentWine = wineArray.size() + 1;
        refreshControls();
    }
//removes wine using control from DB
    private void btnRemoveOnClick() {
        if (!inAddMode) {
            int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this wine?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                Wine wineToDelete = wineArray.get(currentWine - 1);
                if (WineControl.getInstance().deleteWine(wineToDelete.getWineId())) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Wine deleted successfully."
                    );
                    fetchAndRefresh();
                    if (currentWine > wineArray.size()) {
                        currentWine = wineArray.size();
                    }
                    refreshControls();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete wine.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    private void customizeForRole() {
        if("Sales".equalsIgnoreCase(userRole)) {
        	  btnAdd.setEnabled(false);
              btnSave.setEnabled(false);
              btnRemove.setEnabled(false);
        }
        
            
        }
}
