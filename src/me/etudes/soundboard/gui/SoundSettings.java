package me.etudes.soundboard.gui;

import me.etudes.soundboard.config.Config;
import me.etudes.soundboard.main.Main;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SoundSettings {

    private static JPanel contentPane;
    private static JFrame frame;
    private static JComboBox<Mixer.Info> dropDown;
    private static JCheckBox box;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 200;

    private SoundSettings(){}

    public static void showSoundSettings() {
        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {
                e.printStackTrace();
            }

            initFrame();
            initLabels();
            initDropDown();
            initButtons();
            initCheckBox();

            frame.setVisible(true);

        });
    }

    private static void initFrame() {
        frame = new JFrame("Sound Settings");
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setContentPane(contentPane);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon(SoundSettings.class.getClassLoader().getResource("icons/cog.png")).getImage());
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private static void initLabels() {
        JLabel deviceLabel = new JLabel("Output Device: ");
        Font font = new Font("SansSerif", Font.PLAIN, 16);
        deviceLabel.setFont(font);
        deviceLabel.setBounds(10, 10, 130, 40);
        contentPane.add(deviceLabel);

        JLabel infoLabel = new JLabel("<html><center>Note: Make sure you have Virtual Audio Cable Installed,<br />and that the " +
                "Virtual Cable device is selected</html>");
        infoLabel.setFont(Window.DEFAULT_FONT);
        infoLabel.setFont(font);
        infoLabel.setBounds(100, 60, 400, 40);
        contentPane.add(infoLabel);
    }

    private static void initDropDown() {
        Mixer.Info[] outputDevices = getOutputDevices();
        dropDown = new JComboBox(outputDevices);
        dropDown.setFocusable(false);
        dropDown.setFont(Window.DEFAULT_FONT);
        if(Config.getConfig().getSelectedDevice() != null) {
            dropDown.setSelectedItem(Config.getConfig().getSelectedDevice());
        }
        dropDown.setBounds(140, 15, WIDTH - 130 - 10 - 10 - 10, 30);
        contentPane.add(dropDown);
    }

    private static void initButtons() {
        JButton btnOk = new JButton("Ok");
        btnOk.setFocusable(false);
        btnOk.setFont(Window.DEFAULT_FONT);
        btnOk.setBounds(WIDTH - 20 - 80 - 100, HEIGHT - 30 - 20, 80, 30);
        btnOk.addActionListener(e -> {
            Mixer.Info device = dropDown.getItemAt(dropDown.getSelectedIndex());
            Config.getConfig().setDevice(device);
            for(Button button : Main.getWindow().getButtons()) {
                if(button.getSound() != null) {
                    button.getSound().setDevice(device);
                }
            }
            Config.getConfig().setDefaultChecked(box.isSelected());
            frame.dispose();
        });
        contentPane.add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(Window.DEFAULT_FONT);
        btnCancel.setFocusable(false);
        btnCancel.setBounds(WIDTH - 20 - 80, HEIGHT - 30 - 20, 80, 30);
        btnCancel.addActionListener(e -> {
            frame.dispose();
        });
        contentPane.add(btnCancel);
    }

    private static void initCheckBox() {
        box = new JCheckBox();
        box.setFont(Window.DEFAULT_FONT);
        box.setSelected(Config.getConfig().isDefaultChecked());
        box.setBounds(WIDTH - 40, 120, 20, 20);
        contentPane.add(box);

        JLabel label = new JLabel("Play sound through default device as well");
        label.setFont(Window.DEFAULT_FONT);
        label.setBounds(295, 115, 300, 30);
        contentPane.add(label);
    }

    private static Mixer.Info[] getOutputDevices() {
        ArrayList<Mixer.Info> outputDevices = new ArrayList<>();
        for(Mixer.Info info : AudioSystem.getMixerInfo()) {
            if(info.getDescription().equals("Direct Audio Device: DirectSound Playback")) {
                outputDevices.add(info);
            }
        }
        return outputDevices.toArray(new Mixer.Info[0]);
    }

}
