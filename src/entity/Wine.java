package entity;

import enums.SweetnessLevel;

public class Wine {
    private int wineId; 
    private String name;
    private String description;
    private int productionYear;
    private double pricePerBottle;
    private SweetnessLevel sweetnessLevel;
    private String photoPath; 
    private int catalogNumber;
    private Manufacturer manufacturer; 
    private WineType wineType;

    public Wine(int wineId, String name,  int productionYear,String description, double pricePerBottle, SweetnessLevel sweetnessLevel,int catalogNum,Manufacturer manufacturer, String photoPath,WineType wineType) {
        
    	this.wineId = wineId;
        this.name = name;
        this.description = description;
        this.productionYear = productionYear;
        this.pricePerBottle = pricePerBottle;
        this.sweetnessLevel = sweetnessLevel;
        this.photoPath = photoPath;
        this.catalogNumber=catalogNum;
        this.manufacturer=manufacturer;
        this.wineType=wineType;
    }

  
    public WineType getWineType() {
		return wineType;
	}


	public void setWineType(WineType wineType) {
		this.wineType = wineType;
	}


	public int getCatalogNumber() {
		return catalogNumber;
	}


	public void setCatalogNumber(int catalogNumber) {
		this.catalogNumber = catalogNumber;
	}


	public int getWineId() {
        return wineId;
    }

    public void setWineId(int wineId) {
        this.wineId = wineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public double getPricePerBottle() {
        return pricePerBottle;
    }

    public void setPricePerBottle(double pricePerBottle) {
        this.pricePerBottle = pricePerBottle;
    }

    public SweetnessLevel getSweetnessLevel() {
        return sweetnessLevel;
    }

    public void setSweetnessLevel(SweetnessLevel sweetnessLevel) {
        this.sweetnessLevel = sweetnessLevel;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }



    
    
}
