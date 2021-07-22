package src;
import java.awt.*;  
import javax.swing.*;
import java.awt.image.BufferedImage;

public class TeemoRun {

    public static void main(String args[]) {
        
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        Draw draw = new Draw();
        
        //Screen setup
        JFrame obj = new JFrame();

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        obj.setIconImage(Toolkit.getDefaultToolkit().getImage(TeemoRun.class.getResource("/images/teemo/Teemo3.png")));
        obj.getContentPane().setCursor(blankCursor);
        obj.setBounds(0, 0, (int)size.getWidth(), (int)size.getHeight());
        obj.setExtendedState(JFrame.MAXIMIZED_BOTH);   //Fullscreen
        obj.setUndecorated(true);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(draw);

        obj.addKeyListener(draw); 
        obj.addMouseListener(draw);

    }
    
}