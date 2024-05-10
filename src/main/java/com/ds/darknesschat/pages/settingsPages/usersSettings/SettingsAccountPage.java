package com.ds.darknesschat.pages.settingsPages.usersSettings;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.SettingsOption;
import com.ds.darknesschat.additionalNodes.SettingsOptionButton;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.pages.settingsPages.SettingsPage;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.Nullable;

import static com.ds.darknesschat.Constants.WHITE_COLOR;

public class SettingsAccountPage extends Page {
    public SettingsAccountPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createDeveloperLabelInBottom();
        createUserNameAndDateOfRegistrationLabels();
        createOptionsButtons();
        createDeleteAccountButtonAndLogOut();
        SettingsPage.initBackButton(this);

        getTile().applyAlphaWithUserSettings(getUser());
    }

    private void createDeleteAccountButtonAndLogOut() {
        try{
            AdditionalButton additionalButtonDeleteAccount = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.DELETE_ACCOUNT),  408d, 41d, new com.ds.darknesschat.utils.Color(187, 71, 71), WHITE_COLOR, getUser().getId());
            VBox.setMargin(additionalButtonDeleteAccount, new Insets(6d, 40d, 0, 40d));

            AdditionalButton additionalButtonLogOut = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.LOG_OUT),  408d, 41d, new com.ds.darknesschat.utils.Color(47, 38, 38), WHITE_COLOR, getUser().getId());
            VBox.setMargin(additionalButtonLogOut, new Insets(6d, 40d, 0, 40d));

            addNodeToTile(additionalButtonDeleteAccount);
            addNodeToTile(additionalButtonLogOut);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createOptionsButtons() {
        try{
            SettingsOptionButton changePasswordSettingsOptionButton = new SettingsOptionButton(SettingsOption.DEFAULT_WIDTH, SettingsOption.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.CHANGE_PASSWORD), Utils.getImage("bitmaps/icons/others/arrow.png"),
                    11d, 11d, getUser().getId());
            VBox.setMargin(changePasswordSettingsOptionButton, new Insets(50d, 40d, 0, 40d));
            SettingsOptionButton changeNameSettingsOptionButton = new SettingsOptionButton(SettingsOption.DEFAULT_WIDTH, SettingsOption.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.CHANGE_NAME), Utils.getImage("bitmaps/icons/others/arrow.png"),
                    11d, 11d, getUser().getId());
            VBox.setMargin(changeNameSettingsOptionButton, new Insets(8d, 40d, 0, 40d));

            changePasswordSettingsOptionButton.setOnAction(() -> new SettingsAccountChangePasswordPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CHANGING_PASSWORD), true, getUser()).open());
            changeNameSettingsOptionButton.setOnAction(() -> new SettingsAccountChangeNamePage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CHANGING_NAME), true, getUser()).open());

            addNodeToTile(changePasswordSettingsOptionButton);
            addNodeToTile(changeNameSettingsOptionButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createUserNameAndDateOfRegistrationLabels() {
        try {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_LEFT);
            vBox.setSpacing(20d);
            VBox.setMargin(vBox, new Insets(40d, 40d, 0, 40d));

            vBox.getChildren().addAll(createLabel(StringGetterWithCurrentLanguage.getString(StringsConstants.ACCOUNT_NAME) + " Anton K"),
                    createLabel(StringGetterWithCurrentLanguage.getString(StringsConstants.DATE_OF_REGISTRATION) + " 1/1/1111"));

            addNodeToTile(vBox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable Label createLabel(String text){
        try {
            Label label = new Label(text);
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 20d));
            label.setTextFill(Color.WHITE);

            return label;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
