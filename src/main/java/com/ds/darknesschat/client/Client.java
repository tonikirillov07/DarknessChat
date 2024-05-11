package com.ds.darknesschat.client;

import com.ds.darknesschat.server.Server;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Random;

import static com.ds.darknesschat.Constants.DISCONNECT_COMMAND;
import static com.ds.darknesschat.client.ClientKeys.*;

public class Client implements Runnable{
    private final Socket socket;
    private final Server server;
    private final DataOutputStream out;
    private final DataInputStream in;
    private String clientName;
    private Color clientNameColor;

    public Client(@NotNull Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        createRandomNameColor();
    }

    private void createRandomNameColor() {
        Random random = new Random();
        int red = random.nextInt(30, 255);
        int green = random.nextInt(30, 255);
        int blue = random.nextInt(30, 255);

        clientNameColor = Color.rgb(red, green, blue);
    }

    public void sendStringMessage(String message){
        try {
            Log.info(message);
            out.writeUTF(message);
            out.flush();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private String getJSONRecordWithUsersCount(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CLIENTS_COUNT, server.getClientsCount());

        return jsonObject.toString();
    }

    private String getJSONRecordWithUsersNameColor(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CLIENT_NAME_COLOR, clientNameColor.getRed() + ", " + clientNameColor.getGreen() + ", " + clientNameColor.getBlue());

        return jsonObject.toString();
    }

    @Override
    public void run() {
        server.sendStringMessageToAll(generateUserMessage(StringGetterWithCurrentLanguage.getString(StringsConstants.USER) + " " + clientName + " " +
                StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECTED_TO_THE_CHAT), true));
        server.sendStringMessageToAll(getJSONRecordWithUsersCount());

        while(!socket.isClosed()){
            try {
                String message = in.readUTF();

                if (!message.equals(DISCONNECT_COMMAND)) {
                    server.sendStringMessageToAll(generateUserMessage(message, false));
                }else
                    break;

                Thread.sleep(100);
            }catch (Exception e){
                Log.error(e);
            }
        }

        close();
    }

    private String generateUserMessage(String message, boolean fromServer){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CLIENT_MESSAGE, message);
        jsonObject.put(CLIENT_NAME_COLOR, fromServer ? presetColorInRGBSString(Color.WHITE) : presetColorInRGBSString(clientNameColor));
        jsonObject.put(CLIENTS_COUNT, server.getClientsCount());

        return jsonObject.toString();
    }

    private @NotNull String presetColorInRGBSString(@NotNull Color color){
        return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
    }

    private void close(){
        try {
            socket.close();
            server.deleteClient(this);

            server.sendStringMessageToAll(generateUserMessage(clientName + " " + StringGetterWithCurrentLanguage.getString(StringsConstants.USER_DISCONNECTED), true));
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
