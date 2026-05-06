package control;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import entity.Wine;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineReportGenerator {
    // Define the paths
	
	  public void compileReport() throws JRException {
		    String jrxmlPath = "/resources/Wine_Recommendation_Report.jrxml";
		    String jasperPath = "/resources/Wine_Recommendation_Report.jasper";
		    JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
		    System.out.println("Compiled JRXML to JASPER.");
		}

		/**
		 * Generates a report based on the provided list of Wine objects.
		 * 
		 * @param filteredWines List of Wine objects to be included in the report.
		 * @throws JRException If an error occurs during the report generation.
		 */
    
	  public void generateReport(List<Wine> filteredWines) throws JRException {
		    try {
		        if (filteredWines == null || filteredWines.isEmpty()) {
		            throw new JRException("No filtered wine data provided for the report.");
		        }

		        // Load the .jasper file from the classpath
		        InputStream jasperStream = getClass().getResourceAsStream("/Wine_Recommendation_Report.jasper");
		        if (jasperStream == null) {
		            throw new JRException("Jasper file not found in the classpath.");
		        }
		        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		        // Prepare data source
		        List<Map<String, Object>> dataList = new ArrayList<>();
		        for (Wine wine : filteredWines) {
		            Map<String, Object> map = new HashMap<>();
		            map.put("wineId", wine.getWineId());
		            map.put("name", wine.getName());
		            map.put("productionYear", wine.getProductionYear());
		            map.put("pricePerBottle", wine.getPricePerBottle());
		            map.put("description", wine.getDescription());
		            map.put("sweetnessLevel", wine.getSweetnessLevel().name());
		            map.put("catalogNumber", String.valueOf(wine.getCatalogNumber()));
		            map.put("manufacturer", wine.getManufacturer().getManufacturerId());
		            map.put("wineType", wine.getWineType().getName());
		            dataList.add(map);
		        }

		        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

		        // Set parameters
		        Map<String, Object> parameters = new HashMap<>();
		        parameters.put("REPORT_TITLE", "Filtered Wine Recommendation Report");

		        // Fill and view the report
		        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		        JasperViewer.viewReport(jasperPrint, false);

		    } catch (Exception e) {
		        throw new JRException("Error generating report: " + e.getMessage(), e);
		    }
		}

    
}