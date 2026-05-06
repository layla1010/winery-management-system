package control;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.net.URLDecoder;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class JasperCompiler {

    // JRXML file should be in the src folder
    private static final String JRXML_PATH = "src/Wine_Recommendation_Report.jrxml";
    private static final String JASPER_PATH = "src/Wine_Recommendation_Report.jasper";
    
    private static final String CONN_STR = "jdbc:ucanaccess://" + getDBPath();
    
    private static String getDBPath() {
        try {
            String path = JasperCompiler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decoded = URLDecoder.decode(path, "UTF-8");
            
            if (decoded.contains(".jar")) {
                decoded = decoded.substring(0, decoded.lastIndexOf('/'));
                return decoded + "/database/CheersSystemDatabase.accdb";
            } else {
                decoded = decoded.substring(0, decoded.lastIndexOf("bin/"));
                return decoded + "/src/entity/CheersSystemDatabase.accdb";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JasperCompiler() {
    }

    public void compileReport() throws JRException {
        try {
            // Get the base project directory
            String projectDir = System.getProperty("user.dir");
            
            // Create full paths for the source and destination files
            File jrxmlFile = new File(projectDir, JRXML_PATH);
            File jasperFile = new File(projectDir, JASPER_PATH);
            
            System.out.println("JRXML Path: " + jrxmlFile.getAbsolutePath());
            System.out.println("Jasper Path: " + jasperFile.getAbsolutePath());
            
            // Verify the JRXML file exists
            if (!jrxmlFile.exists()) {
                throw new JRException("JRXML file not found at: " + jrxmlFile.getAbsolutePath());
            }

            // Compile the JRXML file to a Jasper file
            JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
            System.out.println("Report compiled successfully.");
        } catch (JRException e) {
            throw new JRException("Error compiling report: " + e.getMessage(), e);
        }
    }

    public void generateReport(Map<String, Object> parameters) throws JRException {
        Connection conn = null;
        try {
            String projectDir = System.getProperty("user.dir");
            File jasperFile = new File(projectDir, JASPER_PATH);

            if (!jasperFile.exists()) {
                throw new JRException("Jasper report not found at: " + jasperFile.getAbsolutePath());
            }

            System.out.println("Connecting to database...");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            
            System.out.println("Using connection string: " + CONN_STR);
            String dbPath = getDBPath();
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                throw new JRException("Database file not found at: " + dbFile.getAbsolutePath());
            }
            
            conn = DriverManager.getConnection(CONN_STR);

            System.out.println("Loading Jasper report...");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);

            System.out.println("Filling the report with data...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            System.out.println("Displaying the report...");
            JasperViewer.viewReport(jasperPrint, false);

        } catch (ClassNotFoundException e) {
            throw new JRException("JDBC Driver not found. Please ensure the UCanAccess driver is available.", e);
        } catch (Exception e) {
            throw new JRException("Error generating report: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed.");
                } catch (Exception e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }
    // Main method to test the JasperCompiler
  public static void main(String[] args) {
    	 JasperCompiler reportGenerator = new JasperCompiler();

			try {
				System.out.println("Compiling report...");
				reportGenerator.compileReport();
			} catch (JRException e) {
				System.err.println("Failed to compile report: " + e.getMessage());
				e.printStackTrace();
				return;
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("REPORT_TITLE", "Wine Recommendation Report");

			try {
				reportGenerator.generateReport(parameters);
				System.out.println("Report generated successfully.");
			} catch (JRException e) {
				System.err.println("Failed to generate report: " + e.getMessage());
				e.printStackTrace();
			}
    }


    
    
}