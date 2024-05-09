package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatPage extends Page{
    private VBox messagesContent;
    private ScrollPane messagesScrollPane;

    protected ChatPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        deleteDefaultPagePaddings();
        getContentVbox().setPadding(new Insets(0, 90d, 0, 90d));
        loadDefaultTileSettings();
        createMessagesScrollPane();
        createMessageTextFieldTile();
        createDeveloperLabelInBottom();

        getTile().setMaxHeight(415d);
    }

    private void createMessageTextFieldTile() {
        try{
            Tile tile = new Tile(756d, 81d);
            tile.setPadding(new Insets(20d));
            tile.setMaxHeight(480d);
            tile.setAlignment(Pos.CENTER_LEFT);
            tile.animate();
            tile.setSpacing(20d);

            HBox hBox = new HBox();
            HBox.setHgrow(hBox, Priority.ALWAYS);
            hBox.setSpacing(15d);

            AdditionalTextField messageAdditionalTextField = new AdditionalTextField(374d, 49d, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_MESSAGE),
                    Utils.getImage("bitmaps/icons/others/message.png"), false);
            hBox.getChildren().add(messageAdditionalTextField);

            AdditionalButton sendButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SEND), 137d, 49d, new Color(164, 62, 62), new Color(255, 255, 255));
            sendButton.addAction(() -> sendMessage(messageAdditionalTextField));
            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 137d, 49d, new Color(255, 255, 255), new Color(0, 0, 0));
            backButton.addAction(this::leaveTheChat);

            hBox.getChildren().addAll(sendButton, backButton);

            tile.addChild(hBox);
            addNodeToPage(tile);
        }catch (Exception e){
            Log.error(e);
        }
    }

    @Contract(pure = true)
    private void sendMessage(@NotNull AdditionalTextField additionalTextField){
        if(!additionalTextField.getText().isEmpty()){
            Label label = new Label(additionalTextField.getText());
            label.setTextFill(javafx.scene.paint.Color.WHITE);
            label.setWrapText(true);
            label.setMaxWidth(756d);
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_PATH), 14d));

            messagesContent.getChildren().add(label);
            Utils.addFadeTransitionToNode(label);
            messagesScrollPane.setVvalue(1);

            //additionalTextField.getTextField().clear();
        }else
            additionalTextField.setError();
    }

    private void leaveTheChat(){
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
