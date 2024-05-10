package com.ds.darknesschat.utils.sounds;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.utils.log.Log;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

import static com.ds.darknesschat.Constants.IGNORE_USER_AGREEMENT;

public class Sounds {
    public static void playSound(String soundPath, long userId){
        try{
            if(userId != IGNORE_USER_AGREEMENT) {
                if (DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(DatabaseConstants.SOUNDS_USING_ROW, userId))))
                    playWithIgnoreUserSettings(soundPath);
            }else
                playWithIgnoreUserSettings(soundPath);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void playWithIgnoreUserSettings(String path){
        try{
            InputStream inputStream = Main.class.getResourceAsStream(path);
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
