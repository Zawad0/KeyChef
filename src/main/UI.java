    package main;


    import entity.Player;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Objects;


    public class UI{
        BufferedImage dialogue;
        public String fullText;
        public String currentDialogue = "";
        private double typeTimer = 0;
        private int charIndex = 0;
        private double typeSpeed = 0.05;
        public boolean typing = false;
        public int textIndex = 1;
        public boolean typingOut = false;
        public Font pixelFont;
        public GraphicsEnvironment ge;

        SpriteSheet idleHearts, damagedHearts;
        BufferedImage emptyHeart;
        ArrayList<Hearts>heartsContainer;

        public class Hearts{
            boolean isfull = true, playDmg;
            double animationTimer = 0, dmgAnimationTimer = 0;
            int currentFrame = 0, currentDmgFrame = 0;
            int x, y;
            Hearts(int x, int y){
                this.x = x;
                this.y = y;
            }


            public void draw(Graphics g, double dt){




                if(isfull){
                    update(dt, idleHearts);
                    BufferedImage heartsFrame = idleHearts.getFrame(currentFrame);
                    int width = heartsFrame.getWidth();
                    int height = heartsFrame.getHeight();
                    g.drawImage(heartsFrame, x, y, width, height, null);

                }
                else {
                    if(Player.gameState != GameState.GAMEOVER){
                        int width = emptyHeart.getWidth();
                        int height = emptyHeart.getHeight();
                        g.drawImage(emptyHeart, x, y, width, height, null);
                    }
                    else{
                        int width = emptyHeart.getWidth()*Constants.SCALE;
                        int height = emptyHeart.getHeight()*Constants.SCALE;
                        g.drawImage(emptyHeart, x, y, width, height, null);
                    }

                }

                if(playDmg){
                    update(dt, damagedHearts);
                    BufferedImage heartsFrame = damagedHearts.getFrame(currentDmgFrame);

                    if(Player.gameState != GameState.GAMEOVER){
                        int width = heartsFrame.getWidth()*4;
                        int height = heartsFrame.getHeight()*4;
                        g.drawImage(heartsFrame, (Constants.SCREEN_WIDTH-(32*4))/2, 250, width, height, null);
                    }
                    else{
                        int width = heartsFrame.getWidth()*Constants.SCALE;
                        int height = heartsFrame.getHeight()*Constants.SCALE;
                        g.drawImage(heartsFrame, x, y, width, height, null);
                    }

                }
            }

             void update(double dt, SpriteSheet spriteSheet){
                animationTimer+=dt;
                if (animationTimer >= 0.5) {

                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= spriteSheet.getFrameCount()) {
                        currentFrame = 0;
                    }
                }

                if(playDmg){
                    dmgAnimationTimer+=dt;
                    if (dmgAnimationTimer >= 0.125) {

                        dmgAnimationTimer = 0;
                        currentDmgFrame++;
                        if (currentDmgFrame >= spriteSheet.getFrameCount()) {
                            currentDmgFrame = 0;
                            playDmg = false;
                        }
                    }
                }
            }

        }

        public UI(){

            try {
                emptyHeart = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/hearts/emptyheart.png")));
                idleHearts = new SpriteSheet("/ui/hearts/idleheart.png", 32);
                damagedHearts = new SpriteSheet("/ui/hearts/damagedheart.png",32);
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().
                        getResourceAsStream("/text/PixelifySans-VariableFont_wght.ttf")));
                dialogue = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/Sprite-Dialogue.png")));

                ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);

                heartsContainer = new ArrayList<>(
                        Arrays.asList(
                                new Hearts(720, 230),
                                new Hearts(750, 230),
                                new Hearts(780, 230)
                        )
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void say(){
            switch (textIndex){
                case 1:
                    startTyping(Constants.DIALOGUE1);
                    textIndex++;
                    typingOut = true;
                    break;
                case 2:
                    startTyping(Constants.DIALOGUE2);
                    textIndex++;
                    typingOut = true;
                    break;
                case 3:
                    startTyping(Constants.DIALOGUE3);
                    textIndex++;
                    typingOut = true;
                    break;
                case 4:
                    startTyping(Constants.DIALOGUE4);
                    textIndex++;
                    typingOut = true;
                    break;
                case 5:
                    startTyping(Constants.DIALOGUE5);
                    textIndex++;
                    typingOut = true;
                    break;
                case 6:
                    startTyping(Constants.DIALOGUE6);
                    textIndex++;
                    typingOut = true;
                    break;
                case 7:
                    startTyping(Constants.DIALOGUE7);
                    textIndex++;
                    typingOut = true;
                    break;
                case 8:
                    startTyping(Constants.DIALOGUE8);
                    textIndex++;
                    typingOut = true;
                    break;
            }
        }

        public void drawDiag(Graphics g){
            if(Player.gameState == GameState.DIALOGUE){
                drawDialogue(g);
            }
        }

        public void drawScore(Graphics g){
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            g.setFont(pixelFont.deriveFont(Font.BOLD, 23f));
            g.setColor(Color.WHITE);

            switch (Player.gameState){
                case ASSEMBLE:
                    g.drawString("Score: " + (GamePanel.score), 150, 120);
                    g.setFont(pixelFont.deriveFont(Font.BOLD, 30f));
                    if (GamePanel.scoreMulti) {
                        g.drawString("3x", 150, 150);
                    } else {
                        g.drawString("1.5x", 150, 150);
                    }
                    break;
                case CHOPPING:
                    g.drawString("Score: "+(GamePanel.score), 210, 250);
                    break;
                case FRYING:
                    g.drawString("Score: "+(GamePanel.score), 20, 100);
                    break;
            }

        }

        public void drawDialogue(Graphics g) {

            g.drawImage(dialogue, Constants.DIALOGUE_WINDOWX, Constants.DIALOGUE_WINDOWY, Constants.DIALOGUE_WINDOW_WIDTH, Constants.DIALOGUE_WINDOW_HEIGHT, null);
        }

        public void type(double dt){
            if(typing){
                typeTimer+=dt;

                if(typeTimer >= typeSpeed && charIndex < fullText.length()){
                    currentDialogue+=fullText.charAt(charIndex);
                    charIndex++;
                    typeTimer = 0;
                    System.out.println(currentDialogue);
                }
                if(charIndex>=fullText.length()){
                    typing = false;
                }
            }
        }
        public void startTyping(String text) {
            fullText = text;
            currentDialogue = "";
            charIndex = 0;
            typeTimer = 0;
            typing = true;
            //System.out.println("Started typing: " + text);

        }

        public void typeOut(Graphics g){

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            g.setFont(pixelFont.deriveFont(Font.BOLD, 23f));
            g.setColor(Color.BLACK);

            g.drawString(currentDialogue, Constants.TEXTX, Constants.TEXTY);
        }




        public void drawHearts(Graphics g, double dt){
            for(int i = 0; i<heartsContainer.size();i++){
                switch (Player.gameState){

                    case FRYING:
                        heartsContainer.get(i).x = 730+ (i*30);
                        heartsContainer.get(i).y = 60;
                        break;

                    case CHOPPING:
                        heartsContainer.get(i).x = 720+ (i*30);
                        heartsContainer.get(i).y = 230;
                        break;

                    case ASSEMBLE:
                        heartsContainer.get(i).x = 770+ (i*30);
                        heartsContainer.get(i).y = 100;
                        break;

                    case GAMEOVER:

                        heartsContainer.get(i).x = ((Constants.SCREEN_WIDTH-180)/2) + (i*60);
                        heartsContainer.get(i).y = 250;
                        break;
                }
                heartsContainer.get(i).draw(g, dt);
            }
        }

        public void heartManage(){
            switch (GamePanel.hearts){
                case 3:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = true;
                    heartsContainer.getLast().isfull = true;
                    break;

                case 2:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = true;
                    heartsContainer.getLast().isfull = false;
                    break;

                case 1:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = false;
                    heartsContainer.getLast().isfull = false;
                    break;

                case 0:
                    heartsContainer.getFirst().isfull = false;
                    heartsContainer.get(1).isfull = false;
                    heartsContainer.getLast().isfull = false;
                    break;
            }

        }

        public void dmgHeart(){
            switch (GamePanel.hearts){
                case 3:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = true;
                    heartsContainer.getLast().isfull = true;
                    break;

                case 2:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = true;
                    heartsContainer.getLast().isfull = false;
                    heartsContainer.getLast().playDmg = true;
                    break;

                case 1:
                    heartsContainer.getFirst().isfull = true;
                    heartsContainer.get(1).isfull = false;
                    heartsContainer.get(1).playDmg = true;
                    heartsContainer.getLast().isfull = false;
                    break;

                case 0:
                    heartsContainer.getFirst().isfull = false;
                    heartsContainer.getFirst().playDmg = true;
                    heartsContainer.get(1).isfull = false;
                    heartsContainer.getLast().isfull = false;
                    break;
            }

        }

        public void drawCenteredText(Graphics g, String text, int y, Color color){
            FontMetrics fm = g.getFontMetrics();
            g.setColor(color);
            int textX = (Constants.SCREEN_WIDTH-fm.stringWidth(text))/2;
            g.drawString(text, textX, y);
        }


        public void reset(){

        }

    }
