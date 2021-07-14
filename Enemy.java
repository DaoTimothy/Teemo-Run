import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {
    static int teemoHealth;
    static int screenWidth;
    static int bgscrollspeed;

    int x;
    int y;
    int hitboxCorrection;
    int spawnDistance;
    double moveSpeed;

    BufferedImage sprite;
    int eWidth;
    int eHeight;
    
    Enemy (int teemoHp, int screenW, int bgscroll) {
        teemoHealth = teemoHp;
        screenWidth = screenW;
        bgscrollspeed = bgscroll;
    }

    Enemy(BufferedImage img, int x, int y, int width, int height, double moveSpeed, int spawnDistance, int hitboxCorrection) {
        sprite = img;
        this.x = x;
        this.y = y;
        eWidth = width;
        eHeight = height;
        this.moveSpeed = moveSpeed;
        this.spawnDistance = spawnDistance;
        this.hitboxCorrection = hitboxCorrection;
    }

    public void collision (int teemox,  int teemoy) {
        if (teemoHealth > 0) {
            int teemoWidth = 100;
            int leftSide = x + hitboxCorrection;
            int rightSide = x + eWidth - hitboxCorrection;
            if (teemox < leftSide && teemox + teemoWidth > leftSide || teemox < rightSide && teemox + teemoWidth > rightSide || teemox > leftSide && teemox + teemoWidth < rightSide) {
                int teemoheight = 100;
                int topSide = y + hitboxCorrection;
                int botSide = y + eHeight - hitboxCorrection;
                if (teemoy + teemoheight >= topSide && teemoy <= topSide || teemoy <= botSide && teemoy + teemoheight >= botSide || teemoy >= topSide && teemoy + teemoheight <= botSide) {
                    teemoHealth -= 1;
                    if (teemoHealth > 0) {
                        x = -200;
                    }
                    
                }
            }
        }
    }

    public void movement () {
        if (x <= -200) {
            x = screenWidth + (int) (Math.random() * spawnDistance);
        } else {
            x = (int) (x + -bgscrollspeed * moveSpeed);
        }
    }

    public void behavior (Graphics g, int teemox, int teemoy) {
        movement();
        collision(teemox, teemoy);
        g.drawImage(sprite, x, y, eWidth, eHeight, null);
    }
}
