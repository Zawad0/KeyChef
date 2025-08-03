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
    Player player = new Player(this);

    Materials materials;

    UI ui = new UI();
    Fade fade = new Fade(1f);

    //KeyHandler keyHandler = new KeyHandler();

    private int currentFrame = 0;
    private double animationTimer = 0;

    boolean isMousePressedOnPlayButton = false;
    double delayTimer = 0;
    boolean wasMousePressed = false;
    boolean wasStartPressed = false;

    int textIndex = 1;
    boolean typing = false;

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
                                resetAnimation();
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
                        if (typing) {
                            ui.currentDialogue = ui.fullText;
                            typing = false;
                        }
                        if(textIndex > 8){
                            resetAnimation();
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

        animationUpdater(dt);
        ui.type(dt);

        if(wasStartPressed){
            fade.alphaValue = 1f;
            Player.gameState = GameState.CHOPPING;
            wasStartPressed = false;
        }

        if(typeGame.timerBar.progressVal <= 0){
            //game over
        }

    }


    void resetAnimation(){
        animationTimer = 0;
        currentFrame = 0;
    }
    void animationUpdater(double dt){
        switch (Player.gameState) {
            case CHOPPING:

                typeGame.timerBar.update(dt);
                if(!typeGame.areWordsShown){
                    typeGame.currentWords = typeGame.getMap(5);
                    typeGame.currentWordsList = new ArrayList<>(typeGame.currentWords.keySet());
                    typeGame.areWordsShown = true;
                }

                //System.out.println("CHOPPING");
                animationTimer += dt;
                fade.transparent(0.024f);
                materials.chopping.frameX = Constants.CHOPX;
                materials.chopping.frameY = Constants.CHOPY;


                player.playerSpriteBack.frameX = Constants.PLAYERX;
                player.playerSpriteBack.frameY = Constants.PLAYERY;


                if(currentFrame >= 12){
                    materials.tomatoShown = true;
                }
                if(currentFrame >=24){
                    materials.onionShown = true;
                }
                if(currentFrame<=12){
                    materials.tomatoShown = false;
                    materials.onionShown = false;
                    materials.cucumberShown = false;
                }


                if (animationTimer >= Constants.FRAME_DURATION) {
                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= materials.chopping.getFrameCount()) {
                        currentFrame = 0;
                    }
                }

                break;

            case FRYING:
                animationTimer+=dt;
                fade.transparent(0.05f);
                materials.frying.frameX = Constants.FRYX;
                materials.frying.frameY = Constants.FRYY;

                if(fade.alphaValue <= 0){
                    if (animationTimer >= Constants.FRY_FRAME_DURATION) {
                        animationTimer = 0;
                        currentFrame++;
                        if (currentFrame >= materials.frying.getFrameCount()) {
                            currentFrame = 15;
                        }
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
                    if (currentFrame >= player.playerSpriteFrontMenu.getFrameCount()) {
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
                    if (currentFrame >= player.playerSpriteFrontTalk.getFrameCount()) {
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
                g.drawImage(background,-800,-100, Constants.SCREEN_WIDTH*Constants.SCALE,Constants.SCREEN_HEIGHT*Constants.SCALE, null);
                materials.drawChopping(g, currentFrame);
                player.drawBack(g, currentFrame);
                if(materials.tomatoShown){
                    g.drawImage(materials.choppingTomato, Constants.TOMATOX, Constants.TOMATOY,
                            materials.choppingTomato.getWidth()*3, materials.choppingTomato.getHeight()*3, null);
                }
                if(materials.onionShown){
                    g.drawImage(materials.choppingOnion, Constants.ONIONX, Constants.ONIONY,
                            materials.choppingOnion.getWidth()*3, materials.choppingOnion.getHeight()*3, null);
                }
                if(materials.cucumberShown){
                    g.drawImage(materials.choppingCucumber, Constants.CUCUMBERX, Constants.CUCUMBERY,
                            materials.choppingCucumber.getWidth()*3, materials.choppingCucumber.getHeight()*3, null);
                }
                typeGame.draw(g, currentFrame);
                fade.draw(g);
                break;

            case FRYING:
                Graphics2D gF = (Graphics2D) g;
                g.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
                Color overlayF = new Color(0, 0,0, 190);
                gF.setColor(overlayF);
                gF.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                materials.drawFrying(g, currentFrame);
                fade.draw(g);
                break;

            case MENU:
                g.drawImage(backgroundMenu,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                player.drawFront(g, currentFrame);
                g.drawImage(menuBoard,0,0,menuBoard.getWidth()*2, menuBoard.getHeight()*2, null);
                if(isMousePressedOnPlayButton){
                    Graphics2D g2 = (Graphics2D) g.create();
                    //g2.drawImage(playButtonDark, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null); //key drawing debug

                }
                else{
                    g.drawImage(playButton, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height, null);
                }


                break;

            case DIALOGUE:

                Graphics2D gD = (Graphics2D) g;
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);
                Color overlayD = new Color(0, 0,0, 150);
                gD.setColor(overlayD);
                gD.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

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

