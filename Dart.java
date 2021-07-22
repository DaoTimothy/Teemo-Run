import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

public class Dart {

    double x;
    double y;
    double xPerFrame;
    double yPerFrame;
    double dartAngle = 0;
    boolean getDartMovement = false;
    boolean isDartMoving = false;

    static double speed = 7.5;
    static int dartNum = 1;
    int dartNumber;

    AffineTransform tx = AffineTransform.getRotateInstance(0, 32, 32);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);  

    Dart(int x, int y) {

        this.x = x;
        this.y = y;
        dartNumber = dartNum;
        dartNum++;
    }

    public void movement(BufferedImage teemoDart, int teemox, int teemoy, double screenWidth, double screenHeight, Point p) {

        //Moves if not active
        if (isDartMoving == false) {

            y = screenHeight - 100;
            x = 100 * dartNumber;

        } 
        
        //Gets angle
        if (isDartMoving == false) {
            dartAngle = 90;
        }

        if (getDartMovement == true) { 
            dartAngle = Math.toDegrees(Math.atan((double)Math.abs(((double)screenHeight - (double)y) - ((double)screenHeight - ((double)p.getY() - 25))) / (double)Math.abs(((double)p.getX() - 25) - (double)x)));
        }

        //Gets dart movement amount
        if (getDartMovement == true) {
            xPerFrame = speed * Math.cos(Math.toRadians(dartAngle));
            yPerFrame = speed * Math.sin(Math.toRadians(dartAngle));

            if (p.getX() - 25 < x) {
                xPerFrame = -xPerFrame;
            }

            if (p.getY() - 25 > y) {
                yPerFrame = -yPerFrame;
            } 

        }
        
        //Moves dart
        if (isDartMoving == true) {

            x += xPerFrame;
            y -= yPerFrame;

        } 

        //Rotates dart
        if (getDartMovement == true) {
            double locationX = teemoDart.getWidth() / 2;
            double locationY = teemoDart.getHeight() / 2;

            double rotationRequired = 0;

            if (p.getX() - 25 > x) {
                rotationRequired = Math.toRadians(-dartAngle);
            } else if (p.getX() - 25 <= x) {
                rotationRequired = Math.toRadians(dartAngle - 180);
            }

            if (p.getY() - 25 >= y) {
                if (p.getX() - 25 >= x) {
                    rotationRequired = Math.toRadians(dartAngle);
                } else {
                    rotationRequired = Math.toRadians(-dartAngle + 180);
                }
            }

        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);  

        getDartMovement = false; 

        }
        
        //Resets dart
        if (x > screenWidth + 50 || x < -50 || y > screenHeight + 50 || y < -50) {
            isDartMoving = false;
        }        

    }

    public void behavior(BufferedImage teemoDart, int teemox, int teemoy, double screenWidth, int screenHeight, Point p) {

        movement(teemoDart, teemox, teemoy, screenWidth, screenHeight, p);
        
    }
    
}
