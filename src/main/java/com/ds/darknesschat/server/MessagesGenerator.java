package com.ds.darknesschat.server;

import com.ds.darknesschat.utils.Utils;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.ds.darknesschat.client.ClientConstants.*;

public class MessagesGenerator {
    public static String generateUserTextMessage(String message, boolean fromServer, Color clientNameColor, int clientsCount){
        JSONObject userStringMessageJsonObject = new JSONObject();
        userStringMessageJsonObject.put(CLIENT_MESSAGE, message);
        userStringMessageJsonObject.put(CLIENT_NAME_COLOR, fromServer ? presentColorInRGBSString(Color.WHITE) : presentColorInRGBSString(clientNameColor));
        userStringMessageJsonObject.put(CLIENTS_COUNT, clientsCount);

        return userStringMessageJsonObject.toString();
    }

    public static String generateUserImageMessage(String jsonObject, Color clientNameColor, int clientsCount){
        JSONObject userImageMessageJSONObject = new JSONObject(jsonObject);
        userImageMessageJSONObject.put(CLIENT_NAME_COLOR, presentColorInRGBSString(clientNameColor));
        userImageMessageJSONObject.put(CLIENTS_COUNT, clientsCount);

        return userImageMessageJSONObject.toString();
    }

    private static @NotNull String presentColorInRGBSString(@NotNull Color color){
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return decimalFormat.format(color.getRed()) + ", " + decimalFormat.format(color.getGreen()) + ", " + decimalFormat.format(color.getBlue());
    }

    public static @NotNull String generateUserStringMessage(String userName, String userMessage){
        return userName + ": " + userMessage + " (" + Utils.getCurrentTime() + ")";
    }
}
