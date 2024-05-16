package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.ChatTile;
import com.ds.darknesschat.additionalNodes.DeveloperLabel;
import com.ds.darknesschat.additionalNodes.ImageButton;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.settingsPages.SettingsMainPage;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.user.UserRecentChats;
import com.ds.darknesschat.user.UserSettings;
import com.ds.darknesschat.utils.Animations;
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

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static com.ds.darknesschat.Constants.*;

public class ChatsPage extends Page{
    private HBox contentHbox;

    protected ChatsPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        deleteDefaultPagePaddings();
        getContentVbox().setAlignment(Pos.CENTER_LEFT);

        initContentHBox();
        createSidePanel();
        initDeveloperAndButtonsVbox();
        updateBackground(getUser().getId());
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
            ImageButton chatsImageButton = new ImageButton(70d, 70d, Utils.getImage("bitmaps/icons/others/chats.png"), getUser().getId());
            VBox.setMargin(chatsImageButton, new Insets(10d));
            addActionToChatsButtonClick(chatsImageButton);

            ImageButton settingsImageButton = new ImageButton(70d, 70d, Utils.getImage("bitmaps/icons/others/settings.png"), getUser().getId());
            settingsImageButton.setOnAction(() -> {
                Animations.addRotateTranslationToNode(settingsImageButton, getUser().getId());
                new SettingsMainPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.SETTINGS), true, getUser()).open();
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

            createChatItem.setOnAction(actionEvent -> new CreateChatPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CREATING_CHAT), true, getUser()).open());
            connectToChatItem.setOnAction(actionEvent -> new ConnectToTheChatPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECTING), true, getUser()).open());

            chatsButtonNode.setOnMouseClicked(mouseEvent -> contextMenu.show(chatsButtonNode, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initContentHBox() {
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
            sidePanelVbox.setStyle(sidePanelVbox.getStyle() + "-fx-background-color: rgba(" +
                    TILE_COLOR.getRed() + ", " + TILE_COLOR.getGreen() + ", " + TILE_COLOR.getBlue() + ", " + UserSettings.getUserAlphaLevel(getUser()) + ");");
            Animations.addTranslateByLeftAnimationToNode(sidePanelVbox, getUser().getId());
            addToContentHbox(sidePanelVbox);

            createHeader(sidePanelVbox);

            VBox scrollPaneContentVbox = createScrollPane(sidePanelVbox);
            assert scrollPaneContentVbox != null;

            List<String> recentChatsList = UserRecentChats.getRecentChats(getUser().getId());

            if(!recentChatsList.isEmpty())
                recentChatsList.forEach(address -> scrollPaneContentVbox.getChildren().add(new ChatTile(ChatTile.DEFAULT_WIDTH, ChatTile.DEFAULT_HEIGHT, address, scrollPaneContentVbox, this)));
            else
                createNoChatsHereLabel(scrollPaneContentVbox);

            createClearButton(sidePanelVbox, scrollPaneContentVbox, recentChatsList.isEmpty(), scrollPaneContentVbox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createClearButton(@NotNull VBox sidePanelVbox, VBox scrollPane, boolean listIsEmpty, VBox scrollPaneContentVbox) {
        try {
            AdditionalButton clearButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.CLEAR_ALL), 308d, 58d, new Color(217, 217, 217), BLACK_COLOR, getUser().getId());
            clearButton.setDisable(listIsEmpty);
            VBox.setMargin(clearButton, new Insets(20));
            sidePanelVbox.getChildren().add(clearButton);

            clearButton.addAction(() -> {
                scrollPane.getChildren().clear();
                UserRecentChats.clearRecentChats(getUser().getId());

                createNoChatsHereLabel(scrollPaneContentVbox);
                clearButton.setDisable(true);
            });
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

    private void createNoChatsHereLabel(@NotNull VBox sidePanelVbox){
        try {
            Label label = new Label(StringGetterWithCurrentLanguage.getString(StringsConstants.NO_RECENT_CHATS_YET_IN_HERE));
            label.setTextFill(javafx.scene.paint.Color.LIGHTGRAY);
            label.setUnderline(true);
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));

            sidePanelVbox.getChildren().add(label);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void addToContentHbox(Node node){
        if(contentHbox != null){
            contentHbox.getChildren().add(node);
        }
    }
}
