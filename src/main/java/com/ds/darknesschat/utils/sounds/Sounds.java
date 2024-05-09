package com.ds.darknesschat.utils.sounds;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.log.Log;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Sounds {
    public static void playSound(String soundPath){
        try{
            InputStream inputStream = Main.class.getResourceAsStream(soundPath);
            assert inputStream != null;
            byte[] data = inputStream.readAllBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.setFramePosition(0);
            clip.start();

            inputStream.close();
            byteArrayInputStream.close();
            audioInputStream.close();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
