package entity;

public class Dish {
    private String name;
   // private List<String> recipeLinks;
private int dishID;
    public Dish(int dishID,String name) {
        this.name = name;
        this.setDishID(dishID);
       // setRecipeLinks(recipeLinks);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  

	public int getDishID() {
		return dishID;
	}

	public void setDishID(int dishID) {
		this.dishID = dishID;
	}
}
