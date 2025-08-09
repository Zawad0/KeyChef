package main;
import javax.swing.*;
import java.awt.*;

public class GameOver {
    UI ui = new UI();
    boolean showEnd = true;
    int endY = 400;
    double duration = 1;
    String text = "";
    int x;



    void served(Graphics g, double dt){
        FontMetrics fm = g.getFontMetrics(ui.pixelFont);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 50f));
        ui.drawCenteredText(g, "ORDERS UP!", endY, Color.WHITE);

        if(showEnd){
            moveEnd(dt);
        }
        else{
            ui.drawCenteredText(g, "SCORE: "+GamePanel.score, 350, Color.WHITE);
            if(duration > 0) duration = Math.max(0, (duration-dt));
        }
    }

    void reset(){
        showEnd = true;
        endY = 400;
        duration = 1;
    }

    void moveEnd(double dt){
        if(duration > 0) duration = Math.max(0, (duration-dt));

        else{
            endY = Math.max(-50,(int)(endY-(550*dt)));
            if(endY <=-20) {
                showEnd = false;
                duration = 2;
            }
        }

    }

}
