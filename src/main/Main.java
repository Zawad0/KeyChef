package main;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Key Chef");

        GamePanel gamePanel = new GamePanel();
        window.setIconImage(gamePanel.icon);
        window.add(gamePanel);
        window.pack();

        gamePanel.startGameThread();
        window.setLocationRelativeTo(null);

        window.setVisible(true);
    }
}


