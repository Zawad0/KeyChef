package entity;

import main.Constants;
import main.GamePanel;
import main.GameState;
import main.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    GamePanel gp;
    SpriteSheet playerSpriteBack;



    public GameState gameState = GameState.CHOPPING;

    public Player(GamePanel gp){
        this.gp = gp;
        {
            try {
                playerSpriteBack = new SpriteSheet("/player_sprite_back.png", 32);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void draw(Graphics g, int currentFrame){
        BufferedImage playerFrame = playerSpriteBack.getFrame(currentFrame);
        playerSpriteBack.frameX = 220;
        playerSpriteBack.frameY = 185;
        int playerX = playerSpriteBack.frameX* Constants.SCALE;
        int playerY = playerSpriteBack.frameY*Constants.SCALE;
        int playerWidth = playerFrame.getWidth()*Constants.SCALE*2*2;
        int playerHeight = playerFrame.getHeight()*Constants.SCALE*2*2;
        g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
    }

}
