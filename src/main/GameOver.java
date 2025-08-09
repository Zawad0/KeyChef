package main;
import javax.swing.*;
import java.awt.*;

public class GameOver {
    UI ui = new UI();
    boolean showEnd = true;
    int endY = 400;
    double duration = 2;


    void served(Graphics g, double dt){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 50f));
        g.setColor(Color.WHITE);
        g.drawString("ORDERS UP!",400,endY);

        if(showEnd){
            moveEnd(dt);
        }
        else{
            g.drawString("SCORE: "+GamePanel.score,400,400);
            if(duration > 0) duration = Math.max(0, (duration-dt));
        }
    }

    void reset(){
        showEnd = true;
        endY = 400;
        duration = 2;
    }

    void moveEnd(double dt){
        if(duration > 0) duration = Math.max(0, (duration-dt));

        else{
            endY = Math.max(-50,(int)(endY-(350*dt)));
            if(endY <=-20) {
                showEnd = false;
                duration = 2;
            }
        }

    }

}
