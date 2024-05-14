package com.ds.darknesschat.pages;

import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;
import com.ds.darknesschat.utils.dialogs.ErrorDialog;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.ds.darknesschat.Constants.*;

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
        enterInAccountAuto();
    }

    private void enterInAccountAuto() {
        try {
            String rememberedLogin = OutsideSettingsManager.getValue(OutsideSettingsKeys.REMEMBERED_USER_LOGIN);
            String rememberedPassword = OutsideSettingsManager.getValue(OutsideSettingsKeys.REMEMBERED_USER_PASSWORD);

            assert rememberedLogin != null;
            assert rememberedPassword != null;
            if (!rememberedLogin.equals(NULL) & !rememberedPassword.equals(NULL)) {
                if(DatabaseService.isUserExists(new User(Utils.decodeString(rememberedLogin), Utils.decodeString(rememberedPassword), null)))
                    openChatsPage(new User(Utils.decodeString(rememberedLogin), Utils.decodeString(rememberedPassword), DatabaseService.getValueWithWhereValue(DatabaseConstants.USER_DATE_OF_REGISTRATION_ROW, DatabaseConstants.USER_NAME_ROW, Utils.decodeString(rememberedLogin))));
            }
        }catch (Exception e){
            Log.error(e);
        }
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
            AdditionalButton nextButton = new AdditionalButton(isLogUpPage ? StringGetterWithCurrentLanguage.getString(StringsConstants.LOG_UP) : StringGetterWithCurrentLanguage.getString(StringsConstants.LOG_IN), AdditionalTextField.DEFAULT_WIDTH + 40, AdditionalTextField.DEFAULT_HEIGHT, new com.ds.darknesschat.utils.Color(217, 217, 217), BLACK_COLOR, IGNORE_USER_AGREEMENT);
            VBox.setMargin(nextButton, new Insets(100, 40, 10, 40));

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

            fillFieldsRememberedData(nameTextField, passwordTextField);

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
            AdditionalButton anotherActionButton = createAnotherActionButton();

            assert nextButton != null;
            assert anotherActionButton != null;
            nextButton.addAction(() -> onNextButtonAction(nameTextField, passwordTextField, isLogUpPage ? repeatPasswordTextField: null));
            anotherActionButton.addAction(this::onAnotherActionButton);

            addNodeToTile(nextButton);
            addNodeToTile(anotherActionButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void fillFieldsRememberedData(AdditionalTextField nameTextField, AdditionalTextField passwordTextField) {
        try {
            String rememberedLogin = OutsideSettingsManager.getValue(OutsideSettingsKeys.REMEMBERED_USER_LOGIN);
            String rememberedPassword = OutsideSettingsManager.getValue(OutsideSettingsKeys.REMEMBERED_USER_PASSWORD);

            assert rememberedLogin != null;
            assert rememberedPassword != null;
            if (!rememberedLogin.equals(NULL) & !rememberedPassword.equals(NULL)) {
                nameTextField.getTextField().setText(Utils.decodeString(rememberedLogin));
                passwordTextField.getTextField().setText(Utils.decodeString(rememberedPassword));
            }
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void onAnotherActionButton() {
        try {
            String title = isLogUpPage ? StringGetterWithCurrentLanguage.getString(StringsConstants.LETS_LOG_IN) : StringGetterWithCurrentLanguage.getString(StringsConstants.LETS_LOG_UP);
            new WelcomePage(this, getContentVbox(), title, true, !isLogUpPage).open();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private @Nullable AdditionalButton createAnotherActionButton() {
        try {
            AdditionalButton anotherActionWithAccount = createNextButton();
            VBox.setMargin(anotherActionWithAccount, new Insets(0, 40, 30, 40));

            assert anotherActionWithAccount != null;
            anotherActionWithAccount.setBackgroundColor(new Color(133, 153, 223));
            anotherActionWithAccount.setTextColor(WHITE_COLOR);
            anotherActionWithAccount.setText(isLogUpPage ? StringGetterWithCurrentLanguage.getString(StringsConstants.HAVE_ACCOUNT): StringGetterWithCurrentLanguage.getString(StringsConstants.HAVENT_ACCOUNT));

            return anotherActionWithAccount;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private boolean checkAllFields(AdditionalTextField nameTextField, AdditionalTextField passwordTextField, AdditionalTextField repeatPasswordTextField){
        try {
            boolean isAllFine = false;

            AdditionalTextField[] additionalTextFieldList = {nameTextField, passwordTextField, repeatPasswordTextField};
            Objects.requireNonNull(Utils.getEmptyFieldsFromArray(additionalTextFieldList)).forEach(additionalTextField -> {
                if(additionalTextField != null)
                    additionalTextField.setError(IGNORE_USER_AGREEMENT);
            });

            if (Utils.isFieldsIsNotEmpty(additionalTextFieldList)) {
                if(isLogUpPage) {
                    if (repeatPasswordTextField.getText().equals(passwordTextField.getText())) {
                        isAllFine = true;
                    } else
                        repeatPasswordTextField.setError(getUser().getId());
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
            User user = new User(nameTextField.getText(), passwordTextField.getText(), DatabaseService.getValueWithWhereValue(DatabaseConstants.USER_DATE_OF_REGISTRATION_ROW, DatabaseConstants.USER_NAME_ROW, nameTextField.getText()));

            if(!isLogUpPage){
                if(DatabaseService.isUserExists(user)){
                    openChatsPage(user);
                }else{
                    nameTextField.setError(IGNORE_USER_AGREEMENT);
                    passwordTextField.setError(IGNORE_USER_AGREEMENT);

                    ErrorDialog.show(new Exception(StringGetterWithCurrentLanguage.getString(StringsConstants.ENTERED_WRONG_DATA)));
                }
            }else{
                if(!DatabaseService.isUserExists(user)){
                    User registerUser = new User(user.getUserName(), user.getUserPassword(), Utils.getFormattedDate());

                    if(!DatabaseService.addUser(registerUser))
                        return;

                    openChatsPage(registerUser);
                }else{
                    ErrorDialog.show(new Exception(StringGetterWithCurrentLanguage.getString(StringsConstants.THIS_USER_ALREADY_EXISTS)));
                }
            }
        }else {
            Sounds.playWithIgnoreUserSettings(SoundsConstants.ERROR_SOUND);
        }
    }

    private void openChatsPage(@NotNull User user){
        try {
            OutsideSettingsManager.changeValue(OutsideSettingsKeys.REMEMBERED_USER_LOGIN, Utils.encodeString(user.getUserName()));
            OutsideSettingsManager.changeValue(OutsideSettingsKeys.REMEMBERED_USER_PASSWORD, Utils.encodeString(user.getUserPassword()));

            User userWithId = new User(user.getUserName(), user.getUserPassword(), user.getUserDateOfRegistration(), Long.parseLong(Objects.requireNonNull(DatabaseService.getValueWithWhereValue(DatabaseConstants.USER_ID_ROW, DatabaseConstants.USER_NAME_ROW, user.getUserName()))));

            ChatsPage chatsPage = new ChatsPage(this, getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.RECENT_CHATS), false, userWithId);
            chatsPage.open();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
