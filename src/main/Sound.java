package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class Sound {
    Clip menuTheme, yapyapTheme, idleTheme, gameTheme, gameOverTheme;
    Clip chop;
    Clip fry;
    Clip lose;
    Clip select;
    public Clip bell;
    boolean isPlaying;

    public Sound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        menuTheme = laodClip("/sound/bgm/headscratcher.wav");
        yapyapTheme = laodClip("/sound/bgm/shopkeepers_theme.wav");
        idleTheme = laodClip("/sound/bgm/8bit Bossa.wav");
        gameTheme = laodClip("/sound/bgm/cutie_pie.wav");
        gameOverTheme = laodClip("/sound/bgm/gameoverjazz.wav");

        chop = laodClip("/sound/soundfx/chops.wav");
        fry =laodClip("/sound/soundfx/gasburner.wav");
        lose = laodClip("/sound/soundfx/losetrumpet.wav");
        select = laodClip("/sound/soundfx/select.wav");
        bell = laodClip("/sound/soundfx/Ding.wav");
    }

    private Clip laodClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(path)));

        Clip clip = AudioSystem.getClip();
        clip.open(ais);

        return clip;
    }

    public void play(Clip clip, boolean loop){
        if(clip.isRunning()){
            clip.stop();
        }
        clip.setFramePosition(0);
        if(loop){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else{
            clip.start();
        }

    }

    public void stop(Clip clip){
        if(clip.isRunning()){
            clip.stop();
        }
    }
}