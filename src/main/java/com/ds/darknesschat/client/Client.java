package com.ds.darknesschat.client;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.chat.messages.MessageUtils;
import com.ds.darknesschat.chat.messages.MessagesGenerator;
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
import java.util.List;
import java.util.Random;

import static com.ds.darknesschat.Constants.CLIENT_AND_SERVER_UPDATE_DELAY_IN_MILLIS;
import static com.ds.darknesschat.Constants.DISCONNECT_COMMAND;
import static com.ds.darknesschat.client.ClientConstants.*;

public class Client implements Runnable{
    private final Socket socket;
    private final Server server;
    private final DataOutputStream out;
    private final DataInputStream in;
    private String clientName;
    private Color clientNameColor;
    private boolean canClientAcceptRequestsFromServer = true;

    public Client(@NotNull Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        createRandomNameColor();
    }

    private void createRandomNameColor() {
        String color = Utils.getRandomStringFromFile(Main.class.getResourceAsStream("settings/nicknames_colors.txt"));

        Log.info("Selected color for user is " + color);

        clientNameColor = Color.web(color);
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

    @Override
    public void run() {
        server.sendStringMessageToAll(MessagesGenerator.generateUserTextMessage(StringGetterWithCurrentLanguage.getString(StringsConstants.USER) + " " + clientName + " " +
                StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECTED_TO_THE_CHAT), true, clientNameColor, server.getClientsCount()));
        server.sendStringMessageToAll(getJSONRecordWithUsersCount());

        while(canClientAcceptRequestsFromServer){
            try {
                String message = in.readUTF();

                if (!message.equals(DISCONNECT_COMMAND)) {
                    if(Utils.isStringAreJSON(message)){
                        JSONObject jsonObject = new JSONObject(message);

                        if(jsonObject.has(CLIENT_SENT_IMAGE)){
                            server.sendStringMessageToAll(MessagesGenerator.generateUserImageMessage(jsonObject.toString(), clientNameColor, server.getClientsCount()));
                        }
                    }else{
                        server.sendStringMessageToAll(MessagesGenerator.generateUserTextMessage(message, false, clientNameColor, server.getClientsCount()));
                    }
                }else
                    break;

                Thread.sleep(CLIENT_AND_SERVER_UPDATE_DELAY_IN_MILLIS);
            }catch (Exception e){
                Log.error(e);

                break;
            }
        }

        close();
    }

    public void close(){
        try {
            canClientAcceptRequestsFromServer = false;

            socket.close();
            server.deleteClient(this);

            server.sendStringMessageToAll(MessagesGenerator.generateUserTextMessage(clientName + " " + StringGetterWithCurrentLanguage.getString(StringsConstants.USER_DISCONNECTED), true, clientNameColor, server.getClientsCount()));
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
