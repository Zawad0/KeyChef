package main;

import minigames.KeyHandler;

import javax.swing.*;

public class WordsHandler {
    JProgressBar timerBar = new JProgressBar();
    KeyHandler kh = new KeyHandler();

    int durationStart = 8;

    WordsHandler(){
        timerBar.setValue(durationStart);
        timerBar.setBounds(100,100,100,40);
    }
}
