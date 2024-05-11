package com.ds.darknesschat.pages.settingsPages.usersSettings;

import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.pages.settingsPages.SettingsPage;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import static com.ds.darknesschat.Constants.WHITE_COLOR;

public class SettingsAccountChangeNamePage extends Page {
    protected SettingsAccountChangeNamePage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createTextField();
        SettingsPage.initBackButton(this);
        createDeveloperLabelInBottom();

        getTile().applyAlphaWithUserSettings(getUser());
    }

    private void createTextField() {
        try{
            AdditionalTextField newNameAdditionalTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_NEW_NAME),
                    Utils.getImage("bitmaps/icons/others/user_1.png"), false);
            VBox.setMargin(newNameAdditionalTextField , new Insets(90d, 40d, 0d, 40d));
            addNodeToTile(newNameAdditionalTextField);
            
            createNextButton(newNameAdditionalTextField);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createNextButton(AdditionalTextField newNameAdditionalTextField) {
        AdditionalButton nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 408d, 41d,
                new Color(164, 62, 62), WHITE_COLOR, getUser().getId());
        VBox.setMargin(nextButton, new Insets(10d, 40d, 0d, 40d));
        addNodeToTile(nextButton);

        nextButton.addAction(() -> onNextButtonAction(newNameAdditionalTextField));
    }

    private void onNextButtonAction(AdditionalTextField newNameAdditionalTextField) {
        try {
            if (Utils.isFieldsIsNotEmpty(new AdditionalTextField[]{newNameAdditionalTextField})) {
                if (newNameAdditionalTextField.getText().equals(getUser().getUserName())) {
                    newNameAdditionalTextField.setError(getUser().getId());
                    ErrorDialog.show(new Exception(StringGetterWithCurrentLanguage.getString(StringsConstants.NEW_NAME_CANT_BE_EQUALS_WITH_OLD)));

                    return;
                }

                DatabaseService.changeValue(DatabaseConstants.USER_NAME_ROW, newNameAdditionalTextField.getText(), getUser().getId());
                getUser().setUserName(newNameAdditionalTextField.getText());

                goToPreviousPage();
            } else {
                newNameAdditionalTextField.setError(getUser().getId());
            }
        }catch (Exception e){
            Log.error(e);
        }
    }
}
