package com.ds.darknesschat;

import com.ds.darknesschat.pages.WelcomePage;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static com.ds.darknesschat.Constants.NO_USER_AGREEMENT;

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
    private VBox contentVbox;
    private double windowX, windowY;

    @FXML
    void initialize() {
        initBackground();
        initHeader();
    }

    private void initHeader() {
        try {
            windowIcon.setImage(Utils.getImage(SettingsReader.getStringValue(SettingsKeys.APP_ICON)));

            initWindowTitle();
            initControlButtons();
            initDrag();
            initPages();
            
            Log.info("Window Header was initialized!");
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
        WelcomePage welcomePage = new WelcomePage(null, contentVbox, StringGetterWithCurrentLanguage.getString(StringsConstants.LETS_LOG_IN), true,false);
        welcomePage.open();

        Log.info("Welcome page was initialized!");
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
        Utils.addActionToNode(closeButtonImageView, this::close, NO_USER_AGREEMENT);
        Utils.addActionToNode(minimizeButtonImageView, this::minimize, NO_USER_AGREEMENT);

        Log.info("Close and Minimize Buttons was initialized!");
    }

    private void initBackground() {
        try{
            BackgroundImage backgroundImage = new BackgroundImage(Utils.getImage("bitmaps/background/background.png"),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            mainVbox.setBackground(new Background(backgroundImage));

            Log.info("Background was initialized!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    private Stage getStage(){
        return (Stage) mainVbox.getScene().getWindow();
    }

    private void minimize(){
        getStage().setIconified(true);
    }

    private void close(){
        try {
            Platform.exit();
            System.exit(0);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
