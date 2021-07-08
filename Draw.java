import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Draw extends JPanel implements ActionListener, KeyListener {
    //Declaring variables
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int)size.getWidth();
    int screenHeight = (int)size.getHeight();

    Timer timer = new Timer(0, this);
    int counter = 0;
    
    private BufferedImage crosshair;
    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[3];
    private BufferedImage gromp;
    private BufferedImage wolf;
    private BufferedImage[] raptor = new BufferedImage[3];

    int teemox = screenWidth / 3, teemoy = screenHeight - 250;

    int grompx = screenWidth;
    int grompy = screenHeight - 325;
    int grompHitboxCorrection = 175;

    int wolfx = screenWidth + 500;
    int wolfy = screenHeight - 275;
    int wolfHitboxCorrection = 125;

    int raptor1x = screenWidth + (int) (Math.random()*3000);
    int raptor2x = screenWidth + (int) (Math.random()*6000);
    int raptor3x = screenWidth + (int) (Math.random()*9000);
    int raptor1y = screenHeight - 500, raptor2y = screenHeight - 500, raptor3y = screenHeight - 500;

    int bg1x = 0, bg2x = screenWidth;
    int bgscrollspeed = screenWidth / 300;
    int bg1type = 0, bg2type = 1;

    

    boolean gameOver = false;

    boolean upPressed = false;
    float jumpStrength = 10F;
    float weight = 0.25F;
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

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    //Draw stuff of screen
    public void paint(Graphics g) {

        if (!gameOver) {

            //Background
            g.drawImage(background[bg1type], bg1x, 0, screenWidth, screenHeight, null);
            g.drawImage(background[bg2type], bg2x, 0, screenWidth, screenHeight, null);

            //Gromp
            g.drawImage(gromp, grompx, grompy, 200, 200, null);
            
            //Wolf
            g.drawImage(wolf, wolfx, wolfy, 150, 150, null);
            
            //Raptors
            g.drawImage(raptor[0], raptor1x, raptor1y, 100, 100, null);

            g.drawImage(raptor[1], raptor2x, raptor2y, 100, 100, null);

            g.drawImage(raptor[2], raptor3x, raptor3y, 100, 100, null);
            

            //Teemo
            if (isCrouch == true) {

                g.drawImage(teemoHat, teemox, teemoy + 50, 100, 50, null);

            } else {

                g.drawImage(teemoSprite[(int)counter/10], teemox, teemoy, 100, 100, null);
                if (!upPressed) {
                    counter++;
                }
                if (counter >= 60) {
                    counter = 0;
                }

            } 

            //Detects collision
            enemyCollision(grompx, grompy, grompHitboxCorrection);
            enemyCollision(wolfx, wolfy, wolfHitboxCorrection);
            
            //Crosshair
            PointerInfo pi = MouseInfo.getPointerInfo();
            Point p = pi.getLocation();
            g.drawImage(crosshair, (int)p.getX()-25, (int)p.getY()-25, 50, 50, null);


        } else {
            resetGame();
        }

        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

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
        if (upPressed == true) {
            teemoy -= jumpStrength;
            jumpStrength -= weight;
            if (teemoy - jumpStrength >= screenHeight - 250) {
                upPressed = false;
                jumpStrength = 10;
                teemoy = screenHeight - 250;
            }
        }

        //Moves enemies
        grompx = enemyMovement(grompx, 1, 1000);
        wolfx = enemyMovement(wolfx, 2, 3000);
        
        //Raptor 1
        raptor1x += -bgscrollspeed*1.5;
        if (raptor1x <= -150) {
            raptor1x = screenWidth + (int) (Math.random() * 5000);
        }
        
        //Raptor 2
        raptor2x += -bgscrollspeed*1.5;
        if (raptor2x <= -150) {
            raptor2x = screenWidth + (int) (Math.random() * 5000);
        }
        
        //Raptor 3
        raptor3x += -bgscrollspeed*1.5;
        if (raptor3x <= -150) {
            raptor3x = screenWidth + (int) (Math.random() * 5000);
        }
        //Repaints screen
        repaint();

    }    

    //Enemy movement method
    public int enemyMovement(int x, int speed, int spawnDistance) {
        if (x <= -200) {
            return screenWidth + (int) (Math.random() * spawnDistance);
        } else {
            return x + -bgscrollspeed * speed;
        }

    }

    //Enemy collision method
    public void enemyCollision(int x, int y, int correction) {
        if (teemox < x + 25 && teemox + 100 > x + 25 || teemox < x + correction && teemox + 100 > x + correction || teemox > x + 25 && teemox + 100 < x + 175) {
            if (teemoy + 50 > y + 25) {
                gameOver = true;
            }
        }
    }

    //Resets game
    public void resetGame() {
        teemoy = screenHeight - 250;
        bg1x = 0;
        bg2x = screenWidth;
        bg1type = 0;
        bg2type = 1;
        grompx = screenWidth;
        wolfx = screenWidth;
        raptor1x = screenWidth;
        raptor2x = screenWidth;
        raptor3x = screenWidth;
        gameOver = false;
        upPressed = false;
        isCrouch = false; 
        jumpStrength = 10F;   
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
    }
}