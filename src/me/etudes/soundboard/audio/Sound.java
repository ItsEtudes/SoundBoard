package me.etudes.soundboard.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;
import java.io.File;

public class Sound {

    private File file;
    private Clip clip;

    public Sound(File file) {
        this.file = file;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Sound(File file, Mixer.Info device) {
        this.file = file;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip(device);
            clip.open(stream);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setDevice(Mixer.Info device) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip(device);
            clip.open(stream);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public File getFile() {
        return file;
    }
}
