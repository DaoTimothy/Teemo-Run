import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

public class Draw extends JPanel implements ActionListener, KeyListener, MouseListener {
    //Declaring variables
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    PointerInfo pi = MouseInfo.getPointerInfo();
    Point p = pi.getLocation();

    boolean mouseClicked = false;
    String gameState = "Menu";
    int screenWidth = (int)size.getWidth();
    int screenHeight = (int)size.getHeight();

    Timer timer = new Timer(0, this);
    int frameCounter = 0;

    int goldEarned = 0;
    int totalGold = 0;

    Color buttonGreen = new Color (51, 153, 102);
    Color healthGreen = new Color (12, 186, 6);
    Color shopBlue = new Color (51, 153, 255);
    Color shopButtonBlue = new Color (51, 102, 153);
    Color goldGold = new Color (252, 186, 3);
    Color grey = new Color (107, 107, 107);
    Font titleFont = new Font("Roboto", Font.BOLD, 100);
    Font buttonFont = new Font("Roboto", Font.PLAIN, 75);
    Font shopButtonFont = new Font ("Roboto", Font.PLAIN, 20);
    Font smallButtonFont = new Font ("Roboto", Font.PLAIN, 50);
    Font scoreFont = new Font("Roboto", Font.PLAIN, 50);
    Font healthFont = new Font("Roboto", Font.PLAIN, 25);
    Font gameOverFont = new Font ("Roboto", Font.PLAIN, 75);
    Font goldFont = new Font("Courier New", Font.BOLD, 30);
    Font shopPrice = new Font("Courier New", Font.PLAIN, 30);
    int score = 0;
    
    private BufferedImage crosshair;
    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[4];
    private BufferedImage grompImg;
    private BufferedImage wolfImg;
    private BufferedImage greyFilter;
    private BufferedImage[] raptor = new BufferedImage[3];
    private BufferedImage teemoDart;
    private BufferedImage[] items = new BufferedImage[6];
    private BufferedImage coin;

    int teemox = screenWidth / 3, teemoy = screenHeight - 250;
    int originalTeemoy = teemoy;
    int teemoMaxHealth = 1;
    int dartAngle = 0;
    double dartSpeed = 7.5;
    double xPerFrame = 0, yPerFrame = 0;
    double rotationRequired = 0;
    AffineTransformOp op;

    Enemy settings;
    Enemy gromp;
    Enemy wolf;
    Enemy raptor1;
    Enemy raptor2;
    Enemy raptor3;

    Item warmogs;
    Item boots;
    Item noonquiver;
    Item IE;

    int bg1x = 0, bg2x = screenWidth;
    int bg1type = 0, bg2type = 1;

    boolean gameOver = false;

    boolean upPressed = false;
    boolean jumping = false;
    boolean upUnPressed = false;
    float jumpStrength = screenHeight / 55;
    float weight = 0.35F;
    boolean isCrouch = false;

    public Draw() {

        //Getting images
        try {

            crosshair = ImageIO.read(getClass().getResourceAsStream("/Images/Crosshair.png"));
            teemoSprite[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo1.png"));
            teemoSprite[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo2.png"));
            teemoSprite[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo3.png"));
            teemoSprite[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo4.png"));
            teemoSprite[4] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo5.png"));
            teemoSprite[5] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo6.png"));
            teemoHat = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/TeemoHat.png"));
            background[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Backgrounds/background.png"));
            background[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Backgrounds/background2.png"));
            background[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Backgrounds/MenuBackground.png"));
            background[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Backgrounds/placeholder_ShopBackground.png"));
            grompImg = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Gromp.png"));
            wolfImg = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Murk-Wolf.png"));
            raptor[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor1.png"));
            raptor[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor2.png"));
            raptor[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor3.png"));
            teemoDart = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/teemoDart.png"));
            items[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Warmogs.png"));
            items[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Boots_of_Swiftness.png"));
            items[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Noonquiver.png"));
            items[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/IE.png"));
            items[4] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Mobility_Boots.png"));
            items[5] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/TP.png"));
            coin = ImageIO.read(getClass().getResourceAsStream("/Images/Poro Coin.png"));

            settings = new Enemy (teemoMaxHealth, screenWidth, screenWidth / 300);
            gromp = new Enemy (grompImg, 10, screenWidth, screenHeight - 325, 200, 200, 1, 2000, 40);
            wolf = new Enemy (wolfImg, 5, screenWidth, screenHeight - 275, 150, 150, 2, 6000, 25);
            raptor1 = new Enemy(raptor[0], 1, screenWidth, screenHeight - 600, 100, 100, 2, 3000, 25);
            raptor2 = new Enemy(raptor[1], 1, screenWidth + 400, screenHeight - 320, 100, 100, 2, 3000, 25);
            raptor3 = new Enemy(raptor[2], 1, screenWidth + 800, screenHeight - 600, 100, 100, 2, 3000, 25);

            warmogs = new Item (items[0], "Warmogs", "Increases Teemo's Health", 5, 500, 1000);
            boots = new Item (items[1], "Boots", "Increases Teemo's Jump Height", 8, 100, 200);
            noonquiver = new Item (items[2], "Noonquiver", "Increases Number of Darts", 3, 1000, 2000);
            IE = new Item (items[3], "Infinity Edge", "Increases Dart Damage", 5, 300, 500);

            greyFilter = ImageIO.read(getClass().getResourceAsStream("/Images/grey-filter.png"));

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    //Draw stuff of screen
    public void paint(Graphics g) {
        switch (gameState) {
            case "Menu":
                drawMenu(g);
                break;
            case "Shop":
                drawShop(g);
                break;
            case "Game":
                drawGame(g);
                break;
            case "GameOver":
                drawGameOver(g);
                break;
        }
        mouseClicked = false;
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();

        switch (gameState) {
            case "Game":
                //Scroll background
                bg1x += -Enemy.bgscrollspeed;
                bg2x += -Enemy.bgscrollspeed;
                
                if (bg1x <= -size.getWidth()) {
                    bg1x = screenWidth;
                    bg1type = (int)Math.floor(Math.random()*(2));
                }
                if (bg2x <= -size.getWidth()) {
                    bg2x = screenWidth;
                    bg2type = (int)Math.floor(Math.random()*(2));
                }

                //Teemo jumps
                if (upUnPressed) {
                    jumping = true;
                    if (jumpStrength > 0) {
                        jumpStrength /= 2;
                    }
                    upUnPressed = false;
                } else if (upPressed) {
                    jumping = true;
                }

                if (jumping) {
                    teemoy -= jumpStrength;
                    jumpStrength -= weight;
                    if (teemoy - jumpStrength >= screenHeight - 250) {
                        jumpStrength = screenHeight / 55 / 1.5F;
                        for (int i = 1; i < boots.level; i++) {
                            jumpStrength += screenHeight / 55 / 1.5F / 8;
                        }
                        teemoy = screenHeight - 250;
                        jumping = false;
                    }
                }

                //Dart
                dart();

                break;
        }
        
        //Repaints screen
        repaint();
    }

    //Drawing menu method
    public void drawMenu(Graphics g) {

        g.drawImage(background[2], 0, 0, screenWidth, screenHeight, null);
        
        drawMenuButton(g, screenWidth / 2 - 400, screenHeight - 400, 300, 200, buttonGreen, buttonFont, Color.BLACK, Color.WHITE, "Play", "Game", 75, 75);
        drawMenuButton(g, screenWidth / 2 + 100, screenHeight - 400, 300, 200, buttonGreen, buttonFont, Color.BLACK, Color.WHITE, "Shop", "Shop", 60, 75);
        if (gameState.equals("Game")) {
            resetGame();
        }
        
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);

    }

    //Drawing shop method
    public void drawShop(Graphics g) {
        
        g.drawImage(background[3], 0, 0, screenWidth, screenHeight, null);

        g.setFont(titleFont);
        g.setColor(shopBlue);
        g.drawString("Shop", screenWidth / 2 - 150, 100);

        drawShopItem(g, warmogs, screenWidth / 2 - 700, screenHeight / 4);
        drawShopItem(g, boots, screenWidth / 2 - 700, screenHeight / 2);
        drawShopItem(g, noonquiver, screenWidth / 2 + 100, screenHeight / 4);
        drawShopItem(g, IE, screenWidth / 2 + 100, screenHeight / 2);
    
        drawItemLevels(g, screenWidth / 2 - 475, screenHeight / 4 + 80, warmogs.maxLevel, warmogs.level);
        drawItemLevels(g, screenWidth / 2 - 475, screenHeight / 2 + 80, boots.maxLevel, boots.level);
        drawItemLevels(g, screenWidth / 2 + 325, screenHeight / 4 + 80, noonquiver.maxLevel, noonquiver.level);
        drawItemLevels(g, screenWidth / 2 + 325, screenHeight / 2 + 80, IE.maxLevel, IE.level);
    
        drawMenuButton(g, 100, screenHeight - 150, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Menu", "Menu", 35, 30);
        drawMenuButton(g, screenWidth - 300, screenHeight - 150, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Play", "Game", 50, 30);
        if (gameState.equals("Game")) {
            resetGame();
        }

        g.setFont(goldFont);
        g.setColor(goldGold);
        g.drawImage(coin, 100, 50, 100, 100, null);
        g.drawString(String.valueOf(totalGold), 200, 125);

        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);


    }

    //Draws item levels
    public void drawItemLevels(Graphics g, int x, int y, int maxLevel, int currentLevel) {

        if (maxLevel == 1) {

            g.fillRect(x, y, 300 / maxLevel, 25);

        } else {

            int barWidth = 300 / ((maxLevel * 2) - 1);

            for (int i = 1; i < maxLevel; i++) {

                g.setColor(grey);
                g.fillRect(x + (i * barWidth * 2), y, barWidth, 25);

            }

            for (int i = 0; i <= currentLevel; i++) {

                g.setColor(goldGold);
                g.fillRect(x + (i * barWidth * 2), y, barWidth, 25);

            }

        }



    }

    //Draws functioning menu button
    public void drawMenuButton(Graphics g, int x, int y, int width, int height, Color buttonColor, Font font, Color textColor, Color textHoverColor, String text, String menu, int textCorrectionx, int textCorrectiony) {

        g.setColor(buttonColor);
        g.setFont(font);
        g.fillRect(x, y, width, height);

        int mouseX = (int)p.getX();
        int mouseY = (int)p.getY();
        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            g.setColor(textHoverColor);
            g.drawString(text, x + textCorrectionx, y + height - textCorrectiony);
            if (mouseClicked) {
                gameState = menu;
                mouseClicked = false;
            }
        } else {
            g.setColor(textColor);
            g.drawString(text, x + textCorrectionx, y + height - textCorrectiony);
        }

    }

    public void drawShopButton(Graphics g, int x, int y, int width, int height, Color buttonColor, Font font, Color textColor, Color textHoverColor, String text, Item item, int textCorrectionx, int textCorrectiony) {

        g.setColor(buttonColor);
        g.setFont(font);
        g.fillRect(x, y, width, height);

        int mouseX = (int)p.getX();
        int mouseY = (int)p.getY();
        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            g.setColor(textHoverColor);
            g.drawString(text, x + textCorrectionx, y + height - textCorrectiony);
            if (mouseClicked && totalGold >= item.price && item.level < item.maxLevel - 1) {
                //Buy item
                totalGold -= item.price;
                item.upgrade();                
                mouseClicked = false;
            }
        } else {
            g.setColor(textColor);
            g.drawString(text, x + textCorrectionx, y + height - textCorrectiony);
        }

    }

    //Draws Shop items
    public void drawShopItem(Graphics g, Item item, int x, int y) {
        g.setFont(shopButtonFont);
        g.setColor(shopBlue);
        g.fillRect(x, y, 600, screenHeight / 5);
        g.drawImage(item.itemImage, x + 25, (y + (screenHeight / 5) / 2) - (screenHeight / 10 - 25), screenHeight / 5 - 50, screenHeight / 5 - 50, null);
        g.setColor(goldGold);
        if (item.level < item.maxLevel - 1) {
            g.drawString(String.format("%17s", "" + item.price + "g"), x + 450 , y + 25);
        } else {
            g.drawString(String.format("Sold Out!"), x + 500 , y + 25);
        }
        g.setColor(Color.BLACK);
        g.drawString(item.description, x + 220, y + 50);
        drawShopButton(g, x + 500, y + screenHeight / 5 - 75, 75, 50, Color.GREEN, healthFont, Color.BLACK, Color.WHITE, "Buy", item, 15, 15);
    }

    //Drawing the actual game method
    public void drawGame(Graphics g) {

        //Background
        g.drawImage(background[bg1type], bg1x, 0, screenWidth, screenHeight, null);
        g.drawImage(background[bg2type], bg2x, 0, screenWidth, screenHeight, null);
        g.drawImage(greyFilter, screenWidth + 200, screenHeight + 200, screenWidth, screenHeight, null);

        //Draws enemies and calculates collision
        level(g);
        
        //Teemo
        if (isCrouch) {

            teemoy = screenHeight - 200;
            g.drawImage(teemoHat, teemox, teemoy, 100, 50, null);

        } else {

            g.drawImage(teemoSprite[(int)frameCounter/10], teemox, teemoy, 100, 100, null);
            if (jumping || Enemy.teemoHealth <= 0) {
            } else {
                frameCounter++;
            }
            if (frameCounter >= 60) {
                frameCounter = 0;
            }

        } 

        //Dart
        g.drawImage(op.filter(teemoDart, null), Enemy.dartx, Enemy.darty, null);

        //Crosshair
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);

        //Score
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString(String.format("Score: %08d", score), screenWidth - 500, 50);
        if (Enemy.teemoHealth > 0) {
            score++;
        }

        //Health
        drawHealthBar(g);
        if (Enemy.teemoHealth <= 0) {
            gameState = "GameOver";
        }
    }

    //Drawing health bar method
    public void drawHealthBar(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(5, 5, 710, 60);

        g.setColor(Color.GRAY);
        g.fillRect(10, 10, 700, 50);

        g.setColor(healthGreen);
        g.fillRect(10, 10, 700 / teemoMaxHealth * Enemy.teemoHealth + 1, 50);

        g.setColor(Color.BLACK);
        g.setFont(healthFont);
        g.drawString(String.valueOf(Enemy.teemoHealth) + "/" + String.valueOf(teemoMaxHealth), 335, 50);

        for(int i = teemoMaxHealth; i > 0; i--) {

            g.fillRect(10 + 700 / teemoMaxHealth * i, 4, 5, 20);

        }

    }

    //Draws game over screen
    public void drawGameOver(Graphics g) {
        Enemy.bgscrollspeed = 0;
        drawGame(g);
        drawGoldCalc(g);

        g.drawImage(greyFilter, 0, 0, screenWidth, screenHeight, null);

        drawMenuButton(g, screenWidth - 300, screenHeight - 350, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Play", "Game", 50, 30);
        drawMenuButton(g, screenWidth - 300, screenHeight - 150, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Shop", "Shop", 45, 30);
        drawMenuButton(g, 100, screenHeight - 150, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Menu", "Menu", 35, 30);
        if (gameState.equals("Game")) {
            resetGame();
        }

        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);
    }

    //All dart stuff
    public void dart() {

        //Makes dart follow teemo
        if (Enemy.isDartMoving == false) {

            Enemy.darty = teemoy + 40;
            Enemy.dartx = teemox + 25;

        }

        //Gets angle
        double degs = Math.toDegrees(Math.atan((double)Math.abs(((double)screenHeight - (double)Enemy.darty) - ((double)screenHeight - (double)p.getY())) / (double)Math.abs((double)p.getX() - (double)Enemy.dartx)));

        //Gets dart movement amount
        if (Enemy.isDartMoving == false) {

            xPerFrame = dartSpeed * Math.cos(Math.toRadians(degs));
            yPerFrame = dartSpeed * Math.sin(Math.toRadians(degs));

            if (p.getX() < teemox) {

                xPerFrame = -xPerFrame - Enemy.bgscrollspeed;
            }
            if (p.getY() > teemoy) {

                yPerFrame = -yPerFrame;

            }

        }

        //Moves dart
        if (Enemy.isDartMoving == true) {

            Enemy.dartx += xPerFrame;
            Enemy.darty -= yPerFrame;

        }   
        
        //Rotates dart
        if (Enemy.isDartMoving == false) {

            double locationX = teemoDart.getWidth() / 2;
            double locationY = teemoDart.getHeight() / 2;

            if (p.getX() > teemox) {
                rotationRequired = Math.toRadians (-degs);
            } else if (p.getX() <= teemox) {
                rotationRequired = Math.toRadians (degs - 180);
            }
            if (p.getY() >= teemoy) {
                if (p.getX() >= teemox) {
                    rotationRequired = Math.toRadians(degs);
                } else {
                    rotationRequired = Math.toRadians(-degs + 180);
                }
            }

            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        }

        //Resets dart
        if (Enemy.dartx > screenWidth + 50 || Enemy.dartx < -50 || Enemy.darty > screenHeight + 50 || Enemy.darty < -50) {
    
            Enemy.isDartMoving = false;
    
        }


    }

    //Calculates gold after Teemo's death
    public void drawGoldCalc(Graphics g) {
        
        g.setFont(gameOverFont);

        g.setColor(Color.WHITE);
        g.fillRect(screenWidth / 2 - 250, 200, 500, 600);

        g.setColor(Color.BLACK);
        g.drawRect(screenWidth / 2 - 250, 200, 500, 600);
        g.drawString("Game Over", screenWidth / 2 - 190, 275);

        g.setFont(goldFont);
        if (goldEarned == 0) {
            goldEarned = (int) score / 150 + Enemy.grompsKilled * 50 + Enemy.wolvesKilled * 25 + Enemy.raptorsKilled * 10;
            totalGold += goldEarned;
        }
        g.drawString(String.format("Score: %-10s%8s", "" + score + "...", "..." + (int) score / 150 + "g"), screenWidth / 2 - 225, 350);
        g.drawString(String.format("Gromps: %-9s%8s", "" + Enemy.grompsKilled + "...", "..." + (int) Enemy.grompsKilled * 50 + "g"), screenWidth / 2 - 225, 400);
        g.drawString(String.format("Wolves: %-9s%8s", "" + Enemy.wolvesKilled + "...", "..." + (int) Enemy.wolvesKilled * 25 + "g"), screenWidth / 2 - 225, 450);
        g.drawString(String.format("Raptors: %-8s%8s", "" + Enemy.raptorsKilled + "...", "..." + (int) Enemy.raptorsKilled * 10 + "g"), screenWidth / 2 - 225, 500);
        g.drawString(String.format("%25s", "Total: " + goldEarned + "g"), screenWidth / 2 - 225, 750);
    }

    //Determines difficulty based of how long player survives
    public void level(Graphics g) {

        if (score >= 0) {
            gromp.behavior(g, teemox, teemoy);
        } 
        if (score >= 1000) {
            wolf.behavior(g, teemox, teemoy);
        }
        if (score >= 2000) {
            raptor1.behavior(g, teemox, teemoy);
            raptor2.behavior(g, teemox, teemoy);
            raptor3.behavior(g, teemox, teemoy);
        }
        if (score % 2500 == 0) {
            Enemy.bgscrollspeed *= 1.25 ;
        }
    }

    //Resets game
    public void resetGame() {
        teemoy = screenHeight - 250;
        bg1x = 0;
        bg2x = screenWidth;
        bg1type = 0;
        bg2type = 1;
        upPressed = false;
        upUnPressed = false;
        jumping = false;
        isCrouch = false; 
        jumpStrength = screenHeight / 55 / 1.5F;   
        score = 0;
        Enemy.bgscrollspeed = screenWidth / 300;
        gromp.x = screenWidth;
        wolf.x = screenWidth;
        raptor1.x = screenWidth;
        raptor2.x = screenWidth + 400;
        raptor3.x = screenWidth + 800;
        goldEarned = 0;
        Enemy.dartx = teemox;
        Enemy.darty = teemoy;
        itemFunctionality();
        Enemy.teemoHealth = teemoMaxHealth;
        gromp.health = gromp.maxHealth;
        wolf.health = wolf.maxHealth;
        raptor1.health = raptor1.maxHealth;
        raptor2.health = raptor2.maxHealth;
        raptor3.health = raptor3.maxHealth;
        Enemy.grompsKilled = 0;
        Enemy.wolvesKilled = 0;
        Enemy.raptorsKilled = 0;
        Enemy.isDartMoving = false;
    }

    public void itemFunctionality() {
        teemoMaxHealth = warmogs.level + 1;
        for (int i = 1; i < boots.level; i++) {
            jumpStrength += screenHeight / 55 / 1.5F / 8;
        }
        /*
        for (int i = 1; i < noonquiver.level; i++) {
            numDarts+=;
        }
        */
        Enemy.dartDamage = IE.level + 1;
    }

    //Accept user input
    public void keyPressed(KeyEvent e) {

        //Teemo jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE && upPressed == false && e.getKeyCode() != KeyEvent.VK_W && isCrouch == false) {
            upPressed = true;
        } 
        //Teemo Crouch
        if (e.getKeyCode() == KeyEvent.VK_W && upPressed == false && e.getKeyCode() != KeyEvent.VK_SPACE && isCrouch == false && jumping == false) {
            isCrouch = true;
            teemoy = screenHeight - 200;
        }

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        //Teemo uncrouch
        if (e.getKeyCode() == KeyEvent.VK_W && isCrouch == true) {
            isCrouch = false;
            teemoy = originalTeemoy;
        }
        //Short vs Long jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            upPressed = false;
            upUnPressed = true;
        }

        if (gameState == "Game" && e.getKeyCode() == KeyEvent.VK_E) {
            Enemy.isDartMoving = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) { 
        mouseClicked = true;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) { }

    @Override
    public void mouseExited(MouseEvent arg0) { }

    @Override
    public void mousePressed(MouseEvent arg0) { }

    @Override
    public void mouseReleased(MouseEvent arg0) { }

}