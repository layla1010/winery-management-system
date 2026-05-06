package entity;

import java.util.ArrayList;
import java.util.List;

public class WineType {
    private String wineTypeName;
     private int wineTypeID;
     private int wineTypeSerialNumber;
   // private List<Dish> foodPairings;        
   // private List<String> suitableOccasions;

    public WineType(int id,int serialNumber,String name) {
        this.wineTypeID = id;
        this.wineTypeSerialNumber=serialNumber;
        this.wineTypeName=name;
     //   this.foodPairings = new ArrayList<>();
      //  this.suitableOccasions = new ArrayList<>();
    }

    public int getWineTypeID() {
		return wineTypeID;
	}

	public void setWineTypeID(int wineTypeID) {
		this.wineTypeID = wineTypeID;
	}

	public int getWineTypeSerialNumber() {
		return wineTypeSerialNumber;
	}

	public void setWineTypeSerialNumber(int wineTypeSerialNumber) {
		this.wineTypeSerialNumber = wineTypeSerialNumber;
	}

	public String getName() {
        return wineTypeName;
    }

    public void setName(String name) {
        this.wineTypeName = name;
    }

   // public List<Dish> getFoodPairings() {
     //   return foodPairings;
   // }

    //public void addFoodPairing(Dish dish) {
    //    if (!foodPairings.contains(dish)) {
     //       foodPairings.add(dish);
   //     }
 //   }

   // public List<String> getSuitableOccasions() {
   //     return suitableOccasions;
  //  }

  //  public void addOccasion(String occasion) {
      //  if (!suitableOccasions.contains(occasion)) {
            //suitableOccasions.add(occasion);
     //   }
 //   }
    @Override
    public String toString() {
        return wineTypeName; // This will display the wine type name in the JComboBox
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WineType wineType = (WineType) obj;
        return wineTypeID == wineType.wineTypeID;  // Compare by ID
    }


}
