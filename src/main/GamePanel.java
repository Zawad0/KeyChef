package main;

import entity.Materials;
import entity.Player;
import minigames.TypeGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    Player player = new Player();

    Materials materials;

    UI ui = new UI();
    Fade fade = new Fade(1f);

    //KeyHandler keyHandler = new KeyHandler();
    double deltaTime = 0;
    private int currentFrame = 0;
    private double animationTimer = 0;

    boolean isMousePressedOnPlayButton = false;
    double delayTimer = 0;
    boolean wasMousePressed = false;
    boolean wasStartPressed = false;

    TypeGame typeGame = new TypeGame();




    GamePanel(){
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        //this.addKeyListener(keyHandler);
        //this.setFocusable(true);
        try {
            typeGame.words.printWords();

            materials = new Materials(this);
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
            setFocusable(true);
            requestFocusInWindow();

            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
//                    if(Player.gameState == GameState.CHOPPING && e.getKeyCode() == KeyEvent.VK_SPACE){
//                        resetAnimation();
//                        fade.reset(1f);
//                        Player.gameState = GameState.FRYING;
//
//                    }

                    char pressed = Character.toUpperCase(e.getKeyChar());
                    String currentWord = typeGame.currentWordsList.get(typeGame.currentWordIndex);
                    char currentChar = currentWord.charAt(typeGame.currentCharIndex);
                    if(pressed == currentChar){

                        List<BufferedImage> spriteList = typeGame.currentWords.get(currentWord);
                        spriteList.set(typeGame.currentCharIndex, typeGame.keys.getPr(String.valueOf(currentChar)));

                        typeGame.currentCharIndex++;

                        if(typeGame.currentCharIndex >= currentWord.length()){
                            typeGame.currentCharIndex = 0;
                            typeGame.currentWordIndex++;

                            if(typeGame.currentWordIndex>=typeGame.currentWordsList.size()){
                                materials.reset();
                                player.reset();
                                fade.reset(2f);
                                Player.gameState = GameState.FRYING;
                            }
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
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

                    }

                    if(Player.gameState == GameState.DIALOGUE){
                        if (ui.typingOut) {
                            ui.currentDialogue = ui.fullText;
                            ui.typingOut = false;
                        }
                        if(ui.textIndex > 8){
                            player.reset();
                            materials.reset();
                            Player.gameState = GameState.IDLE;
                        }


                    }

                    if(Player.gameState == GameState.IDLE && startButtonBounds.contains(e.getPoint())){
                        wasStartPressed = true;
                        fade.reset(1f);

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
            this.deltaTime = deltaTime;
            update(deltaTime);
            repaint();

            try{
                Thread.sleep(Constants.FPS_CAP);
            } catch (Exception e) {

            }




        }
    }

    public void update(double dt){
//        System.out.println(String.format("%.2f", 1/dt) + "fps"); //Shows FPS in logs

        stateUpdater(dt);
        ui.type(dt);
        if(wasStartPressed){
            fade.reset(1f);
            materials.reset();
            player.reset();
            Player.gameState = GameState.CHOPPING;
            wasStartPressed = false;
        }

        if(typeGame.timerBar.progressVal <= 0){
            //game over
        }

    }



    void stateUpdater(double dt){
        switch (Player.gameState) {
            case CHOPPING:

                typeGame.timerBar.update(dt);
                if(!typeGame.areWordsShown){
                    typeGame.currentWords = typeGame.getMap(5);
                    typeGame.currentWordsList = new ArrayList<>(typeGame.currentWords.keySet());
                    typeGame.areWordsShown = true;
                }


                fade.transparent(0.024f);


                break;

            case FRYING:

                fade.transparent(0.05f);


                break;

            case MENU:


                if(wasMousePressed){
                    delayTimer+=dt;

                    if(delayTimer >= 1.2){
                        player.reset();
                        materials.reset();
                        Player.gameState = GameState.DIALOGUE;
                        wasMousePressed = false;
                        delayTimer = 0;
                    }
                }
                break;

            case DIALOGUE:
                fade.transparent(0.009f);

                if(ui.typingOut) ui.type(dt);


                if(!ui.typingOut && ui.textIndex <= 8){
                    ui.say();

                }

                break;



        }


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);


        switch (Player.gameState) {
            case CHOPPING:
                g.drawImage(background,-800,-100, Constants.SCREEN_WIDTH*Constants.SCALE,Constants.SCREEN_HEIGHT*Constants.SCALE, null);
                materials.drawChopping(g, deltaTime);
                player.drawBack(g, deltaTime);
                materials.drawChoppingVegetable(g);
                typeGame.draw(g, currentFrame);
                fade.draw(g);
                break;

            case FRYING:
                Graphics2D gF = (Graphics2D) g;
                g.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
                Color overlayF = new Color(0, 0,0, 190);
                gF.setColor(overlayF);
                gF.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                materials.drawFrying(g, deltaTime, fade.alphaValue);
                fade.draw(g);
                break;

            case MENU:
                g.drawImage(backgroundMenu,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                player.drawFront(g, deltaTime);
                g.drawImage(menuBoard,0,0,menuBoard.getWidth()*2, menuBoard.getHeight()*2, null);
                if(isMousePressedOnPlayButton){
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawImage(playButtonDark, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null); //key drawing debug

                }
                else{
                    g.drawImage(playButton, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);
                }


                break;

            case DIALOGUE:

                Graphics2D gD = (Graphics2D) g;
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                g.setColor(Color.BLACK);
                g.fillRect(0,0,Constants.SCREEN_WIDTH, 80);
                g.fillRect(0, Constants.SCREEN_HEIGHT-80,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                Color overlayD = new Color(0, 0,0, 150);
                gD.setColor(overlayD);
                gD.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

                player.drawFrontTalk(g,deltaTime);
                ui.draw(g);


                fade.draw(g);
                ui.typeOut(g);

                break;

            case IDLE:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                g.drawImage(startButton, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, null);
                g.setColor(Color.BLACK);
                g.fillRect(0,0,Constants.SCREEN_WIDTH, 80);
                g.fillRect(0, Constants.SCREEN_HEIGHT-80,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                materials.drawChoppingIdle(g, deltaTime);
                materials.drawChoppingVegetableIdle(g);
                materials.drawFryingIdle(g, deltaTime);
                player.drawBackIdle(g, deltaTime);
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

