package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    }

    private void createInfoLabel() {
        try{
            String text = StringGetterWithCurrentLanguage.getString(StringsConstants.APP_NAME) + " " + SettingsReader.getStringValue(SettingsKeys.APP_NAME) + "\n"
                    + StringGetterWithCurrentLanguage.getString(StringsConstants.DEVELOPER) + " " + SettingsReader.getStringValue(SettingsKeys.APP_DEVELOPER) + "\n"
                    + StringGetterWithCurrentLanguage.getString(StringsConstants.VERSION) + " " + SettingsReader.getStringValue(SettingsKeys.APP_VERSION);

            Label label = new Label(text);
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 20));
            label.setTextFill(Color.WHITE);
            VBox.setMargin(label, new Insets(60d, 50, 0d, 50d));

            addNodeToTile(label);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
