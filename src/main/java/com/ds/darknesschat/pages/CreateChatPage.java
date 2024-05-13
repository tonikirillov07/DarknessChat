package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.server.Server;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.InputTypes;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

import static com.ds.darknesschat.Constants.*;

public class CreateChatPage extends Page{
    private AdditionalTextField chatPortTextField;
    private Label currentChatIPLabel;

    protected CreateChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        initTextField();
        initChatAddressLabel();
        initButtons();
        createDeveloperLabelInBottom();
        getTile().applyAlphaWithUserSettings(getUser());

        currentChatIPLabel.setText(Utils.getLocalIP4Address() + ":" + chatPortTextField.getText());
    }

    private void initButtons() {
        try {
            AdditionalButton nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 308d, 41d, new com.ds.darknesschat.utils.Color(164, 62, 62), WHITE_COLOR, getUser().getId());
            nextButton.addAction(this::createServer);
            VBox.setMargin(nextButton, new Insets(200d, 40d, 0, 40d));
            addNodeToTile(nextButton);

            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 308d, 41d, new com.ds.darknesschat.utils.Color(255, 255, 255), BLACK_COLOR, getUser().getId());
            backButton.addAction(this::goToPreviousPage);
            VBox.setMargin(backButton, new Insets(10d, 40d, 30d, 40d));
            addNodeToTile(backButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createServer() {
        try{
            if(!Utils.isPortNormal(Integer.parseInt(chatPortTextField.getText()))) {
                ErrorDialog.show(new Exception("Invalid port! Try write another port"));

                return;
            }

            Server server = new Server();
            if(!server.create(Integer.parseInt(chatPortTextField.getText())))
                return;

            openChatPage(server);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void openChatPage(Server server){
        try {
            if (Utils.isFieldsIsNotEmpty(new AdditionalTextField[]{chatPortTextField})) {
                ChatPage chatPage = new ChatPage(this, getContentVbox(), Utils.getLocalIP4Address() + ":" + chatPortTextField.getText(), true, getUser(), server);
                chatPage.open();
                if(!chatPage.connectToServer(Utils.getLocalIP4Address() + ":" + chatPortTextField.getText()))
                    chatPage.goToPreviousPage();
            } else
                chatPortTextField.setError(getUser().getId());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initChatAddressLabel() {
        try {
            currentChatIPLabel = new Label(StringGetterWithCurrentLanguage.getString(StringsConstants.CURRENT_CHAT_IP) + "192.168.0.102: 2020");
            currentChatIPLabel.setTextFill(Color.web("#0500FF"));
            currentChatIPLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));
            addNodeToTile(currentChatIPLabel);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initTextField() {
        try {
            chatPortTextField = new AdditionalTextField(308d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_PORT), Utils.getImage("bitmaps/icons/others/digits.png"), false);
            chatPortTextField.setInputType(InputTypes.NUMERIC);
            chatPortTextField.getTextField().setText(String.valueOf(new Random().nextInt(MIN_PORT_VALUE, MAX_PORT_VALUE)));
            chatPortTextField.addOnTextTyping(currentText -> {
                chatPortTextField.getTextField().setStyle(chatPortTextField.getTextField().getStyle() + "; -fx-text-fill: " + (Utils.isPortNormal(Utils.stringCanBeConvertedToInt(currentText) ? Integer.parseInt(currentText): MIN_PORT_VALUE) ? "white; ": "red; "));
                currentChatIPLabel.setText(Utils.getLocalIP4Address() + ":" + currentText);
            });
            VBox.setMargin(chatPortTextField, new Insets(50d, 40d, 30d, 40d));
            addNodeToTile(chatPortTextField);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
