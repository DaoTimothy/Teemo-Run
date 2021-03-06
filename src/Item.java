package src;
import java.awt.image.BufferedImage;

public class Item {

    BufferedImage itemImage;
    String itemName;
    String description;
    int level;
    int maxLevel;
    int originPrice;
    int price;
    int priceChange;

    Item (BufferedImage img, String name, String desc, int maxLevel, int originPrice, int priceChange) {

        itemImage = img;
        itemName = name;
        description = desc;
        level = 0;
        this.maxLevel = maxLevel;
        this.originPrice = originPrice;
        this.priceChange = priceChange;

    }

    public void upgrade() {

        level++;
        price += priceChange;
        
    }
}