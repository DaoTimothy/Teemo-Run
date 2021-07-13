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
    Font buttonFont = new Font("Roboto", Font.PLAIN, 75);
    Font gameoverButtonFont = new Font ("Roboto", Font.PLAIN, 50);
    Font scoreFont = new Font("Roboto", Font.PLAIN, 50);
    Font healthFont = new Font("Roboto", Font.PLAIN, 25);
    int score = 0;
    
    private BufferedImage crosshair;
    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[3];
    private BufferedImage gromp;
    private BufferedImage wolf;
    private BufferedImage[] raptor = new BufferedImage[3];
    private BufferedImage teemoDart;

    int teemox = screenWidth / 3, teemoy = screenHeight - 250;
    int teemoMaxHealth = 1, teemoHealth = teemoMaxHealth;
    int dartx = teemox, darty = teemoy, dartAngle = 0;
    double dartSpeed = 7.5;
    double xPerFrame = 0, yPerFrame = 0;
    boolean isDartMoving = false;
    double rotationRequired = 0;
    AffineTransformOp op;

    int grompx = screenWidth;
    int grompy = screenHeight - 325;

    int wolfx = screenWidth + 500;
    int wolfy = screenHeight - 275;

    int raptor1x = screenWidth + (int) (Math.random()*3000);
    int raptor2x = screenWidth + (int) (Math.random()*6000);
    int raptor3x = screenWidth + (int) (Math.random()*9000);
    int raptory = screenHeight - 600;

    int bg1x = 0, bg2x = screenWidth;
    int bgscrollspeed = screenWidth / 300;
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
            background[0] = ImageIO.read(getClass().getResourceAsStream("/Images/background.png"));
            background[1] = ImageIO.read(getClass().getResourceAsStream("/Images/background2.png"));
            background[2] = ImageIO.read(getClass().getResourceAsStream("/Images/MenuBackground.png"));
            gromp = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Gromp.png"));
            wolf = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Murk-Wolf.png"));
            raptor[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor1.png"));
            raptor[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor2.png"));
            raptor[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Raptor3.png"));
            teemoDart = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/teemoDart.png"));

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
                bg1x += -bgscrollspeed;
                bg2x += -bgscrollspeed;
                
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
                level();

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

    //Drawing the actual game method
    public void drawGame(Graphics g) {

        //Background
        g.drawImage(background[bg1type], bg1x, 0, screenWidth, screenHeight, null);
        g.drawImage(background[bg2type], bg2x, 0, screenWidth, screenHeight, null);

        //Gromp
        g.drawImage(gromp, grompx, grompy, 200, 200, null);
        
        //Wolf
        g.drawImage(wolf, wolfx, wolfy, 150, 150, null);
        
        //Raptors
        g.drawImage(raptor[0], raptor1x, raptory, 100, 100, null);

        g.drawImage(raptor[1], raptor2x, raptory, 100, 100, null);

        g.drawImage(raptor[2], raptor3x, raptory, 100, 100, null);
        
        //Teemo
        if (isCrouch) {

            g.drawImage(teemoHat, teemox, teemoy + 50, 100, 50, null);

        } else {

            g.drawImage(teemoSprite[(int)frameCounter/10], teemox, teemoy, 100, 100, null);
            if (jumping || gameOver) {
            } else {
                frameCounter++;
            }
            if (frameCounter >= 60) {
                frameCounter = 0;
            }

        } 

        //Dart
        if (!gameOver) {
            rotateDart();
        }
        g.drawImage(op.filter(teemoDart, null), dartx, darty, null);

        //Detects collision
        if (!gameOver) {
            grompx = enemyCollision(grompx, grompy, 200, 200, 40);
            wolfx = enemyCollision(wolfx, wolfy, 150, 150, 25);
            raptor1x = enemyCollision(raptor1x, raptory, 100, 100, 25);
            raptor2x = enemyCollision(raptor2x, raptory, 100, 100, 25);
            raptor3x = enemyCollision(raptor3x, raptory, 100, 100, 25);
        }
        
        //Crosshair
        pi = MouseInfo.getPointerInfo();
        p = pi.getLocation();
        g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);

        //Score
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString(String.format("Score: %08d", score), screenWidth - 500, 50);
        if (!gameOver) {
            score++;
        }

        //Health
        drawHealthBar(g);

        if (gameOver) {
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
        g.fillRect(10, 10, 700 / teemoMaxHealth * teemoHealth + 1, 50);

        g.setColor(Color.BLACK);
        g.setFont(healthFont);
        g.drawString(String.valueOf(teemoHealth) + "/" + String.valueOf(teemoMaxHealth), 335, 50);

        for(int i = teemoMaxHealth; i > 0; i--) {

            g.fillRect(10 + 700 / teemoMaxHealth * i, 4, 5, 20);

        }

    }

    public void drawGameOver (Graphics g) {
        bgscrollspeed = 0;
        drawGame(g);
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

        PointerInfo pi = MouseInfo.getPointerInfo();
        Point p = pi.getLocation();

        double degs = Math.toDegrees(Math.atan((double)Math.abs(((double)screenHeight - (double)darty) - ((double)screenHeight - (double)p.getY())) / (double)Math.abs((double)p.getX() - (double)dartx)));

        return degs;

    }

    //Rotates dart
    public void rotateDart() {

        if (isDartMoving == false) {

            PointerInfo pi = MouseInfo.getPointerInfo();
            Point p = pi.getLocation();

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

            xPerFrame = (double)dartSpeed * Math.cos(Math.toRadians(getAngle()));
            yPerFrame = (double)dartSpeed * Math.sin(Math.toRadians(getAngle()));

            PointerInfo pi = MouseInfo.getPointerInfo();
            Point p = pi.getLocation();

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
    public void level() {

        if (score >= 0) {
            grompx = enemyMovement(grompx, 1, 2000);
        } 
        if (score >= 2000) {
            wolfx = enemyMovement(wolfx, 2, 6000);
        }
        if (score >= 5000) {
            raptor1x = enemyMovement(raptor1x, 2, 3000);
            raptor2x = enemyMovement(raptor2x, 2, 3000);
            raptor3x = enemyMovement(raptor3x, 2, 3000); 
        }
        if (score % 10000 == 0) {
            bgscrollspeed *= 1.25;
        }
    }

    //Enemy movement method
    public int enemyMovement(int x, double speed, int spawnDistance) {
        if (x <= -200) {
            return screenWidth + (int) (Math.random() * spawnDistance);
        } else {
            return (int) (x + -bgscrollspeed * speed);
        }

    }

    //Enemy collision method
    public int enemyCollision(int ex, int ey, int ewidth, int eheight, int correction) {
        int teemowidth = 100;
        int leftSide = ex + correction;
        int rightSide = ex + ewidth - correction;
        if (teemox < leftSide && teemox + teemowidth > leftSide || teemox < rightSide && teemox + teemowidth > rightSide || teemox > leftSide && teemox + teemowidth < rightSide) {
            int teemoheight = 100;
            int topSide = ey + correction;
            int botSide = ey + eheight - correction;
            if (teemoy + teemoheight >= topSide && teemoy <= topSide || teemoy <= botSide && teemoy + teemoheight >= botSide || teemoy >= topSide && teemoy + teemoheight <= botSide) {
                teemoHealth -= 1;
                if (teemoHealth <= 0) {
                    gameOver = true;
                    return ex;
                }
                return -200;
            }
        }
        return ex;
    }

    //Resets game
    public void resetGame() {
        teemoy = screenHeight - 250;
        teemoHealth = teemoMaxHealth;
        bg1x = 0;
        bg2x = screenWidth;
        bg1type = 0;
        bg2type = 1;
        grompx = screenWidth;
        wolfx = screenWidth;
        raptor1x = screenWidth + (int) (Math.random()*3000);
        raptor2x = screenWidth + (int) (Math.random()*6000);
        raptor3x = screenWidth + (int) (Math.random()*9000);
        upPressed = false;
        upUnPressed = false;
        jumping = false;
        isCrouch = false; 
        jumpStrength = screenHeight / 55;   
        score = 0;
        bgscrollspeed = screenWidth / 300;
        gameOver = false;
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