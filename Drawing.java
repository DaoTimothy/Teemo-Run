import java.awt.*;
import javax.swing.JFrame;
public class Drawing extends Canvas{
    public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = (int)size.getWidth();
    public static int height = (int)size.getHeight();
    public static void main (String[] args) {
        JFrame frame = new JFrame("Teemo Run");
        Canvas canvas = new Drawing();
        
        canvas.setSize(width, height);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint (Graphics g) {
        Background b1 = new Background(0);
        while (true) {
            if (b1.x < -width) {
                b1.x = width*2;
            }
            g.drawImage(b1.img, b1.x, 0, width, height, null);
            b1.x -= 1;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Thread generates an error.");
            }
            g.clearRect(0,0, width, height);
        }
    }
}