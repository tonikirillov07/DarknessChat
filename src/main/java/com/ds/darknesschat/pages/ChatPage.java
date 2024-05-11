package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.server.Server;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.ChatAddress;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.ds.darknesschat.Constants.*;
import static com.ds.darknesschat.client.ClientKeys.*;
import static com.ds.darknesschat.utils.languages.StringsConstants.USERS_IN_CHAT;

public class ChatPage extends Page{
    private VBox messagesContent;
    private ScrollPane messagesScrollPane;
    private AdditionalTextField messageAdditionalTextField;
    private Label usersCountLabel;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Server server;

    protected ChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user, Server server) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        deleteDefaultPagePaddings();
        getContentVbox().setPadding(new Insets(0, 90d, 0, 90d));
        loadDefaultTileSettings();
        createUserCountLabel();
        createMessagesScrollPane();
        createMessageTextFieldTile();
        createDeveloperLabelInBottom();

        getTile().setMaxHeight(415d);
        getTile().applyAlphaWithUserSettings(getUser());
        Utils.addActionToNode(getTile().getTitleLabel(), () -> Utils.copyStringToClipboard(getTitle()), getUser().getId());
    }

    private void createUserCountLabel() {
        try {
            usersCountLabel = new Label("1");
            usersCountLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(FONT_BOLD_ITALIC_PATH), 16d));
            usersCountLabel.setTextFill(javafx.scene.paint.Color.WHITE);

            Tooltip tooltip = new Tooltip(StringGetterWithCurrentLanguage.getString(USERS_IN_CHAT) + " " + usersCountLabel.getText());
            tooltip.setFont(Font.loadFont(Main.class.getResourceAsStream(FONT_BOLD_ITALIC_PATH), 15d));
            tooltip.setWrapText(true);
            usersCountLabel.setTooltip(tooltip);

            ImageView imageView = new ImageView(Utils.getImage("bitmaps/icons/others/users.png"));
            imageView.setFitWidth(32d);
            imageView.setFitHeight(32d);
            imageView.setEffect(new DropShadow());

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(15d);
            hBox.getChildren().addAll(imageView, usersCountLabel);

            addNodeToTile(hBox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public boolean connectToServer(String address){
        try{
            ChatAddress chatAddress = extractPortAndAddressFromAddress(address);

            assert chatAddress != null;
            socket = new Socket(chatAddress.address(), chatAddress.port());

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            sendNameToServer();
            if(!checkCanUserConnect())
                return false;

            new Thread(() -> {
                try {
                    while (!socket.isClosed() & isOpen()) {
                        String message = in.readUTF();

                        if (Utils.isStringAreJSON(message)) {
                            JSONObject jsonObject = new JSONObject(message);

                            Platform.runLater(() -> {
                                usersCountLabel.setText(String.valueOf(jsonObject.getInt(CLIENTS_COUNT)));

                                if (jsonObject.has(CLIENT_NAME_COLOR)) {
                                    int[] userColorComponents = Utils.parseColorRGBFromString(jsonObject.getString(CLIENT_NAME_COLOR));
                                    createMessageLabel(jsonObject.getString(CLIENT_MESSAGE), javafx.scene.paint.Color.rgb(userColorComponents[0], userColorComponents[1], userColorComponents[2]));
                                }
                            });

                        }

                        Thread.sleep(100);
                    }
                }catch (Exception e){
                    Log.error(e);
                }

                disconnect();
            }).start();

            return true;
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }

    private boolean checkCanUserConnect() {
        try {
            String canConnectJSON = in.readUTF();
            if (Utils.isStringAreJSON(canConnectJSON)) {
                if (!Boolean.parseBoolean(Utils.getStringFromJSON(canConnectJSON, CAN_CONNECT))) {
                    ErrorDialog.show(new ConnectException(Utils.getStringFromJSON(canConnectJSON, REASON)));

                    return false;
                }
            }
        }catch (Exception e){
            Log.error(e);

            return false;
        }

        return true;
    }

    private void sendNameToServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLIENT_NAME, getUser().getUserName());
            sendMessageToServer(jsonObject.toString());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void disconnect() {
        try {
            out.writeUTF(DISCONNECT_COMMAND);

            in.close();
            out.close();
            socket.close();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable ChatAddress extractPortAndAddressFromAddress(@NotNull String address){
        try {
            int colonIndex = address.indexOf(":") + 1;
            StringBuilder portBuilder = new StringBuilder();
            StringBuilder addressBuilder = new StringBuilder();

            for (int i = colonIndex; i < address.length(); i++) {
                portBuilder.append(address.charAt(i));
            }

            for (int i = 0; i < colonIndex - 1; i++) {
                addressBuilder.append(address.charAt(i));
            }

            int port = Integer.parseInt(portBuilder.toString());
            String parsedAddress = addressBuilder.toString();

            return new ChatAddress(parsedAddress, port);
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private void createMessageTextFieldTile() {
        try{
            Tile tile = new Tile(756d, 81d, 0.43f);
            tile.setPadding(new Insets(20d));
            tile.setMaxHeight(480d);
            tile.setAlignment(Pos.CENTER_LEFT);
            tile.applyAlphaWithUserSettings(getUser());
            tile.animate(getUser().getId());
            tile.setSpacing(20d);

            HBox hBox = new HBox();
            HBox.setHgrow(hBox, Priority.ALWAYS);
            hBox.setSpacing(15d);

            messageAdditionalTextField = new AdditionalTextField(374d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_MESSAGE),
                    Utils.getImage("bitmaps/icons/others/message.png"), false);
            messageAdditionalTextField.addOnEnterKeyPressed(() -> sendMessage(messageAdditionalTextField.getText()));
            hBox.getChildren().add(messageAdditionalTextField);

            AdditionalButton sendButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SEND), 137d, 49d, new Color(164, 62, 62), WHITE_COLOR, getUser().getId());
            sendButton.addAction(() -> sendMessage(messageAdditionalTextField.getText()));

            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 137d, 49d, WHITE_COLOR, BLACK_COLOR, getUser().getId());
            backButton.addAction(this::leaveTheChat);

            hBox.getChildren().addAll(sendButton, backButton);

            tile.addChild(hBox);
            addNodeToPage(tile);
        }catch (Exception e){
            Log.error(e);
        }
    }

    @Contract(pure = true)
    private void sendMessage(@NotNull String message){
        try {
            if (!message.trim().isEmpty()) {
                if (messageAdditionalTextField != null)
                    messageAdditionalTextField.getTextField().clear();

                sendMessageToServer(getUser().getUserName() + ": " + message + " (" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ")");
            } else
                messageAdditionalTextField.setError(getUser().getId());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void sendMessageToServer(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createMessageLabel(String text, javafx.scene.paint.Color userNameColor){
        try {
            Label userLabel = createLabel(Utils.extractUserNameFromMessage(text), userNameColor);
            Label messageLabel = createLabel(Utils.extractMessageTextFromMessage(text), javafx.scene.paint.Color.WHITE);

            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().addAll(userLabel, messageLabel);

            Label label = new Label();
            label.setGraphic(textFlow);

            messagesContent.getChildren().add(label);
            Animations.addFadeTransitionToNode(label, getUser().getId());
            messagesScrollPane.setVvalue(1);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @NotNull Label createLabel(String text, javafx.scene.paint.Color color){
        Label label = new Label(text);
        label.setTextFill(color);
        label.setWrapText(true);
        label.setMaxWidth(756d);
        label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_PATH), 14d));

        return label;
    }

    private void leaveTheChat(){
        disconnect();
        goToPreviousPage();
    }

    private void createMessagesScrollPane() {
        try {
            messagesScrollPane = new ScrollPane();
            messagesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            messagesScrollPane.setPannable(true);
            VBox.setMargin(messagesScrollPane, new Insets(10d));
            VBox.setVgrow(messagesScrollPane, Priority.ALWAYS);

            messagesContent = new VBox();
            messagesContent.setSpacing(15d);
            messagesContent.setMaxWidth(756d);
            messagesScrollPane.setContent(messagesContent);

            addNodeToTile(messagesScrollPane);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
