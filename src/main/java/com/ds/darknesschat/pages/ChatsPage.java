package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.ChatTile;
import com.ds.darknesschat.additionalNodes.DeveloperLabel;
import com.ds.darknesschat.additionalNodes.ImageButton;
import com.ds.darknesschat.pages.settingsPages.SettingsMainPage;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatsPage extends Page{
    private HBox contentHbox;

    protected ChatsPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        deleteDefaultPagePaddings();
        getContentVbox().setAlignment(Pos.CENTER_LEFT);

        initContentHbox();
        createSidePanel();
        initDeveloperAndButtonsVbox();
    }

    private void initDeveloperAndButtonsVbox() {
        try{
            HBox secondPartHbox = new HBox();
            secondPartHbox.setAlignment(Pos.TOP_RIGHT);
            HBox.setHgrow(secondPartHbox, Priority.ALWAYS);
            addToContentHbox(secondPartHbox);

            VBox buttonsAndDeveloperLabelVbox = new VBox();
            buttonsAndDeveloperLabelVbox.setAlignment(Pos.TOP_RIGHT);
            secondPartHbox.getChildren().add(buttonsAndDeveloperLabelVbox);

            createDeveloperLabelArea(buttonsAndDeveloperLabelVbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createDeveloperLabelArea(@NotNull VBox buttonsAndDeveloperLabelVbox) {
        try {
            ImageButton chatsImageButton = new ImageButton(70d, 70d, Utils.getImage("bitmaps/icons/others/chats.png"));
            VBox.setMargin(chatsImageButton, new Insets(10d));
            addActionToChatsButtonClick(chatsImageButton);

            ImageButton settingsImageButton = new ImageButton(70d, 70d, Utils.getImage("bitmaps/icons/others/settings.png"));
            settingsImageButton.setOnAction(() -> {
                Utils.addRotateTranslationToNode(settingsImageButton);
                new SettingsMainPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.SETTINGS), true).open();
            });
            VBox.setMargin(settingsImageButton, new Insets(10d));

            VBox developerLabelArea = new DeveloperLabel().wrapInVbox();
            developerLabelArea.setPadding(new Insets(15d));
            buttonsAndDeveloperLabelVbox.getChildren().addAll(chatsImageButton, settingsImageButton, developerLabelArea);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void addActionToChatsButtonClick(@NotNull Node chatsButtonNode){
        try {
            ContextMenu contextMenu = new ContextMenu();

            MenuItem createChatItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.CREATE_CHAT));
            MenuItem connectToChatItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECT_TO_CHAT));
            contextMenu.getItems().addAll(createChatItem, connectToChatItem);

            createChatItem.setOnAction(actionEvent -> new CreateChatPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CREATING_CHAT), true).open());
            connectToChatItem.setOnAction(actionEvent -> new ConnectToTheChatPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECTING), true).open());

            chatsButtonNode.setOnMouseClicked(mouseEvent -> contextMenu.show(chatsButtonNode, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initContentHbox() {
        try {
            contentHbox = new HBox();
            contentHbox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(contentHbox, Priority.ALWAYS);
            VBox.setVgrow(contentHbox, Priority.ALWAYS);

            addNodeToPage(contentHbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createSidePanel() {
        try {
            VBox sidePanelVbox = new VBox();
            sidePanelVbox.setPrefWidth(374d);
            sidePanelVbox.setEffect(new DropShadow());
            sidePanelVbox.setAlignment(Pos.TOP_CENTER);
            sidePanelVbox.getStyleClass().add("tile");
            Utils.addTranslateByLeftAnimationToNode(sidePanelVbox);
            addToContentHbox(sidePanelVbox);

            createHeader(sidePanelVbox);

            VBox scrollPaneContentVbox = createScrollPane(sidePanelVbox);
            assert scrollPaneContentVbox != null;
            scrollPaneContentVbox.getChildren().add(new ChatTile(ChatTile.DEFAULT_WIDTH, ChatTile.DEFAULT_HEIGHT, "192.168.0.102:6565", scrollPaneContentVbox, this));

            createClearButton(sidePanelVbox, scrollPaneContentVbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createClearButton(@NotNull VBox sidePanelVbox, VBox scrollPane) {
        try {
            AdditionalButton clearButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.CLEAR_ALL), 308d, 58d, new Color(217, 217, 217), new Color(0,0,0));
            VBox.setMargin(clearButton, new Insets(20));
            sidePanelVbox.getChildren().add(clearButton);

            clearButton.addAction(() -> scrollPane.getChildren().clear());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createHeader(@NotNull VBox sidePanelVbox) {
        try {
            Label label = new Label(getTitle());
            label.getStyleClass().add("header-label");
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 32));
            label.setUnderline(true);
            VBox.setMargin(label, new Insets(20, 0, 0, 0));
            sidePanelVbox.getChildren().add(label);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable VBox createScrollPane(@NotNull VBox sidePanelVbox){
        try{
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            VBox.setMargin(scrollPane, new Insets(20, 0, 0, 0));

            VBox scrollPaneContentVbox = new VBox();
            scrollPaneContentVbox.setAlignment(Pos.TOP_CENTER);
            scrollPaneContentVbox.setSpacing(20d);
            VBox.setVgrow(scrollPaneContentVbox, Priority.ALWAYS);
            HBox.setHgrow(scrollPaneContentVbox, Priority.ALWAYS);
            scrollPaneContentVbox.setPadding(new Insets(15d));
            scrollPane.setContent(scrollPaneContentVbox);

            sidePanelVbox.getChildren().add(scrollPane);

            return scrollPaneContentVbox;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private void addToContentHbox(Node node){
        if(contentHbox != null){
            contentHbox.getChildren().add(node);
        }
    }
}
