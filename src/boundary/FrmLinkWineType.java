package boundary;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import control.ReportControl;
import control.WineControl;
import control.WineTypeControl;
import entity.Dish;
import entity.Occasion;
import entity.Wine;
import entity.WineType;

public class FrmLinkWineType extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tblOccasions;
    private JTable tblDishes;
    private JTable tblWineTypes;
    private JTable tblLinkedDishes;
    private JTable tblLinkedOccasions;
    
    private JButton btnLink;
    private JButton btnDelete;

    public FrmLinkWineType() {
        setBackground(new Color(105, 20, 56));
        setLayout(null);
        setBounds(100, 100, 800, 713);
        initComponents();
        loadAllData();
    }

    private void initComponents() {
        // WineTypes Panel
    	
        JPanel pnlWineTypes = new JPanel(null);
        pnlWineTypes.setBackground(new Color(69, 16, 32));

        pnlWineTypes.setBorder(BorderFactory.createTitledBorder("WineType Table"));
        ((TitledBorder) pnlWineTypes.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlWineTypes.setBounds(320, 204, 300, 150);

        tblWineTypes = new JTable(new DefaultTableModel(
            new Object[] { "WineType ID", "WineType", "SerialNumber" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblWineTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tblWineTypes.setBackground(new Color(105, 20, 56));

        tblWineTypes.setForeground(new Color(234, 171, 55));
        tblWineTypes.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        JScrollPane spWineTypes = new JScrollPane(tblWineTypes);
        spWineTypes.setBounds(10, 25, 280, 114);
        pnlWineTypes.add(spWineTypes);
        add(pnlWineTypes);

        // Occasions Panel (for available occasions)
        JPanel pnlOccasions = new JPanel(null);
        pnlOccasions.setBackground(new Color(69, 16, 32));

        pnlOccasions.setBorder(BorderFactory.createTitledBorder("Occasion Table"));
        ((TitledBorder) pnlOccasions.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlOccasions.setBounds(10, 54, 610, 139);

        tblOccasions = new JTable(new DefaultTableModel(
            new Object[] { "Occasion ID", "Occasion", "Season", "Location", "Description" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblOccasions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOccasions.setBackground(new Color(105, 20, 56));

        tblOccasions.setForeground(new Color(234, 171, 55));
        JScrollPane spOccasions = new JScrollPane(tblOccasions);
        spOccasions.setBounds(10, 20, 590, 105);
        pnlOccasions.add(spOccasions);
        add(pnlOccasions);

        // Dishes Panel (for available dishes)
        JPanel pnlDishes = new JPanel(null);
        pnlDishes.setBackground(new Color(69, 16, 32));
        pnlDishes.setBorder(BorderFactory.createTitledBorder("Dish Table"));
        ((TitledBorder) pnlDishes.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlDishes.setBounds(10, 204, 300, 150);

        tblDishes = new JTable(new DefaultTableModel(
            new Object[] { "Dish ID", "Dish" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblDishes.setBackground(new Color(105, 20, 56));

        tblDishes.setForeground(new Color(234, 171, 55));
        tblDishes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDishes.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        JScrollPane spDishes = new JScrollPane(tblDishes);
        spDishes.setBounds(10, 25, 280, 114);
        pnlDishes.add(spDishes);
        add(pnlDishes);

        // Linked Dishes Panel (shows WineType and linked Dish with hidden IDs)
        JPanel pnlLinkedDishes = new JPanel(null);
        pnlLinkedDishes.setBackground(new Color(69, 16, 32));
        pnlLinkedDishes.setBorder(BorderFactory.createTitledBorder("Linked Dishes"));
        
        ((TitledBorder) pnlLinkedDishes.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlLinkedDishes.setBounds(10, 365, 300, 150);

        tblLinkedDishes = new JTable(new DefaultTableModel(
            new Object[] { "WineType", "Dish", "WineTypeID", "DishID" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblLinkedDishes.setBackground(new Color(105, 20, 56));
        tblLinkedDishes.setForeground(new Color(234, 171, 55));


        tblOccasions.setForeground(new Color(234, 171, 55));
        tblOccasions.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        // Allow selection so the user can delete links
        tblLinkedDishes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Hide the extra ID columns
        tblLinkedDishes.getColumnModel().getColumn(2).setMinWidth(0);
        tblLinkedDishes.getColumnModel().getColumn(2).setMaxWidth(0);
        tblLinkedDishes.getColumnModel().getColumn(3).setMinWidth(0);
        tblLinkedDishes.getColumnModel().getColumn(3).setMaxWidth(0);
        tblLinkedDishes.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        JScrollPane spLinkedDishes = new JScrollPane(tblLinkedDishes);
        spLinkedDishes.setBounds(10, 20, 280, 119);
        pnlLinkedDishes.add(spLinkedDishes);
        spLinkedDishes.setBackground(new Color(105, 20, 56));

        spLinkedDishes.getViewport().setBackground(new Color(105, 20, 56));
        add(pnlLinkedDishes);

        // Linked Occasions Panel (shows WineType and linked Occasion with hidden IDs)
        JPanel pnlLinkedOccasions = new JPanel(null);
        pnlLinkedOccasions.setBackground(new Color(69, 16, 32));

        pnlLinkedOccasions.setBorder(BorderFactory.createTitledBorder("Linked Occasions"));
        ((TitledBorder) pnlLinkedOccasions.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlLinkedOccasions.setBounds(320, 365, 300, 150);

        tblLinkedOccasions = new JTable(new DefaultTableModel(
            new Object[] { "WineType", "Occasion", "WineTypeID", "OccasionID" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblLinkedOccasions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLinkedOccasions.getColumnModel().getColumn(2).setMinWidth(0);
        tblLinkedOccasions.getColumnModel().getColumn(2).setMaxWidth(0);
        tblLinkedOccasions.getColumnModel().getColumn(3).setMinWidth(0);
        tblLinkedOccasions.getColumnModel().getColumn(3).setMaxWidth(0);
        tblLinkedOccasions.setBackground(new Color(105, 20, 56));
        tblLinkedOccasions.setForeground(new Color(234, 171, 55));
        tblLinkedOccasions.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        
        JScrollPane spLinkedOccasions = new JScrollPane(tblLinkedOccasions);
        spLinkedOccasions.setBounds(10, 20, 280, 119);
        spLinkedOccasions.getViewport().setBackground(new Color(105, 20, 56));
        spLinkedOccasions.setBackground(new Color(105, 20, 56));

        pnlLinkedOccasions.add(spLinkedOccasions);
 
        add(pnlLinkedOccasions);

        // Title and Instructions
        JLabel lblTitle = new JLabel("Link WineType");
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBounds(10, 11, 359, 14);
        add(lblTitle);

        JLabel lblInstructions = new JLabel(
            "Select one row from WineType, Dish and/or Occasion to create a link.");
        lblInstructions.setForeground(new Color(234, 171, 55));
        lblInstructions.setBounds(10, 36, 417, 14);
        add(lblInstructions);

        // Link Button
        btnLink = new JButton("Link");
        btnLink.setBorder(new CompoundBorder());
        btnLink.setBackground(new Color(194, 65, 78));
        btnLink.setForeground(Color.WHITE);
        btnLink.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        btnLink.setBounds(489, 14, 131, 29);
        btnLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLink();
            }
        });
        add(btnLink);
        
        // Delete Link Button
        btnDelete = new JButton("Delete Link");
        btnDelete.setBackground(new Color(194, 65, 78));

        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        btnDelete.setBorder(new CompoundBorder());
        btnDelete.setBounds(349, 14, 131, 29);
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDeleteLink();
            }
        });
        add(btnDelete);
        
        // When a WineType is selected, refresh the linked tables.
        tblWineTypes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadLinkedDishes();
                loadLinkedOccasions();
            }
        });
    }

    private void loadAllData() {
        loadWineTypes();
        loadOccasions();
        loadDishes();
        clearLinkedTables();
    }
    
    private void clearLinkedTables() {
        ((DefaultTableModel) tblLinkedDishes.getModel()).setRowCount(0);
        ((DefaultTableModel) tblLinkedOccasions.getModel()).setRowCount(0);
    }

    private void loadWineTypes() {
        ArrayList<WineType> wineTypes = WineTypeControl.getInstance().getWineTypes();
        DefaultTableModel model = (DefaultTableModel) tblWineTypes.getModel();
        model.setRowCount(0);
        for (WineType wineType : wineTypes) {
            model.addRow(new Object[] {
                wineType.getWineTypeID(),
                wineType.getName(),
                wineType.getWineTypeSerialNumber()
            });
        }
    }

    private void loadOccasions() {
        ArrayList<Occasion> occasions = ReportControl.getInstance().getAllOccasions();
        DefaultTableModel model = (DefaultTableModel) tblOccasions.getModel();
        model.setRowCount(0);
        for (Occasion occasion : occasions) {
            model.addRow(new Object[] {
                occasion.getOccasionId(),
                occasion.getOccasionName(),
                occasion.getSeason(),
                occasion.getLocation(),
                occasion.getDescription()
            });
        }
    }

    private void loadDishes() {
        ArrayList<Dish> dishes = ReportControl.getInstance().getAllDishes();
        DefaultTableModel model = (DefaultTableModel) tblDishes.getModel();
        model.setRowCount(0);
        for (Dish dish : dishes) {
            model.addRow(new Object[] {
                dish.getDishID(),
                dish.getName()
            });
        }
    }
    
    // Fetch and display the linked dishes for the selected WineType.
    private void loadLinkedDishes() {
        int selectedRow = tblWineTypes.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblLinkedDishes.getModel();
        model.setRowCount(0);
        if (selectedRow < 0) {
            return;
        }
        int wineTypeId = (Integer) tblWineTypes.getValueAt(selectedRow, 0);
        String wineTypeName = (String) tblWineTypes.getValueAt(selectedRow, 1);
        ArrayList<Dish> linkedDishes = ReportControl.getInstance().getLinkedDishesByWineTypeId(wineTypeId);
        for (Dish dish : linkedDishes) {
            // Add hidden IDs (wineTypeId and dish.getDishID())
            model.addRow(new Object[] { wineTypeName, dish.getName(), wineTypeId, dish.getDishID() });
        }
    }
    
    // Fetch and display the linked occasions for the selected WineType.
    private void loadLinkedOccasions() {
        int selectedRow = tblWineTypes.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblLinkedOccasions.getModel();
        model.setRowCount(0);
        if (selectedRow < 0) {
            return;
        }
        int wineTypeId = (Integer) tblWineTypes.getValueAt(selectedRow, 0);
        String wineTypeName = (String) tblWineTypes.getValueAt(selectedRow, 1);
        ArrayList<Occasion> linkedOccasions = ReportControl.getInstance().getLinkedOccasionsByWineTypeId(wineTypeId);
        for (Occasion occ : linkedOccasions) {
            model.addRow(new Object[] { wineTypeName, occ.getOccasionName(), wineTypeId, occ.getOccasionId() });
        }
    }

    /**
     * Performs the link operation based on selected rows.
     */
    private void performLink() {
        int wineTypeRow = tblWineTypes.getSelectedRow();
        if (wineTypeRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a WineType.");
            return;
        }
        int wineTypeId = (Integer) tblWineTypes.getValueAt(wineTypeRow, 0);
        boolean linkSuccess = true;
        
        // Process linking for Dish
        int dishRow = tblDishes.getSelectedRow();
        if (dishRow >= 0) {
            int dishId = (Integer) tblDishes.getValueAt(dishRow, 0);
            if (!ReportControl.getInstance().isWineTypeRelatedToDish(wineTypeId, dishId)) {
                linkSuccess &= ReportControl.getInstance().addWineTypeDish(wineTypeId, dishId);
            } else {
                JOptionPane.showMessageDialog(this, "This WineType is already linked to the selected Dish.");
            }
        }
        
        // Process linking for Occasion
        int occasionRow = tblOccasions.getSelectedRow();
        if (occasionRow >= 0) {
            int occasionId = (Integer) tblOccasions.getValueAt(occasionRow, 0);
            if (!ReportControl.getInstance().isWineTypeRelatedToOccasion(wineTypeId, occasionId)) {
                linkSuccess &= ReportControl.getInstance().addOccasionPerWineType(wineTypeId, occasionId);
            } else {
                JOptionPane.showMessageDialog(this, "This WineType is already linked to the selected Occasion.");
            }
        }
        
        if (linkSuccess) {
            JOptionPane.showMessageDialog(this, "Link(s) created successfully.");
            loadLinkedDishes();
            loadLinkedOccasions();
        } else {
            JOptionPane.showMessageDialog(this, "Error creating link(s). Please check your selection.");
        }
    }
    
    /**
     * Performs deletion of a selected link.
     * It checks which linked table has a selected row, retrieves the hidden IDs,
     * calls the appropriate deletion method, and refreshes the table.
     */
    private void performDeleteLink() {
        // First check if a row is selected in the linked dishes table.
        int selectedDishRow = tblLinkedDishes.getSelectedRow();
        int selectedOccasionRow = tblLinkedOccasions.getSelectedRow();
        
        if (selectedDishRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tblLinkedDishes.getModel();
            int wineTypeId = (Integer) model.getValueAt(selectedDishRow, 2);
            int dishId = (Integer) model.getValueAt(selectedDishRow, 3);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Delete the link between WineType and Dish?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = ReportControl.getInstance().deleteWineTypeDish(wineTypeId, dishId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Link deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete link.");
                }
                loadLinkedDishes();
            }
        } else if (selectedOccasionRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tblLinkedOccasions.getModel();
            int wineTypeId = (Integer) model.getValueAt(selectedOccasionRow, 2);
            int occasionId = (Integer) model.getValueAt(selectedOccasionRow, 3);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Delete the link between WineType and Occasion?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = ReportControl.getInstance().deleteOccasionPerWineType(wineTypeId, occasionId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Link deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete link.");
                }
                loadLinkedOccasions();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a link to delete from either table.");
        }
    }
}
