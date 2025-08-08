package main;


import javax.swing.*;
import java.awt.*;


public class Fade extends JPanel {

    float alphaValue;
    Fade(float alphaValue){
        this.alphaValue = alphaValue;
    }



    public void transparent(float f){
        alphaValue = Math.max(0,alphaValue - f);


    }

    public void reset(){
        alphaValue = 1f;
    }



    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        g2.dispose();
    }
}
