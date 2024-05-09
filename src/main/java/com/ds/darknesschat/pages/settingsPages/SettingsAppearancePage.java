package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.additionalNodes.SettingsOption;
import com.ds.darknesschat.additionalNodes.SettingsOptionSwitchButton;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.utils.interfaces.IOnSwitch;
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

import static com.ds.darknesschat.Constants.ON_OFF_OPTIONS_LIST;

public class SettingsAppearancePage extends Page {
    protected SettingsAppearancePage(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        super(prevoiusPage, contentVbox, title, createStandardTile);
    }

    @Override
    public void onOpen() {
        applyDefaultPagePaddings();
        loadDefaultTileSettings();
        createOptionsSwitchButtons();
        SettingsPage.initResetButton(this, () -> {});
        SettingsPage.initBackButton(this);
        createDeveloperLabelInBottom();
    }

    private void createOptionsSwitchButtons() {
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.ANIMATIONS), currentValue -> {}, ON_OFF_OPTIONS_LIST, 0, true);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SOUNDS), currentValue -> {}, ON_OFF_OPTIONS_LIST, 0, false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.OPACITY), currentValue -> {}, findAppearanceValues(), 0, false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACKGROUND), currentValue -> {}, ON_OFF_OPTIONS_LIST, 0, false);
    }

    @Contract(pure = true)
    private @NotNull List<String> findAppearanceValues(){
        List<String> appearanceValuesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        for (double i = 0.12d; i < 1d; i+=0.1d) {
            appearanceValuesList.add(decimalFormat.format(i));
        }

        appearanceValuesList.add(String.valueOf(1d));

        return appearanceValuesList;
    }

    private void addOptionButton(String text, IOnSwitch onSwitch, List<String> switchValues, int startValue, boolean isFirst){
        try {
            SettingsOptionSwitchButton optionButton = new SettingsOptionSwitchButton(SettingsOption.DEFAULT_WIDTH, SettingsOption.DEFAULT_HEIGHT, text, switchValues, startValue);
            optionButton.setOnClick(onSwitch);
            VBox.setMargin(optionButton, new Insets(isFirst ? 50d : 5d, 40d, 0, 40d));
            addNodeToTile(optionButton);
        }catch (Exception e){
            Log.error(e);
        }

    }
}
