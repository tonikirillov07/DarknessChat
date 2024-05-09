package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.utils.InputTypes;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CreateChatPage extends Page{
    protected CreateChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        initTextField();
        initChatAddressLabel();
        initButtons();
        createDeveloperLabelInBottom();
    }

    private void initButtons() {
        try {
            AdditionalButton nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 308d, 41d, new com.ds.darknesschat.utils.Color(164, 62, 62), new com.ds.darknesschat.utils.Color(255, 255, 255));
            VBox.setMargin(nextButton, new Insets(200d, 40d, 0, 40d));
            addNodeToTile(nextButton);

            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 308d, 41d, new com.ds.darknesschat.utils.Color(255, 255, 255), new com.ds.darknesschat.utils.Color(0, 0, 0));
            backButton.addAction(this::goToPreviousPage);
            VBox.setMargin(backButton, new Insets(10d, 40d, 30d, 40d));
            addNodeToTile(backButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initChatAddressLabel() {
        try {
            Label addressLabel = new Label(StringGetterWithCurrentLanguage.getString(StringsConstants.CURRENT_CHAT_IP) + "192.168.0.102: 2020");
            addressLabel.setTextFill(Color.web("#0500FF"));
            addressLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));
            addNodeToTile(addressLabel);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initTextField() {
        try {
            AdditionalTextField additionalPortTextField = new AdditionalTextField(308d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_PORT), Utils.getImage("bitmaps/icons/others/digits.png"), false);
            additionalPortTextField.setInputType(InputTypes.NUMERIC);
            VBox.setMargin(additionalPortTextField, new Insets(50d, 40d, 30d, 40d));
            addNodeToTile(additionalPortTextField);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
