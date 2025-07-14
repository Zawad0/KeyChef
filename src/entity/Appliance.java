package entity;

import main.Constants;
import main.GamePanel;
import main.GameState;
import main.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Appliance {
    GamePanel gp;
    SpriteSheet spriteSheet;





    public Appliance(GamePanel gp, SpriteSheet spriteSheet){
        this.gp = gp;
        this.spriteSheet = spriteSheet;

    }

    public void draw(Graphics g, int currentFrame){
        BufferedImage applianceFrame = spriteSheet.getFrame(currentFrame);
        spriteSheet.frameX = 228;
        spriteSheet.frameY = 153;
        int applianceX = spriteSheet.frameX*Constants.SCALE;
        int applianceY = spriteSheet.frameY*Constants.SCALE;
        int applianceWidth = applianceFrame.getWidth()*Constants.SCALE*2;
        int applianceHeight = applianceFrame.getHeight()*Constants.SCALE*2;
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }

}
