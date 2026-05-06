package boundary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import control.WineTypeControl; // or another control class if you prefer
import entity.Dish;
import entity.DishLink;
import util.JTextFieldLimiter;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class FrmDish extends JPanel {

	private static final long serialVersionUID = 1L;

	// Data + State
    private final Color pink = new Color(194, 65, 78);
    private final Color BG_FIELD = new Color(217, 166, 165);


	private ArrayList<Dish> dishArray;
	private Integer currentDish; // 1-based index in dishArray
	private boolean inAddMode;

	// Main GUI Components
	private JPanel contentPane;

	// Text fields for Dish info
	private JTextField tfDishID;
	private JTextField tfDishName;
	private JTextField tfNavigation; 
	private JTextField tfSearch;

	// Navigation + CRUD Buttons
	private JButton btnFirst   = new JButton("|<");
	private JButton btnPrev    = new JButton("<<");
	private JButton btnNext    = new JButton(">>");
	private JButton btnLast    = new JButton("|>");
	private JButton btnSave    = new JButton("Save");
	private JButton btnAdd     = new JButton("Add New");
	private JButton btnRemove  = new JButton("Delete");

	// Table for DishLinks
	private JTable dishLinkTable;
	private DefaultTableModel dishLinkTableModel;

	public FrmDish() {
        setBackground(new Color(105, 20, 56));
		initComponents();
		fetchAndRefresh();
		createEvents();
		 setLayout(null);
		  add(contentPane, BorderLayout.CENTER);
	}

	/**
	 * Initialize the entire UI (layout, panels, fields, etc.).
	 */
	private void initComponents() {

		setBounds(150, 150, 750, 450);
		

		contentPane = new JPanel();
		contentPane.setBounds(10, 5, 730, 434);
		contentPane.setBackground(new Color(105, 20, 56));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		// ---------------------------
		// 1) Panel for Dish fields
		// ---------------------------
		JPanel pnlDishDetails = new JPanel();
		pnlDishDetails.setForeground(new Color(100, 11, 13));
		pnlDishDetails.setBackground(new Color(105, 20, 56));
		pnlDishDetails.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, 
						new Color(234, 171, 55), 
						new Color(234, 171, 55)),
				"Dish Details",
				TitledBorder.LEADING,
				TitledBorder.TOP,
				null,
				new Color(234, 171, 55))				);
		pnlDishDetails.setBounds(20, 24, 515, 114);
		contentPane.add(pnlDishDetails);

		// Text fields
		tfDishID = new JTextField();
		tfDishID.setBackground(BG_FIELD);
		tfDishID.setEditable(false);
		tfDishID.setColumns(10);

		tfDishName = new JTextField();
		tfDishName.setBackground(BG_FIELD);
		tfDishName.setColumns(10);
		tfDishName.setDocument(new JTextFieldLimiter(100));

		// Labels
		JLabel lblDishID   = new JLabel("Dish ID:");
		lblDishID.setForeground(new Color(234, 171, 55));
		lblDishID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		JLabel lblDishName = new JLabel("Dish Name:");
		lblDishName.setForeground(new Color(234, 171, 55));
		lblDishName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Layout for the top panel
		GroupLayout gl_pnlDishDetails = new GroupLayout(pnlDishDetails);
		gl_pnlDishDetails.setHorizontalGroup(
				gl_pnlDishDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlDishDetails.createSequentialGroup()
						.addGap(27)
						.addComponent(lblDishID)
						.addGap(18)
						.addComponent(tfDishID, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
						.addGap(64)
						.addComponent(lblDishName)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(tfDishName, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
						.addGap(57))
				);
		gl_pnlDishDetails.setVerticalGroup(
				gl_pnlDishDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlDishDetails.createSequentialGroup()
						.addGap(34)
						.addGroup(gl_pnlDishDetails.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfDishID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDishID)
								.addComponent(lblDishName)
								.addComponent(tfDishName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(38, Short.MAX_VALUE))
				);
		pnlDishDetails.setLayout(gl_pnlDishDetails);

		// --------------------------------
		// 2) Panel for navigation + buttons
		// --------------------------------
		JPanel pnlActionBtn = new JPanel();
		pnlActionBtn.setBackground(new Color(105, 20, 56));
		pnlActionBtn.setBounds(20, 149, 515, 46);
		contentPane.add(pnlActionBtn);
		pnlActionBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// Navigation buttons
		btnFirst.setPreferredSize(new Dimension(48, 23));
		btnFirst.setMinimumSize(new Dimension(40, 23));
		btnFirst.setMaximumSize(new Dimension(60, 23));
		btnFirst.setForeground(new Color(202, 187, 149));
		btnFirst.setBorder(null);
		btnFirst.setBackground(pink);
		pnlActionBtn.add(btnFirst);

		btnPrev.setPreferredSize(new Dimension(50, 23));
		btnPrev.setBorder(null);
		btnPrev.setBackground(pink);
		pnlActionBtn.add(btnPrev);

		tfNavigation = new JTextField();
		tfNavigation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfNavigation.setColumns(9);
		tfNavigation.setEditable(false);
		pnlActionBtn.add(tfNavigation);

		btnNext.setBackground(pink);
		btnNext.setPreferredSize(new Dimension(50, 23));
		btnNext.setBorder(null);
		pnlActionBtn.add(btnNext);

		btnLast.setForeground(new Color(202, 187, 149));
		btnLast.setBackground(pink);
		btnLast.setBorder(null);
		btnLast.setMaximumSize(new Dimension(48, 23));
		btnLast.setPreferredSize(new Dimension(48, 23));
		pnlActionBtn.add(btnLast);

		// Search TextField
		tfSearch = new JTextField();
		tfSearch.setBackground(BG_FIELD);
		tfSearch.setBorder(new TitledBorder(
				null, 
				"Search", 
				TitledBorder.CENTER, 
				TitledBorder.TOP, 
				null, 
				null
				));
		tfSearch.setColumns(15);
		tfSearch.setToolTipText("Search by Dish Name or ID...");
		pnlActionBtn.add(tfSearch);

		// ------------------------------------------------
		// 3) Table at the bottom showing "Links by Dish"
		// ------------------------------------------------
		JPanel pnlDetails = new JPanel(new BorderLayout());
		pnlDetails.setBackground(new Color(105, 20, 56));
		pnlDetails.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(234, 171, 55), new Color(234, 171, 55)),
				"Links by Dish",
				TitledBorder.LEADING,
				TitledBorder.TOP,
				null,
				new Color(234, 171, 55)	)			);
		pnlDetails.setBounds(20, 217, 515, 134);
		contentPane.add(pnlDetails);

		// Build the table model for DishLinks
		dishLinkTableModel = new DefaultTableModel(
				new String[] {"Link ID", "Link"}, 5  // Ensure 5 rows are always present
				) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1; // Allow editing only in the "Link" column
			}
		};

		dishLinkTable = new JTable(dishLinkTableModel);
		dishLinkTable.setSelectionBackground(new Color(193, 21, 26));
		dishLinkTable.setBackground(new Color(105, 20, 56));
		dishLinkTable.setForeground(		new Color(234, 171, 55));
		dishLinkTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Ensure 5 rows are always present
		for (int i = 0; i < 5; i++) {
			if (dishLinkTableModel.getRowCount() < 5) {
				dishLinkTableModel.addRow(new Object[]{null, ""}); // Empty row for new link
			}
		}

		// Add listener to detect table changes (Enable Save Button)
		dishLinkTable.getModel().addTableModelListener(e -> enableSaveButton());

		// Header styling
		dishLinkTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(
					JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(new Color(234, 171, 55));
				c.setForeground(new Color(105, 20, 56));
				return c;
			}
		});




		JScrollPane scrollPane = new JScrollPane(dishLinkTable);
		scrollPane.setBackground(new Color(202, 167, 75));
		scrollPane.getViewport().setBackground(new Color(105, 20, 56));
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(true);

		pnlDetails.add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBackground(new Color(105, 20, 56));
		panel.setBounds(545, 217, 107, 114);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnDeleteLink = new JButton("Delete");
		btnDeleteLink.addActionListener(e -> btnDeleteLinkOnClick());

		btnDeleteLink.setBounds(10, 11, 84, 23);
		btnDeleteLink.setPreferredSize(new Dimension(61, 23));
		btnDeleteLink.setEnabled(true);
		btnDeleteLink.setBorder(null);
		btnDeleteLink.setBackground(new Color(196, 84, 84));
		panel.add(btnDeleteLink);

		JButton btnSaveLink = new JButton("Save");
		btnSaveLink.addActionListener(e -> btnSaveLinkOnClick());

		btnSaveLink.setBounds(10, 80, 84, 23);
		btnSaveLink.setPreferredSize(new Dimension(56, 23));
		btnSaveLink.setEnabled(true);
		btnSaveLink.setBorder(null);
		btnSaveLink.setBackground(new Color(169, 84, 84));
		panel.add(btnSaveLink);

		JButton btnAddLink = new JButton("Add New");
		btnAddLink.addActionListener(e -> btnAddLinkOnClick());

		btnAddLink.setBounds(10, 45, 84, 23);
		btnAddLink.setPreferredSize(new Dimension(70, 23));
		btnAddLink.setEnabled(true);
		btnAddLink.setBorder(null);
		btnAddLink.setBackground(new Color(196, 84, 84));
		panel.add(btnAddLink);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBackground(new Color(105, 20, 56));
		panel_1.setBounds(545, 24, 107, 120);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		btnSave.setBounds(11, 80, 86, 23);
		panel_1.add(btnSave);

		// Data manipulation buttons
		btnSave.setBackground(new Color(169, 84, 84));
		btnSave.setPreferredSize(new Dimension(56, 23));
		btnSave.setBorder(null);
		btnAdd.setBounds(11, 11, 86, 23);
		panel_1.add(btnAdd);

		btnAdd.setPreferredSize(new Dimension(70, 23));
		btnAdd.setBackground(new Color(196, 84, 84));
		btnAdd.setBorder(null);
		btnRemove.setBounds(11, 45, 86, 23);
		panel_1.add(btnRemove);

		btnRemove.setPreferredSize(new Dimension(61, 23));
		btnRemove.setBackground(new Color(196, 84, 84));
		btnRemove.setBorder(null);
	}

	/**
	 * Set up event listeners (buttons, textfield changes, etc.).
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
		addChangeListeners(tfDishName);

		// Live Search
		tfSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String query = tfSearch.getText().trim().toLowerCase();
				performSearch(query);
			}
		});

		// (Optional) Double-click on table row -> something like "open link detail" if desired
		dishLinkTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedRow = dishLinkTable.getSelectedRow();
					if (selectedRow != -1) {
						int linkID = (int) dishLinkTableModel.getValueAt(selectedRow, 0);
						// You could open a form for editing that DishLink if needed
						JOptionPane.showMessageDialog(
								FrmDish.this, 
								"Double-clicked LinkID: " + linkID
								);
					}
				}
			}
		});
	}

	/**
	 * Add DocumentListeners that re-enable the Save button if text changes.
	 */
	private void addChangeListeners(JTextField textField) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { enableSaveButton(); }
			@Override public void removeUpdate(DocumentEvent e) { enableSaveButton(); }
			@Override public void changedUpdate(DocumentEvent e) { enableSaveButton(); }
		});
	}

	/**
	 * Called at startup or after an add/remove. Fetches data and refreshes UI.
	 */
	private void fetchAndRefresh() {
		// Get all dishes
		dishArray = WineTypeControl.getInstance().getDishes(); 
		// or DishControl.getInstance().getDishes() if you separated your control class

		// If we have data, set currentDish = 1, else null
		currentDish = (!dishArray.isEmpty()) ? 1 : null;
		inAddMode = (currentDish == null);
		refreshControls();
	}

	/**
	 * Refresh all aspects of the UI (navigation, fields, buttons, table).
	 */
	private void refreshControls() {
		refreshNavigation();
		refreshDishFields();
		refreshDataButtons();

		if (!inAddMode && currentDish != null) {
			Dish d = dishArray.get(currentDish - 1);
			refreshDishLinkTableForDish(d);
		} else {
			// in Add mode => clear the table
			dishLinkTableModel.setRowCount(0);
		}
	}

	/**
	 * Handle top navigation label and enabling/disabling nav buttons.
	 */
	private void refreshNavigation() {
		tfNavigation.setText(
				(!inAddMode)
				? (currentDish + " of " + dishArray.size())
						: (dishArray.size() + 1) + " of " + (dishArray.size() + 1)
				);

		btnFirst.setEnabled(currentDish != null && currentDish > 1);
		btnPrev.setEnabled(currentDish != null && currentDish > 1);
		btnNext.setEnabled(currentDish != null && currentDish < dishArray.size());
		btnLast.setEnabled(currentDish != null && currentDish < dishArray.size());
	}

	/**
	 * Load current Dish data into text fields (or blank for new).
	 */
	private void refreshDishFields() {
		Dish d = (!inAddMode && currentDish != null)
				? dishArray.get(currentDish - 1)
						: null;

		tfDishID.setText( (d != null)
				? String.valueOf(d.getDishID())
						: "(NEW)"
				);
		tfDishName.setText( (d != null)
				? d.getName()
						: ""
				);
	}

	/**
	 * Enable/disable Save, Add, Remove based on mode and record presence.
	 */
	private void refreshDataButtons() {
		boolean hasRecord = (currentDish != null && currentDish > 0);
		// If we're in Add mode or we have a record, let them Save
		btnSave.setEnabled(inAddMode || hasRecord);

		// Add is only relevant if we're not already adding
		btnAdd.setEnabled(!inAddMode);
		// Remove is only relevant if not inAddMode and there's an existing record
		btnRemove.setEnabled(!inAddMode && hasRecord);
	}

	/**
	 * Populate the DishLink table for the given Dish.
	 */
	/**
	 * Load Dish Links for a given Dish and ensure at least 5 rows exist.
	 */
	private void refreshDishLinkTableForDish(Dish d) {
	    dishLinkTableModel.setRowCount(0); // Clear existing data
	    if (d == null) return;

	    // Load existing Dish Links
	    ArrayList<DishLink> links = (ArrayList<DishLink>)
	        WineTypeControl.getInstance().getDishLinksByDishID(d.getDishID());

	    for (DishLink link : links) {
	        dishLinkTableModel.addRow(new Object[] {
	            link.getLinkID(),
	            link.getLink()
	        });
	    }

	    // Ensure 5 rows always exist
	    for (int i = dishLinkTableModel.getRowCount(); i < 5; i++) {
	        dishLinkTableModel.addRow(new Object[]{null, ""});
	    }
	}


	// ---- Navigation Handlers ----
	private void btnFirstOnClick() {
		currentDish = 1;
		inAddMode = false;
		refreshControls();
	}

	private void btnPrevOnClick() {
		if (currentDish > 1) {
			currentDish--;
		}
		inAddMode = false;
		refreshControls();
	}

	private void btnNextOnClick() {
		if (currentDish < dishArray.size()) {
			currentDish++;
		}
		inAddMode = false;
		refreshControls();
	}

	private void btnLastOnClick() {
		currentDish = dishArray.size();
		inAddMode = false;
		refreshControls();
	}

	/**
	 * Searching by partial dish name or partial dish ID (converted to string).
	 */
	private void performSearch(String query) {
		if (query.isEmpty()) {
			fetchAndRefresh(); // revert to showing all
			return;
		}
		// Grab the full dish list again
		ArrayList<Dish> fullList = WineTypeControl.getInstance().getDishes();

		ArrayList<Dish> filtered = new ArrayList<>();
		for (Dish d : fullList) {
			String nameLower = (d.getName() != null) ? d.getName().toLowerCase() : "";
			String dishIdStr = String.valueOf(d.getDishID());

			if (nameLower.contains(query) || dishIdStr.contains(query)) {
				filtered.add(d);
			}
		}

		if (!filtered.isEmpty()) {
			this.dishArray = filtered;
			currentDish = 1;
			inAddMode = false;
			refreshControls();
		} else {
			JOptionPane.showMessageDialog(
					this,
					"No results found for \"" + query + "\""
					);
			tfSearch.setText("");
		}
	}

	// ---- Button Clicks for Data ----
	private void btnSaveOnClick() {
		try {
			// Validate mandatory fields
			if (tfDishName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(
						this,
						"Dish Name is required.",
						"Input Error",
						JOptionPane.ERROR_MESSAGE
						);
				return;
			}

			int dishID = inAddMode
					? 0
							: Integer.parseInt(tfDishID.getText().replace("(NEW)", "0"));

			Dish dish = new Dish(
					dishID,
					tfDishName.getText()
					);

			boolean success;
			if (inAddMode) {
				// Add new
				success = WineTypeControl.getInstance().addDish(dish);
				if (success) {
					JOptionPane.showMessageDialog(this, "Dish added successfully.");
				}
			} else {
				// Update existing
				success = WineTypeControl.getInstance().updateDish(dish);
				if (success) {
					JOptionPane.showMessageDialog(this, "Dish updated successfully.");
				}
			}

			if (success) {
				fetchAndRefresh();
			} else {
				JOptionPane.showMessageDialog(
						this,
						"Failed to save Dish.",
						"Error",
						JOptionPane.ERROR_MESSAGE
						);
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(
					this,
					"Please enter a valid numeric value for Dish ID (if needed).",
					"Input Error",
					JOptionPane.ERROR_MESSAGE
					);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(
					this,
					"An unexpected error occurred: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
					);
			ex.printStackTrace();
		}
	}

	private void btnAddOnClick() {
		inAddMode = true;
		currentDish = dishArray.size(); 
		refreshControls();
	}

	private void btnRemoveOnClick() {
		if (currentDish != null) {
			int confirmation = JOptionPane.showConfirmDialog(
					this,
					"Are you sure you want to delete this Dish?",
					"Delete Confirmation",
					JOptionPane.YES_NO_OPTION
					);
			if (confirmation == JOptionPane.YES_OPTION) {
				int dishId = Integer.parseInt(tfDishID.getText().replace("(NEW)", "0"));
				boolean success = WineTypeControl.getInstance().deleteDish(dishId);

				if (success) {
					JOptionPane.showMessageDialog(this, "Dish deleted successfully.");
					fetchAndRefresh();
				} else {
					JOptionPane.showMessageDialog(
							this,
							"Failed to delete Dish.",
							"Error",
							JOptionPane.ERROR_MESSAGE
							);
				}
			}
		}
	}

	/**
	 * Whenever text changes in the fields, we allow Save to become enabled.
	 */
	private void enableSaveButton() {
		btnSave.setEnabled(true);
	}
	private void btnAddLinkOnClick() {
	    // Force the table to stop editing and commit changes
	    if (dishLinkTable.isEditing()) {
	        dishLinkTable.getCellEditor().stopCellEditing();
	    }

	    // Get the selected row
	    int selectedRow = dishLinkTable.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a row to add a link.");
	        return;
	    }

	    // Get the dish ID
	    int dishID;
	    try {
	        dishID = Integer.parseInt(tfDishID.getText());
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Invalid Dish ID.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // Check if the selected row is valid (empty LinkID)
	    Object linkIDObj = dishLinkTableModel.getValueAt(selectedRow, 0);
	    Object linkTextObj = dishLinkTableModel.getValueAt(selectedRow, 1);

	    if (linkIDObj != null) {
	        JOptionPane.showMessageDialog(this, "This row already contains a saved link. Use the Save button instead.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    String linkText = (linkTextObj != null) ? linkTextObj.toString().trim() : "";
	    if (linkText.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Link text cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // Add the new link
	    DishLink newLink = new DishLink(0, dishID, linkText);
	    boolean success = WineTypeControl.getInstance().addDishLink(newLink);

	    if (success) {
	        JOptionPane.showMessageDialog(this, "Link added successfully.");
	        fetchAndRefresh();  // Refresh to get the new LinkID
	        
	        
	    } else {
	        JOptionPane.showMessageDialog(this, "Failed to add link.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	private void btnDeleteLinkOnClick() {
		int selectedRow = dishLinkTable.getSelectedRow();
		if (selectedRow != -1) {
			Object linkIDObj = dishLinkTableModel.getValueAt(selectedRow, 0);

			if (linkIDObj != null) {
				int linkID = Integer.parseInt(linkIDObj.toString());

				int confirm = JOptionPane.showConfirmDialog(
						this, 
						"Are you sure you want to delete this link?", 
						"Confirm Delete", 
						JOptionPane.YES_NO_OPTION
						);

				if (confirm == JOptionPane.YES_OPTION) {
					boolean success = WineTypeControl.getInstance().deleteDishLink(linkID);

					if (success) {
						JOptionPane.showMessageDialog(this, "Link deleted successfully.");
						fetchAndRefresh(); // Refresh the table
					} else {
						JOptionPane.showMessageDialog(this, "Failed to delete link.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please select a valid link to delete.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a link to delete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void btnSaveLinkOnClick() {
		try {
			int dishID = Integer.parseInt(tfDishID.getText());

			for (int i = 0; i < dishLinkTableModel.getRowCount(); i++) {
				Object linkIDObj = dishLinkTableModel.getValueAt(i, 0);
				Object linkTextObj = dishLinkTableModel.getValueAt(i, 1);

				if (linkTextObj != null && !linkTextObj.toString().trim().isEmpty()) {
					String linkText = linkTextObj.toString().trim();

					if (linkIDObj != null) {
						// Update existing link
						int linkID = Integer.parseInt(linkIDObj.toString());
						DishLink updatedLink = new DishLink(linkID, dishID, linkText);
						boolean success = WineTypeControl.getInstance().updateDishLink(updatedLink);

						if (!success) {
							JOptionPane.showMessageDialog(this, "Failed to update link.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						// Add new link (if the user typed into an empty row but didn't click "Add New")
						DishLink newLink = new DishLink(0, dishID, linkText);
						boolean success = WineTypeControl.getInstance().addDishLink(newLink);

						if (!success) {
							JOptionPane.showMessageDialog(this, "Failed to add link.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}

			JOptionPane.showMessageDialog(this, "Links saved successfully.");
			fetchAndRefresh(); // Refresh the table
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
