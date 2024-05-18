package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.additionalNodes.*;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.pages.settingsPages.usersSettings.SettingsAccountPage;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;
import com.ds.darknesschat.utils.dialogs.ConfirmDialog;
import com.ds.darknesschat.utils.dialogs.InfoDialog;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import static com.ds.darknesschat.client.ClientConstants.TRUE;
import static com.ds.darknesschat.utils.languages.StringsConstants.DO_YOU_REALLY_WANT_TO_RESET_SETTINGS;

public class SettingsMainPage extends Page {
    public SettingsMainPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();

        initOptionsButtons();
        SettingsPage.initResetButton(this, this::resetAllSettings);
        SettingsPage.initBackButton(this);
        createDeveloperLabelInBottom();

        getTile().applyAlphaWithUserSettings(getUser());
        goToPreviousPageByKey(true);
    }

    private void resetAllSettings(){
        try {
            if(ConfirmDialog.show(StringGetterWithCurrentLanguage.getString(DO_YOU_REALLY_WANT_TO_RESET_SETTINGS))) {
                DatabaseService.changeValue(DatabaseConstants.ANIMATIONS_USING_ROW, TRUE, getUser().getId());
                DatabaseService.changeValue(DatabaseConstants.OPACITY_LEVEL_ROW, Constants.DEFAULT_OPACITY_LEVEL, getUser().getId());
                DatabaseService.changeValue(DatabaseConstants.SOUNDS_USING_ROW, TRUE, getUser().getId());
                DatabaseService.changeValue(DatabaseConstants.BACKGROUND_PATH_ROW, Constants.DEFAULT_BACKGROUND_VALUE, getUser().getId());
                DatabaseService.setNull(DatabaseConstants.REMEMBERED_CHAT_ADDRESS_ROW, getUser().getId());
                DatabaseService.changeValue(DatabaseConstants.USER_USING_NOTIFICATIONS_ROW, TRUE, getUser().getId());

                Log.info("User settings rested");
                reopen();
            }
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void initOptionsButtons() {
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.APPEARANCE), Utils.getImage("bitmaps/icons/others/appearance.png"), () -> new SettingsAppearancePage(this, getContentVbox(), getSectionTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.APPEARANCE)), true, getUser()).open(), true);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.ACCOUNT), Utils.getImage("bitmaps/icons/others/user_2.png"), () -> new SettingsAccountPage(this, getContentVbox(), getSectionTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.ACCOUNT)), true, getUser()).open(), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.ABOUT), Utils.getImage("bitmaps/icons/others/info.png"), () -> new SettingsAboutPage(this, getContentVbox(), getSectionTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.ABOUT)), true, getUser()).open(), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.OTHER_SETTINGS), Utils.getImage("bitmaps/icons/others/others_settings.png"), () -> new SettingsOthersPage(this, getContentVbox(), getSectionTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.OTHER_SETTINGS)), true, getUser()).open(), false);

        SettingsOptionButton languageSettingsOptionButton = new SettingsOptionButton(SettingsOptionButton.DEFAULT_WIDTH, SettingsOptionButton.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.LANGUAGE) + " " + StringGetterWithCurrentLanguage.getString(StringsConstants.LANGUAGE_NAME), Utils.getImage("bitmaps/icons/others/language.png"),
                SettingsOptionButton.DEFAULT_IMAGE_FIT_WIDTH, SettingsOptionButton.DEFAULT_IMAGE_FIT_HEIGHT, getUser().getId());
        VBox.setMargin(languageSettingsOptionButton, new Insets(5d, 40d, 0, 40d));
        languageSettingsOptionButton.setOnAction(this::changeLanguage);
        addNodeToTile(languageSettingsOptionButton);
    }

    private void changeLanguage() {
        Utils.changeCurrentLanguage();
        reopen();

        if(Boolean.parseBoolean(OutsideSettingsManager.getValue(OutsideSettingsKeys.CHANGING_APP_FIRST_TIME))){
            InfoDialog.show(StringGetterWithCurrentLanguage.getString(StringsConstants.LANGUAGE_FIRST_TIME_CHANGING_MESSAGE));
            OutsideSettingsManager.changeValue(OutsideSettingsKeys.CHANGING_APP_FIRST_TIME, "false");
        }
    }

    private @NotNull String getSectionTitle(String sectionName){
        return StringGetterWithCurrentLanguage.getString(StringsConstants.SETTINGS) + "->" + sectionName;
    }

    private void addOptionButton(String text, Image image, IOnAction onAction, boolean isFirst){
        try {
            SettingsOptionButton optionButton =
                    new SettingsOptionButton(SettingsOptionButton.DEFAULT_WIDTH, SettingsOptionButton.DEFAULT_HEIGHT, text, image, SettingsOptionButton.DEFAULT_IMAGE_FIT_WIDTH, SettingsOptionButton.DEFAULT_IMAGE_FIT_HEIGHT, getUser().getId());
            optionButton.setOnAction(onAction);
            VBox.setMargin(optionButton, new Insets(isFirst ? 50d : 5d, 40d, 0, 40d));
            addNodeToTile(optionButton);
        }catch (Exception e){
            Log.error(e);
        }

    }
}
