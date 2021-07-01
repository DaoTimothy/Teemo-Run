import processing.core.*;
class Background extends PApplet{
    int x;
    PImage img;
    PApplet sketch;
    int screenWidth;
    int screenHeight;
    public Background (PApplet sketch, PImage tempImg, int x, int width, int height) {
        this.sketch = sketch;
        img = tempImg;
        this.x = x;
        screenWidth = width;
        screenHeight = height;
    }

    public void scroll () {
        x -= 10;   
    }

    public void display() {
        sketch.stroke(0);
        sketch.image(img, x, 0, screenWidth, screenHeight);
    }

    public void restart () {
        if (this.x < -screenWidth) {
            this.x = screenWidth;
        }
    }
}