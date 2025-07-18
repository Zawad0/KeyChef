package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Fade extends JPanel {

    //Timer alphaTime= new Timer(20,this);
    //BufferedImage bufferedImage;

    float alphaValue = 1f;
//    public Fade(String filepath){
//        loadBufferedImage(filepath);
//        //alphaTime.start();
//    }
//    public void loadBufferedImage(String filepath){
//        bufferedImage = null;
//        try {
//            bufferedImage = ImageIO.read(getClass().getClassLoader().getResource(filepath));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void transparent(){
        alphaValue -= 0.01f;

        if(alphaValue <0){
            alphaValue = 0;
        }
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        alphaValue -= 0.01f;
//
//        if(alphaValue <0){
//            alphaValue = 0;
//            //alphaTime.stop();
//        }
//        repaint();
//    }

//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
//
//    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }
}
