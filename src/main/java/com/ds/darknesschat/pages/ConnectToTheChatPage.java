package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.ds.darknesschat.Constants.*;

public class ConnectToTheChatPage extends Page{
    private AdditionalTextField additionalChatAddressTextField;
    private AdditionalButton nextButton;
    private CheckBox checkBox;

    public ConnectToTheChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        initTextField();
        initCheckBox();
        initButtons();
        createDeveloperLabelInBottom();
        getTile().applyAlphaWithUserSettings(getUser());
    }

    public void setAddressValue(String addressValue){
        if(additionalChatAddressTextField != null)
            additionalChatAddressTextField.getTextField().setText(addressValue);
        else
            Log.error(new Exception("Field is null and value " + addressValue + " cannot be set"));
    }

    private void initButtons() {
        try{
            nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 308d, 41d, new com.ds.darknesschat.utils.Color(164, 62, 62), WHITE_COLOR, getUser().getId());
            nextButton.addAction(this::onConnectToTheChatButtonAction);
            VBox.setMargin(nextButton, new Insets(200d, 40d, 0, 40d));
            addNodeToTile(nextButton);

            checkEnteredAddress(additionalChatAddressTextField.getText());

            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 308d, 41d, WHITE_COLOR, BLACK_COLOR, getUser().getId());
            backButton.addAction(this::goToPreviousPage);
            VBox.setMargin(backButton, new Insets(10d, 40d, 30d, 40d));
            addNodeToTile(backButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void onConnectToTheChatButtonAction(){
        if(additionalChatAddressTextField != null){
            if(Utils.isFieldsIsNotEmpty(new AdditionalTextField[]{additionalChatAddressTextField})){
                if(checkBox.isSelected())
                    DatabaseService.changeValue(DatabaseConstants.REMEMBERED_CHAT_ADDRESS_ROW, additionalChatAddressTextField.getText(), getUser().getId());
                else
                    DatabaseService.setNull(DatabaseConstants.REMEMBERED_CHAT_ADDRESS_ROW, getUser().getId());

                ChatPage chatPage = new ChatPage(this, getContentVbox(), additionalChatAddressTextField.getText(), true, getUser(), null);
                if(!chatPage.tryConnectToServer(additionalChatAddressTextField.getText()))
                    return;

                chatPage.open();
            }else {
                additionalChatAddressTextField.setError(getUser().getId());
            }
        }else{
            initTextField();
        }
    }

    private void initCheckBox() {
        try {
            checkBox = new CheckBox(StringGetterWithCurrentLanguage.getString(StringsConstants.REMEMBER_THIS_ADDRESS));
            checkBox.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));
            checkBox.setTextFill(Color.WHITE);
            checkBox.setSelected(DatabaseService.getValue(DatabaseConstants.REMEMBERED_CHAT_ADDRESS_ROW, getUser().getId()) != null);
            VBox.setMargin(checkBox, new Insets(10d, 0d, 0, 48d));

            HBox checkBoxHbox = new HBox();
            checkBoxHbox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(checkBoxHbox, new Insets(0d, 40d, 0, 48d));
            checkBoxHbox.getChildren().add(checkBox);

            addNodeToTile(checkBoxHbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initTextField() {
        try {
            additionalChatAddressTextField = new AdditionalTextField(308d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_CHAT_ADDRESS), Utils.getImage("bitmaps/icons/others/digits.png"), false);
            additionalChatAddressTextField.addOnTextTyping(this::checkEnteredAddress);
            VBox.setMargin(additionalChatAddressTextField, new Insets(50d, 40d, 30d, 40d));
            addNodeToTile(additionalChatAddressTextField);

            String rememberedAddress = DatabaseService.getValue(DatabaseConstants.REMEMBERED_CHAT_ADDRESS_ROW, getUser().getId());
            if(rememberedAddress != null)
                additionalChatAddressTextField.getTextField().setText(rememberedAddress);

        }catch (Exception e){
            Log.error(e);
        }
    }

    private void checkEnteredAddress(String currentText) {
        boolean serverAddressMeetsToRegexes = Utils.stringMeetsAtLeastOneRegex(SERVER_ADDRESS_REGEXES, currentText);

        additionalChatAddressTextField.getTextField().setStyle("-fx-text-fill: " + (serverAddressMeetsToRegexes ? "white; " : "red; "));
        if(nextButton != null)
            nextButton.setDisable(!serverAddressMeetsToRegexes);
    }
}
