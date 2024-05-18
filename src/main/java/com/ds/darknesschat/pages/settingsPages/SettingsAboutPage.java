package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

public class SettingsAboutPage extends Page {
    protected SettingsAboutPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createInfoLabel();
        SettingsPage.initBackButton(this);

        getTile().applyAlphaWithUserSettings(getUser());
        goToPreviousPageByKey(true);
    }

    private void createInfoLabel() {
        try{
            String mainInfoText = StringGetterWithCurrentLanguage.getString(StringsConstants.APP_NAME) + " " + SettingsReader.getStringValue(SettingsKeys.APP_NAME) + "\n"
                    + StringGetterWithCurrentLanguage.getString(StringsConstants.DEVELOPER) + " " + SettingsReader.getStringValue(SettingsKeys.APP_DEVELOPER) + "\n"
                    + StringGetterWithCurrentLanguage.getString(StringsConstants.VERSION) + " " + SettingsReader.getStringValue(SettingsKeys.APP_VERSION);

            String javaVersionText = StringGetterWithCurrentLanguage.getString(StringsConstants.JAVA_VERSION) + " " + System.getProperty("java.version");
            Label javaVersionLabel = createLabel(javaVersionText);
            VBox.setMargin(javaVersionLabel, new Insets(40d, 0, 0, 0));

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            VBox.setMargin(scrollPane, new Insets(60d, 38d, 0d, 38d));

            VBox vBox = new VBox();
            vBox.setEffect(new DropShadow());
            vBox.setPadding(new Insets(23d));
            vBox.setStyle("-fx-background-radius: 13px; -fx-border-radius: 13px; -fx-border-color: white; -fx-border-width: 2px;");
            vBox.getChildren().addAll(createLabel(mainInfoText), javaVersionLabel);

            scrollPane.setContent(vBox);
            addNodeToTile(scrollPane);

            Animations.addFadeTransitionToNode(scrollPane, getUser().getId());
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @NotNull Label createLabel(String text){
        Label label = new Label(text);
        label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 20));
        label.setTextFill(Color.LIGHTGRAY);
        label.setLineSpacing(14d);

        return label;
    }
}
