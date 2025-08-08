package main;


import entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;


public class UI{
    BufferedImage dialogue;
    public String fullText;
    public String currentDialogue = "";
    private double typeTimer = 0;
    private int charIndex = 0;
    private double typeSpeed = 0.05;
    public boolean typing = false;
    public int textIndex = 1;
    public boolean typingOut = false;
    Font pixelFont;

    UI(){

        try {

            pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().
                    getResourceAsStream("/text/PixelifySans-VariableFont_wght.ttf")));
            dialogue = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/Sprite-Dialogue.png")));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void say(){
        switch (textIndex){
            case 1:
                startTyping(Constants.DIALOGUE1);
                textIndex++;
                typingOut = true;
                break;
            case 2:
                startTyping(Constants.DIALOGUE2);
                textIndex++;
                typingOut = true;
                break;
            case 3:
                startTyping(Constants.DIALOGUE3);
                textIndex++;
                typingOut = true;
                break;
            case 4:
                startTyping(Constants.DIALOGUE4);
                textIndex++;
                typingOut = true;
                break;
            case 5:
                startTyping(Constants.DIALOGUE5);
                textIndex++;
                typingOut = true;
                break;
            case 6:
                startTyping(Constants.DIALOGUE6);
                textIndex++;
                typingOut = true;
                break;
            case 7:
                startTyping(Constants.DIALOGUE7);
                textIndex++;
                typingOut = true;
                break;
            case 8:
                startTyping(Constants.DIALOGUE8);
                textIndex++;
                typingOut = true;
                break;
        }
    }

    public void draw(Graphics g){
        if(Player.gameState == GameState.DIALOGUE){
            drawDialogue(g);
        }
    }

    public void drawDialogue(Graphics g) {

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
