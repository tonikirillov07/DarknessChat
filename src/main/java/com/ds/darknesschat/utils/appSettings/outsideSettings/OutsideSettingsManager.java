package com.ds.darknesschat.utils.appSettings.outsideSettings;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class OutsideSettingsManager {
    private static final Properties properties = new Properties();

    static {
        try{
            FileInputStream fileInputStream = new FileInputStream(Constants.OUTSIDE_SETTINGS_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static @Nullable String getValue(String key){
        return (String) properties.get(key);
    }

    public static void changeValue(String key, String newValue){
        try {
            properties.setProperty(key, newValue);

            FileWriter fileOutputStream =new FileWriter(Constants.OUTSIDE_SETTINGS_PATH);
            properties.store(fileOutputStream, null);
            fileOutputStream.close();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
