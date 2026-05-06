package entity;

public class DishLink {
    private int linkID;
    private int dishID; // Foreign key reference to Dish
    private String link;

    // Constructor
    public DishLink(int linkID, int dishID, String link) {
        this.linkID = linkID;
        this.dishID = dishID;
        this.link = link;
    }

    // Getters and Setters
    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }

    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    
}
