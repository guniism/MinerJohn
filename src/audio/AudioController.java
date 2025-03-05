package audio;

import javax.sound.sampled.*;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

public class AudioController {
    private Clip clip;
    private FloatControl volumeControl;
    public static AudioController bgMusic;

    // Constructor to load audio file
    public AudioController(String fileName) {
    if(bgMusic!=null)
    bgMusic.stop();
        try {
//        	String playerPath = ClassLoader.getSystemResource("boy.png").toString();
//          spriteSheet = new Image(playerPath);
        	
        	File audioFile = new File("res/" + fileName + ".wav");
        	AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play sound
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    // Loop sound
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Pause sound
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Resume sound
    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    // Stop sound
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Reset to start
        }
    }
    
    //setVolume
    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float newVolume = min + (max - min) * volume;
            volumeControl.setValue(newVolume);
        }
    }


    // Check if sound is playing
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}