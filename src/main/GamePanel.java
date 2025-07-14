package main;

import entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{

    Thread gameThread;
    BufferedImage background;
    SpriteSheet choppingSpriteSheet;
    SpriteSheet playerSpriteBack;
    Player player = new Player();
    private int currentFrame = 0;
    private double animationTimer = 0;
    private final double frameDuration = 0.125;

    GamePanel(){
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        try {
            background = ImageIO.read(getClass().getResource("/kitchen.png"));
            choppingSpriteSheet = new SpriteSheet("/chopping.png", 64);

            playerSpriteBack = new SpriteSheet("/player_sprite_back.png", 32);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }


    public class Time {
        public static double timeStarted = System.nanoTime();

        public static double getTime(){
            return (System.nanoTime() - timeStarted) * 1E-9;
        }
    }


    @Override
    public void run() {
        double lastFrameTime = Time.getTime();
        while(true){
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);
            repaint();

            try{
                Thread.sleep(32);
            } catch (Exception e) {

            }




        }
    }

    public void update(double dt){
        System.out.println(1/dt + "fps");

        if (player.gameState == GameState.CHOPPING) {
            animationTimer += dt;

            if (animationTimer >= frameDuration) {
                animationTimer = 0;
                currentFrame++;
                if (currentFrame >= choppingSpriteSheet.getFrameCount()) {
                    currentFrame = 0; // loop the animation
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);

        if (player.gameState == GameState.CHOPPING) {
            BufferedImage applianceFrame = choppingSpriteSheet.getFrame(currentFrame);
            choppingSpriteSheet.frameX = 314;
            choppingSpriteSheet.frameY = 101;
            int applianceX = choppingSpriteSheet.frameX*Constants.SCALE;
            int applianceY = choppingSpriteSheet.frameY*Constants.SCALE;
            int applianceWidth = applianceFrame.getWidth()*Constants.SCALE;
            int applianceHeight = applianceFrame.getHeight()*Constants.SCALE;
            g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);

            BufferedImage playerFrame = playerSpriteBack.getFrame(currentFrame);
            playerSpriteBack.frameX = 305;
            playerSpriteBack.frameY = 115;
            int playerX = playerSpriteBack.frameX*Constants.SCALE;
            int playerY = playerSpriteBack.frameY*Constants.SCALE;
            int playerWidth = playerFrame.getWidth()*Constants.SCALE*2;
            int playerHeight = playerFrame.getHeight()*Constants.SCALE*2;
            g.drawImage(playerFrame, playerX, playerY, playerWidth, playerHeight, null);
        }
    }
}
