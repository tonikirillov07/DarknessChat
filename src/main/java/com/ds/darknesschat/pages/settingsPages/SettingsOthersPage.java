package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.additionalNodes.SettingsOption;
import com.ds.darknesschat.additionalNodes.SettingsOptionSwitchButton;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.eventListeners.IOnSwitch;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.client.ClientConstants.FALSE;
import static com.ds.darknesschat.client.ClientConstants.TRUE;

public class SettingsOthersPage extends Page {
    public SettingsOthersPage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        loadDefaultTileSettings();
        applyDefaultPagePaddings();
        
        createOptions();
        
        SettingsPage.initResetButton(this, this::resetAll);
        SettingsPage.initBackButton(this);

        goToPreviousPageByKey(true);
    }

    private void createOptions() {
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.NOTIFICATIONS), this::changeNotifications, Constants.ON_OFF_OPTIONS_LIST, DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(DatabaseConstants.USER_USING_NOTIFICATIONS_ROW, getUser().getId()))) ? 0: 1, true);
    }

    private void changeNotifications(@NotNull String currentValue) {
        DatabaseService.changeValue(DatabaseConstants.USER_USING_NOTIFICATIONS_ROW, currentValue.equals(Constants.ON_OFF_OPTIONS_LIST.getFirst()) ? TRUE : FALSE, getUser().getId());
    }

    private void addOptionButton(String text, IOnSwitch onSwitch, List<String> switchValues, int startValue, boolean isFirst){
        try {
            SettingsOptionSwitchButton optionButton = new SettingsOptionSwitchButton(SettingsOption.DEFAULT_WIDTH, SettingsOption.DEFAULT_HEIGHT, text, switchValues, startValue, getUser().getId());
            optionButton.setOnClick(onSwitch);
            VBox.setMargin(optionButton, new Insets(isFirst ? 50d : 5d, 40d, 0, 40d));
            addNodeToTile(optionButton);
        }catch (Exception e){
            Log.error(e);
        }

    }

    private void resetAll() {
        DatabaseService.changeValue(DatabaseConstants.USER_USING_NOTIFICATIONS_ROW, TRUE, getUser().getId());
        reopen();
    }
}
