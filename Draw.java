import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Draw extends JPanel implements ActionListener, KeyListener {
    //Declaring variables
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int)size.getWidth();
    int height = (int)size.getHeight();

    Timer timer = new Timer(0, this);
    int counter = 0;

    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[3];
    private BufferedImage gromp;
    private BufferedImage wolf;

    int teemox = width / 3, teemoy = height - 250;
    int bg1x = 0, bg2x = width;
    int bgscrollspeed = width / 300;
    int bg1type = 0, bg2type = 1;
    int grompx = width;
    int grompy = height - 325;
    int wolfx = width;
    int wolfy = height - 275;

    boolean gameOver = false;
    boolean upPressed = false;
    float jumpStrength = 10F;
    float weight = 0.25F;
    boolean isCrouch = false;

    public Draw() {

        //Getting images
        try {

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

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    //Draw stuff of screen
    public void paint(Graphics g) {
        if (!gameOver) {
            gameOver = false;
            //Background
            g.drawImage(background[bg1type], bg1x, 0, width, height, null);
            g.drawImage(background[bg2type], bg2x, 0, width, height, null);
            
            //Gromp
            g.drawImage(gromp, grompx, grompy, 200, 200, null);
            
            //Wolf
            g.drawImage(wolf, wolfx, wolfy, 150, 150, null);

            //Teemo
            if (isCrouch == true) {

                g.drawImage(teemoHat, teemox, teemoy + 50, 100, 50, null);

            } else {

                g.drawImage(teemoSprite[(int)counter/18], teemox, teemoy, 100, 100, null);
                counter++;
                if (counter >= 108) {
                    counter = 0;
                }

            } 
            
            // Gromp Collision

            //g.drawRect(grompx + 25, grompy + 25, 150 , 175);
            //Rectangle representing gromp's hitbox
            
            if (teemox < grompx + 25 && teemox + 100 > grompx + 25 || teemox < grompx + 175 && teemox + 100 > grompx + 175 || teemox > grompx + 25 && teemox + 100 < grompx + 175) {
                if (teemoy + 50 > grompy + 25) {
                    gameOver = true;
                }
            }
            
            //Wolf Collision
            //g.drawRect(wolfx + 25, wolfy + 25, 100 , 100);

            if (teemox < wolfx + 25 && teemox + 100 > wolfx + 25 || teemox < wolfx + 125 && teemox + 100 > wolfx + 125 || teemox > wolfx + 25 && teemox + 100 < wolfx + 175) {
                if (teemoy + 50 > wolfy + 25) {
                    gameOver = true;
                }
            }
            
        } else {
            //g.drawImage(background[2], 0, 0, width, height, null);
        }
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

        //Scroll background
        bg1x += -bgscrollspeed;
        bg2x += -bgscrollspeed;
        
        if (bg1x <= -size.getWidth()) {
            bg1x = width;
            bg1type = (int)Math.floor(Math.random()*(2));
        }
        if (bg2x <= -size.getWidth()) {
            bg2x = width;
            bg2type = (int)Math.floor(Math.random()*(2));
        }

        //Teemo jumps
        if (upPressed == true) {
            teemoy -= jumpStrength;
            jumpStrength -= weight;
            if (teemoy - jumpStrength >= height - 250) {
                upPressed = false;
                jumpStrength = 10;
                teemoy = height - 250;
            }
        }

        //Gromp
        grompx += -bgscrollspeed;
        if (grompx <= -200) {
            grompx = width + (int) (Math.random()*1000);
        }

        //Wolf
        wolfx += -bgscrollspeed*2;
        if (wolfx <= -200) {
            wolfx = width + (int) (Math.random()*3000);
        }

        
        repaint();

    }

    //Accept user input
    public void keyPressed(KeyEvent e) {

        //Teemo jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE && upPressed == false && e.getKeyCode() != KeyEvent.VK_DOWN && isCrouch == false) {
            upPressed = true;
        } 
        //Teemo Crouch
        if (e.getKeyCode() == KeyEvent.VK_DOWN && upPressed == false && e.getKeyCode() != KeyEvent.VK_SPACE && isCrouch == false) {
            isCrouch = true;
        }

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        //Teemo uncrouch
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isCrouch = false;
        }
    }
}