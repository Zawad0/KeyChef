package minigames;

import main.Constants;
import main.SpriteSheet;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.*;
import java.util.List;

public class ClickGame {

    BufferedImage board;
    public BufferedImage lmb, rmb;
    public BufferedImage lmb_press, rmb_press;
    Map<String, BufferedImage>map;
    public List<Pair>currentList;
    int space = 32;
    int totalWidth = -space;

    public int moveX = 0;
    public double startX = Constants.SCREEN_WIDTH+5;
    public String currentClick;
    public int currentClickIndex = 0;
    public Pair lmbPressed, rmbPressed;
    BufferedImage barFrame;
    SpriteSheet clock;
    public TimerBar timerBar = new TimerBar("/ui/progress_bar.png",7, 0.5);

    public boolean comboGen;
    public ClickGame(){
        try {
            board = ImageIO.read(Objects.requireNonNull(getClass().getResource("/frying/clickgameboard.png")));
            lmb = ImageIO.read(Objects.requireNonNull(getClass().getResource("/keys/Mouse/lmb.png")));
            rmb = ImageIO.read(Objects.requireNonNull(getClass().getResource("/keys/Mouse/rmb.png")));
            lmb_press = ImageIO.read(Objects.requireNonNull(getClass().getResource("/keys/Mouse/lmb_pressed.png")));
            rmb_press =ImageIO.read(Objects.requireNonNull(getClass().getResource("/keys/Mouse/rmb_pressed.png")));
            lmbPressed = new Pair("L", lmb_press);
            rmbPressed = new Pair("R", rmb_press);
            clock = new SpriteSheet("/ui/clock.png", 32);
            barFrame = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/bar.png")));
            timerBar.width = (int) (timerBar.barFill.getWidth());
            timerBar.height = (int) (timerBar.barFill.getHeight());
            timerBar.x = 5;
            timerBar.y = 5;
            clock.frameX = 30;
            clock.frameY = 20;

            map = new HashMap<>(Map.of(
                    "L",lmb, "R",rmb
            ));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Pair{
        public String click;
        public BufferedImage image;
        public Pair(String click, BufferedImage image){
            this.click = click;
            this.image = image;
        }
    }
    public List<Pair> getRandomCombo(int count){
        List<Pair> randomArray;
        randomArray = new ArrayList<>();
        for(int i = 0; i<count;i++){
            String string = Math.random() < 0.5 ? "L" : "R";

            randomArray.add(new Pair(string, (string.equals("L") ? lmb : rmb)));
            totalWidth += lmb.getWidth()*Constants.SCALE + space;
        }
        return randomArray;
    }

    public void pop(double dt){
        moveX=(Constants.SCREEN_WIDTH - totalWidth)/2;

        startX-=1500*dt;
        if(startX <= moveX){
            startX = moveX;
        }
    }

    public void draw(Graphics g, List<Pair>list, int currentFrame){
        int x=(int)startX, y=580;
        BufferedImage clockFrame = clock.getFrame(currentFrame);

        int clockScaledWidth = (int) (clockFrame.getWidth());
        int clockScaledHeight = (int) (clockFrame.getHeight());


        int frameScaledWidth = (int) (barFrame.getWidth());
        int frameScaledHeight = (int)(barFrame.getHeight());


        g.drawImage(board, 0,-20,board.getWidth()* Constants.SCALE, board.getHeight()*Constants.SCALE,null);
        timerBar.draw(g);
        g.drawImage(barFrame, timerBar.x, timerBar.y, frameScaledWidth,
                frameScaledHeight, null);
        g.drawImage(clockFrame,clock.frameX,clock.frameY,clockScaledWidth, clockScaledHeight, null);

        for(Pair i : list){
            BufferedImage image = i.image;
            int imageWidth = image.getWidth()*Constants.SCALE;
            int imageHeight = image.getHeight()*Constants.SCALE;
            g.drawImage(image, x, y, imageWidth, imageHeight, null);

            x+=imageWidth+space;
        }


    }

    public void reset(){
        if(currentList != null) currentList.clear();
        currentList = null;
        space = 32;
        totalWidth = -space;
        moveX = 0;
        startX = Constants.SCREEN_WIDTH+5;
        currentClickIndex = 0;
        timerBar.progressVal = 100;
        comboGen = false;

    }


}
