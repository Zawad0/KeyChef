package minigames;

import entity.Player;
import main.Constants;
import main.GameState;
import main.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TimeGame {
    public BufferedImage counter, bottom_bun, top_bun, ketchup, ketchup_bottle, onion1,
            onion2, lettuce, patty, tomato, plate, pickles;
    public SpriteSheet bell;
    double animationTimer = 0;
    double currentFrame = 0;
    public double x = -240;
    public double plateX = 190;
    public List<Pair> finalX = new ArrayList<>();
    double velocityX = 0;
    int allFinalX = 10;
    boolean allFinalXReached = false;
    int center = 200;
    int backgroundX;
    public List<Pair> ingredients;
    public int currentIndex = 0;
    boolean end = true;




    public TimeGame() {
        try {
            bell = new SpriteSheet("/assemble/bell.png",64);
            counter = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/counter.png")));
            bottom_bun = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/bottom_bun.png")));
            top_bun = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/top_bun.png")));
            ketchup = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/ketchup.png")));
            ketchup_bottle = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/ketchup_bottle.png")));
            onion1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/onion1.png")));
            onion2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/onion2.png")));
            lettuce = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/lettuce.png")));
            patty = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/patty.png")));
            tomato = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/tomato.png")));
            plate = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/plate.png")));
            pickles = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assemble/pickles.png")));
            ingredients = new ArrayList<>(
                    Arrays.asList(new Pair(bottom_bun, false),
                            new Pair(ketchup, false),
                            new Pair(pickles, false),
                            new Pair(patty, false),
                            new Pair(lettuce, false),
                            new Pair(tomato, false),
                            new Pair(onion1, false),
                            new Pair(onion2, false),
                            new Pair(top_bun, false))
            );
            bell.frameX = 900;
            bell.frameY = 350;


            backgroundX = (Constants.SCREEN_WIDTH / 2) - ((int) (counter.getWidth() * 2.5 / 2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public static class Pair {
        BufferedImage bufferedImage;
        public boolean bool;
        public int x;

        Pair(BufferedImage bufferedImage, boolean bool) {
            this.bufferedImage = bufferedImage;
            this.bool = bool;
        }
        public Pair(int x, boolean bool) {
            this.x = x;
            this.bool = bool;
        }

    }

    public void itemSwing(double dt, double alphaValue) {
            if(alphaValue <= 0){
                double springConstant = 5;
                double accel = (center - x) * springConstant;
                velocityX += accel * dt;
                x += velocityX * dt;
            }




    }

    public void movePlate(double dt){

        plateX = Math.max(10, plateX-200*dt);

        for(int i=0; i<finalX.size(); i++){
            int x = finalX.get(i).x;
            finalX.get(i).x = Math.max(allFinalX, x-(int) (200*dt));

            if(finalX.get(i).x <= allFinalX) finalX.get(i).bool = true;

        }
        bell.frameX = (int) Math.max(600, bell.frameX-200*dt);

        for(int i = 0; i<finalX.size(); i++){
            if(!finalX.get(i).bool) break;
            else if (i>=finalX.size()-1 && finalX.get(i).bool) allFinalXReached = true;
        }

        if(allFinalXReached){
            if(bell.frameX <= 600 && end){
                animationTimer+=dt;
                if (animationTimer >= Constants.FRAME_DURATION) {

                    animationTimer = 0;
                    currentFrame++;
                    if (currentFrame >= bell.getFrameCount()) {
                        currentFrame = 0;
                        end = false;

                        reset();
                        Player.gameState = GameState.SERVE;
                    }
                }
            }
        }

    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(counter, backgroundX, 50, (int) (counter.getWidth() * 2.5),
                (int) (counter.getHeight() * 2.5), null);
        g2.drawImage(plate, (int)(plateX), 220, plate.getWidth() * Constants.SCALE,
                plate.getHeight() * Constants.SCALE, null);
        if (currentIndex < 9)
            g2.drawImage(ingredients.get(currentIndex).bufferedImage ==
                    ketchup?ketchup_bottle:ingredients.get(currentIndex).bufferedImage,
                    (int) x, 90, bottom_bun.getWidth() * Constants.SCALE,
                    bottom_bun.getHeight() * Constants.SCALE, null);
        BufferedImage bellFrame = bell.getFrame((int)currentFrame);
        int width = bellFrame.getWidth()*Constants.SCALE;
        int height = bellFrame.getHeight()*Constants.SCALE;
        if(currentIndex>=9) g2.drawImage(bellFrame, bell.frameX, bell.frameY, width, height, null);


        for (int i = 0; i < currentIndex; i++) {
            if (ingredients.get(i).bool) {
                g2.drawImage(ingredients.get(i).bufferedImage, finalX.get(i).x, 200 - (i == 8 ? (i * (-2)) : i),
                        ingredients.get(i).bufferedImage.getWidth() * Constants.SCALE,
                        ingredients.get(i).bufferedImage.getHeight() * Constants.SCALE, null);
            }
        }


    }


    public void reset(){
        animationTimer = 0;
        currentFrame = 0;
        x = -240;
        plateX = 190;
        finalX.clear();
        velocityX = 0;
        allFinalXReached = false;
        currentIndex = 0;
        end = true;
        for(Pair i:ingredients){
            i.bool = false;
        }
    }

}


