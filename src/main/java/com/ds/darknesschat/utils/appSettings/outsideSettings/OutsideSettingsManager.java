package com.ds.darknesschat.utils.appSettings.outsideSettings;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class OutsideSettingsManager {
    public static @Nullable String getValue(String key){
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(Constants.OUTSIDE_SETTINGS_PATH);
            properties.load(fileInputStream);

            String result = (String) properties.get(key);

            fileInputStream.close();

            return result;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static void changeValue(String key, String newValue){
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(Constants.OUTSIDE_SETTINGS_PATH);
            FileOutputStream fileOutputStream = new FileOutputStream(Constants.OUTSIDE_SETTINGS_PATH);
            properties.load(fileInputStream);

            properties.setProperty(key, newValue);
            properties.store(fileOutputStream, null);

            fileInputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
