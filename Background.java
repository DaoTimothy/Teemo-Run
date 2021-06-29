import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Background extends Canvas{
    int x;
    BufferedImage img = null;
    

    public Background (int x) {
        this.x = x;
        try{
            this.img = ImageIO.read(new File("Background.png"));
        }catch (IOException e) {
            System.out.println ("Failed");
        }
    }

    public void paint (Graphics g) {
    
    }
}
