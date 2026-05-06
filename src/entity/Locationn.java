package entity;

public class Locationn {
    private int locationUniqueNumber;
    private String locationName;

    // Constructor
    public Locationn(int locationUniqueNumber, String locationName) {
        this.locationUniqueNumber = locationUniqueNumber;
        this.locationName = locationName;
    }

    // Getters and Setters
    public int getLocationUniqueNumber() { return locationUniqueNumber; }
    public void setLocationUniqueNumber(int locationUniqueNumber) { this.locationUniqueNumber = locationUniqueNumber; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
}
