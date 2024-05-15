package com.ds.darknesschat.utils.appSettings.settingsReader;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import static com.ds.darknesschat.Constants.BUILT_IN_SETTINGS_PATH;
import static com.ds.darknesschat.Constants.NULL;

public final class SettingsReader {
    private static @Nullable Object getValue(String key){
        try {
            Properties properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(BUILT_IN_SETTINGS_PATH)));

            properties.load(inputStreamReader);
            Object result = properties.getOrDefault(key, NULL);

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
