import processing.core.*;
public class TeemoRun extends PApplet {
    PImage jungleBackground;
    PImage menuBackground;
    Background jungle1;
    Background jungle2;
    Background menu;
    boolean exitMenu = false;
    public void setup () {
        frameRate(100);
        jungleBackground = loadImage("Photos/JungleBackground.png");
        menuBackground = loadImage("Photos/MenuBackground.png");
        jungle1 = new Background (this, jungleBackground, 0, width, height);
        jungle2 = new Background (this, jungleBackground, width, width, height);
        menu = new Background (this, menuBackground, 0, width, height);
    }   
    public void settings () {
        fullScreen();
    }
    
    public void draw() {
        if (!exitMenu) {
            menu.display();
        } else {
            gameplayLoop();
        }
    }

    public static void main(String[] args) {
        String[] appletArgs = new String[] { "TeemoRun" };
        PApplet.main(appletArgs);
    }

    public void gameplayLoop () {
        jungle1.scroll();
        jungle1.display();
        jungle1.restart();
        jungle2.scroll();
        jungle2.display();
        jungle2.restart();
    }

    public void mousePressed () {
        exitMenu = true;
    }

}
