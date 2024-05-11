package com.ds.darknesschat.utils.languages;

import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

import static com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys.APP_LANGUAGE;

public class StringGetterWithCurrentLanguage {
    public static @Nullable String getString(String key){
        try{
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(OutsideSettingsManager.getValue(APP_LANGUAGE)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            properties.load(bufferedReader);

            String result = (String) properties.get(key);

            fileInputStream.close();
            bufferedReader.close();

            return result;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
