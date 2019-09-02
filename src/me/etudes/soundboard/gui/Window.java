package me.etudes.soundboard.gui;

import com.sun.javafx.application.PlatformImpl;
import javafx.stage.FileChooser;
import me.etudes.soundboard.audio.Sound;
import me.etudes.soundboard.config.Config;

import javax.sound.sampled.Mixer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class Window {

    private JFrame frame;
    private Button[] buttons;
    private JTextField filePath;
    private JButton selectButton;
    private JButton browseButton;
    private JButton setButton;

    private int selectedButton;
    private String selectedFilePath = null;

    private JPanel contentPane;

    private int width;
    private int height;
    private String title;

    public static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        initFrame();
        setupButtons();
        initTextField();
        initMenu();
        frame.pack();
        frame.setVisible(true);
    }

    private void initFrame() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBounds(0, 0, width, height);
        contentPane.setPreferredSize(new Dimension(width, height));
        contentPane.setLayout(null);
        frame.add(contentPane);
    }

    private void setupButtons() {
        final int width = 182;
        final int height = 117;
        buttons = new Button[4 * 4];

        // listener for deselecting focus
        KeyAdapter listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if(selectedButton >= 0 && selectedButton <= 16) {
                        buttons[selectedButton].setFocusable(false);
                    }
                }
            }
        };
        Font font = new Font("Arial", Font.PLAIN, 16);

        int buttonCount = 0;
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                int xPos = 20 + x * (10 + width);
                int yPos = 20 + y * (10 + height);
                Button button;
                if(Config.getConfig().getSoundPath(buttonCount) == null) {
                    button = new Button("<html><center>Sound " + buttonCount + "<br />(Not Set)</html>",
                            xPos,  yPos, width, height);
                } else {
                    String soundName = Config.getConfig().getSoundName(buttonCount);
                    button = new Button("<html><center>Sound " + buttonCount + "<br />" + soundName +
                            "</html>", xPos, yPos, width, height);
                    File file = new File(Config.getConfig().getSoundPath(buttonCount));
                    Mixer.Info device = Config.getConfig().getSelectedDevice();
                    if(device == null) {
                        button.setSound(new Sound(file));
                    } else {
                        button.setSound(new Sound(file, device));
                    }
                }
                button.addKeyListener(listener);
                button.setFont(font);
                button.addActionListener(e -> {
                    if(button.hasSound()) {
                        button.playSound();
                        if(Config.getConfig().isDefaultChecked()) {
                            File file = button.getSound().getFile();
                            Sound sound = new Sound(file);
                            sound.play();
                        }
                    }
                });
                buttons[x + y * 4] = button;
                contentPane.add(button);
                buttonCount++;
            }
        }

        selectButton = new JButton("Select");
        selectButton.setFont(DEFAULT_FONT);
        selectButton.setFocusable(false);
        selectButton.addActionListener(e -> {
            String data = JOptionPane.showInputDialog("Enter Sound Number");
            try {
                this.selectedButton = Integer.parseInt(data);
                buttons[selectedButton].setFocusable(true);
                buttons[selectedButton].requestFocus();
            } catch(NumberFormatException ex) {
                if(data != null) JOptionPane.showMessageDialog(null, "Invalid number", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        selectButton.setBounds(this.width - 50 - 154, this.height - 20 - 20, 80, 30);
        contentPane.add(selectButton);

        browseButton = new JButton("Browse");
        browseButton.setFont(DEFAULT_FONT);
        browseButton.setBounds(this.width - 50 - 64, this.height - 20 - 20, 80, 30);
        browseButton.setFocusable(false);
        browseButton.addActionListener(e -> {
            PlatformImpl.startup(() -> {
                try {
                    FileChooser fc = new FileChooser();
                    String path = fc.showOpenDialog(null).getPath();
                    File file = new File(path);
                    if (file.getName().contains(".wav") && file.exists()) {
                        selectedFilePath = path;
                        JOptionPane.showMessageDialog(null, "Successfully set file path " +
                                "to: " + selectedFilePath);
                    } else if (!file.exists()) {
                        JOptionPane.showMessageDialog(null, filePath.getText() + " is not an " +
                                "existing file!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid file type. Make sure your " +
                                "file is a .wav file format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    filePath.setText(selectedFilePath);
                } catch(NullPointerException ignored) {}
            });
        });
        contentPane.add(browseButton);

        setButton = new JButton("Set");
        setButton.setFont(DEFAULT_FONT);
        setButton.setBounds(20, this.height - 20 - 20, 90, 30);
        setButton.setFocusable(false);
        setButton.addActionListener(e -> {
            if(selectedButton >= 0 && selectedButton <= 16 && selectedFilePath != null) {
                File file = new File(selectedFilePath);
                buttons[selectedButton].setFocusable(false);
                if(Config.getConfig().getSelectedDevice() != null) {
                    buttons[selectedButton].setSound(new Sound(new File(selectedFilePath), Config.getConfig().getSelectedDevice()));
                } else {
                    buttons[selectedButton].setSound(new Sound(new File(selectedFilePath)));
                }
                Config.getConfig().setSoundName(selectedButton, file.getName());
                Config.getConfig().setSoundPath(selectedButton, file.getPath());
                buttons[selectedButton].setText("<html><center>Sound " + selectedButton +
                        "<br />(" + Config.getConfig().getSoundName(selectedButton) + ")</html>");
            }
        });
        contentPane.add(setButton);
    }

    private void initTextField() {
        filePath = new JTextField("");
        filePath.setFont(DEFAULT_FONT);
        filePath.setBounds(120, height - 20 - 20, width - 40 - 195 - 100, 30);
        filePath.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    File file = new File(filePath.getText());
                    if(file.getName().contains(".wav") && file.exists()) {
                        selectedFilePath = filePath.getText();
                        JOptionPane.showMessageDialog(null, "Successfully set file path " +
                                "to: " + selectedFilePath);
                    } else if(!file.exists()) {
                        JOptionPane.showMessageDialog(null, filePath.getText() + " is not an " +
                                "existing file!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid file type. Make sure your " +
                                "file is a .wav file format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    contentPane.requestFocusInWindow();
                }
            }
        });
        contentPane.add(filePath);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(DEFAULT_FONT);
        JMenu soundMenu = new JMenu("Sound");
        soundMenu.setFont(DEFAULT_FONT);
        JMenuItem soundSettings = new JMenuItem("Settings");
        soundSettings.setFont(DEFAULT_FONT);

        // settings icon
        ImageIcon unscaledIcon = new ImageIcon(this.getClass().getClassLoader().getResource("icons/cog.png"));
        Image i = unscaledIcon.getImage();
        Image scaledImg = i.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledImg);
        soundSettings.setIcon(icon);

        soundSettings.addActionListener(e -> {
            SoundSettings.showSoundSettings();
        });

        soundMenu.add(soundSettings);
        menuBar.add(soundMenu);
        frame.setJMenuBar(menuBar);
    }

    public Button[] getButtons() {
        return buttons;
    }
}
