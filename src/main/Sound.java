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
    Clip heartDmg;
    public Clip bell;
    boolean isPlaying;

    public Sound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        menuTheme = loadClip("/sound/bgm/headscratcher.wav");
        yapyapTheme = loadClip("/sound/bgm/shopkeepers_theme.wav");
        idleTheme = loadClip("/sound/bgm/8bit Bossa.wav");
        gameTheme = loadClip("/sound/bgm/alongtheway.wav");
        gameOverTheme = loadClip("/sound/bgm/gameoverjazz.wav");

        chop = loadClip("/sound/soundfx/chops.wav");
        fry = loadClip("/sound/soundfx/gasburner.wav");
        lose = loadClip("/sound/soundfx/losetrumpet.wav");
        select = loadClip("/sound/soundfx/select.wav");
        bell = loadClip("/sound/soundfx/Ding.wav");
        heartDmg = loadClip("/sound/soundfx/heartdown.wav");
    }

    private Clip loadClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(path)));

        Clip clip = AudioSystem.getClip();
        clip.open(ais);

        return clip;
    }

    public void play(Clip clip, boolean loop, float volume){
        if(clip.isRunning()){
            clip.stop();
        }
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volume);
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