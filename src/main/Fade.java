package main;


import javax.swing.*;
import java.awt.*;


public class Fade extends JPanel {

    float alphaValue;
    Fade(float alphaValue){
        this.alphaValue = alphaValue;
    }



    public void transparent(float f){
        alphaValue -= f;

        if(alphaValue <0){
            alphaValue = 0;
        }

    }

    public void reset(float f){
        alphaValue = f;
    }



    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        g2.dispose();
    }
}
