package com.ds.darknesschat.utils.log;

import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.info.FileSize;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.util.logging.Logger;

import static com.ds.darknesschat.Constants.LOGS_PATH;
import static com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys.DO_LOGS;

public final class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    public static void info(String message){
        if(SettingsReader.getBooleanValue(DO_LOGS)) {
            FileSize freeMemory = FileSize.getMoreComfortableImageSizeFromBytes(Runtime.getRuntime().freeMemory());

            logger.info(message + " (" + freeMemory.size() + " " + freeMemory.format() + " free)");
            writeLogsIntoFile(message);
        }
    }

    public static void error(@NotNull Exception e){
        if(SettingsReader.getBooleanValue(DO_LOGS)) {
            logger.severe(e.toString());
            writeLogsIntoFile(e.toString());

            e.printStackTrace();
            ErrorDialog.show(e);
        }
    }

    private static void writeLogsIntoFile(String log){
        try {
            FileWriter fileWriter = new FileWriter(LOGS_PATH, true);
            fileWriter.append(log).append("\n");
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e){
            System.err.println(e);;
        }
    }
}
