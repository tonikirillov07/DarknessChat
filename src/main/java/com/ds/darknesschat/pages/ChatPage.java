package com.ds.darknesschat.pages;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.additionalNodes.ImageButton;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.server.Server;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.user.UserRecentChats;
import com.ds.darknesschat.utils.*;
import com.ds.darknesschat.utils.dialogs.ConfirmDialog;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.UTFDataFormatException;
import java.net.ConnectException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Objects;

import static com.ds.darknesschat.Constants.*;
import static com.ds.darknesschat.chat.ImageMessageUtils.createMessageImageView;
import static com.ds.darknesschat.chat.MessageUtils.createMessageLabel;
import static com.ds.darknesschat.client.ClientConstants.*;
import static com.ds.darknesschat.server.MessagesGenerator.generateUserStringMessage;
import static com.ds.darknesschat.utils.Utils.extractPortAndAddressFromAddress;
import static com.ds.darknesschat.utils.languages.StringsConstants.USERS_IN_CHAT;

public class ChatPage extends Page{
    private VBox messagesContent;
    private ScrollPane messagesScrollPane;
    private AdditionalTextField messageAdditionalTextField;
    private Label usersCountLabel;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final Server server;
    private boolean isClientCanAcceptRequestsFromServer = true;

    protected ChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user, Server server) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
        this.server = server;
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

        getStage().setOnCloseRequest(windowEvent -> {
            disconnect();

            if(server != null)
                server.close();
        });
    }

    @Override
    public void onClose() {
        super.onClose();

        getStage().setOnCloseRequest(windowEvent -> {});

        if(isClientCanAcceptRequestsFromServer)
            disconnect();
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
            UserRecentChats.addUserRecentChat(getUser().getId(), address);
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
                    while (isClientCanAcceptRequestsFromServer) {
                        String message = in.readUTF();

                        if (Utils.isStringAreJSON(message)) {
                            JSONObject jsonObject = new JSONObject(message);
                            Platform.runLater(() -> {
                                if(jsonObject.has(CLIENTS_COUNT))
                                    usersCountLabel.setText(String.valueOf(jsonObject.getInt(CLIENTS_COUNT)));

                                if (jsonObject.has(CLIENT_NAME_COLOR)){
                                    int[] userColorComponents = Utils.parseColorRGBFromString(jsonObject.getString(CLIENT_NAME_COLOR));

                                    if(jsonObject.has(CLIENT_SENT_IMAGE)) {
                                        createMessageImageView(jsonObject.getJSONArray(CLIENT_SENT_IMAGE), jsonObject.getString(CLIENT_NAME), javafx.scene.paint.Color.rgb(userColorComponents[0], userColorComponents[1], userColorComponents[2]), jsonObject.getString(CLIENT_MESSAGE), messagesContent, messagesScrollPane, getUser().getId());
                                    }else {
                                        String currentMessage = jsonObject.getString(CLIENT_MESSAGE);
                                        if (!Utils.isStringAreJSON(currentMessage)) {
                                            createMessageLabel(currentMessage, javafx.scene.paint.Color.rgb(userColorComponents[0], userColorComponents[1], userColorComponents[2]), messagesScrollPane, messagesContent, getUser().getId());
                                        }
                                    }
                                }
                            });

                        }

                        Thread.sleep(CLIENT_AND_SERVER_UPDATE_DELAY_IN_MILLIS);
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
            isClientCanAcceptRequestsFromServer = false;

            in.close();
            out.close();
            socket.close();
        }catch (Exception e){
            Log.error(e);
        }
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

            ImageButton attachmentButton = new ImageButton(52d, 50d, Utils.getImage("bitmaps/icons/others/attachment.png"), getUser().getId());
            attachmentButton.setOnAction(this::onAttachmentButton);

            AdditionalButton sendButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SEND), 137d, 49d, new Color(164, 62, 62), WHITE_COLOR, getUser().getId());
            sendButton.setMinWidth(137d);
            sendButton.addAction(() -> sendMessage(messageAdditionalTextField.getText()));

            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 137d, 49d, WHITE_COLOR, BLACK_COLOR, getUser().getId());
            backButton.addAction(this::leaveTheChat);

            hBox.getChildren().addAll(attachmentButton, sendButton, backButton);

            tile.addChild(hBox);
            addNodeToPage(tile);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void onAttachmentButton() {
        try{
            FileChooser fileChooserImage = new FileChooser();
            fileChooserImage.setTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.SELECT_YOUR_IMAGE_FOR_SEND));
            fileChooserImage.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.IMAGES)), "*.png*", "*.jpg*", "*.jpeg*"),
                    new FileChooser.ExtensionFilter(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.EVERYTHING)), "*.*")
            );

            String lastPathInDatabase = DatabaseService.getValue(DatabaseConstants.USER_LAST_PATH_IN_ATTACHMENTS, getUser().getId());
            if(lastPathInDatabase != null){
                File lastDirectoryPath = new File(lastPathInDatabase);

                if(lastDirectoryPath.exists())
                    fileChooserImage.setInitialDirectory(lastDirectoryPath);
            }

            File selectedFile = fileChooserImage.showOpenDialog(getStage());

            if(selectedFile != null){
                DatabaseService.changeValue(DatabaseConstants.USER_LAST_PATH_IN_ATTACHMENTS, selectedFile.getParent(), getUser().getId());
                sendImageToServer(ImageUtils.convertImageFileToBytes(ImageUtils.compressImage(selectedFile, Utils.getFileFormat(selectedFile.getName()))), messageAdditionalTextField.getText());
            }
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

                sendMessageToServer(generateUserStringMessage(getUser().getUserName(), message));
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

    private void sendImageToServer(byte[] image, String message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLIENT_MESSAGE, message);
            jsonObject.put(CLIENT_NAME, getUser().getUserName());
            jsonObject.put(CLIENT_SENT_IMAGE, image);

            int messageSize = jsonObject.toString().getBytes().length;
            double messageSizeInMegaBytes = Double.parseDouble(new DecimalFormat("#.###").format(messageSize * Math.pow(10, -6)));

            if(messageSize > MESSAGE_SIZE_LIMIT_IN_BYTES) {
                boolean showSizeInMegaBytes = messageSizeInMegaBytes >= 1d;

                ErrorDialog.show(new UTFDataFormatException(StringGetterWithCurrentLanguage.getString(StringsConstants.YOUR_MESSAGE_IS_TOO_BIG) + " "
                        + MESSAGE_SIZE_LIMIT_IN_BYTES + " bytes. " + StringGetterWithCurrentLanguage.getString(StringsConstants.YOUR_MESSAGE_SIZE) + " "
                        + (showSizeInMegaBytes ? messageSizeInMegaBytes : messageSize) + (showSizeInMegaBytes ? " megaBytes" : " bytes")));
                return;
            }

            messageAdditionalTextField.getTextField().clear();
            sendMessageToServer(jsonObject.toString());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void leaveTheChat(){
        if(ConfirmDialog.show(StringGetterWithCurrentLanguage.getString(server == null ? StringsConstants.DO_YOU_REALLY_WANT_TO_LEAVE_IF_NO_HOST: StringsConstants.DO_YOU_REALLY_WANT_TO_LEAVE_IF_HOST))) {
            disconnect();
            goToPreviousPage();

            if(server != null)
                server.close();
        }
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
