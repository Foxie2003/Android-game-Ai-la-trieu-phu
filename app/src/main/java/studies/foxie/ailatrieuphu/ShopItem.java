package studies.foxie.ailatrieuphu;

public class ShopItem {
    private int id;
    private int categoryId;
    private String name;
    private long price;
    private String image;
    private boolean isBought;

    public ShopItem() {
    }

    public ShopItem(int id, int categoryId, String name, long price, String image, boolean isBought) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.isBought = isBought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
