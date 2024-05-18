package com.ds.darknesschat;

import com.ds.darknesschat.pages.WelcomePage;
import com.ds.darknesschat.utils.ScreenshotsMaker;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.ds.darknesschat.Constants.DEFAULT_BACKGROUND_PATH;
import static com.ds.darknesschat.Constants.IGNORE_USER_AGREEMENT;

public class MainController {
    @FXML
    private Label appNameLabel;

    @FXML
    private ImageView closeButtonImageView;

    @FXML
    private HBox headerHbox;

    @FXML
    private VBox mainVbox;

    @FXML
    private ImageView minimizeButtonImageView;

    @FXML
    private ImageView windowIcon;

    @FXML
    private HBox windowControlsButtonsHbox;

    @FXML
    private VBox contentVbox;
    private double windowX, windowY;

    @FXML
    void initialize() {
        initBackground();
        initHeader();
    }

    private void initHeader() {
        try {
            initWindowIcon();
            initWindowTitle();
            initControlButtons();
            initDrag();
            initPages();

            Log.info("Window Header was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initWindowIcon() {
        try {
            windowIcon.setImage(Utils.getImage(SettingsReader.getStringValue(SettingsKeys.APP_ICON)));

            MenuItem closeMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.CLOSE_WINDOW));
            MenuItem minimizeMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.MINIMIZE_WINDOW));
            MenuItem pinMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.PIN_WINDOW));
            MenuItem createScreenshotMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.CREATE_SCREENSHOT));

            closeMenuItem.setOnAction(actionEvent -> close());
            minimizeMenuItem.setOnAction(actionEvent -> minimize());
            pinMenuItem.setOnAction(actionEvent -> changeOnTop(pinMenuItem));
            createScreenshotMenuItem.setOnAction(actionEvent -> ScreenshotsMaker.createScreenshot(Objects.requireNonNull(getStage())));

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(closeMenuItem, minimizeMenuItem, pinMenuItem, createScreenshotMenuItem);

            if(SettingsReader.getBooleanValue(SettingsKeys.APP_RESIZABLE)){
                MenuItem maximizeMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.MAXIMIZE_WINDOW));
                maximizeMenuItem.setOnAction(actionEvent -> resizeWindow());

                contextMenu.getItems().add(2, maximizeMenuItem);
            }

            windowIcon.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(windowIcon, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void changeOnTop(@NotNull MenuItem pinMenuItem) {
        try {
            boolean stageIsAlwaysOnTop = Objects.requireNonNull(getStage()).isAlwaysOnTop();
            pinMenuItem.setText(stageIsAlwaysOnTop ? StringGetterWithCurrentLanguage.getString(StringsConstants.PIN_WINDOW) : StringGetterWithCurrentLanguage.getString(StringsConstants.UNPIN_WINDOW));

            getStage().setAlwaysOnTop(!stageIsAlwaysOnTop);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initWindowTitle() {
        try {
            appNameLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 16));
            appNameLabel.setText(SettingsReader.getStringValue(SettingsKeys.APP_NAME));

            Log.info("Window Title was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initPages() {
        try {
            WelcomePage welcomePage = new WelcomePage(null, contentVbox, StringGetterWithCurrentLanguage.getString(StringsConstants.LETS_LOG_IN), true, false);
            welcomePage.open();

            Log.info("Welcome page was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initDrag() {
        try {
            headerHbox.setOnMousePressed(event -> {
                windowX = event.getSceneX();
                windowY = event.getSceneY();
            });

            headerHbox.setOnMouseDragged(event -> {
                headerHbox.getScene().getWindow().setX(event.getScreenX() - windowX);
                headerHbox.getScene().getWindow().setY(event.getScreenY() - windowY);
            });

            Log.info("Window Header Drug was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initControlButtons() {
        try {
            Utils.addActionToNode(closeButtonImageView, this::close, IGNORE_USER_AGREEMENT);
            Utils.addActionToNode(minimizeButtonImageView, this::minimize, IGNORE_USER_AGREEMENT);

            if(SettingsReader.getBooleanValue(SettingsKeys.APP_RESIZABLE))
                windowControlsButtonsHbox.getChildren().add(1, createResizeButton());

            Log.info("Close and Minimize Buttons was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @NotNull Node createResizeButton() {
        ImageView imageView = new ImageView(Utils.getImage("bitmaps/icons/others/resize.png"));
        imageView.setFitWidth(35d);
        imageView.setFitHeight(35d);
        imageView.getStyleClass().add("controls-buttons");
        Utils.addActionToNode(imageView, this::resizeWindow, IGNORE_USER_AGREEMENT);

        return imageView;
    }

    private void resizeWindow() {
        Objects.requireNonNull(getStage()).setMaximized(!getStage().isMaximized());
    }

    private void initBackground() {
        try{
            Utils.changeMainBackground(mainVbox, Main.class.getResourceAsStream(DEFAULT_BACKGROUND_PATH), getStage());
            Log.info("Background was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable Stage getStage(){
        return mainVbox.getScene() != null ? ((Stage) mainVbox.getScene().getWindow()) : null;
    }

    private void minimize(){
        try{
            Objects.requireNonNull(getStage()).setIconified(true);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void close(){
        try {
            Log.info("Stopping app...");

            Platform.exit();
            Runtime.getRuntime().exit(0);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
