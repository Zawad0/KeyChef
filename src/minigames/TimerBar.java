package minigames;

import main.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class TimerBar extends JProgressBar {

    BufferedImage barFill;
    public double progressVal = 100;
    double durationSec;

    public TimerBar(String filepath, double durationSec){
        super(0,100);
        setOpaque(false);
        this.durationSec = durationSec;
        try {
            barFill = ImageIO.read(Objects.requireNonNull(getClass().getResource(filepath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(double dt){
        progressVal -= (dt/durationSec)*100;

        if(progressVal<0) progressVal = 0;
        if(progressVal>100) progressVal = 100;

        setValue((int) progressVal);
    }



    public void draw(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        int fillWidth = (int) ((getPercentComplete()) * barFill.getWidth()*Constants.SCALE);
        int scaledHeight = barFill.getHeight() * Constants.SCALE;

        Shape oldClip = g2.getClip();
        g2.clipRect(Constants.TIMERX,Constants.TIMERY,fillWidth, scaledHeight);

        g2.drawImage(barFill,Constants.TIMERX,Constants.TIMERY, barFill.getWidth() * Constants.SCALE, scaledHeight, null);
        g2.setClip(oldClip);
        
        
    }

}

