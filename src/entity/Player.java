package entity;

import main.Constants;
import main.GamePanel;
import main.GameState;
import main.SpriteSheet;

import java.awt.*;

import java.awt.image.BufferedImage;

public class Player {
    GamePanel gp;
    public SpriteSheet playerSpriteBack;
    public SpriteSheet playerSpriteFrontMenu;
    public SpriteSheet playerSpriteFrontTalk;
    double animationTimer = 0;
    int currentFrame = 0;



    //public GameState gameState = GameState.CHOPPING;
    public static GameState gameState =  GameState.MENU;

    public Player(){
        {
            try {
                playerSpriteBack = new SpriteSheet("/character/Sprite-charaback3.png", 32);
                playerSpriteFrontMenu = new SpriteSheet("/character/Sprite-charafront2.png", 32);
                playerSpriteFrontTalk = new SpriteSheet("/character/Sprite-charafront_talk.png", 32);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void reset(){
        animationTimer =0;
        currentFrame = 0;
    }
    public void drawBack(Graphics g, double dt){
        animationTimer+=dt;


        if (animationTimer >= Constants.FRAME_DURATION) {
            animationTimer = 0;
            currentFrame++;
            if (currentFrame >= playerSpriteBack.getFrameCount()) {
                currentFrame = 0;
            }
        }

        playerSpriteBack.frameX = Constants.PLAYERX;
        playerSpriteBack.frameY = Constants.PLAYERY;
        BufferedImage playerFrame = playerSpriteBack.getFrame(currentFrame);


        int playerX = playerSpriteBack.frameX* Constants.SCALE;
        int playerY = playerSpriteBack.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*4;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*4;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }


    public void drawBackIdle(Graphics g, double dt){
        animationTimer+=dt;


        if (animationTimer >= Constants.FRAME_DURATION && animationTimer>=Materials.delay) {
            animationTimer = 0;
            currentFrame++;
            if (currentFrame >= playerSpriteBack.getFrameCount()) {
                currentFrame = 0;
            }
        }

        if(Constants.stationSwitch == 1){
            playerSpriteBack.frameX = 305;
            playerSpriteBack.frameY = 110;
        } else if (Constants.stationSwitch == 2) {
            playerSpriteBack.frameX = 250;
            playerSpriteBack.frameY = 115;
        }

        BufferedImage playerFrame = playerSpriteBack.getFrame(currentFrame);


        int playerX = playerSpriteBack.frameX* Constants.SCALE;
        int playerY = playerSpriteBack.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*2;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*2;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }

    public void drawFront(Graphics g, double dt){
        Graphics2D g2 = (Graphics2D) g.create();
        animationTimer+=dt;
        playerSpriteFrontMenu.frameX = Constants.PLAYER_MENUX;
        playerSpriteFrontMenu.frameY = Constants.PLAYER_MENUY;
        if (animationTimer >= Constants.FRAME_DURATION) {
            animationTimer = 0;
            currentFrame++;
            if (currentFrame >= playerSpriteFrontMenu.getFrameCount()) {
                currentFrame = 0;
            }
        }

        BufferedImage playerFrame = playerSpriteFrontMenu.getFrame(currentFrame);

        int playerX = playerSpriteFrontMenu.frameX* Constants.SCALE;
        int playerY = playerSpriteFrontMenu.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*2;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*2;
        g2.rotate(Math.toRadians(45),Constants.PLAYER_MENUX+playerWidth/4,Constants.PLAYER_MENUX+playerHeight/4);
        g2.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
        g2.dispose();
    }

    public void drawFrontTalk(Graphics g, double dt){
        animationTimer += dt;
        playerSpriteFrontTalk.frameX = Constants.PLAYER_IDLEX;
        playerSpriteFrontTalk.frameY = Constants.PLAYER_IDLEY;


        if (animationTimer >= Constants.FRAME_DURATION) {
            animationTimer = 0;
            currentFrame++;
            if (currentFrame >= playerSpriteFrontTalk.getFrameCount()) {
                currentFrame = 0;
            }
        }


        BufferedImage playerFrame = playerSpriteFrontTalk.getFrame(currentFrame);

        int playerX = playerSpriteFrontTalk.frameX* Constants.SCALE;
        int playerY = playerSpriteFrontTalk.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*5;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*5;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }

}
