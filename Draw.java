import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Draw extends JPanel implements ActionListener, KeyListener {
    //Declaring variables
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    Timer timer = new Timer(0, this);
    int counter = 0;
    int score = 000000;

    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage teemoHat;
    private BufferedImage[] background = new BufferedImage[2];
    private BufferedImage gromp;
    private BufferedImage wolf;

    int teemox = (int)size.getWidth() / 3, teemoy = (int)size.getHeight() - 250;
    int bg1x = 0, bg2x = (int)size.getWidth();
    int bgscrollspeed = 5;
    int bg1type = 0, bg2type = 1;
    int grompx = (int)size.getWidth();
    int wolfx = (int)size.getWidth();

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
            gromp = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Gromp.png"));
            wolf = ImageIO.read(getClass().getResourceAsStream("/Images/Monsters/Murk-Wolf.png"));

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    //Draw stuff of screen
    public void paint(Graphics g) {

        //Background
        g.drawImage(background[bg1type], bg1x, 0, (int)size.getWidth(), (int)size.getHeight(), null);
        g.drawImage(background[bg2type], bg2x, 0, (int)size.getWidth(), (int)size.getHeight(), null);
        
        //Gromp
        g.drawImage(gromp, grompx, (int)size.getHeight() - 325, 200, 200, null);
        
        //Wolf
        g.drawImage(wolf, wolfx, (int)size.getHeight() - 275, 150, 150, null);

        //Teemo
        if (isCrouch == true) {

            g.drawImage(teemoHat, teemox, teemoy + 50, 150, 75, null);

        } else {

            g.drawImage(teemoSprite[(int)counter/18], teemox, teemoy - 50, 150, 150, null);
            counter++;
            if (counter >= 108) {
                counter = 0;
            }

        } 

        Font font = new Font("Verdana", Font.BOLD, 120);
        g.setFont(font);
        g.drawString(String.valueOf(score), 0, 100);

        score++;
        
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

        //Scroll background
        bg1x += -bgscrollspeed;
        bg2x += -bgscrollspeed;
        
        if (bg1x <= -size.getWidth()) {
            bg1x = (int)size.getWidth();
            bg1type = (int)Math.floor(Math.random()*(2));
        }
        if (bg2x <= -size.getWidth()) {
            bg2x = (int)size.getWidth();
            bg2type = (int)Math.floor(Math.random()*(2));
        }

        //Teemo jumps
        if (upPressed == true) {
            teemoy -= jumpStrength;
            jumpStrength -= weight;
            if (teemoy - jumpStrength >= (int)size.getHeight() - 250) {
                upPressed = false;
                jumpStrength = 10;
                teemoy = (int)size.getHeight() - 250;
            }
        }

        //Gromp
        grompx += -bgscrollspeed;
        if (grompx <= -200) {
            grompx = (int)size.getWidth() + (int) (Math.random()*1000);
        }

        //Wolf
        wolfx += -bgscrollspeed*2;
        if (wolfx <= -200) {
            wolfx = (int)size.getWidth() + (int) (Math.random()*3000);
        }

        repaint();

    }

    //Accept user input
    public void keyPressed(KeyEvent e) {

        //Teemo jump
        if (e.getKeyCode() == KeyEvent.VK_UP && upPressed == false && e.getKeyCode() != KeyEvent.VK_DOWN && isCrouch == false) {
            upPressed = true;
        } 
        //Teemo Crouch
        if (e.getKeyCode() == KeyEvent.VK_DOWN && upPressed == false && e.getKeyCode() != KeyEvent.VK_UP && isCrouch == false) {
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