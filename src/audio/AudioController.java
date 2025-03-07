package audio;

import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioController {
    private Clip clip;
    private FloatControl volumeControl;
    public static AudioController bgMusic;

    // Constructor to load audio file
    public AudioController(String fileName) {
        // Stop any existing background music if playing
        if (bgMusic != null) {
            bgMusic.stop();
        }

        try {
            // Try to load the audio file (.wav first, then .WAV)
            InputStream audioStream = getAudioStream(fileName);
            if (audioStream == null) {
                throw new IllegalArgumentException("Audio file not found: " + fileName);
            }

            // Wrap the stream in a BufferedInputStream
            BufferedInputStream bufferedInputStream = new BufferedInputStream(audioStream);

            // Get the audio input stream
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedInputStream);

            // Initialize the clip
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Set up volume control
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to attempt loading both .wav and .WAV
    private InputStream getAudioStream(String fileName) {
        InputStream audioStream = getClass().getResourceAsStream("/" + fileName + ".wav");
        if (audioStream == null) {
            audioStream = getClass().getResourceAsStream("/" + fileName + ".WAV");
        }
        return audioStream;
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

    // Set volume
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
