package me.etudes.soundboard.gui;

import me.etudes.soundboard.audio.Sound;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    private Sound sound = null;
    private String name;
    private Rectangle bounds;

    public Button(String name, int x, int y, int width, int height) {
        super(name);
        bounds = new Rectangle(x, y, width, height);
        setBounds(bounds);
        setFocusable(false);
        setVisible(true);
    }

    public boolean hasSound() {
        return sound != null;
    }

    public void playSound() {
        sound.play();

    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

}
