package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;

public class SpriteSheet {
    private BufferedImage spriteSheet;
    private ArrayList<BufferedImage> frames = new ArrayList<>();

    private int frameHeight;
    public int frameX = Constants.SCREEN_WIDTH/2;
    public int frameY = Constants.SCREEN_HEIGHT/2;
    public SpriteSheet(String filepath, int frameWidth) throws Exception {
        spriteSheet = ImageIO.read(getClass().getResource(filepath));
        frameHeight = spriteSheet.getHeight();
        int numFrames = spriteSheet.getWidth()/frameWidth;

        System.out.println("Loaded image: " + filepath);
        System.out.println("Image size: " + spriteSheet.getWidth() + "x" + spriteSheet.getHeight());

        for(int i = 0; i<numFrames; i++){
            BufferedImage frame = spriteSheet.getSubimage(i*frameWidth, 0, frameWidth, frameHeight);
            frames.add(frame);
        }


    }

    public BufferedImage getFrame(int index) {
        return frames.get(index % frames.size());
    }

    public int getFrameCount() {
        return frames.size();
    }
}
