package minigames;
import main.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class TypeGame {
    public boolean areWordsShown = false;
    public Map<String, List<BufferedImage>>currentWords = new HashMap<>();
    public WordBank words = new WordBank("text/words_alpha_common.txt");
    public Keys keys = new Keys();
    public TimerBar timerBar = new TimerBar("/ui/progress_bar.png",5, Constants.SCALE);

    public List<String>currentWordsList;
    public int currentWordIndex = 0;
    public int currentCharIndex = 0;

    BufferedImage barBack;
    BufferedImage barFrame;
    SpriteSheet clock;


    public TypeGame(){

        try {
            barFrame = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/bar.png")));
            clock = new SpriteSheet("/ui/clock.png",32);
            barBack = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/bar_background.png")));
            clock.frameX = 55;
            clock.frameY = 30;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void draw(Graphics g, int currentFrame){
        int x=70, y=150;
        BufferedImage clockFrame = clock.getFrame(currentFrame);
        int clockScaledWidth = clockFrame.getWidth()*3;
        int clockScaledHeight = clockFrame.getHeight()*3;

        for(List<BufferedImage> i : currentWords.values()){
            for(BufferedImage j : i){
                g.drawImage(j, x,y, (int) (j.getWidth()*2.2), (int) (j.getHeight()*2.2),null);
                x+=32;
            }
            x+=45;
        }
        g.drawImage(barBack, Constants.TIMERX, Constants.TIMERY, barFrame.getWidth()*Constants.SCALE,
                barFrame.getHeight()*Constants.SCALE, null);
        timerBar.draw(g);
        g.drawImage(barFrame, Constants.TIMERX, Constants.TIMERY, barFrame.getWidth()*Constants.SCALE,
                barFrame.getHeight()*Constants.SCALE, null);
        g.drawImage(clockFrame,clock.frameX,clock.frameY,clockScaledWidth, clockScaledHeight, null);


    }





    public class Keys {

        public Map<String, BufferedImage> keySprites = new HashMap<>();
        public Map<String, BufferedImage> keyPressedSprites = new HashMap<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


        Keys(){
            for(int i = 0; i<alphabet.length();i++) {
                String keyName = String.valueOf(alphabet.charAt(i));
                String path = String.format("/keys/Letters/L. Key %d.png", i+1);
                try {
                    keySprites.put(keyName, ImageIO.read(Objects.requireNonNull(getClass().getResource(path))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            for(int i = 0; i<alphabet.length();i++) {
                String keyName = String.valueOf(alphabet.charAt(i));
                String path = String.format("/keys/Letters Pressed/L. Key %d.png", i+1);
                try {
                    keyPressedSprites.put(keyName, ImageIO.read(Objects.requireNonNull(getClass().getResource(path))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        public BufferedImage get(String s){
            return keySprites.get(s.toUpperCase());
        }
        public BufferedImage getPr(String s){
            return keyPressedSprites.get(s.toUpperCase());
        }

        public List<BufferedImage> getSpriteList(String s){
            List<BufferedImage>sprites = new ArrayList<>();
            for(char i : s.toCharArray()){
                sprites.add(get(String.valueOf(i)));
            }
            return sprites;
        }




    }

    public static class WordBank {
        private List<String> words = new ArrayList<>();
        private Random ran = new Random();
        BufferedReader bufferedReader;

        public WordBank(String filepath){
            loadWords(filepath);
        }

        private void loadWords(String filepath) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filepath))))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length()>=4){
                        words.add(line.trim());
                    }

                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        //To check if class is loading words into arraylist
        public void printWords(){
            for(int i=0;i<10;i++){
                System.out.println(words.get(i));
            }
        }

        public String getRandomWord() {
            String s = words.get(ran.nextInt(words.size()));
            return s.toUpperCase();
        }

        public List<String> getRandomArray(int size){
            List<String> randomArray;
            int arraySize;
            do {
                 randomArray = new ArrayList<>();
                arraySize = 0;

                for(int i=0;i<size;i++){
                    randomArray.add(getRandomWord());
                }
                for(String word : randomArray){
                    arraySize += word.length();
                }
            } while (arraySize>23);

            return randomArray;
        }
    }



    public Map<String, List<BufferedImage>> getMap (int size){
        Map<String, List<BufferedImage>> map = new HashMap<>();
        List<String> list = words.getRandomArray(size);
        for(int i=0;i<size;i++){
            map.put(list.get(i),keys.getSpriteList(list.get(i)));
        }

        return map;
    }





}
