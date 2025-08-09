package main;

import java.awt.*;

public class GameOver {
    UI ui = new UI();

    void served(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 50f));
        g.setColor(Color.WHITE);
        g.drawString("ORDERS UP!",400,400);
    }
}
