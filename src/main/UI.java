package main;


import entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class UI{
    BufferedImage dialogue;

    public String fullText;
    public String currentDialogue = "";
    private double typeTimer = 0;
    private int charIndex = 0;
    private double typeSpeed = 0.05;
    public boolean typing = false;
    Font pixelFont;

    UI(){

        try {

            pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().
                    getResourceAsStream("/PixelifySans-VariableFont_wght.ttf")));
            dialogue = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Sprite-Dialogue.png")));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g){
        if(Player.gameState == GameState.DIALOGUE){
            drawDialouge(g);
        }
    }

    public void drawDialouge(Graphics g) {

        g.drawImage(dialogue, Constants.DIALOGUE_WINDOWX, Constants.DIALOGUE_WINDOWY, Constants.DIALOGUE_WINDOW_WIDTH, Constants.DIALOGUE_WINDOW_HEIGHT, null);
    }

    public void type(double dt){
        if(typing){
            typeTimer+=dt;

            if(typeTimer >= typeSpeed && charIndex < fullText.length()){
                currentDialogue+=fullText.charAt(charIndex);
                charIndex++;
                typeTimer = 0;
                System.out.println(currentDialogue);
            }
            if(charIndex>=fullText.length()){
                typing = false;
            }
        }
    }
    public void startTyping(String text) {
        fullText = text;
        currentDialogue = "";
        charIndex = 0;
        typeTimer = 0;
        typing = true;
        //System.out.println("Started typing: " + text);

    }

    public void typeOut(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setFont(pixelFont.deriveFont(Font.BOLD, 23f));
        g.setColor(Color.BLACK);

        g.drawString(currentDialogue, Constants.TEXTX, Constants.TEXTY);
    }

}
