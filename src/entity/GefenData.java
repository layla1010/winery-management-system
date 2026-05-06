package entity;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import java.util.List;

@JacksonXmlRootElement(localName = "GefenData")
public class GefenData {

    @JacksonXmlElementWrapper(localName = "Manufacturers")
    @JacksonXmlProperty(localName = "Manufacturer")
    public List<ManufacturerXML> manufacturers;

    public static class ManufacturerXML {
        @JacksonXmlProperty(localName = "FullName")
        public String fullName;

        @JacksonXmlProperty(localName = "PhoneNumber")
        public String phoneNumber;

        @JacksonXmlProperty(localName = "Address")
        public String address;

        @JacksonXmlProperty(localName = "Email")
        public String email;

        @JacksonXmlElementWrapper(localName = "Wines")
        @JacksonXmlProperty(localName = "Wine")
        public List<WineXML> wines;
    }

    public static class WineXML {
        @JacksonXmlProperty(localName = "WineName")
        public String wineName;

        @JacksonXmlProperty(localName = "ProductionYear")
        public int productionYear;

        @JacksonXmlProperty(localName = "Description")
        public String description;

        @JacksonXmlProperty(localName = "PricePerBottle")
        public double pricePerBottle;

        @JacksonXmlProperty(localName = "SweetnessLevel")
        public String sweetnessLevel;

        @JacksonXmlProperty(localName = "CatalogNumber")
        public int catalogNumber;

        @JacksonXmlProperty(localName = "Photo")
        public String photo;

        @JacksonXmlProperty(localName = "WineTypeID")
        public int wineTypeID;
    }
}
