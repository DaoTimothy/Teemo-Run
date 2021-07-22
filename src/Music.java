package src;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

    Clip clip;

    public void setFile(String filename) {

        try {
            File file = new File (filename);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        } catch (Exception e) {
        }

    }

    public void play() {

        clip.setFramePosition(0);
        clip.start();

    }

    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void stop() {

        clip.stop();
        clip.close();
        
    }

    public void playSound(String soundName) {
        try {
            File file = new File (soundName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}