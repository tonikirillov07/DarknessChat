package com.ds.darknesschat.utils.log;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.util.logging.Logger;

import static com.ds.darknesschat.Constants.LOGS_PATH;

public final class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    public static void info(String message){
        logger.info(message);
        writeLogsIntoFile(message);
    }

    public static void error(@NotNull Exception e){
        logger.severe(e.toString());
        writeLogsIntoFile(e.toString());

        e.printStackTrace();
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
