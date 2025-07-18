package entity;

import main.Constants;
import main.GamePanel;
import main.GameState;
import main.SpriteSheet;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player {
    GamePanel gp;
    public SpriteSheet playerSpriteBack;
    public SpriteSheet playerSpriteFrontMenu;
    public SpriteSheet playerSpriteFrontTalk;



    //public GameState gameState = GameState.CHOPPING;
    public static GameState gameState =  GameState.MENU;

    public Player(GamePanel gp){
        this.gp = gp;
        {
            try {
                playerSpriteBack = new SpriteSheet("/Sprite-charaback3.png", 32);
                playerSpriteFrontMenu = new SpriteSheet("/Sprite-charafront.png", 32);
                playerSpriteFrontTalk = new SpriteSheet("/Sprite-charafront_talk.png", 32);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void drawBack(Graphics g, int currentFrame){
        BufferedImage playerFrame = playerSpriteBack.getFrame(currentFrame);

        int playerX = playerSpriteBack.frameX* Constants.SCALE;
        int playerY = playerSpriteBack.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*4;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*4;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }

    public void drawFront(Graphics g, int currentFrame){
        Graphics2D g2 = (Graphics2D) g.create();
        BufferedImage playerFrame = playerSpriteFrontMenu.getFrame(currentFrame);

        int playerX = playerSpriteFrontMenu.frameX* Constants.SCALE;
        int playerY = playerSpriteFrontMenu.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*2;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*2;
        g2.rotate(Math.toRadians(45),Constants.PLAYER_MENUX+playerWidth/4,Constants.PLAYER_MENUX+playerHeight/4);
        g2.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
        g2.dispose();
    }

    public void drawFrontTalk(Graphics g, int currentFrame){
        BufferedImage playerFrame = playerSpriteFrontTalk.getFrame(currentFrame);

        int playerX = playerSpriteFrontTalk.frameX* Constants.SCALE;
        int playerY = playerSpriteFrontTalk.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*5;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*5;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }

}
