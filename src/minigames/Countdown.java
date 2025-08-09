package minigames;

import main.Constants;
import main.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Countdown extends JPanel {
    BufferedImage count3, count2, count1;
    public double second=0;
    public boolean isComplete = false;
    int width, height, x, y;
    UI ui = new UI();

    public Countdown(){
        //            count1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count1.png")));
//            count2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count2.png")));
//            count3 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/count3.png")));
//            height = count1.getHeight()* Constants.SCALE;
//            width = count1.getWidth()* Constants.SCALE;
        x = (Constants.SCREEN_WIDTH/2);
        y = (Constants.SCREEN_HEIGHT/2);
    }

    public void draw(Graphics g, double dt){
        if(isComplete) return;
        second+=dt;

        if(second >=3.5){
            isComplete = true;
            return;
        }
        else if(second>=2.5){
            zoomin(g, "1", dt);
        } else if (second>=1.5) {
            zoomin(g, "2", dt);
        } else if(second >= 0.5){
            zoomin(g, "3", dt);
        }
    }

    void zoomin(Graphics g, String num, double dt){
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 120f));
        g2.setColor(Color.BLACK);
        g2.drawString(num, ((Constants.SCREEN_WIDTH-50)/2), ((Constants.SCREEN_HEIGHT-30)/2));
        g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 80f));
        g2.setColor(Color.WHITE);
        g2.drawString(num, ((Constants.SCREEN_WIDTH-50)/2), ((Constants.SCREEN_HEIGHT-30)/2));
    }

    public void reset(){
        second = 0;
        isComplete = false;
    }
}
