package main;

import entity.Appliance;
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
    Player player = new Player(this);
    Appliance chopping;
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
            chopping = new Appliance(this, choppingSpriteSheet);

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


        if (player.gameState == GameState.CHOPPING) {
            g.drawImage(background,-800,-100, Constants.SCREEN_WIDTH*2,Constants.SCREEN_HEIGHT*2, null);
            chopping.draw(g, currentFrame);
            player.draw(g, currentFrame);
        }
        else{
            g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
        }
    }
}
