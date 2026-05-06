package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import control.ReportControl;
import control.WineControl;
import control.WineTypeControl;
import control.WineReportGenerator;
import entity.Dish;
import entity.Wine;
import entity.WineType;
import entity.Occasion;
import net.sf.jasperreports.engine.JRException;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public class FrmRecommendation extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final Color pink = new Color(194, 65, 78);
    private final Color BG_FIELD = new Color(217, 166, 165);

    private JTable tblOccasions;
    private JTable tblDishes;
    private JTable tblWines;
    private JTable tblWineTypes;

    public FrmRecommendation() {
        setBackground(new Color(202, 187, 149));
        setLayout(null); // Consider using proper layout managers instead
        initComponents();
    }

    private void initComponents() {
  
        setBounds(100, 100, 800, 713);
        setBackground(new Color(105, 20, 56));

       
       setLayout(null);

        // WineTypes Panel
        JPanel pnlWineTypes = new JPanel(null);
        pnlWineTypes.setBackground(new Color(105, 20, 56));
        pnlWineTypes.setBorder(BorderFactory.createTitledBorder("WineType Table"));
        pnlWineTypes.setBounds(333, 204, 301, 150);

        tblWineTypes = new JTable(new DefaultTableModel(
            new Object[] { "WineType ID", "Name", "SerialNumber" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        setupTableStyle(tblWineTypes);
        
        JScrollPane spWineTypes = new JScrollPane(tblWineTypes);
        spWineTypes.getViewport().setBackground(new Color(105, 20, 56));
        spWineTypes.setBounds(10, 25, 281, 98);
        pnlWineTypes.add(spWineTypes);
       add(pnlWineTypes);

        // Occasions Panel
       JPanel pnlOccasions = new JPanel(null);
       pnlOccasions.setBackground(new Color(105, 20, 56));
       TitledBorder occasionBorder = BorderFactory.createTitledBorder("Occasion Table");
       occasionBorder.setTitleColor(new Color(234, 171, 55));
       pnlOccasions.setBorder(occasionBorder);
       pnlOccasions.setBounds(10, 54, 623, 139);
       
       
        tblOccasions = new JTable(new DefaultTableModel(
            new Object[] { "Occasion ID", "Occasion", "Season", "Location", "Description" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        setupTableStyle(tblOccasions);

        JScrollPane spOccasions = new JScrollPane(tblOccasions);
        spOccasions.getViewport().setBackground(new Color(105, 20, 56));
        spOccasions.setBounds(10, 20, 601, 105);
        pnlOccasions.add(spOccasions);
       add(pnlOccasions);

        // Dishes Panel
       JPanel pnlDishes = new JPanel(null);
       pnlDishes.setBackground(new Color(105, 20, 56));
       TitledBorder dishBorder = BorderFactory.createTitledBorder("Dish Table");
       dishBorder.setTitleColor(new Color(234, 171, 55));
       pnlDishes.setBorder(dishBorder);
       pnlDishes.setBounds(10, 204, 301, 150);

        tblDishes = new JTable(new DefaultTableModel(
            new Object[] { "Dish ID", "Name" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        setupTableStyle(tblDishes);

        JScrollPane spDishes = new JScrollPane(tblDishes);
        spDishes.getViewport().setBackground(new Color(202, 187, 149));
        spDishes.setBounds(10, 25, 281, 100);
        pnlDishes.add(spDishes);
       add(pnlDishes);

        // Wines Panel
       JPanel pnlWines = new JPanel(null);
       pnlWines.setBackground(new Color(105, 20, 56));
       TitledBorder wineBorder = BorderFactory.createTitledBorder("Wine Table");
       wineBorder.setTitleColor(new Color(234, 171, 55));
       pnlWines.setBorder(wineBorder);
       pnlWines.setBounds(10, 365, 624, 150);

        tblWines = new JTable(new DefaultTableModel(
            new Object[] { "Wine ID", "Name", "Production Year", "Price", "Description", 
                         "Sweetness Level", "Catalog #", "ManufacturerID", "WineType" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        setupTableStyle(tblWines);
        
        tblWines.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblWines.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        tblWines.getColumnModel().getColumn(0).setPreferredWidth(20);
        
        JScrollPane spWines = new JScrollPane(tblWines);
        spWines.getViewport().setBackground(new Color(202, 187, 149));
        spWines.setBounds(10, 20, 601, 105);
        pnlWines.add(spWines);
       add(pnlWines);

        // Labels
        JLabel lblTitle = new JLabel("Wine Recommendation Report");
        lblTitle.setForeground(new Color(234, 171, 55));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBounds(10, 11, 359, 14);
      add(lblTitle);

        JLabel lblInstructions = new JLabel(
            "Click on Ctrl And select all requested fields, click again for unselecting specific field");
        lblInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblInstructions.setForeground(new Color(234, 171, 55));
        lblInstructions.setBounds(0, 36, 634, 14);
       add(lblInstructions);

        // Report Generation Button
        JButton btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.setBorder(new CompoundBorder());
        btnGenerateReport.setBackground(pink);
        btnGenerateReport.setForeground(Color.WHITE);
        btnGenerateReport.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        btnGenerateReport.setBounds(471, 2, 163, 41);
        btnGenerateReport.addActionListener(e -> generateReport());
       add(btnGenerateReport);

        // Setup table listeners and load data
        setupTableListeners(tblOccasions);
        setupTableListeners(tblDishes);
        tblWineTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Enforce single selection

        tblWineTypes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filterWines(); // Update the wine table whenever the selection changes
            }
        });

        // Optional: Add a double-click action to clear the selection
        tblWineTypes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tblWineTypes.clearSelection(); // Clear the selection on double-click
                }
            }
        });

        tblWines.setRowSelectionAllowed(false);
        tblWines.setColumnSelectionAllowed(false);
        tblWines.setFocusable(false);

        // Load initial data
        loadAllData();
    }



	private void setupTableStyle(JTable table) {
        table.setSelectionBackground(new Color(202, 77, 102));
        table.setBackground(new Color(105, 20, 56));
        table.setForeground(new Color(234, 171, 55));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(105, 20, 56));
                return c;
            }
        });
       
    }

    private void setupTableListeners(JTable table) {
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filterWines();
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    table.clearSelection();
                }
            }
        });
    }

    private void generateReport() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Get filtered wines
            ArrayList<Wine> filteredWines = getFilteredWines();

            // Ensure there is data to generate a report
            if (filteredWines.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No wines match the selected criteria. Cannot generate a report.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Generate the report
            WineReportGenerator reportGenerator = new WineReportGenerator();
            reportGenerator.generateReport(filteredWines);

            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error generating report: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

  

    private void loadAllData() {
        loadWineTypes();
        loadOccasions();
        loadDishes();
        loadAllWines();
    }

    private void loadAllWines() {
        ArrayList<Wine> wines = WineControl.getInstance().getWines();
        DefaultTableModel model = (DefaultTableModel) tblWines.getModel();
        model.setRowCount(0);
        for (Wine wine : wines) {
            model.addRow(new Object[] {
                wine.getWineId(),
                wine.getName(),
                wine.getProductionYear(),
                wine.getPricePerBottle(),
                wine.getDescription(),
                wine.getSweetnessLevel(),
                wine.getCatalogNumber(),
                wine.getManufacturer().getManufacturerId(),
                wine.getWineType()
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
    private ArrayList<Wine> getFilteredWines() {
        ArrayList<Wine> allWines = WineControl.getInstance().getWines();
        ArrayList<Wine> filteredWines = new ArrayList<>();

        // Get selected items
        ArrayList<Integer> selectedDishIds = getSelectedIds(tblDishes, 0);
        ArrayList<Integer> selectedOccasionIds = getSelectedIds(tblOccasions, 0);
        ArrayList<Integer> selectedWineTypeIds = getSelectedIds(tblWineTypes, 0);

        // Filter wines
        for (Wine wine : allWines) {
            int wineTypeId = wine.getWineType().getWineTypeID();
            if (meetsFilterCriteria(wineTypeId, selectedWineTypeIds, selectedDishIds, selectedOccasionIds)) {
                filteredWines.add(wine);
            }
        }
        updateWineTable(filteredWines);

        return filteredWines;
    }

    private void filterWines() {
        ArrayList<Wine> filteredWines = getFilteredWines();
        updateWineTable(filteredWines);
    }


    private ArrayList<Integer> getSelectedIds(JTable table, int column) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int row : table.getSelectedRows()) {
            ids.add((Integer) table.getValueAt(row, column));
        }
        return ids;
    }

    private boolean meetsFilterCriteria(int wineTypeId, ArrayList<Integer> selectedWineTypeIds,
            ArrayList<Integer> selectedDishIds, ArrayList<Integer> selectedOccasionIds) {
        
        boolean matchesWineType = selectedWineTypeIds.isEmpty() || 
                                 selectedWineTypeIds.contains(wineTypeId);
        
        boolean matchesDish = selectedDishIds.isEmpty() || 
                             selectedDishIds.stream().anyMatch(dishId -> 
                                 ReportControl.getInstance().isWineTypeRelatedToDish(wineTypeId, dishId));
        
        boolean matchesOccasion = selectedOccasionIds.isEmpty() || 
                                 selectedOccasionIds.stream().anyMatch(occasionId -> 
                                     ReportControl.getInstance().isWineTypeRelatedToOccasion(wineTypeId, occasionId));

        return matchesWineType && matchesDish && matchesOccasion;
    }

    private void updateWineTable(ArrayList<Wine> wines) {
        DefaultTableModel model = (DefaultTableModel) tblWines.getModel();
        model.setRowCount(0);
        for (Wine wine : wines) {
            model.addRow(new Object[] {
                wine.getWineId(),
                wine.getName(),
                wine.getProductionYear(),
                wine.getPricePerBottle(),
                wine.getDescription(),
                wine.getSweetnessLevel(),
                wine.getCatalogNumber(),
                wine.getManufacturer().getManufacturerId(),
                wine.getWineType()
            });
        }
    }
}