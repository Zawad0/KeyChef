package entity;
import main.Constants;
import main.GamePanel;
import main.SpriteSheet;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
public class Materials {
    GamePanel gp;



    public SpriteSheet chopping;
    public SpriteSheet frying;
    public BufferedImage choppingTomato;
    public BufferedImage choppingOnion;
    public BufferedImage choppingCucumber;
    public Boolean tomatoShown, onionShown, cucumberShown;
    double animationTimer = 0;
    public int currentFrame = 0;
    public static double delay = 0;
    double fryDelay = 0;

    public Materials(GamePanel gp){
        this.gp = gp;
        try {
            chopping = new SpriteSheet("/chopping.png", 64);
            frying = new SpriteSheet("/frying.png", 330);
            frying.frameX = 100;
            frying.frameY = 100;

            choppingTomato = ImageIO.read(Objects.requireNonNull(getClass().getResource("/tomato_slice.png")));

            choppingOnion = ImageIO.read(Objects.requireNonNull(getClass().getResource("/onion_slice.png")));

            choppingCucumber = ImageIO.read(Objects.requireNonNull(getClass().getResource("/cucumber_slice.png")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void reset(){
        animationTimer = 0;
        currentFrame = 0;
        tomatoShown = cucumberShown = onionShown = false;
        delay = 0;
    }
    public void drawChopping(Graphics g, double dt){

        animationTimer += dt;
        chopping.frameX = Constants.CHOPX;
        chopping.frameY = Constants.CHOPY;



        if(currentFrame >= 12){
            tomatoShown = true;
        }
        if(currentFrame >=24){
            onionShown = true;
        }
        if(currentFrame >= 40){
            cucumberShown = true;
            delay = 1;
        }
        if(currentFrame<=12){
            tomatoShown = false;
            onionShown = false;
            cucumberShown = false;
        }


        if (animationTimer >= Constants.FRAME_DURATION && animationTimer>=delay) {
            animationTimer = 0;
            delay = 0;
            currentFrame++;
            if (currentFrame >= chopping.getFrameCount()) {
                currentFrame = 0;
            }
        }


        BufferedImage applianceFrame = chopping.getFrame(currentFrame);
        int applianceX = chopping.frameX*Constants.SCALE;
        int applianceY = chopping.frameY*Constants.SCALE;
        int applianceWidth = applianceFrame.getWidth()*Constants.SCALE*2;
        int applianceHeight = applianceFrame.getHeight()*Constants.SCALE*2;
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }


    public void drawChoppingIdle(Graphics g, double dt){

        if(Constants.stationSwitch == 1){
            animationTimer += dt;
            chopping.frameX = 314;
            chopping.frameY = 101;



            if(currentFrame >= 12){
                tomatoShown = true;
            }
            if(currentFrame >=24){
                onionShown = true;
            }
            if(currentFrame >= 40){
                cucumberShown = true;
                Constants.stationSwitch = 2;
            }
            if(currentFrame<=12){
                tomatoShown = false;
                onionShown = false;
                cucumberShown = false;
            }

            if (animationTimer >= Constants.FRAME_DURATION) {

                animationTimer = 0;
                currentFrame++;
                if (currentFrame >= chopping.getFrameCount()) {
                    currentFrame = 0;
                }
            }


            BufferedImage fryFrame = frying.getFrame(0);
            BufferedImage fryFrameCrop = fryFrame.getSubimage(1,1,fryFrame.getWidth()-85,fryFrame.getHeight()-10);
            int fryX = 517;
            int fryY = 210;
            int fryWidth = (int)(0.397*fryFrameCrop.getWidth());
            int fryHeight = (int)(0.397*fryFrameCrop.getHeight());
            g.drawImage(fryFrameCrop, fryX, fryY, fryWidth, fryHeight, null);




            BufferedImage applianceFrame = chopping.getFrame(currentFrame);
            int applianceX = chopping.frameX*Constants.SCALE;
            int applianceY = chopping.frameY*Constants.SCALE;
            int applianceWidth = applianceFrame.getWidth()*Constants.SCALE;
            int applianceHeight = applianceFrame.getHeight()*Constants.SCALE;
            g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
        }

    }

    public void drawChoppingVegetable(Graphics g){
        if(tomatoShown){
            g.drawImage(choppingTomato, Constants.TOMATOX, Constants.TOMATOY,
                    choppingTomato.getWidth()*3, choppingTomato.getHeight()*3, null);
        }
        if(onionShown){
            g.drawImage(choppingOnion, Constants.ONIONX, Constants.ONIONY,
                    choppingOnion.getWidth()*3, choppingOnion.getHeight()*3, null);
        }
        if(cucumberShown){
            g.drawImage(choppingCucumber, Constants.CUCUMBERX, Constants.CUCUMBERY,
                    choppingCucumber.getWidth()*3, choppingCucumber.getHeight()*3, null);
        }
    }

    public void drawChoppingVegetableIdle(Graphics g){
        if(tomatoShown){
            g.drawImage(choppingTomato, Constants.IDLETOMATOX, Constants.IDLETOMATOY,
                    choppingTomato.getWidth()*Constants.SCALE, choppingTomato.getHeight()*Constants.SCALE, null);
        }
        if(onionShown){
            g.drawImage(choppingOnion, Constants.IDLEONIONX, Constants.IDLEONIONY,
                    choppingOnion.getWidth()*Constants.SCALE, choppingOnion.getHeight()*Constants.SCALE, null);
        }
        if(cucumberShown){
            g.drawImage(choppingCucumber, Constants.IDLECUCUMBERX, Constants.IDLECUCUMBERY,
                    choppingCucumber.getWidth()*Constants.SCALE, choppingCucumber.getHeight()*Constants.SCALE, null);
        }
    }

    public void drawFrying(Graphics g, double dt, double alphaValue){

        animationTimer+=dt;
        frying.frameX = Constants.FRYX;
        frying.frameY = Constants.FRYY;

        if(alphaValue <= 0){
            if (animationTimer >= Constants.FRY_FRAME_DURATION) {
                animationTimer = 0;
                currentFrame++;
                if (currentFrame >= frying.getFrameCount()) {
                    currentFrame = 15;
                }
            }
        }

        BufferedImage applianceFrame = frying.getFrame(currentFrame);
        int applianceX = frying.frameX*Constants.SCALE;
        int applianceY = frying.frameY*Constants.SCALE;
        int applianceWidth = (int) (applianceFrame.getWidth()*Constants.SCALE);
        int applianceHeight = (int) (applianceFrame.getHeight()*Constants.SCALE);
        g.drawImage(applianceFrame, applianceX, applianceY, applianceWidth, applianceHeight, null);
    }


    public void drawFryingIdle(Graphics g, double dt){

        if(Constants.stationSwitch == 2){
            animationTimer+=dt;
            fryDelay+=dt;
            frying.frameX = Constants.FRYX;
            frying.frameY = Constants.FRYY;

            if (animationTimer >= Constants.FRY_FRAME_DURATION) {
                animationTimer = 0;
                currentFrame++;
                if (currentFrame >= frying.getFrameCount()) {
                    currentFrame = 15;
                }
            }

            if(fryDelay >= 10){
                currentFrame = 0;
                Constants.stationSwitch = 1;
                fryDelay = 0;
            }


            BufferedImage applianceFrame = frying.getFrame(currentFrame);
            BufferedImage applianceFrameCrop = applianceFrame.getSubimage(1,1,applianceFrame.getWidth()-85,applianceFrame.getHeight()-10);
            int applianceX = 517;
            int applianceY = 210;
            int applianceWidth = (int)(0.397*applianceFrameCrop.getWidth());
            int applianceHeight = (int)(0.397*applianceFrameCrop.getHeight());
            g.drawImage(applianceFrameCrop, applianceX, applianceY, applianceWidth, applianceHeight, null);
        }

    }




}
