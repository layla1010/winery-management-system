package control;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import entity.Consts;
//S
public class XMLExporter {

    /**
     * Exports data from the database to an XML file.
     * @param query SQL query to fetch data.
     * @param rootElementName Root element name for the XML.
     * @param filePath Path where the XML file will be saved.
     * package util;
**/

    
	
	public static String exportToXML(String query, String rootElementName, String filePath) {
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(query);
	             ResultSet rs = stmt.executeQuery()) {

	            // Create a new document
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.newDocument();

	            // Create root element (e.g., "WineProducers" or "Manufacturers")
	            Element rootElement = doc.createElement(rootElementName);
	            rootElement.setAttribute("exportDate", LocalDateTime.now().toString());
	            doc.appendChild(rootElement);

	            // Iterate over the result set and build XML elements
	            while (rs.next()) {
	                // Use the provided root element name to determine the item element name
	                String itemElementName = rootElementName.equals("Data") ? "TblWine" : "TblManufacturer";
	                Element item = doc.createElement(itemElementName);

	                // Set the "id" attribute from the first column in the result set
	                Attr attr = doc.createAttribute("id");
	                attr.setValue(rs.getString(1)); // Assuming the first column is always the ID
	                item.setAttributeNode(attr);

	                // Iterate over the remaining columns and add them as child elements
	                for (int i = 2; i <= rs.getMetaData().getColumnCount(); i++) {
	                    Element element = doc.createElement(rs.getMetaData().getColumnName(i));
	                    rs.getObject(i); // for wasNull() check
	                    element.appendChild(doc.createTextNode(rs.wasNull() ? "" : rs.getString(i)));
	                    item.appendChild(element);
	                }

	                // Append the item element to the root element
	                rootElement.appendChild(item);
	            }

	            // Write the content into an XML file
	            DOMSource source = new DOMSource(doc);
	            File file = new File(filePath);
	            file.getParentFile().mkdirs();
	            StreamResult result = new StreamResult(file);

	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	            transformer.transform(source, result);

	            return  " data exported successfully to " + filePath;

	        } catch (SQLException | ParserConfigurationException | TransformerException e) {
	            e.printStackTrace();
	            return "Failed to export : " + e.getMessage();
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        return "Failed to export : " + e.getMessage();
	    }
	}



    
}
