package minigames;

import main.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TimeGame {
    public BufferedImage counter, bottom_bun, top_bun, ketchup, ketchup_bottle, onion1,
            onion2, lettuce, patty, tomato, plate, pickles;
    public double x = -240;
    public double y = 50;
    public List<Integer> finalX = new ArrayList<>();
    public List<Integer> finalY = new ArrayList<>();
    double velocityX = 0;
    double velocityY = 0;
    double gravity = 8;
    int center = 200;
    int backgroundX;
    public List<Pair> ingredients;
    public int currentIndex = 0;




    public TimeGame() {
        try {

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


            backgroundX = (Constants.SCREEN_WIDTH / 2) - ((int) (counter.getWidth() * 2.5 / 2));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }


    }


    public static class Pair {
        BufferedImage bufferedImage;
        public boolean bool;

        Pair(BufferedImage bufferedImage, boolean bool) {
            this.bufferedImage = bufferedImage;
            this.bool = bool;
        }

    }

    public void itemSwing(double dt) {
            double springConstant = 10;
            double accel = (center - x) * springConstant;
            velocityX += accel * dt;
            x += velocityX * dt;



    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(counter, backgroundX, 50, (int) (counter.getWidth() * 2.5),
                (int) (counter.getHeight() * 2.5), null);
        g2.drawImage(plate, 190, 220, plate.getWidth() * Constants.SCALE,
                plate.getHeight() * Constants.SCALE, null);
        if (currentIndex < 9)
            g2.drawImage(ingredients.get(currentIndex).bufferedImage ==
                    ketchup?ketchup_bottle:ingredients.get(currentIndex).bufferedImage,
                    (int) x, 90, bottom_bun.getWidth() * Constants.SCALE,
                    bottom_bun.getHeight() * Constants.SCALE, null);


        for (int i = 0; i < currentIndex; i++) {
            if (ingredients.get(i).bool) {
                g2.drawImage(ingredients.get(i).bufferedImage, finalX.get(i), 200 - (i == 8 ? (i * (-2)) : i),
                        ingredients.get(i).bufferedImage.getWidth() * Constants.SCALE,
                        ingredients.get(i).bufferedImage.getHeight() * Constants.SCALE, null);
            }
        }


    }

}


