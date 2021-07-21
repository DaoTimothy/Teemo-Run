import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
    int highscore = 0;
    
    private BufferedImage crosshair;
    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[3];
    private BufferedImage grompImg;
    private BufferedImage wolfImg;
    private BufferedImage greyFilter;
    private BufferedImage[] raptorImg = new BufferedImage[3];
    private BufferedImage teemoDart;
    private BufferedImage[] items = new BufferedImage[6];
    private BufferedImage coin;

    int teemox = screenWidth / 3, teemoy = screenHeight - 250;
    int originalTeemoy = teemoy;
    int teemoMaxHealth = 1;

    long raptorRand = 0;

    Enemy settings;
    Enemy gromp;
    Enemy wolf;
    Enemy raptor;

    Dart dart;

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

    boolean saveInitalized = false;
    File[] file = new File[3];

    Music gameMusic;
    boolean gameMusicPlaying = false;
    String gameMusicPath;

    Music menuMusic; 
    boolean menuMusicPlaying = false;
    String menuMusicPath;
    

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
            grompImg = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Gromp.png"));
            wolfImg = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Murk-Wolf.png"));
            raptorImg[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor1.png"));
            raptorImg[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor2.png"));
            raptorImg[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor3.png"));
            teemoDart = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/teemoDart.png"));
            items[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Warmogs.png"));
            items[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Boots_of_Swiftness.png"));
            items[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Noonquiver.png"));
            items[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/IE.png"));
            items[4] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Mobility_Boots.png"));
            items[5] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/TP.png"));
            coin = ImageIO.read(getClass().getResourceAsStream("/Images/Poro Coin.png"));

            settings = new Enemy (teemoMaxHealth, screenWidth, screenWidth / 300);
            gromp = new Enemy (10, screenWidth, screenHeight - 325, 200, 200, 1, 2000, 40);
            wolf = new Enemy (5, screenWidth, screenHeight - 275, 150, 150, 2, 6000, 25);
            raptor = new Enemy(1, screenWidth, screenHeight - 500, 100, 100, 1.75, 4000, 25);

            dart = new Dart(teemox, teemoy);

            warmogs = new Item (items[0], "Warmogs", "Increases Teemo's Health", 4, 500, 1000);
            boots = new Item (items[1], "Boots", "Increases Teemo's Jump Height", 7, 100, 200);
            noonquiver = new Item (items[2], "Noonquiver", "Increases Number of Darts", 2, 1000, 2000);
            IE = new Item (items[3], "Infinity Edge", "Increases Dart Damage", 4, 300, 500);

            greyFilter = ImageIO.read(getClass().getResourceAsStream("/Images/grey-filter.png"));

            gameMusic = new Music();
            gameMusicPath = "Sounds/teemo_run_track.wav";

            menuMusic = new Music();
            menuMusicPath = "Sounds/teemo_run_track.wav";

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    //Draw stuff of screen
    public void paint(Graphics g) {
        if (!saveInitalized) {
            initializeSave();
        }
        switch (gameState) {
            case "Menu":
                if (!menuMusicPlaying) {
                    menuMusic.setFile(menuMusicPath);
                    menuMusic.play();
                    menuMusic.loop();
                    menuMusicPlaying = true;
                }
                drawMenu(g);
                break;
            case "Shop":
                if (!menuMusicPlaying) {
                    menuMusic.setFile(menuMusicPath);
                    menuMusic.play();
                    menuMusic.loop();
                    menuMusicPlaying = true;
                }
                drawShop(g);
                break;
            case "Save":
                drawSaveGame(g);
                break;
            case "Game":
                menuMusic.stop();
                menuMusicPlaying = false;
                if (!gameMusicPlaying) {
                    gameMusic.setFile(gameMusicPath);
                    gameMusic.play();
                    gameMusic.loop();
                    gameMusicPlaying = true;
                }
                drawGame(g);
                break;
            case "GameOver":
                gameMusic.stop();
                gameMusicPlaying = false;
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
                        jumpStrength = screenHeight / 55 / 1.25F;
                        for (int i = 1; i < boots.level; i++) {
                            jumpStrength += screenHeight / 55 / 1.25F / 8;
                        }
                        teemoy = screenHeight - 250;
                        jumping = false;
                    }
                }

                //Dart
                //dart();

                break;
        }
        
        //Repaints screen
        repaint();
    }

    //Drawing menu method
    public void drawMenu(Graphics g) {

        g.drawImage(background[2], -5, 0, screenWidth + 5, screenHeight, null);
        
        drawMenuButton(g, screenWidth / 2 - 550, screenHeight - 400, 300, 200, buttonGreen, buttonFont, Color.BLACK, Color.WHITE, "Play", "Game", 75, 75);
        drawMenuButton(g, screenWidth / 2 - 150, screenHeight - 400, 300, 200, buttonGreen, buttonFont, Color.BLACK, Color.WHITE, "Shop", "Shop", 60, 75);
        drawMenuButton(g, screenWidth / 2 + 250, screenHeight - 400, 300, 200, buttonGreen, buttonFont, Color.BLACK, Color.white, "Save", "Save", 60, 75);
        if (gameState.equals("Game")) {
            resetGame();
        }
        
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);

    }

    //Drawing shop method
    public void drawShop(Graphics g) {
        
        g.drawImage(background[2], -5, 0, screenWidth + 5, screenHeight, null);

        g.setFont(titleFont);
        g.setColor(shopBlue);
        g.drawString("Shop", screenWidth / 2 - 150, 100);

        drawShopItem(g, warmogs, screenWidth / 2 - 700, screenHeight / 4);
        drawShopItem(g, boots, screenWidth / 2 - 700, screenHeight / 2);
        drawShopItem(g, noonquiver, screenWidth / 2 + 100, screenHeight / 4);
        drawShopItem(g, IE, screenWidth / 2 + 100, screenHeight / 2);
    
        drawItemLevels(g, screenWidth / 2 - 700 + screenHeight / 5, screenHeight / 4 + 100, warmogs.maxLevel, warmogs.level);
        drawItemLevels(g, screenWidth / 2 - 700 + screenHeight / 5, screenHeight / 2 + 100, boots.maxLevel, boots.level);
        drawItemLevels(g, screenWidth / 2 + 100 + screenHeight / 5, screenHeight / 4 + 100, noonquiver.maxLevel, noonquiver.level);
        drawItemLevels(g, screenWidth / 2 + 100 + screenHeight / 5, screenHeight / 2 + 100, IE.maxLevel, IE.level);
    
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

            for (int i = 0; i < maxLevel; i++) {

                g.setColor(grey);
                g.fillRect(x + (i * barWidth * 2), y, barWidth, 25);

            }

            for (int i = 0; i < currentLevel; i++) {

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
            if (mouseClicked && totalGold >= item.price && item.level < item.maxLevel) {
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
        item.price = item.originPrice + (item.priceChange * item.level);
        if (item.level < item.maxLevel) {
            g.drawString(String.format("%17s", "" + item.price + "g"), x + 450 , y + 25);
        } else {
            g.drawString(String.format("Sold Out!"), x + 500 , y + 25);
        }
        g.setColor(Color.BLACK);
        g.drawString(item.description, x + screenHeight / 5, y + 50);
        drawShopButton(g, x + 500, y + screenHeight / 5 - 75, 75, 50, Color.GREEN, healthFont, Color.BLACK, Color.WHITE, "Buy", item, 15, 15);
    }

    //Drawing Save Screen
    public void drawSaveGame(Graphics g) {
        g.drawImage(background[2], -5, 0, screenWidth + 5, screenHeight, null);

        drawSaveGameOption(g, file[0], screenHeight / 2 - 350);
        drawSaveGameOption(g, file[1], screenHeight / 2 - 100);
        drawSaveGameOption(g, file[2], screenHeight / 2 + 150);
        
        drawMenuButton(g, screenWidth - 320, screenHeight - 110, 200, 100, buttonGreen, smallButtonFont, Color.BLACK, Color.WHITE, "Menu", "Menu", 35, 30);
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);
    }

    //Drawing Save Screen Option
    public void drawSaveGameOption(Graphics g, File fileName, int y) {
        g.setColor(shopBlue);
        g.setFont(shopButtonFont);
        g.fillRect(screenWidth / 2 - 600, y, 500, 150);
        g.setColor(Color.BLACK);
        g.drawString(fileName.toString().substring(0, fileName.toString().length() - 4), screenWidth / 2 - 550, y + 50);
        g.drawString(previewSave(fileName), screenWidth / 2 - 550, y + 100);

        int mx = (int)p.getX();
        int my = (int)p.getY();
        
        g.setColor(shopBlue);
        g.setFont(smallButtonFont);
        g.fillRect(screenWidth / 2, y, 175, 150);

        if (mx >= screenWidth / 2 && mx <= screenWidth / 2 + 175 && my >= y && my <= y + 150) {
            g.setColor(Color.WHITE);
            g.drawString("Save", screenWidth / 2 + 25, y + 90);
            if (mouseClicked) {
                saveGame(fileName);
                mouseClicked = false;
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Save", screenWidth / 2 + 25, y + 90);
        }

        mx = (int)p.getX();
        my = (int)p.getY();

        g.setColor(shopBlue);
        g.fillRect(screenWidth / 2 + 225, y, 175, 150);

        if (mx >= screenWidth / 2 + 225 && mx <= screenWidth / 2 + 400 && my >= y && my <= y + 150) {
            g.setColor(Color.WHITE);
            g.drawString("Load", screenWidth / 2 + 250, y + 90);
            if (mouseClicked) {
                loadGame(fileName);
                mouseClicked = false;
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Load", screenWidth / 2 + 250, y + 90);
        }

        mx = (int)p.getX();
        my = (int)p.getY();

        g.setColor(Color.RED);
        g.fillRect(screenWidth / 2 + 450, y, 200, 150);

        if (mx >= screenWidth / 2 + 450 && mx <= screenWidth / 2 + 650 && my >= y && my <= y + 150) {
            g.setColor(Color.WHITE);
            g.drawString("Delete", screenWidth / 2 + 475, y + 90);
            if (mouseClicked) {
                deleteGame(fileName);
                mouseClicked = false;
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Delete", screenWidth / 2 + 475, y + 90);
        }
    }

    public String previewSave(File fileName) {
        try {
            Scanner sc = new Scanner (fileName);
            String temp = sc.nextLine();
            sc.close();
            return temp;
            
        } catch (IOException err) {
            err.printStackTrace();
        }
        return "";
    }

    public void initializeSave() {
        
        String[] fileNames = {"SaveFile1.txt", "SaveFile2.txt", "SaveFile3.txt"};
        for (int i = 0; i < file.length; i++) {
            try {
                file[i] = new File (fileNames[i]);
                Scanner sc = new Scanner (file[i]);
            } catch (FileNotFoundException e) {
                try {
                    file[i].createNewFile();
                } catch (IOException er) {
                    
                }
            }
        }

        saveInitalized = true;
        
        for (int i = 0; i < file.length; i++) {
            try {
                previewSave(file[i]);
            } catch (NoSuchElementException err) {
                try {
                    FileWriter fw = new FileWriter(fileNames[i]);
                    fw.write("Highscore: 0\n");
                    fw.write("Gold: 0\n");
                    fw.write("Warmog Level: 0\n");
                    fw.write("Boots Level: 0\n");
                    fw.write("Noonquiver Level: 0\n");
                    fw.write("IE Level: 0\n");
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
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
        if (Dart.isDartMoving == true) {
            g.drawImage(dart.op.filter(teemoDart, null), (int)dart.x, (int)dart.y, null);
        } else {
            g.drawImage(teemoDart, (int)dart.x, (int)dart.y, null);
        }

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

        Dart.isDartMoving = false;

        if (score > highscore) {

            highscore = score;

        }

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

    //Calculates gold after Teemo's death
    public void drawGoldCalc(Graphics g) {
        
        g.setFont(gameOverFont);

        g.setColor(Color.WHITE);
        g.fillRect(screenWidth / 2 - 250, 200, 500, 600);

        g.setColor(Color.BLACK);
        g.drawRect(screenWidth / 2 - 250, 200, 500, 600);
        g.drawString("Game Over", screenWidth / 2 - 190, 275);

        g.setFont(goldFont);
        g.drawString(String.format("Highscore: " + highscore), screenWidth / 2 - 120, 550);
        if (goldEarned == 0) {
            goldEarned = (int) score / 100 + Enemy.grompsKilled * 50 + Enemy.wolvesKilled * 25 + Enemy.raptorsKilled * 10;
            totalGold += goldEarned;
        }
        g.drawString(String.format("Score: %-10s%8s", "" + score + "...", "..." + (int) score / 100 + "g"), screenWidth / 2 - 225, 350);
        g.drawString(String.format("Gromps: %-9s%8s", "" + Enemy.grompsKilled + "...", "..." + (int) Enemy.grompsKilled * 50 + "g"), screenWidth / 2 - 225, 400);
        g.drawString(String.format("Wolves: %-9s%8s", "" + Enemy.wolvesKilled + "...", "..." + (int) Enemy.wolvesKilled * 25 + "g"), screenWidth / 2 - 225, 450);
        g.drawString(String.format("Raptors: %-8s%8s", "" + Enemy.raptorsKilled + "...", "..." + (int) Enemy.raptorsKilled * 10 + "g"), screenWidth / 2 - 225, 500);
        g.drawString(String.format("%25s", "Total: " + goldEarned + "g"), screenWidth / 2 - 225, 750);
    }

    //Determines difficulty based of how long player survives
    public void level(Graphics g) {

        if (score >= 0) {
            gromp.behavior(teemox, teemoy, dart.x, dart.y);
        } 
        if (score >= 1000) {
            wolf.behavior(teemox, teemoy, dart.x, dart.y);
        }
        if (score >= 3000) {
            raptor.behavior(teemox, teemoy, dart.x, dart.y);
            if (raptor.x <= -200) {
                raptorRand = Math.round(Math.random() * 2);
                if (raptorRand == 0) {
                    raptor.y = screenHeight - 500; 
                } else if (raptorRand == 1) {
                    raptor.y = screenHeight - 320;
                } else if (raptorRand == 2) {
                    raptor.y = screenHeight - 600;
                }
                raptorRand = Math.round(Math.random() * 2);
            }
        }
        if (score % 2500 == 0) {
            Enemy.bgscrollspeed *= 1.25 ;
        }

        dart.behavior(teemoDart, teemox, teemoy, screenWidth, screenHeight, p);

        g.drawImage(grompImg, gromp.x, gromp.y, gromp.eWidth, gromp.eHeight, null);
        g.drawImage(wolfImg, wolf.x, wolf.y, wolf.eWidth, wolf.eHeight, null);
        g.drawImage(raptorImg[(int)raptorRand], raptor.x, raptor.y, raptor.eWidth, raptor.eHeight, null);

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
        jumpStrength = screenHeight / 55 / 1.25F;   
        score = 0;
        Enemy.bgscrollspeed = screenWidth / 300;
        gromp.x = screenWidth;
        wolf.x = screenWidth;
        raptor.x = screenWidth;
        goldEarned = 0;
        itemFunctionality();
        Enemy.teemoHealth = teemoMaxHealth;
        gromp.health = gromp.maxHealth;
        wolf.health = wolf.maxHealth;
        raptor.health = raptor.maxHealth;
        Enemy.grompsKilled = 0;
        Enemy.wolvesKilled = 0;
        Enemy.raptorsKilled = 0;
        Dart.isDartMoving = false;
    }

    public void saveGame(File fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write("Highscore: " + highscore + "\n");
            fw.write("Gold: " + totalGold + "\n");
            fw.write("Warmog Level: " + warmogs.level + "\n");
            fw.write("Boots Level: " + boots.level + "\n");
            fw.write("Noonquiver Level: " + noonquiver.level + "\n");
            fw.write("IE Level: " + IE.level);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving game.");
            e.printStackTrace();
        }
    }

    public void loadGame(File fileName) {
        try {
            Scanner sc = new Scanner(fileName);
            highscore = Integer.parseInt(sc.nextLine().substring(11));
            totalGold = Integer.parseInt(sc.nextLine().substring(6));
            warmogs.level = Integer.parseInt(sc.nextLine().substring(14));
            boots.level = Integer.parseInt(sc.nextLine().substring(13));
            noonquiver.level = Integer.parseInt(sc.nextLine().substring(18));
            IE.level = Integer.parseInt(sc.nextLine().substring(10));
            sc.close();
            gameState = "Menu";
        } catch (IOException er) {
            System.out.println("Error loading game.");
            er.printStackTrace();
        }
    }

    public void deleteGame(File fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write("Highscore: 0\n");
            fw.write("Gold: 0\n");
            fw.write("Warmog Level: 0\n");
            fw.write("Boots Level: 0\n");
            fw.write("Noonquiver Level: 0\n");
            fw.write("IE Level: 0\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    public void itemFunctionality() {
        teemoMaxHealth = warmogs.level + 1;
        for (int i = 1; i < boots.level; i++) {
            jumpStrength += screenHeight / 55 / 1.25F / 8;
        }
        /*
        for (int i = 1; i < noonquiver.level; i++) {
            numDarts+=;
        }
        */
        Enemy.dartDamage = IE.level;
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

        if (gameState == "Game" && e.getKeyCode() == KeyEvent.VK_E && Dart.isDartMoving == false) {
            dart.x = teemox + 25;
            dart.y = teemoy + 25;
            Dart.isDartMoving = true;
            Dart.getDartMovement = true;
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