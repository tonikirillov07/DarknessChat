package com.ds.darknesschat.utils.appSettings.settingsReader;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import static com.ds.darknesschat.Constants.BUILT_IN_SETTINGS_PATH;
import static com.ds.darknesschat.Constants.NULL;

public final class SettingsReader {
    private static final Properties properties = new Properties();

    static {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(BUILT_IN_SETTINGS_PATH)));
            properties.load(inputStreamReader);
            inputStreamReader.close();
        } catch (IOException e) {
            Log.error(e);
        }
    }

    private static @Nullable Object getValue(String key) {
        return properties.getOrDefault(key, NULL);
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
