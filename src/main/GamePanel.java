package main;

import entity.Materials;
import entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{

    Thread gameThread;
    BufferedImage background;
    BufferedImage backgroundMenu;
    BufferedImage playButton;
    BufferedImage playButtonDark;
    Rectangle playButtonBounds;
    SpriteSheet choppingSpriteSheet;
    SpriteSheet choppingTomato;
    SpriteSheet choppingOnion;
    SpriteSheet choppingCucumber;
    SpriteSheet playerSpriteFrontTalk;
    Player player = new Player(this);
    Materials chopping;
    Materials tomato;
    Materials onion;
    Materials cucumber;
    Fade fade = new Fade();
    private int currentFrame = 0;
    private double animationTimer = 0;
    boolean isMousePressedOnPlayButton = false;

    GamePanel(){
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        try {
            background = ImageIO.read(getClass().getResource("/kitchen.png"));
            backgroundMenu = ImageIO.read(getClass().getResource("/Sprite-menu.png"));
            playButton = ImageIO.read(getClass().getResource("/playbutton.png"));
            playButtonDark = ImageIO.read(getClass().getResource("/playbuttondark.png"));
            playButtonBounds = new Rectangle(357, 315, 154*2, 48*2);

            choppingSpriteSheet = new SpriteSheet("/chopping.png", 64);
            chopping = new Materials(this, choppingSpriteSheet);

            choppingTomato = new SpriteSheet("/tomato_slice.png", 32);
            choppingTomato.frameX = Constants.TOMATOX;
            choppingTomato.frameY = Constants.TOMATOY;
            tomato = new Materials(this, choppingTomato);

            choppingOnion = new SpriteSheet("/onion_slice.png", 32);
            choppingOnion.frameX = Constants.ONIONX;
            choppingOnion.frameY = Constants.ONIONY;
            onion = new Materials(this, choppingOnion);

            choppingCucumber = new SpriteSheet("/cucumber_slice.png",32);
            choppingCucumber.frameX = Constants.CUCUMBERX;
            choppingCucumber.frameY = Constants.CUCUMBERY;
            cucumber = new Materials(this, choppingCucumber);

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
                        player.gameState = GameState.IDLE;
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
                Thread.sleep(Constants.FPS_CAP);
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
                break;

            case IDLE:
                fade.transparent();
                animationTimer += dt;
                player.playerSpriteFrontTalk.frameX = Constants.PLAYER_IDLEX;
                player.playerSpriteFrontTalk.frameY = Constants.PLAYER_IDLEY;

                if (animationTimer >= Constants.FRAME_DURATION) {
                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= choppingSpriteSheet.getFrameCount()) {
                        currentFrame = 0;
                    }
                }
                break;


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

                if(isMousePressedOnPlayButton){
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(playButtonDark, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);

                }
                else{
                    g.drawImage(playButton, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);
                }

                player.drawFront(g, currentFrame);
                break;

            case IDLE:

                Graphics2D g2 = (Graphics2D) g;
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                Color overlay = new Color(0, 0,0, 150);
                g2.setColor(overlay);
                g2.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

                player.drawFrontTalk(g,currentFrame);

                fade.draw(g);
                break;
            default:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                break;
        }

        }
    }

