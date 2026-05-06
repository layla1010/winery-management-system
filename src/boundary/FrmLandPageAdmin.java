package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

import control.*;

public class FrmLandPageAdmin extends JPanel {

    private JLabel lblGreeting;
    private JLabel lblTotalWines;
    private JLabel lblTotalCustomers;
    private JLabel lblTotalOrders;

    public FrmLandPageAdmin(String username) {
        setSize(900, 600);
        setLayout(null);
        setBackground(new Color(105, 20, 56));

        lblGreeting = new JLabel("Welcome Back Admin");
        lblGreeting.setForeground(new Color(234, 171, 55));
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 20));
        lblGreeting.setBounds(20, 20, 300, 30);
        add(lblGreeting);

        // Wines Panel
        JPanel pnlWines = new JPanel(null);
        pnlWines.setBounds(50, 77, 250, 120);
        pnlWines.setBorder(BorderFactory.createTitledBorder("Wines"));
        ((TitledBorder) pnlWines.getBorder()).setTitleColor(new Color(234, 171, 55));
        JLabel iconWines = new JLabel();
        iconWines.setIcon(new ImageIcon(getClass().getResource("/boundary/images/Wines.png")));
        pnlWines.setBackground(new Color(69, 16, 32));
        iconWines.setBounds(10, 20, 64, 64);
        pnlWines.add(iconWines);
        lblTotalWines = new JLabel("Total Wines: " + WineTypeControl.getInstance().getWineTypes().size());
        lblTotalWines.setForeground(new Color(234, 171, 55));
        lblTotalWines.setBounds(80, 50, 150, 30);
        pnlWines.add(lblTotalWines);
        add(pnlWines);

        // Customers Panel
        JPanel pnlCustomers = new JPanel(null);
        pnlCustomers.setBounds(315, 77, 250, 120);
        pnlCustomers.setBackground(new Color(69, 16, 32));
        pnlCustomers.setBorder(BorderFactory.createTitledBorder("Customers"));
        ((TitledBorder) pnlCustomers.getBorder()).setTitleColor(new Color(234, 171, 55));
        JLabel iconCustomers = new JLabel();
        iconCustomers.setIcon(new ImageIcon(getClass().getResource("/boundary/images/customers.png")));
        iconCustomers.setBounds(10, 20, 64, 64);
        pnlCustomers.add(iconCustomers);
        lblTotalCustomers = new JLabel("Total Customers: " + PersonControl.getInstance().getCustomers().size());
        lblTotalCustomers.setForeground(new Color(234, 171, 55));
        lblTotalCustomers.setBounds(80, 50, 150, 30);
        pnlCustomers.add(lblTotalCustomers);
        add(pnlCustomers);

        // Orders Panel
        JPanel pnlOrders = new JPanel(null);
        pnlOrders.setBounds(586, 77, 250, 120);
        pnlOrders.setBackground(new Color(69, 16, 32));
        pnlOrders.setBorder(BorderFactory.createTitledBorder("Orders"));
        ((TitledBorder) pnlOrders.getBorder()).setTitleColor(new Color(234, 171, 55));
        JLabel iconOrders = new JLabel();
        iconOrders.setIcon(new ImageIcon(getClass().getResource("/boundary/images/orders.png")));
        iconOrders.setBounds(10, 20, 64, 64);
        pnlOrders.add(iconOrders);
        lblTotalOrders = new JLabel("Total Orders: " + OrderControl.getInstance().getOrders().size());
        lblTotalOrders.setForeground(new Color(234, 171, 55));
        lblTotalOrders.setBounds(90, 50, 150, 30);
        pnlOrders.add(lblTotalOrders);
        add(pnlOrders);

        JPanel pnlJSON = new JPanel(null);
        pnlJSON.setBackground(new Color(69, 16, 32));
        pnlJSON.setBorder(BorderFactory.createTitledBorder("Generate Weekly Report"));
        ((TitledBorder) pnlJSON.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlJSON.setBounds(450, 208, 386, 120);
        add(pnlJSON);

        // Buttons
        JButton btnGenerateJson = new JButton("Generate JSON Report");
        btnGenerateJson.setBackground(new Color(194, 65, 78));
        btnGenerateJson.setBounds(200, 42, 180, 40);
        btnGenerateJson.addActionListener(e -> {
            ReportControl.getInstance().generateInventoryReport();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if(topFrame instanceof RootLayout) {
                ((RootLayout)topFrame).addNotification("Inventory report generated successfully at " + new java.util.Date());
            }
        });
        pnlJSON.add(btnGenerateJson);

        JButton btnSendJson = new JButton("Send JSON Report");
        btnSendJson.setBackground(new Color(194, 65, 78));
        btnSendJson.setBounds(10, 42, 180, 40);
        btnSendJson.addActionListener(e -> {
            try {
                File reportFile = new File("CurrentInventoryReport.json");
                if (!reportFile.exists()) {
                    JOptionPane.showMessageDialog(this, "Please generate the report first!", "No Report", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                String subject = "Inventory Report JSON";
                String body = "Please find the inventory report attached:\n\n" + reportFile.getAbsolutePath();
                URI mailto = new URI("mailto:?subject=" + URLEncoder.encode(subject, "UTF-8") +
                        "&body=" + URLEncoder.encode(body, "UTF-8"));
                desktop.mail(mailto);
                JOptionPane.showMessageDialog(this, "Email client opened. Please attach the JSON file manually.", "Email", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to open email client.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        pnlJSON.add(btnSendJson);

        JPanel pnlXML = new JPanel(null);
        pnlXML.setBackground(new Color(69, 16, 32));
        pnlXML.setBorder(BorderFactory.createTitledBorder("Import monthly Data"));
        ((TitledBorder) pnlXML.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlXML.setBounds(50, 208, 390, 120);
        add(pnlXML);

        JButton btnImportXml = new JButton("Import XML");
        btnImportXml.setBackground(new Color(194, 65, 78));
        btnImportXml.setBounds(10, 39, 182, 40);
        btnImportXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select XML File to Import");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));
                int userSelection = fileChooser.showOpenDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File xmlFile = fileChooser.getSelectedFile();
                    ReportControl.getInstance().importGefenXML(xmlFile);
                }
            }
        });
        pnlXML.add(btnImportXml);

        JButton btnExportXml = new JButton("Export XML");
        btnExportXml.setBackground(new Color(194, 65, 78));
        btnExportXml.setBounds(202, 39, 182, 40);
        btnExportXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save XML File");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    if (!fileToSave.getAbsolutePath().endsWith(".xml")) {
                        fileToSave = new File(fileToSave + ".xml");
                    }
                    ReportControl.getInstance().exportToXML(fileToSave);
                }
            }
        });
        pnlXML.add(btnExportXml);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 405, 786, 184);
        add(scrollPane);
        DefaultTableModel reportModel = new DefaultTableModel(
                new Object[] {
                        "EmployeeID", "Name", "Phone", "Office Address", "Email",
                        "Start Date", "Urgent Orders", "Regular Orders", "Total Orders", "Unproductive"
                },
                0
        );
        JTable tblReport = new JTable(reportModel);
        tblReport.setForeground(new Color(234, 171, 55));
        tblReport.setBackground(new Color(69, 16, 32));
        tblReport.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(234, 171, 55));
                c.setForeground(new Color(69, 16, 32));
                return c;
            }
        });
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2020, java.util.Calendar.JANUARY, 1);
        java.util.Date startDate = cal.getTime();
        cal.set(2025, java.util.Calendar.MAY, 1);
        java.util.Date endDate = cal.getTime();
        ArrayList<Object[]> reportData = ReportControl.getInstance().getUnproductiveEmployeeReportData(startDate, endDate);
        reportModel = (DefaultTableModel) tblReport.getModel();
        reportModel.setRowCount(0);
        for (Object[] row : reportData) {
            reportModel.addRow(row);
        }
        scrollPane.setViewportView(tblReport);

        JLabel lblNewLabel = new JLabel("All Unproductive Employees This month");
        lblNewLabel.setForeground(new Color(234, 171, 55));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel.setBounds(71, 366, 465, 28);
        add(lblNewLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test FrmLandPage");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.getContentPane().add(new FrmLandPageAdmin("Admin"));
            testFrame.pack();
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        });
    }
}
