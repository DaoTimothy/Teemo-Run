import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Draw extends JPanel implements ActionListener {
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    Timer timer = new Timer(0, this);
    int counter = 0;

    private BufferedImage[] teemoSprite = new BufferedImage[6];
    private BufferedImage background1;
    private BufferedImage background2;

    int teemox = (int)size.getWidth() / 3, teemoy = (int)size.getHeight() - 300;
    int bg1x = 0, bg2x = (int)size.getWidth();
    int bgscrollspeed = 2;

    public Draw() {

        try {

            teemoSprite[0] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo1.png"));
            teemoSprite[1] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo2.png"));
            teemoSprite[2] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo3.png"));
            teemoSprite[3] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo4.png"));
            teemoSprite[4] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo5.png"));
            teemoSprite[5] = ImageIO.read(getClass().getResourceAsStream("/Images/Teemo/Teemo6.png"));
            background1 = ImageIO.read(getClass().getResourceAsStream("/Images/background.png"));
            background2 = ImageIO.read(getClass().getResourceAsStream("/Images/background.png"));

        } catch (IOException e) {

            e.printStackTrace();    

        }

    }

    public void paint(Graphics g) {

        g.drawImage(background1, bg1x, 0, (int)size.getWidth(), (int)size.getHeight(), null);
        g.drawImage(background2, bg2x, 0, (int)size.getWidth(), (int)size.getHeight(), null);

        
        g.drawImage(teemoSprite[(int)counter/18], teemox, teemoy, 100, 100, null);

        timer.start();
        counter++;
        if (counter >= 108) {
            counter = 0;
        }
    }

    public void actionPerformed(ActionEvent e) {

        bg1x += -bgscrollspeed;
        bg2x += -bgscrollspeed;
        if (bg1x <= -size.getWidth()) {
            bg1x = (int)size.getWidth();
        }
        if (bg2x <= -size.getWidth()) {
            bg2x = (int)size.getWidth();
        }

        repaint();

    }
    
}
