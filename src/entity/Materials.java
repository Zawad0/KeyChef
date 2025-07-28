package entity;
import main.Constants;
import main.GamePanel;
import main.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Materials {
    GamePanel gp;
    public boolean isShown;

    public SpriteSheet chopping;
    public SpriteSheet frying;
    public SpriteSheet assembling;
    public BufferedImage choppingTomato;
    public BufferedImage choppingOnion;
    public BufferedImage choppingCucumber;
    public Boolean tomatoShown, onionShown, cucumberShown;

    public Materials(GamePanel gp){
        this.gp = gp;
        try {
            chopping = new SpriteSheet("/chopping.png", 64);
            frying = new SpriteSheet("/frying.png", 330);
            frying.frameX = 100;
            frying.frameY = 100;

            choppingTomato = ImageIO.read(getClass().getResource("/tomato_slice.png"));

            choppingOnion = ImageIO.read(getClass().getResource("/onion_slice.png"));

            choppingCucumber = ImageIO.read(getClass().getResource("/cucumber_slice.png"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    public void drawChopping(Graphics g, int currentFrame){
        BufferedImage applianceFrame = chopping.getFrame(currentFrame);
        int applianceX = chopping.frameX*Constants.SCALE;
        int applianceY = chopping.frameY*Constants.SCALE;
        int applianceWidth = applianceFrame.getWidth()*Constants.SCALE*2;
        int applianceHeight = applianceFrame.getHeight()*Constants.SCALE*2;
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }

    public void drawFrying(Graphics g, int currentFrame){
        BufferedImage applianceFrame = frying.getFrame(currentFrame);
        int applianceX = frying.frameX*Constants.SCALE;
        int applianceY = frying.frameY*Constants.SCALE;
        int applianceWidth = applianceFrame.getWidth()*Constants.SCALE;
        int applianceHeight = applianceFrame.getHeight()*Constants.SCALE;
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }

    public void drawAssembling(Graphics g, int currentFrame){
        BufferedImage applianceFrame = assembling.getFrame(currentFrame);
        int applianceX = assembling.frameX*Constants.SCALE;
        int applianceY = assembling.frameY*Constants.SCALE;
        int applianceWidth = applianceFrame.getWidth()*Constants.SCALE*2;
        int applianceHeight = applianceFrame.getHeight()*Constants.SCALE*2;
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }

}
