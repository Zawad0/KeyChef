package main;

import entity.Appliance;
import entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{

    Thread gameThread;
    BufferedImage background;
    BufferedImage backgroundMenu;
    BufferedImage playButton;
    Rectangle playButtonBounds;
    SpriteSheet choppingSpriteSheet;
    SpriteSheet choppingTomato;
    SpriteSheet choppingOnion;
    SpriteSheet choppingCucumber;
    SpriteSheet playerSpriteBack;
    Player player = new Player(this);
    Appliance chopping;
    Appliance tomato;
    Appliance onion;
    Appliance cucumber;
    private int currentFrame = 0;
    private double animationTimer = 0;
    boolean isMousePressedOnPlayButton = false;

    GamePanel(){
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        try {
            background = ImageIO.read(getClass().getResource("/kitchen.png"));
            backgroundMenu = ImageIO.read(getClass().getResource("/Sprite-menu1.png"));
            playButton = ImageIO.read(getClass().getResource("/playbutton.png"));
            playButtonBounds = new Rectangle(357, 315, 154, 48);

            choppingSpriteSheet = new SpriteSheet("/chopping.png", 64);
            chopping = new Appliance(this, choppingSpriteSheet);

            choppingTomato = new SpriteSheet("/tomato_slice.png", 32);
            choppingTomato.frameX = Constants.TOMATOX;
            choppingTomato.frameY = Constants.TOMATOY;
            tomato = new Appliance(this, choppingTomato);

            choppingOnion = new SpriteSheet("/onion_slice.png", 32);
            choppingOnion.frameX = Constants.ONIONX;
            choppingOnion.frameY = Constants.ONIONY;
            onion = new Appliance(this, choppingOnion);

            choppingCucumber = new SpriteSheet("/cucumber_slice.png",32);
            choppingCucumber.frameX = Constants.CUCUMBERX;
            choppingCucumber.frameY = Constants.CUCUMBERY;
            cucumber = new Appliance(this, choppingCucumber);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if(player.gameState == GameState.MENU && playButtonBounds.contains(e.getPoint())){
                        isMousePressedOnPlayButton = true;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if(player.gameState == GameState.MENU && playButtonBounds.contains(e.getPoint())){
                        player.gameState = GameState.CHOPPING;
                    }
                    isMousePressedOnPlayButton = false;
                }
            });


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

        animationUpdater(dt);

    }

    void animationUpdater(double dt){
        switch (player.gameState) {
            case CHOPPING:
                animationTimer += dt;

                choppingSpriteSheet.frameX = Constants.CHOPX;
                choppingSpriteSheet.frameY = Constants.CHOPY;


                player.playerSpriteBack.frameX = Constants.PLAYERX;
                player.playerSpriteBack.frameY = Constants.PLAYERY;


                if(currentFrame >= 12){
                    tomato.isShown = true;
                }
                if(currentFrame >=24){
                    onion.isShown = true;
                }
                if(currentFrame<=12){
                    tomato.isShown = false;
                    onion.isShown = false;
                    cucumber.isShown = false;
                }


                if (animationTimer >= Constants.FRAME_DURATION) {
                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= choppingSpriteSheet.getFrameCount()) {
                        currentFrame = 0; // This loops the animation
                    }
                }

                break;

            case MENU:
                animationTimer += dt;

                player.playerSpriteFrontMenu.frameX = Constants.PLAYER_MENUX;
                player.playerSpriteFrontMenu.frameY = Constants.PLAYER_MENUY;

                if (animationTimer >= Constants.FRAME_DURATION) {
                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= choppingSpriteSheet.getFrameCount()) {
                        currentFrame = 0;
                    }
                }

        }


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);


        switch (player.gameState) {
            case CHOPPING:
                g.drawImage(background,-800,-100, Constants.SCREEN_WIDTH*2,Constants.SCREEN_HEIGHT*2, null);
                chopping.draw(g, currentFrame);
                player.drawBack(g, currentFrame);
                if(tomato.isShown){
                    tomato.draw(g, currentFrame);
                }
                if(onion.isShown){
                    onion.draw(g, currentFrame);
                }
                if(cucumber.isShown){
                    cucumber.draw(g, currentFrame);
                }
                break;

            case MENU:
                g.drawImage(backgroundMenu,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                g.drawImage(playButton, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width*2, playButtonBounds.height*2, null);
                player.drawFront(g, currentFrame);
                break;
            default:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                break;
        }

        }
    }

