package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.additionalNodes.SettingsOption;
import com.ds.darknesschat.additionalNodes.SettingsOptionSwitchButton;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.eventListeners.IOnSwitch;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.Constants.*;
import static com.ds.darknesschat.client.ClientConstants.FALSE;
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
        List<String> backgroundOptionsList = List.of(DEFAULT_BACKGROUND_VALUE, ANOTHER_BACKGROUND_VALUE);
        int backgroundIndex = Objects.equals(DatabaseService.getValue(BACKGROUND_PATH_ROW, getUser().getId()), DEFAULT_BACKGROUND_VALUE) ? 0 : 1;

        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.ANIMATIONS), this::changeAnimations, ON_OFF_OPTIONS_LIST, getIndexValueForONAndOffList(DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(ANIMATIONS_USING_ROW, getUser().getId())))), true);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.SOUNDS), this::changeSounds, ON_OFF_OPTIONS_LIST, getIndexValueForONAndOffList(DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(SOUNDS_USING_ROW, getUser().getId())))), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.OPACITY), this::changeAlpha, findAppearanceValues(), findAppearanceValues().indexOf(DatabaseService.getValue(OPACITY_LEVEL_ROW, getUser().getId())), false);
        addOptionButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACKGROUND), this::changeBackground, backgroundOptionsList, backgroundIndex, false);
    }

    private void changeBackground(@NotNull String currentValue) {
        try {
            File temporaryFolder = new File(TEMPORARY_FOLDER);

            switch (currentValue){
                case DEFAULT_BACKGROUND_VALUE -> {
                    DatabaseService.changeValue(BACKGROUND_PATH_ROW, DEFAULT_BACKGROUND_VALUE, getUser().getId());
                    updateBackground(getUser().getId());

                    if(temporaryFolder.exists()) {
                        String[] temporaryFilesList = temporaryFolder.list();

                        assert temporaryFilesList != null;
                        for (String fileName : temporaryFilesList) {
                            File currentFile = new File(TEMPORARY_FOLDER + "/" + fileName);

                            if(currentFile.canWrite()){
                                Log.info("File " + currentFile + " deleted with result " + currentFile.delete());
                            }
                        }
                    }
                }
                case ANOTHER_BACKGROUND_VALUE -> {
                    File file = Utils.openFileDialog(StringGetterWithCurrentLanguage.getString(StringsConstants.SELECT_YOUR_BACKGROUND_FILE), DatabaseService.getValue(USER_LAST_BACKGROUND_PATH, getUser().getId()),
                            getStage(), List.of(new FileChooser.ExtensionFilter(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.IMAGES)), "*.png*", "*.jpg*", "*.jpeg*"),
                                    new FileChooser.ExtensionFilter(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.EVERYTHING)), "*.*")));

                    if (file != null) {
                        if(!temporaryFolder.exists())
                            temporaryFolder.mkdir();

                        FileUtils.copyFileToDirectory(file, temporaryFolder);

                        DatabaseService.changeValue(USER_LAST_BACKGROUND_PATH, file.getParent(), getUser().getId());
                        DatabaseService.changeValue(BACKGROUND_PATH_ROW, temporaryFolder.getName() + "/" + file.getName(), getUser().getId());
                        updateBackground(getUser().getId());
                    }
                }
            }
        }catch (Exception e){
            Log.error(e);
        }
    }

    private int getIndexValueForONAndOffList(boolean value){
        return value ? 0: 1;
    }

    private void changeAnimations(String value) {
        int valueId = ON_OFF_OPTIONS_LIST.indexOf(value);
        DatabaseService.changeValue(ANIMATIONS_USING_ROW, valueId == 0 ? TRUE: FALSE, getUser().getId());
    }

    private void changeSounds(String value){
        int valueId = ON_OFF_OPTIONS_LIST.indexOf(value);
        DatabaseService.changeValue(SOUNDS_USING_ROW, valueId == 0 ? TRUE: FALSE, getUser().getId());
    }

    private void changeAlpha(String value){
        DatabaseService.changeValue(OPACITY_LEVEL_ROW, value, getUser().getId());
        getTile().setColor(new Color(Float.parseFloat(value), TILE_COLOR.getRed(), TILE_COLOR.getGreen(), TILE_COLOR.getBlue()));
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
