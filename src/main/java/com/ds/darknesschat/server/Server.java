package com.ds.darknesschat.server;

import com.ds.darknesschat.client.Client;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.ds.darknesschat.client.ClientKeys.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket client;
    private final List<Client> clients = new ArrayList<>();

    public boolean create(int port){
        try {
            serverSocket = new ServerSocket(port);

            Log.info("Created server with address " + Utils.getLocalIP4Address() + ":" + port);

            new Thread(() -> {
                while (!serverSocket.isClosed()){
                    try {
                        client = serverSocket.accept();

                        Client newClient = new Client(client, this);

                        String sentClientMessage = newClient.getIn().readUTF();
                        if(Utils.isStringAreJSON(sentClientMessage)) {
                            String clientName = Utils.getStringFromJSON(sentClientMessage, CLIENT_NAME);
                            newClient.setClientName(clientName);

                            if(!isUserExists(clientName)) {
                                clients.add(newClient);
                                new Thread(newClient).start();

                                sendCanConnectInfoToClient(newClient, TRUE, NONE_REASON);
                                Log.info("New client " + client + " connected");
                            }else {
                                sendCanConnectInfoToClient(newClient, FALSE, THIS_USER_ALREADY_IN_CHAT_REASON);
                                Log.error(new Exception("User is exists and couldn't be connected"));
                            }
                        }
                    } catch (IOException e) {
                        Log.error(e);
                    }
                }
            }).start();

            return true;
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }

    private void sendCanConnectInfoToClient(@NotNull Client client, String can, String reason){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CAN_CONNECT, can);
            jsonObject.put(REASON, reason);

            DataOutputStream dataOutputStream = client.getOut();
            dataOutputStream.writeUTF(jsonObject.toString());
            dataOutputStream.flush();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private boolean isUserExists(String checkingName){
        boolean isExists = false;

        for (Client currentClient : clients) {
            if(currentClient.getClientName().equals(checkingName)){
                isExists = true;
                break;
            }
        }

        return isExists;
    }

    public void close(){
        try {
            serverSocket.close();
            client.close();

            Log.info("Server closed");
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void sendStringMessageToAll(String message){
        clients.forEach(currentClient -> currentClient.sendStringMessage(message));
    }

    public void deleteClient(Client client){
        try {
            boolean isClientRemoved = clients.remove(client);

            Log.info("Remove client " + client + " result is " + isClientRemoved);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public int getClientsCount(){
        return clients.size();
    }
}
