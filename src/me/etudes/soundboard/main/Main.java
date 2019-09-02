package me.etudes.soundboard.main;

import me.etudes.soundboard.gui.Window;

import javax.swing.*;

public class Main {

    private static Window window;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            window = new Window(800, 570, "SoundBoard");
        });
    }

    public static Window getWindow() {
        return window;
    }

}
