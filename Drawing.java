import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
public class Drawing extends Canvas{
    public static void main (String[] args) {
        JFrame frame = new JFrame("League of Invaders 2");
        Canvas canvas = new Drawing();
        canvas.setSize(500, 500);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint (Graphics g) {
        g.setColor(Color.red);
        g.fillOval(150, 150, 200, 200);
    }
}