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
    Point p;
    boolean mouseClicked = false;
    String gameState = "Menu";
    int screenWidth = (int)size.getWidth();
    int screenHeight = (int)size.getHeight();

    Timer timer = new Timer(0, this);
    int frameCounter = 0;

    Color buttonGreen = new Color (51, 153, 102);
    Color healthGreen = new Color (12, 186, 6);
    Color shopBlue = new Color (51, 153, 255);
    Color shopButtonBlue = new Color (51, 102, 153);
    Font titleFont = new Font("Roboto", Font.BOLD, 100);
    Font buttonFont = new Font("Roboto", Font.PLAIN, 75);
    Font shopButtonFont = new Font ("Roboto", Font.PLAIN, 20);
    Font gameoverButtonFont = new Font ("Roboto", Font.PLAIN, 50);
    Font scoreFont = new Font("Roboto", Font.PLAIN, 50);
    Font healthFont = new Font("Roboto", Font.PLAIN, 25);
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
    private BufferedImage leagueCoin;

    int teemox = screenWidth / 3, teemoy = screenHeight - 250;
    int teemoMaxHealth = 3;
    int dartx = teemox, darty = teemoy, dartAngle = 0;
    double dartSpeed = 7.5;
    double xPerFrame = 0, yPerFrame = 0;
    boolean isDartMoving = false;
    double rotationRequired = 0;
    AffineTransformOp op;

    Enemy settings;
    Enemy gromp;
    Enemy wolf;
    Enemy raptor1;
    Enemy raptor2;
    Enemy raptor3;

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
            items[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Mobility_Boots.png"));
            items[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/IE.png"));
            items[4] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/Noonquiver.png"));
            items[5] = ImageIO.read(getClass().getResourceAsStream("/Images/Items/TP.png"));
            leagueCoin = ImageIO.read(getClass().getResourceAsStream("/Images/LeagueCoin.png"));

            settings = new Enemy (teemoMaxHealth, screenWidth, screenWidth / 300);
            gromp = new Enemy (grompImg, screenWidth, screenHeight - 325, 200, 200, 1, 2000, 40);
            wolf = new Enemy (wolfImg, screenWidth, screenHeight - 275, 150, 150, 2, 6000, 25);
            raptor1 = new Enemy(raptor[0], screenWidth, screenHeight - 600, 100, 100, 2, 3000, 25);
            raptor2 = new Enemy(raptor[1], screenWidth + 400, screenHeight - 600, 100, 100, 2, 3000, 25);
            raptor3 = new Enemy(raptor[2], screenWidth + 800, screenHeight - 600, 100, 100, 2, 3000, 25);
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
                        jumpStrength = screenHeight / 55;
                        teemoy = screenHeight - 250;
                        jumping = false;
                    }
                }

                if (isDartMoving == false) {

                    darty = teemoy;
                    dartx = teemox;

                }

                //Repaints screen
                

                getDartMovement();

                moveDart();

                resetDart();

                break;
        }
        
        repaint();
    }

    //Drawing menu method
    public void drawMenu(Graphics g) {

        g.drawImage(background[2], 0, 0, screenWidth, screenHeight, null);
                
        g.setFont(buttonFont);

        g.setColor(buttonGreen);
        g.fillRect(screenWidth / 2 - 400, screenHeight - 400, 300, 200);
        g.fillRect(screenWidth / 2 + 100, screenHeight - 400, 300, 200);

        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();
        int mouseX = (int)p.getX();
        int mouseY = (int)p.getY();
        if (mouseX > screenWidth / 2 - 400 && mouseX < screenWidth / 2 - 100 && mouseY > screenHeight - 400 && mouseY < screenHeight - 200) {
            g.setColor(Color.WHITE);
            g.drawString("Play", screenWidth / 2 - 325, screenHeight - 275);
            g.setColor(Color.BLACK);
            g.drawString("Shop", screenWidth / 2 + 160, screenHeight - 275);
            if (mouseClicked) {
                resetGame();
                gameState = "Game";
                mouseClicked = false;
            }
        } else if (mouseX > screenWidth / 2 + 100 && mouseX < screenWidth / 2 + 300 && mouseY > screenHeight - 400 && mouseY < screenHeight - 200) {
            g.setColor(Color.BLACK);
            g.drawString("Play", screenWidth / 2 - 325, screenHeight - 275);
            g.setColor(Color.WHITE);
            g.drawString("Shop", screenWidth / 2 + 160, screenHeight - 275);
            if (mouseClicked) {
                gameState = "Shop";
                mouseClicked = false;
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Shop", screenWidth / 2 + 160, screenHeight - 275);
            g.drawString("Play", screenWidth / 2 - 325, screenHeight - 275);
        }
        
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);

    }

    //Drawing shop method
    public void drawShop(Graphics g) {
        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();
        
        g.drawImage(background[3], 0, 0, screenWidth, screenHeight, null);
        g.setFont(titleFont);
        g.setColor(shopButtonBlue);
        g.fillRect(screenWidth / 15, screenHeight / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.fillRect(screenWidth / 15, screenHeight * 3 / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.fillRect(screenWidth / 15, screenHeight * 5 / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.fillRect(screenWidth * 6 / 15, screenHeight / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.fillRect(screenWidth * 6 / 15, screenHeight * 3 / 8 + 100, screenWidth / 4, screenHeight / 5);

        g.drawImage(items[0], screenWidth / 15 + 30, screenHeight / 8 + 130, 150, 150, null);
        g.drawImage(items[1], screenWidth / 15 + 30, screenHeight * 3 / 8 + 130, 150, 150, null);
        g.drawImage(items[5], screenWidth / 15 + 30, screenHeight * 5 / 8 + 130, 150, 150, null);
        g.drawImage(items[4], screenWidth * 6 / 15 + 30, screenHeight / 8 + 130, 150, 150, null);
        g.drawImage(items[3], screenWidth * 6 / 15 + 30, screenHeight  * 3/ 8 + 130, 150, 150, null);

        g.setColor(shopBlue);
        g.drawString("Shop", screenWidth / 2 - 150, 100);
        g.drawRect(screenWidth / 15 - 1, screenHeight / 8 + 99, screenWidth / 4 + 2, screenHeight / 5 + 2);
        g.drawRect(screenWidth / 15, screenHeight / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.drawRect(screenWidth / 15 + 1, screenHeight / 8 + 101, screenWidth / 4 - 2, screenHeight / 5 - 2);

        g.drawRect(screenWidth / 15 - 1, screenHeight * 3 / 8 + 99, screenWidth / 4 + 2, screenHeight / 5 + 2);
        g.drawRect(screenWidth / 15, screenHeight * 3 / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.drawRect(screenWidth / 15 + 1, screenHeight * 3 / 8 + 101, screenWidth / 4 - 2, screenHeight / 5 - 2);

        g.drawRect(screenWidth / 15 - 1, screenHeight * 5 / 8 + 99, screenWidth / 4 + 2, screenHeight / 5 + 2);
        g.drawRect(screenWidth / 15, screenHeight * 5 / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.drawRect(screenWidth / 15 + 1, screenHeight * 5 / 8 + 101, screenWidth / 4 - 2, screenHeight / 5 - 2);

        g.drawRect(screenWidth * 6 / 15 - 1, screenHeight / 8 + 99, screenWidth / 4 + 2, screenHeight / 5 + 2);
        g.drawRect(screenWidth * 6 / 15, screenHeight / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.drawRect(screenWidth * 6 / 15 + 1, screenHeight / 8 + 101, screenWidth / 4 - 2, screenHeight / 5 - 2);

        g.drawRect(screenWidth * 6 / 15 - 1, screenHeight * 3 / 8 + 99, screenWidth / 4 + 2, screenHeight / 5 + 2);
        g.drawRect(screenWidth * 6 / 15, screenHeight * 3 / 8 + 100, screenWidth / 4, screenHeight / 5);
        g.drawRect(screenWidth * 6 / 15 + 1, screenHeight * 3 / 8 + 101, screenWidth / 4 - 2, screenHeight / 5 - 2);
        
        g.setColor(Color.BLACK);
        g.setFont(shopButtonFont);
        g.drawString("Increases Teemo's Health", screenWidth / 15 + 200, screenHeight / 8 + 200);
        g.drawString("Increases Teemo's", screenWidth / 15 + 200, screenHeight * 3 / 8 + 200);
        g.drawString("Jump Strength", screenWidth / 15 + 200, screenHeight * 3 / 8 + 225);
        g.drawString("Boss Fight", screenWidth / 15 + 200, screenHeight * 5 / 8 + 200);
        g.drawString("Increases Dart's Damage", screenWidth * 6 / 15 + 200, screenHeight / 8 + 200);
        g.drawString("Increases Number of Darts", screenWidth * 6 / 15 + 200, screenHeight * 3 / 8 + 200);

        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);
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

            g.drawImage(teemoHat, teemox, teemoy + 50, 100, 50, null);

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
        if (Enemy.teemoHealth > 0) {
            rotateDart();
        }
        g.drawImage(op.filter(teemoDart, null), dartx, darty, null);

        //Crosshair
        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();
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

    public void drawGameOver (Graphics g) {
        Enemy.bgscrollspeed = 0;
        drawGame(g);

        g.drawImage(greyFilter, 0, 0, screenWidth, screenHeight, null);
        g.setFont(gameoverButtonFont);

        g.setColor(buttonGreen);
        g.fillRect(100, screenHeight - 150, 200, 100);
        g.fillRect(screenWidth - 600, screenHeight - 150, 200, 100);
        g.fillRect(screenWidth - 300, screenHeight - 150, 200, 100);

        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();
        int mouseX = (int)p.getX();
        int mouseY = (int)p.getY();
        if (mouseX > screenWidth - 600 && mouseX < screenWidth - 400 && mouseY > screenHeight - 150 && mouseY < screenHeight - 50) {
            g.setColor(Color.WHITE);
            g.drawString("Play", screenWidth - 550, screenHeight - 80);
            g.setColor(Color.BLACK);
            g.drawString("Shop", screenWidth - 250, screenHeight - 80);
            g.drawString("Menu", 135, screenHeight - 80);
            if (mouseClicked) {
                resetGame();
                gameState = "Game";
                mouseClicked = false;
            }
        } else if (mouseX > screenWidth - 300 && mouseX < screenWidth - 100 && mouseY > screenHeight - 150 && mouseY < screenHeight - 50) {
            g.setColor(Color.BLACK);
            g.drawString("Play", screenWidth - 550, screenHeight - 80);
            g.drawString("Menu", 135, screenHeight - 80);
            g.setColor(Color.WHITE);
            g.drawString("Shop", screenWidth - 250, screenHeight - 80);
            if (mouseClicked) {
                gameState = "Shop";
                mouseClicked = false;
            }
        } else if (mouseX > 100 && mouseX < 300 && mouseY > screenHeight - 150 && mouseY < screenHeight - 50) {
            g.setColor(Color.BLACK);
            g.drawString("Play", screenWidth - 550, screenHeight - 80);
            g.drawString("Shop", screenWidth - 250, screenHeight - 80);
            g.setColor(Color.WHITE);
            g.drawString("Menu", 135, screenHeight - 80);
            if (mouseClicked) {
                gameState = "Menu";
                mouseClicked = false;
            }
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Shop", screenWidth - 250, screenHeight - 80);
            g.drawString("Play", screenWidth - 550, screenHeight - 80);
            g.drawString("Menu", 135, screenHeight - 80);
        }
        
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);
    }
    
    //Gets angle of dart and teemo
    public double getAngle() {

        double degs = Math.toDegrees(Math.atan((double)Math.abs(((double)screenHeight - (double)darty) - ((double)screenHeight - (double)p.getY())) / (double)Math.abs((double)p.getX() - (double)dartx)));

        return degs;

    }

    //Rotates dart
    public void rotateDart() {

        if (isDartMoving == false) {

            double locationX = teemoDart.getWidth() / 2;
            double locationY = teemoDart.getHeight() / 2;

            if (p.getX() > teemox) {
                rotationRequired = Math.toRadians (-getAngle());
            } else if (p.getX() <= teemox) {
                rotationRequired = Math.toRadians (getAngle() - 180);
            }
            if (p.getY() >= teemoy) {
                if (p.getX() >= teemox) {
                    rotationRequired = Math.toRadians(getAngle());
                } else {
                    rotationRequired = Math.toRadians(-getAngle() + 180);
                }
            }

            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        }

    }

    //Gets the amount of x and y the dart is going to move by
    public void getDartMovement() {

        if (isDartMoving == false) {

            xPerFrame = dartSpeed * Math.cos(Math.toRadians(getAngle()));
            yPerFrame = dartSpeed * Math.sin(Math.toRadians(getAngle()));

            if (p.getX() < teemox) {

                xPerFrame = -xPerFrame;
            }
            if (p.getY() > teemoy) {

                yPerFrame = -yPerFrame;

            }

        }

    }

    //Moves the dart
    public void moveDart() {

        if (isDartMoving == true) {

            dartx += xPerFrame;
            darty -= yPerFrame;

        }

    }

    //Resets dart when hit border
    public void resetDart() {

        if (dartx > screenWidth + 50 || dartx < -50 || darty > screenHeight + 50 || darty < -50) {
    
            isDartMoving = false;
    
        }
    
    }

    //Determines difficulty based of how long player survives
    public void level(Graphics g) {

        if (score >= 0) {
            gromp.behavior(g, teemox, teemoy);
        } 
        if (score >= 1000) {
            wolf.behavior(g, teemox, teemoy);
        }
        if (score >= 3000) {
            raptor1.behavior(g, teemox, teemoy);
            raptor2.behavior(g, teemox, teemoy);
            raptor3.behavior(g, teemox, teemoy);
        }
        if (score % 5000 == 0) {
            Enemy.bgscrollspeed *= 1.25;
        }
    }

    //Resets game
    public void resetGame() {
        teemoy = screenHeight - 250;
        Enemy.teemoHealth = teemoMaxHealth;
        bg1x = 0;
        bg2x = screenWidth;
        bg1type = 0;
        bg2type = 1;
        upPressed = false;
        upUnPressed = false;
        jumping = false;
        isCrouch = false; 
        jumpStrength = screenHeight / 55;   
        score = 0;
        Enemy.bgscrollspeed = screenWidth / 300;
        gromp.x = screenWidth;
        wolf.x = screenWidth;
        raptor1.x = screenWidth;
        raptor2.x = screenWidth + 400;
        raptor3.x = screenWidth + 800;
    }

    //Accept user input
    public void keyPressed(KeyEvent e) {

        //Teemo jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE && upPressed == false && e.getKeyCode() != KeyEvent.VK_W && isCrouch == false) {
            upPressed = true;
        } 
        //Teemo Crouch
        if (e.getKeyCode() == KeyEvent.VK_W && upPressed == false && e.getKeyCode() != KeyEvent.VK_SPACE && isCrouch == false) {
            isCrouch = true;
        }

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        //Teemo uncrouch
        if (e.getKeyCode() == KeyEvent.VK_W) {
            isCrouch = false;
        }
        //Short vs Long jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            upPressed = false;
            upUnPressed = true;
        }

        if (gameState == "Game" && e.getKeyCode() == KeyEvent.VK_E) {
            isDartMoving = true;
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