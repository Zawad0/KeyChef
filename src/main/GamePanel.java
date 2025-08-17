package main;

import entity.Materials;
import entity.Player;
import entity.SaveGame;
import minigames.ClickGame;
import minigames.Countdown;
import minigames.TimeGame;
import minigames.TypeGame;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable{

    Thread gameThread;
    BufferedImage background;
    BufferedImage backgroundMenu;
    BufferedImage menuBoard;
    BufferedImage playButton;
    BufferedImage playButtonDark;
    BufferedImage startButton;
    BufferedImage startButtonDark;
    BufferedImage icon;
    SpriteSheet menuSmoke;
    Sound sound;

    Rectangle playButtonBounds;
    Rectangle startButtonBounds;
    Player player = new Player();

    Materials materials;
    ClickGame clickGame = new ClickGame();
    TimeGame timeGame = new TimeGame();


    UI ui = new UI();
    Countdown countdown = new Countdown();
    Fade fade = new Fade(1f);
    GameOver gameOver = new GameOver();

    //KeyHandler keyHandler = new KeyHandler();
    double deltaTime = 0;

    boolean isMousePressedOnPlayButton = false;
    double delayTimer = 0;
    boolean wasMousePressed = false;
    boolean wasStartPressed = false;
    int prevWrongPressed = -1;

    public static int burgerCount = 0;
    public static int score = 0;
    public static int currentScore = 0;
    public static int highscore = 0;
    public static int hearts = 3;
    static boolean scoreMulti = true;
    public double diffModif = 1;
    public static float volume = 0.8f;
    SaveGame saveGame = new SaveGame();
    Reader reader;

    GameState previousGameState = null;
    Clip currentBackgroundMusic = null;

    double animationTimer = 0;
    int currentFrame = 0;
    TypeGame typeGame = new TypeGame();




    GamePanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setBackground(Color.black);
        setDoubleBuffered(true);
        try {
            typeGame.words.printWords();
            materials = new Materials(this);
            menuSmoke = new SpriteSheet("/menu/menusmoke-Sheet.png",512);
            icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/icon.png")));
            background = ImageIO.read(Objects.requireNonNull(getClass().getResource("/kitchen.png")));
            backgroundMenu = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/menubg.png")));
            menuBoard = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/menuboard.png")));
            System.out.println("Width "+menuBoard.getWidth()+" Height "+menuBoard.getHeight());
            playButton = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/playbutton.png")));
            playButtonDark = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/playbuttondark.png")));
            startButton = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/startbutton.png")));
            startButtonDark = ImageIO.read(Objects.requireNonNull(getClass().getResource("/menu/startbuttondark.png")));
            sound = new Sound();
            playButtonBounds = new Rectangle(357, 315, 154*2, 48*2);
            startButtonBounds = new Rectangle(357, 550, 154*2, 48*2);
            typeGame.timerBar.durationSec = 10;
            clickGame.timerBar.durationSec = 8;
            highscore = saveGame.getHighscore();
            setFocusable(true);
            requestFocusInWindow();

            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(Player.gameState == GameState.CHOPPING){
                        int wrongPressed = typeGame.currentWordIndex;
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
                                    reset();
                                    clickGame.timerBar.progressVal = 100;
                                    if(typeGame.timerBar.progressVal>=50){
                                        currentScore+= (int) (200*(1+diffModif/2));
                                    } else currentScore+=(int) (100*(1+diffModif/2));
                                    Player.gameState = GameState.FRYING;
                                }
                            }
                        }
                        else{
                            if (prevWrongPressed != wrongPressed){
                                hearts--;
                                sound.play(sound.heartDmg, false,volume);
                                ui.dmgHeart();
                                prevWrongPressed=wrongPressed;
                            }

                        }

                    }

                    else if(Player.gameState == GameState.GAMEOVER){
                        if(e.getKeyChar() == KeyEvent.VK_SPACE){
                            reset();
                            Player.gameState = GameState.IDLE;
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
                        sound.play(sound.select, false, volume);

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
                            reset();
                            Player.gameState = GameState.IDLE;
                        }


                    }

                    if(Player.gameState == GameState.IDLE && startButtonBounds.contains(e.getPoint())){
                        wasStartPressed = true;
                        fade.reset();


                    }

                    if(Player.gameState == GameState.ASSEMBLE && fade.alphaValue <= 0){

                        if(timeGame.currentIndex < timeGame.ingredients.size()){



                            if(timeGame.x <=-30 || timeGame.x >= 400){
                                hearts--;
                                sound.play(sound.heartDmg,false,volume );
                                ui.dmgHeart();
                            }
                            if(timeGame.x <=140 || timeGame.x >= 250){
                                scoreMulti = false;
                            }

                            System.out.println(hearts);
                            timeGame.finalX.add(new TimeGame.Pair( ((int)(timeGame.x)), false));

                            timeGame.ingredients.get(timeGame.currentIndex).bool = true;
                            timeGame.currentIndex++;
                            if(timeGame.currentIndex == timeGame.ingredients.size()){
                                if(scoreMulti){
                                    currentScore *=3;
                                }
                                else
                                    currentScore = (int)(currentScore * 1.5);

                            }


                        }

                    }


                    if (Player.gameState == GameState.FRYING && fade.alphaValue <= 0) {
                        if(SwingUtilities.isLeftMouseButton(e)){
                            clickGame.currentClick = "L";
                        }
                        else if (SwingUtilities.isRightMouseButton(e)) clickGame.currentClick = "R";

                        if(Objects.equals(clickGame.currentList.get(clickGame.currentClickIndex).click, clickGame.currentClick)){

                            ClickGame.Pair pressedPair = (Objects.equals(clickGame.currentClick, "L") ? clickGame.lmbPressed : clickGame.rmbPressed);
                            clickGame.currentList.set(clickGame.currentClickIndex, new ClickGame.Pair(pressedPair.click, pressedPair.image));

                            clickGame.currentClickIndex++;

                            if(clickGame.currentClickIndex >= clickGame.currentList.size()){
                                clickGame.currentClickIndex = 0;

                                if(clickGame.timerBar.progressVal >=50) currentScore+=(int) (200*(1+diffModif/2));
                                else currentScore+=(int) (100*(1+diffModif/2));

                                reset();
                                Player.gameState = GameState.ASSEMBLE;
                                System.out.println(Player.gameState);
                            }
                        }
                        else{
                            hearts--;
                            sound.play(sound.heartDmg,false,volume);
                            ui.dmgHeart();
                        }
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
                Thread.sleep(1000/Constants.FPS_CAP);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }




        }
    }

    public void update(double dt){
//        System.out.println(String.format("%.2f", 1/dt) + "fps"); //Shows FPS in logs

        stateUpdater(dt);
        updateBgm(Player.gameState);
        if(score>highscore) highscore = score;
        ui.heartManage();
        if(hearts<=0) Player.gameState=GameState.GAMEOVER;
        ui.type(dt);
        if(wasStartPressed){
            reset();
            Player.gameState = GameState.CHOPPING;
        }

        if(typeGame.timerBar.progressVal <= 0){
            //game over
        }

    }



    void stateUpdater(double dt){
        switch (Player.gameState) {
            case CHOPPING:
                if(countdown.isComplete){
                    typeGame.timerBar.update(dt);
                    if(!typeGame.areWordsShown){
                        difficulty();

                    }
                    if(typeGame.timerBar.progressVal == 0){
                        hearts--;
                        sound.play(sound.heartDmg,false,volume);
                        ui.dmgHeart();
                        typeGame.timerBar.progressVal = 100;
                    }


                    fade.transparent(0.024f);
                }



                break;

            case FRYING:

                if(fade.alphaValue <= 0){
                    clickGame.timerBar.update(dt);
                }
                fade.transparent(0.0099f);
                if(!clickGame.comboGen) {
                  difficulty();
                }
                if(clickGame.startX != clickGame.moveX && fade.alphaValue <=0){
                    clickGame.pop(dt);
                }
                if(clickGame.timerBar.progressVal == 0){
                    hearts--;
                    sound.play(sound.heartDmg,false,volume);
                    ui.dmgHeart();
                    clickGame.timerBar.progressVal = 100;
                }



                break;

            case ASSEMBLE:
                fade.transparent(0.0099f);
                if(fade.alphaValue <= 0){

                    if(timeGame.currentIndex<timeGame.ingredients.size())
                        difficulty();
                    else{
                        timeGame.movePlate(dt);
                    }
                }


                break;

            case MENU:

                animationTimer+=dt;
                if (animationTimer >= .2) {

                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= menuSmoke.getFrameCount()) {
                        currentFrame = 0;
                    }
                }


                if(wasMousePressed){
                    delayTimer+=dt;

                    if(delayTimer >= 1.2){
                        reset();
                        Player.gameState = GameState.DIALOGUE;

                    }
                }
                break;

            case IDLE:
                animationTimer+=dt;
                if (animationTimer >= .2) {

                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= menuSmoke.getFrameCount()) {
                        currentFrame = 0;
                    }
                }

            case DIALOGUE:
                fade.transparent(0.009f);

                if(ui.typingOut) ui.type(dt);


                if(!ui.typingOut && ui.textIndex <= 8){
                    ui.say();

                }

                break;

            case SERVE:
                if(gameOver.duration <= 0 && !gameOver.showEnd){
                    reset();
                    burgerCount++;
                    Player.gameState = GameState.CHOPPING;
                }
                break;





        }


    }

    void updateBgm(GameState currentState){
        Clip newMusic = null;


            switch (currentState){
                case MENU:
                    newMusic = sound.menuTheme;
                    break;

                case DIALOGUE:
                    newMusic = sound.yapyapTheme;
                    break;

                case IDLE:
                    newMusic = sound.idleTheme;
                    break;

                case CHOPPING:
                case FRYING:
                case ASSEMBLE:
                    newMusic = sound.gameTheme;
                    break;

                case SERVE:
                    newMusic = currentBackgroundMusic;
                    break;

                case GAMEOVER:
                    newMusic = sound.gameOverTheme;
                    break;
            }


            if (newMusic != null && newMusic != currentBackgroundMusic) {
                if (currentBackgroundMusic != null) {
                    sound.stop(currentBackgroundMusic);
                }
                currentBackgroundMusic = newMusic;
                sound.play(currentBackgroundMusic, true, volume);
                if(currentState == GameState.GAMEOVER)sound.play(sound.lose,false, volume);
            }

            previousGameState = currentState;


        }


    void difficulty(){
        System.out.println(burgerCount+"   "+diffModif);
        diffModif = Math.min(3, burgerCount/2);
        switch (Player.gameState){

            case CHOPPING:
                if(diffModif <= 2){
                    typeGame.currentWords = typeGame.getMap((int) (2+diffModif), false);
                }
                else
                    typeGame.currentWords = typeGame.getMap((int) (2+diffModif), true);

                typeGame.currentWordsList = new ArrayList<>(typeGame.currentWords.keySet());
                typeGame.areWordsShown = true;
                typeGame.timerBar.durationSec -= (0.3*diffModif);
                break;

            case FRYING:
                clickGame.currentList = clickGame.getRandomCombo((int) (3+diffModif));
                clickGame.comboGen = true;
                clickGame.timerBar.durationSec -= (0.5*diffModif);
                break;

            case ASSEMBLE:
                timeGame.itemSwing(4+diffModif,deltaTime, fade.alphaValue);
                break;

        }
    }
    void reset(){

        switch (Player.gameState){

            case IDLE:
                fade.reset();
                materials.reset();
                player.reset();
                wasStartPressed = false;

                break;

            case MENU:
                player.reset();
                materials.reset();
                wasMousePressed = false;
                delayTimer = 0;

                break;

            case CHOPPING:
                prevWrongPressed = -1;
                materials.reset();
                player.reset();
                fade.reset();
                countdown.reset();
                typeGame.reset();
                typeGame.timerBar.durationSec = 10;


                break;

            case FRYING:
                fade.reset();
                materials.reset();
                player.reset();
                countdown.reset();
                clickGame.reset();

                break;

            case SERVE:
                saveGame.savegame();
                scoreMulti=true;
                gameOver.reset();
                break;

            case GAMEOVER:
                saveGame.savegame();
                fade.reset();
                materials.reset();
                player.reset();
                wasStartPressed = false;
                gameOver.reset();
                countdown.reset();
                clickGame.reset();
                typeGame.reset();
                timeGame.reset();
                wasMousePressed = false;
                delayTimer = 0;
                hearts = 3;
                score = 0;
                diffModif = 1;
                burgerCount = 0;

                typeGame.timerBar.durationSec = 10;
                clickGame.timerBar.durationSec = 7;

        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);


        switch (Player.gameState) {
            case CHOPPING:
                Graphics2D gC = (Graphics2D) g;
                g.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
                Color overlayC = new Color(0, 0,0, 190);
                gC.setColor(overlayC);
                gC.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                materials.drawChopping(g, deltaTime);
                if(!countdown.isComplete){
                    gC.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                    countdown.draw(g, deltaTime);
                }
                else{

                    //player.drawBack(g, deltaTime);
                    //materials.drawChoppingVegetable(g);
                    typeGame.draw(g, materials.currentFrame);
                    ui.drawScore(g);
                    ui.drawHearts(g, deltaTime);
                    fade.draw(g);
                }


                break;

            case FRYING:
                Graphics2D gF = (Graphics2D) g;
                g.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
                Color overlayF = new Color(0, 0,0, 190);
                gF.setColor(overlayF);
                gF.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                materials.drawFrying(g, deltaTime, fade.alphaValue);
                clickGame.draw(g, clickGame.currentList, materials.currentFrame);
                ui.drawScore(g);
                ui.drawHearts(g, deltaTime);
                fade.draw(g);
                break;

            case ASSEMBLE:
                Graphics2D gA = (Graphics2D) g;
                g.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
                Color overlayA = new Color(0, 0,0, 190);
                gA.setColor(overlayA);
                gA.fillRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                timeGame.draw(g);
                ui.drawScore(g);
                ui.drawHearts(g, deltaTime);

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
                BufferedImage smokeFrame = menuSmoke.getFrame(currentFrame);
                g.drawImage(smokeFrame,0,0,smokeFrame.getWidth()*Constants.SCALE, smokeFrame.getHeight()*Constants.SCALE, null);


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
                ui.drawDiag(g);


                fade.draw(g);
                ui.typeOut(g);

                break;

            case IDLE:
                g.drawImage(background,0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT, null);



                materials.drawChoppingIdle(g, deltaTime);
                materials.drawChoppingVegetableIdle(g);
                materials.drawFryingIdle(g, deltaTime);
                BufferedImage smokeFrameIdle = menuSmoke.getFrame(currentFrame);
                g.drawImage(smokeFrameIdle,590,15,smokeFrameIdle.getWidth(), smokeFrameIdle.getHeight(), null);
                player.drawBackIdle(g, deltaTime);

                Graphics2D gI = (Graphics2D) g;
                Color overlayI = new Color(0, 0,0, 210);
                gI.setColor(overlayI);
                gI.fillRect(0,startButtonBounds.y-10,Constants.SCREEN_WIDTH,startButtonBounds.height+15 );

                g.drawImage(startButton, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, null);
                if(isMousePressedOnPlayButton){
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.drawImage(startButtonDark, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, null);


                }
                g.setColor(Color.BLACK);
                g.fillRect(0,0,Constants.SCREEN_WIDTH, 80);
                g.fillRect(0, Constants.SCREEN_HEIGHT-80,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
                g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 40f));
                g.setColor(Color.WHITE);
                g.drawString("Highscore: "+highscore, 40, 50);


                break;

            case SERVE:

                gameOver.served(g, deltaTime);
                break;

            case GAMEOVER:
                Graphics2D gG = (Graphics2D)g;
                g.setColor(Color.BLACK);
                g.fillRect(0,0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
                gG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 80f));
                g.setColor(Color.WHITE);


                ui.drawCenteredText(g, "GAME OVER", 200, Color.WHITE);
                g.setFont(ui.pixelFont.deriveFont(Font.BOLD, 40));

                ui.drawCenteredText(g, "SCORE: "+score, 450, Color.WHITE);

                ui.drawCenteredText(g, "HIGHEST SCORE: "+highscore, 500, Color.WHITE);

                ui.drawCenteredText(g,">PRESS SPACE TO CONTINUE<", 600, Color.WHITE);

                ui.drawHearts(g, deltaTime);
                break;
        }

        }
    }

