package com.ds.darknesschat.pages;

import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class WelcomePage extends Page{
    private final boolean isLogUpPage;

    public WelcomePage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, boolean isLogUpPage) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
        this.isLogUpPage = isLogUpPage;
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        initTile();
        createDeveloperLabelInBottom();
    }

    private void initTile() {
        try {
            Tile tile = getTile();
            tile.setSpacing(10);
            tile.addTitle(getTitle());

            createTextFieldsAndButton();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable AdditionalButton createNextButton() {
        try {
            AdditionalButton nextButton = new AdditionalButton(isLogUpPage ? StringGetterWithCurrentLanguage.getString(StringsConstants.LOG_UP) : StringGetterWithCurrentLanguage.getString(StringsConstants.LOG_IN), AdditionalTextField.DEFAULT_WIDTH + 40, AdditionalTextField.DEFAULT_HEIGHT, new com.ds.darknesschat.utils.Color(217, 217, 217), new Color(0,0,0));
            VBox.setMargin(nextButton, new Insets(100, 40, 30, 40));

            return nextButton;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private void createTextFieldsAndButton() {
        try {
            AdditionalTextField nameTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_NAME),
                    Utils.getImage("bitmaps/icons/others/user_1.png"), false);
            VBox.setMargin(nameTextField, new Insets(70, 50, 0, 50));

            AdditionalTextField passwordTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.WRITE_YOUR_PASSWORD),
                    Utils.getImage("bitmaps/icons/others/password.png"), true);
            VBox.setMargin(passwordTextField, new Insets(6, 50, 0, 50));

            addNodeToTile(nameTextField);
            addNodeToTile(passwordTextField);

            AdditionalTextField repeatPasswordTextField;

            if(isLogUpPage) {
                repeatPasswordTextField = new AdditionalTextField(AdditionalTextField.DEFAULT_WIDTH, AdditionalTextField.DEFAULT_HEIGHT, StringGetterWithCurrentLanguage.getString(StringsConstants.REPEAT_YOUR_PASSWORD),
                        Utils.getImage("bitmaps/icons/others/password.png"), true);
                VBox.setMargin(repeatPasswordTextField, new Insets(6, 50, 0, 50));

                addNodeToTile(repeatPasswordTextField);
            } else {
                repeatPasswordTextField = null;
            }

            AdditionalButton nextButton = createNextButton();

            assert nextButton != null;
            nextButton.addAction(() -> onNextButtonAction(nameTextField, passwordTextField, isLogUpPage ? repeatPasswordTextField: null));
            addNodeToTile(nextButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private boolean checkAllFields(AdditionalTextField nameTextField, AdditionalTextField passwordTextField, AdditionalTextField repeatPasswordTextField){
        try {
            boolean isAllFine = false;

            AdditionalTextField[] additionalTextFieldList = {nameTextField, passwordTextField, repeatPasswordTextField};
            Objects.requireNonNull(Utils.getEmptyFieldsFromArray(additionalTextFieldList)).forEach(additionalTextField -> {
                if(additionalTextField != null)
                    additionalTextField.setError();
            });

            if (Utils.isFieldsIsNotEmpty(additionalTextFieldList)) {
                if(isLogUpPage) {
                    if (repeatPasswordTextField.getText().equals(passwordTextField.getText())) {
                        isAllFine = true;
                    } else
                        repeatPasswordTextField.setError();
                }else{
                    isAllFine = true;
                }
            }

            return isAllFine;
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }

    private void onNextButtonAction(AdditionalTextField nameTextField, AdditionalTextField passwordTextField, AdditionalTextField repeatPasswordTextField){
        if(checkAllFields(nameTextField, passwordTextField, repeatPasswordTextField)){
            ChatsPage chatsPage = new ChatsPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.RECENT_CHATS), false);
            chatsPage.open();
        }else {
            Sounds.playSound(SoundsConstants.ERROR_SOUND);
        }
    }
}
