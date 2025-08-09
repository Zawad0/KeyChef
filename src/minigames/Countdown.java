package minigames;

import main.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Countdown extends JPanel {
    BufferedImage count3, count2, count1;
    public double second=0;
    int width, height, x, y;

    public Countdown(){
        try {
            count1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count1.png")));
            count2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count2.png")));
            count3 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count3.png")));
            height = count1.getHeight()* Constants.SCALE;
            width = count1.getWidth()* Constants.SCALE;
            x = (Constants.SCREEN_WIDTH/2) - (width/2);
            y = (Constants.SCREEN_HEIGHT/2) - (height/2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g, double dt){
        second+=dt;

        if(second >=4) return;
        else if(second>=2){
            zoomin(g, count1, dt);
        } else if (second>=1) {
            zoomin(g, count2, dt);
        } else {
            zoomin(g, count3, dt);
        }
    }

    void zoomin(Graphics g, BufferedImage bufferedImage, double dt){

        g.drawImage(bufferedImage, x,y,width, height, null);
    }

    public void reset(){
        second = 0;
    }
}
