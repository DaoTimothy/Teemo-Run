import java.awt.*;  
import javax.swing.*;

public class TeemoRun {

    public static void main(String args[]) {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        Draw draw = new Draw();

        //Screen setup
        JFrame obj = new JFrame();
        obj.setBounds(0, 0, (int)size.getWidth(), (int)size.getHeight());
        obj.setBackground(Color.BLUE);
        obj.setExtendedState(JFrame.MAXIMIZED_BOTH);   //Fullscreen
        obj.setUndecorated(true);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(draw);
        obj.addKeyListener(draw); 

    }
    
}