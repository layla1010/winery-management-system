package boundary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import control.WineControl;
import entity.Manufacturer;
import entity.Wine;

/**this class is designed to show the wine data related to each producer through a table**/
public class FrmManufacturerDetails extends JPanel {

	private static final long serialVersionUID = 1L;
	private Manufacturer currentManufacturer;

	private JTable wineTable;
	private DefaultTableModel wineTableModel;

	public FrmManufacturerDetails() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel pnlDetails = new JPanel();
		pnlDetails.setBackground(new Color(81, 23, 48));
		pnlDetails.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(166, 84, 97), new Color(166, 84, 97)), "Manufacturer Wines", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(166, 84, 97)));
		pnlDetails.setLayout(new BorderLayout());

		wineTableModel = new DefaultTableModel(new String[]{
				"Wine ID", "Name", "Production Year", "Price", 
				"Description", "Sweetness Level",  "Catalog Number","Photo"
		}, 0){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		wineTable = new JTable(wineTableModel);
		wineTable.setSelectionBackground(new Color(193, 21, 26));
		wineTable.setBackground(new Color(166, 84, 97));
		wineTable.setForeground(new Color(81, 23, 48));
		wineTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(new Color(81, 23, 48)); 
				c.setForeground(new Color(166, 84, 97)
); 

				return c;
			}
		});


		add(pnlDetails, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(wineTable);
		scrollPane.setBackground(new Color(81, 23, 48));

		scrollPane.getViewport().setBackground(new Color(81, 23, 48)); 
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(true);
		pnlDetails.add(scrollPane, BorderLayout.CENTER);

		add(pnlDetails, BorderLayout.CENTER);
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

	private void openWineDetails(int wineId) {
		SwingUtilities.invokeLater(() -> {
			FrmWine wineFrame = new FrmWine("Admin");
			wineFrame.setVisible(true);
			wineFrame.displayWineById(wineId);
		});
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.currentManufacturer = manufacturer;
		refreshWineTable();
	}

	private void refreshWineTable() {
		wineTableModel.setRowCount(0);
		if (currentManufacturer != null) {
			ArrayList<Wine> wines = WineControl.getInstance().getWinesByManufacturerId(currentManufacturer.getManufacturerId());
			for (Wine wine : wines) {
				wineTableModel.addRow(new Object[]{
						wine.getWineId(), 
						wine.getName(), 
						wine.getProductionYear(), 
						wine.getPricePerBottle(), 
						wine.getDescription(), 
						wine.getSweetnessLevel(), 
						wine.getCatalogNumber() ,
						wine.getPhotoPath()
				});
			}
		}
	}
}
