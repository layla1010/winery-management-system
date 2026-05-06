package entity;

import enums.*;

public class Occasion {
private Season season;
private Location location;
private int occasionId;
private String description;
private String occasionName;




public Occasion(Season season, Location location, int occasionId, String description,String occasionName) {
	this.season = season;
	this.location = location;
	this.occasionId = occasionId;
	this.description = description;
	this.occasionName=occasionName;
}
public String getOccasionName() {
	return occasionName;
}
public void setOccasionName(String occasionName) {
	this.occasionName = occasionName;
}
public Season getSeason() {
	return season;
}
public void setSeason(Season season) {
	this.season = season;
}
public Location getLocation() {
	return location;
}
public void setLocation(Location location) {
	this.location = location;
}
public int getOccasionId() {
	return occasionId;
}
public void setOccasionId(int occasionId) {
	this.occasionId = occasionId;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}


}
