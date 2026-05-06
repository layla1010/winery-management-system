package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import control.PersonControl;
import control.ReportControl;
import control.UserControl;
import entity.Customer;
import entity.Employee;
import entity.Order;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class FrmLandPageSales extends JPanel implements ActionListener {

    private JLabel lblGreeting;
    private JTable tblReport;
    private JButton btnBtnunproductive; // Make button a class field so we can refer to it in actionPerformed
    private JDateChooser dcStartDate;
    private JDateChooser dcEndDate;

    public FrmLandPageSales(String username) {
        setSize(900, 600);
        setLayout(null);
        setBackground(new Color(105, 20, 56));


        // Retrieve employee for the given username
        Employee employee = UserControl.getInstance().getEmployeeNameByUsername(username);
        String welcomeText = (employee != null) ? "Welcome Back " + employee.getName() : "Welcome Back " + username;
        lblGreeting = new JLabel(welcomeText);
		lblGreeting.setForeground(new Color(234, 171, 55));
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 20));
        lblGreeting.setBounds(20, 20, 300, 30);
        add(lblGreeting);

        // Panel for report generation
        JPanel pnlXML = new JPanel(null);
        pnlXML.setBackground(new Color(69, 16, 32));
        pnlXML.setBorder(BorderFactory.createTitledBorder("Generate Unproductive Employees Report"));
        ((TitledBorder) pnlXML.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlXML.setBounds(50, 168, 786, 81);
        add(pnlXML);

        JLabel lblStartDate = new JLabel("Start Date:");
        lblStartDate.setForeground(new Color(234, 171, 55));
        lblStartDate.setBounds(20, 25, 100, 25);
        pnlXML.add(lblStartDate);

        dcStartDate = new JDateChooser();
        dcStartDate.setBackground(new Color(194, 65, 78));
        dcStartDate.setBounds(81, 26, 150, 25);
        pnlXML.add(dcStartDate);

        JLabel lblEndDate = new JLabel("End Date:");
        lblEndDate.setForeground(new Color(234, 171, 55));
        lblEndDate.setBounds(258, 26, 100, 25);
        pnlXML.add(lblEndDate);

        dcEndDate = new JDateChooser();
        dcEndDate.setBackground(new Color(194, 65, 78));
        dcEndDate.setBounds(317, 26, 150, 25);
        pnlXML.add(dcEndDate);

        btnBtnunproductive = new JButton("GenerateReport");
        btnBtnunproductive.setBounds(511, 26, 161, 30);
        btnBtnunproductive.setBackground(new Color(194, 65, 78));
        btnBtnunproductive.addActionListener(this);
        pnlXML.add(btnBtnunproductive);

        // Orders Table in a JScrollPane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(69, 16, 32));
        scrollPane.getViewport().setBackground(new Color(69, 16, 32));
        scrollPane.setBounds(50, 405, 786, 184);
        add(scrollPane);

        DefaultTableModel reportModel = new DefaultTableModel(
                new Object[]{"Order ID", "Order Date", "Shipment Date", "Employee", "Status"},
                0 // initial row count
        );
        tblReport = new JTable(reportModel);
        scrollPane.setViewportView(tblReport);
        
        tblReport.setForeground(new Color(234, 171, 55));
        tblReport.setBackground(new Color(105, 20, 56));
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
        tblReport.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JLabel lblNewLabel = new JLabel("All Assigned Orders");
        lblNewLabel.setForeground(new Color(234, 171, 55));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel.setBounds(50, 366, 177, 28);
        add(lblNewLabel);

        // Populate orders table for the employee if available
        if (employee != null) {
            ArrayList<Order> orders = PersonControl.getInstance().getOrdersByEmployee(employee.getEmployeeID());
            for (Order order : orders) {
                reportModel.addRow(new Object[]{
                    order.getOrder_ID(),
                    order.getOrder_Date(),
                    order.getShipment_Date(),
                    order.getEmployee().getName(),
                    order.getCurrentStatus()
                });
            }
        }

        // Contact Panel
        JPanel pnlContact = new JPanel(null);
        pnlContact.setBackground(new Color(69, 16, 32));
        pnlContact.setBorder(BorderFactory.createTitledBorder("Contact"));
        ((TitledBorder) pnlContact.getBorder()).setTitleColor(new Color(234, 171, 55));
        pnlContact.setBounds(50, 274, 786, 81);
        add(pnlContact);

        JButton btnNewButton = new JButton("send Email");
        btnNewButton.setBackground(new Color(194, 65, 78));
        btnNewButton.setBounds(512, 25, 161, 30);
        pnlContact.add(btnNewButton);

        JComboBox comboBox = new JComboBox();
        comboBox.setBackground(new Color(194, 65, 78));
        comboBox.setBounds(41, 29, 213, 30);
        pnlContact.add(comboBox);

        ArrayList<Customer> customers = PersonControl.getInstance().getCustomers();
        for (Customer cust : customers) {
            comboBox.addItem(cust);
        }
        pnlContact.add(comboBox);

        // Action Listener for send Email button
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer selectedCustomer = (Customer) comboBox.getSelectedItem();
                if (selectedCustomer != null) {
                    String email = selectedCustomer.getEmail();
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:" + email));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error launching email client: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer.");
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBtnunproductive) {
            generateCSVReport();
        }
    }

    // Method to generate CSV report
    private void generateCSVReport() {
        // Open a file chooser to select the save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as CSV");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File("Unproductive_Employees_Report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Replace dummy data with your actual method:
            ArrayList<Object[]> reportData = ReportControl.getInstance()
                    .getUnproductiveEmployeeReportData(dcStartDate.getDate(), dcEndDate.getDate());
            saveCSVReport(reportData, fileToSave.getAbsolutePath());
        }
    }

   

    // Save CSV file using plain Java I/O
    private void saveCSVReport(ArrayList<Object[]> reportData, String filePath) {
        String[] headers = {"EmployeeID", "Employee Name", "Phone", "Office Address", "Email",
                "Start Date", "Urgent Orders", "Regular Orders", "Total Orders", "Unproductive"};
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.join(",", headers));
            writer.newLine();
            for (Object[] row : reportData) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    Object value = row[i];
                    String cell = (value != null) ? value.toString() : "";
                    if (cell.contains(",") || cell.contains("\"") || cell.contains("\n")) {
                        cell = cell.replace("\"", "\"\"");
                        cell = "\"" + cell + "\"";
                    }
                    sb.append(cell);
                    if (i < row.length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.flush();
            JOptionPane.showMessageDialog(this, "CSV report saved at: " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving CSV report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test FrmLandPage");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.getContentPane().add(new FrmLandPageSales("Admin"));
            testFrame.pack();
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        });
    }
}
