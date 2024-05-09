package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
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

public class ConnectToTheChatPage extends Page{
    private AdditionalTextField additionalPortTextField;

    protected ConnectToTheChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        initTextField();
        initCheckBox();
        initButtons();
        createDeveloperLabelInBottom();
    }

    private void initButtons() {
        try{
            AdditionalButton nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 308d, 41d, new com.ds.darknesschat.utils.Color(164, 62, 62), new com.ds.darknesschat.utils.Color(255, 255, 255));
            nextButton.addAction(this::onConnectToTheChatButtonAction);
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

    public void onConnectToTheChatButtonAction(){
        if(additionalPortTextField != null){
            if(Utils.isFieldIsNotEmpty(new AdditionalTextField[]{additionalPortTextField})){
                System.out.println(1);
            }else {
                additionalPortTextField.setError();
            }
        }else{
            initTextField();
        }
    }

    private void initCheckBox() {
        try {
            CheckBox checkBox = new CheckBox(StringGetterWithCurrentLanguage.getString(StringsConstants.REMEMBER_THIS_ADDRESS));
            checkBox.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));
            checkBox.setTextFill(Color.WHITE);
            VBox.setMargin(checkBox, new Insets(10d, 0d, 0, 40d));

            HBox checkBoxHbox = new HBox();
            checkBoxHbox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(checkBoxHbox, new Insets(0d, 40d, 0, 40d));
            checkBoxHbox.getChildren().add(checkBox);

            addNodeToTile(checkBoxHbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initTextField() {
        try {
            additionalPortTextField = new AdditionalTextField(308d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_CHAT_ADDRESS), Utils.getImage("bitmaps/icons/others/digits.png"), false);
            VBox.setMargin(additionalPortTextField, new Insets(50d, 40d, 30d, 40d));
            addNodeToTile(additionalPortTextField);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
