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
    BufferedImage menuBoard;
    BufferedImage playButton;
    BufferedImage playButtonDark;
    BufferedImage startButton;
    BufferedImage startButtonDark;

    Rectangle playButtonBounds;
    Rectangle startButtonBounds;
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

    UI ui = new UI();
    Fade fade = new Fade();

    private int currentFrame = 0;
    private double animationTimer = 0;

    boolean isMousePressedOnPlayButton = false;
    double delayTimer = 0;
    boolean wasMousePressed = false;
    boolean wasStartPressed = false;

    int textIndex = 1;
    boolean typing = false;



    GamePanel(){
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        try {
            background = ImageIO.read(getClass().getResource("/kitchen.png"));
            backgroundMenu = ImageIO.read(getClass().getResource("/Sprite-menuBoardless.png"));
            menuBoard = ImageIO.read(getClass().getResource("/Sprite-board.png"));
            System.out.println("Width "+menuBoard.getWidth()+" Height "+menuBoard.getHeight());
            playButton = ImageIO.read(getClass().getResource("/playbutton.png"));
            playButtonDark = ImageIO.read(getClass().getResource("/playbuttondark.png"));
            startButton = ImageIO.read(getClass().getResource("/startbutton.png"));
            startButtonDark = ImageIO.read(getClass().getResource("/startbuttondark.png"));
            playButtonBounds = new Rectangle(357, 315, 154*2, 48*2);
            startButtonBounds = new Rectangle(357, 550, 154*2, 48*2);



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
                    if( (Player.gameState == GameState.MENU && playButtonBounds.contains( e.getPoint() )) ||
                    (Player.gameState == GameState.IDLE && startButtonBounds.contains( e.getPoint() ))){
                        isMousePressedOnPlayButton = true;

                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if( Player.gameState == GameState.MENU && playButtonBounds.contains( e.getPoint())){
                        wasMousePressed = true;
                        delayTimer = 0;
                        //player.gameState = GameState.IDLE;
                    }

                    if(Player.gameState == GameState.DIALOGUE){
                        if (typing) {
                            ui.currentDialogue = ui.fullText;
                            typing = false;
                        }
                        if(textIndex >= 8){
                            Player.gameState = GameState.IDLE;
                        }

                        //player.gameState = GameState.IDLE;
                    }

                    if(Player.gameState == GameState.IDLE && startButtonBounds.contains(e.getPoint())){
                        wasStartPressed = true;

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
        //System.out.println(1/dt + "fps");

        animationUpdater(dt);
        ui.type(dt);

        if(wasStartPressed){
            fade.alphaValue = 1f;
            Player.gameState = GameState.CHOPPING;
            wasStartPressed = false;
        }

    }



    void animationUpdater(double dt){
        switch (Player.gameState) {
            case CHOPPING:
                System.out.println("CHOPPING");
                animationTimer += dt;
                fade.transparent(0.024f);
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
                if(wasMousePressed){
                    delayTimer+=dt;

                    if(delayTimer >= 1.2){
                        Player.gameState = GameState.DIALOGUE;
                        wasMousePressed = false;
                        delayTimer = 0;
                    }
                }
                break;

            case DIALOGUE:
                fade.transparent(0.009f);
                animationTimer += dt;
                player.playerSpriteFrontTalk.frameX = Constants.PLAYER_IDLEX;
                player.playerSpriteFrontTalk.frameY = Constants.PLAYER_IDLEY;


                if(typing) ui.type(dt);


                if(!typing && textIndex <= 8){
                    switch (textIndex){
                        case 1:
                            ui.startTyping(Constants.DIALOGUE1);
                            textIndex++;
                            typing = true;
                            break;
                        case 2:
                            ui.startTyping(Constants.DIALOGUE2);
                            textIndex++;
                            typing = true;
                            break;
                        case 3:
                            ui.startTyping(Constants.DIALOGUE3);
                            textIndex++;
                            typing = true;
                            break;
                        case 4:
                            ui.startTyping(Constants.DIALOGUE4);
                            textIndex++;
                            typing = true;
                            break;
                        case 5:
                            ui.startTyping(Constants.DIALOGUE5);
                            textIndex++;
                            typing = true;
                            break;
                        case 6:
                            ui.startTyping(Constants.DIALOGUE6);
                            textIndex++;
                            typing = true;
                            break;
                        case 7:
                            ui.startTyping(Constants.DIALOGUE7);
                            textIndex++;
                            typing = true;
                            break;
                        case 8:
                            ui.startTyping(Constants.DIALOGUE8);
                            textIndex++;
                            typing = true;
                            break;
                }

                }
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


        switch (Player.gameState) {
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
                fade.draw(g);
                break;


            case MENU:
                g.drawImage(backgroundMenu,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                player.drawFront(g, currentFrame);
                g.drawImage(menuBoard,0,0,menuBoard.getWidth()*2, menuBoard.getHeight()*2, null);
                if(isMousePressedOnPlayButton){
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(playButtonDark, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);

                }
                else{
                    g.drawImage(playButton, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);
                }


                break;

            case DIALOGUE:

                Graphics2D g2 = (Graphics2D) g;
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                Color overlay = new Color(0, 0,0, 150);
                g2.setColor(overlay);
                g2.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

                player.drawFrontTalk(g,currentFrame);
                ui.draw(g);

                //System.out.println("Current text: " + ui.currentDialogue);

                fade.draw(g);
                ui.typeOut(g);

                break;

            case IDLE:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                g.drawImage(startButton, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, null);
                if(isMousePressedOnPlayButton){
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.drawImage(startButtonDark, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, null);


                }
                break;
            default:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                break;
        }

        }
    }

