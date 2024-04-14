package studies.foxie.ailatrieuphu;

public class ShopItem {
    private int image;
    private long price;

    public ShopItem() {
    }

    public ShopItem(int image, long price) {
        this.image = image;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
