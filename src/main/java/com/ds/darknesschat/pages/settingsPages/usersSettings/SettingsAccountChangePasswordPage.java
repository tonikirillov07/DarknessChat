package com.ds.darknesschat.pages.settingsPages.usersSettings;

import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.pages.settingsPages.SettingsPage;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.PasswordGenerator;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.util.Random;

public class SettingsAccountChangePasswordPage extends Page {
    protected SettingsAccountChangePasswordPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createTextFields();
        SettingsPage.initBackButton(this);
        createDeveloperLabelInBottom();
    }

    private void createTextFields() {
        try {
            AdditionalTextField currentPasswordTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT,
                    StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_CURRENT_PASSWORD), Utils.getImage("bitmaps/icons/others/password.png"), true);
            VBox.setMargin(currentPasswordTextField, new Insets(50d, 40d, 0, 40d));

            AdditionalTextField newPasswordTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT,
                    StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_NEW_PASSWORD), Utils.getImage("bitmaps/icons/others/password.png"), true);
            VBox.setMargin(newPasswordTextField, new Insets(10d, 40d, 0, 40d));

            addNodeToTile(currentPasswordTextField);
            addNodeToTile(newPasswordTextField);

            createButtons(currentPasswordTextField, newPasswordTextField);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createButtons(AdditionalTextField currentPasswordTextField, AdditionalTextField newPasswordTextField) {
        try{
            AdditionalButton generateStrongPasswordButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.GENERATE_STRONG_PASSWORD), 408d, 41d,
                    new Color(62, 109, 164), new Color(255, 255, 255));
            VBox.setMargin(generateStrongPasswordButton, new Insets(10d, 40d, 0d, 40d));
            generateStrongPasswordButton.addAction(() -> {
                String generatedPassword = PasswordGenerator.generate(new Random().nextInt(7, 12));

                Utils.copyStringToClipboard(generatedPassword);
                currentPasswordTextField.getTextField().setText(generatedPassword);
            });

            AdditionalButton nextButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NEXT), 408d, 41d,
                    new Color(164, 62, 62), new Color(255, 255, 255));
            VBox.setMargin(nextButton, new Insets(10d, 40d, 0d, 40d));

            addNodeToTile(generateStrongPasswordButton);
            addNodeToTile(nextButton);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
