import java.awt.*;
import javax.swing.JFrame;
public class Drawing extends Canvas{
    public static void main (String[] args) {
        JFrame frame = new JFrame("Teemo Run");
        Canvas canvas = new Drawing();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        canvas.setSize(width, height);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        
    }

    public void paint (Graphics g) {
        Background b1 = new Background(200);
        g.drawImage(b1.img, 0, 0, null);
        
    }
}