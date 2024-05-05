package com.ds.darknesschat.utils;

import com.ds.darknesschat.Test;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import static com.ds.darknesschat.Constants.SETTINGS_PATH;

public final class SettingsReader {
    private static @Nullable Object getValue(String key){
        try {
            Properties properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(Test.class.getResourceAsStream(SETTINGS_PATH)));

            properties.load(inputStreamReader);
            Object result = properties.get(key);

            inputStreamReader.close();

            return result;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static String getStringValue(String key){
        return (String) getValue(key);
    }

    public static boolean getBooleanValue(String key){
        return Boolean.parseBoolean(getStringValue(key));
    }

    public static int getIntegerValue(String key){
        return Integer.parseInt(getStringValue(key));
    }

    public static double getDoubleValue(String key){
        return Double.parseDouble(getStringValue(key));
    }
}
