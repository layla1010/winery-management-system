package entity;

public class Inventory {
    private int inventoryID;
    private int wineID;
    private int locationUniqueNumber;
    private int quantity;

    // Constructor
    public Inventory(int inventoryID, int wineID, int locationUniqueNumber, int quantity) {
        this.inventoryID = inventoryID;
        this.wineID = wineID;
        this.locationUniqueNumber = locationUniqueNumber;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getInventoryID() 
    {
    	return inventoryID;
    	}
    public void setInventoryID(int inventoryID) {
    	this.inventoryID = inventoryID;
    	}

    public int getWineID() {
    	return wineID; 
    	}
    public void setWineID(int wineID) { 
    	this.wineID = wineID; 
    	}

    public int getLocationUniqueNumber() {
    	return locationUniqueNumber; 
    	}
    public void setLocationUniqueNumber(int locationUniqueNumber) { 
    	this.locationUniqueNumber = locationUniqueNumber;
}

    public int getQuantity() {
    	return quantity; 
    }
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    	}
}
