package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.additionalNodes.SettingsOption;
import com.ds.darknesschat.additionalNodes.SettingsOptionSwitchButton;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.eventListeners.IOnSwitch;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.Constants.ON_OFF_OPTIONS_LIST;
import static com.ds.darknesschat.client.ClientConstants.TRUE;
import static com.ds.darknesschat.database.DatabaseConstants.*;

public class SettingsAppearancePage extends Page {
    protected SettingsAppearancePage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        super(prevoiusPage, contentVbox, title, createStandardTile, user);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createOptionsSwitchButtons();
        SettingsPage.initResetButton(this, this::resetAppearanceSettings);
        SettingsPage.initBackButton(this);
        createDeveloperLabelInBottom();

        getTile().applyAlphaWithUserSettings(getUser());
    }

    private void resetAppearanceSettings(){
        DatabaseService.changeValue(ANIMATIONS_USING_ROW, TRUE, getUser().getId());
        DatabaseService.changeValue(SOUNDS_USING_ROW, TRUE, getUser().getId());
        DatabaseService.changeValue(OPACITY_LEVEL_ROW, "0.8", getUser().getId());

        Log.info("Appearance settings rested");
        reopen();
    }

    private void createOptionsSwitchButtons() {
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.ANIMATIONS), this::changeAnimations, ON_OFF_OPTIONS_LIST, getIndexValueForONAndOffList(DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(ANIMATIONS_USING_ROW, getUser().getId())))), true);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SOUNDS), this::changeSounds, ON_OFF_OPTIONS_LIST, getIndexValueForONAndOffList(DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(SOUNDS_USING_ROW, getUser().getId())))), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.OPACITY), this::changeAlpha, findAppearanceValues(), findAppearanceValues().indexOf(DatabaseService.getValue(OPACITY_LEVEL_ROW, getUser().getId())), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACKGROUND), currentValue -> {}, ON_OFF_OPTIONS_LIST, 0, false);
    }

    private int getIndexValueForONAndOffList(boolean value){
        return value ? 0: 1;
    }

    private void changeAnimations(String value) {
        int valueId = ON_OFF_OPTIONS_LIST.indexOf(value);
        DatabaseService.changeValue(ANIMATIONS_USING_ROW, valueId == 0 ? "true": "false", getUser().getId());
    }

    private void changeSounds(String value){
        int valueId = ON_OFF_OPTIONS_LIST.indexOf(value);
        DatabaseService.changeValue(SOUNDS_USING_ROW, valueId == 0 ? "true": "false", getUser().getId());
    }

    private void changeAlpha(String value){
        DatabaseService.changeValue(OPACITY_LEVEL_ROW, value, getUser().getId());
    }

    @Contract(pure = true)
    private @NotNull List<String> findAppearanceValues(){
        List<String> appearanceValuesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        for (double i = 0.12d; i < 1d; i+=0.01d) {
            appearanceValuesList.add(decimalFormat.format(i));
        }

        appearanceValuesList.add(String.valueOf(1d));

        return appearanceValuesList;
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
}
