package com.ds.darknesschat.utils;

import com.ds.darknesschat.utils.dialogs.ErrorDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Objects;

public class FilesChecker {
    public static boolean checkFiles(){
        File languageFolder = new File("languages");
        File databaseFolder = new File("database");
        File editingSettingsFile = new File("editingAppSettings/settings.properties");

        boolean isLanguagesFolderFine = false;
        boolean isDatabaseFolderFine = false;
        boolean isEditingSettingsFileFine;

        if(languageFolder.exists())
            isLanguagesFolderFine = Objects.requireNonNull(languageFolder.listFiles((dir, name) -> name.endsWith(".properties"))).length > 0;
        if(databaseFolder.exists())
            isDatabaseFolderFine = Objects.requireNonNull(databaseFolder.listFiles((dir, name) -> name.endsWith(".db"))).length > 0;
        isEditingSettingsFileFine = editingSettingsFile.exists();

        if(!isLanguagesFolderFine)
            ErrorDialog.show(new FileNotFoundException("No languages files found. Try to reinstall app"));

        if(!isDatabaseFolderFine)
            ErrorDialog.show(new FileNotFoundException("No database file found. Try to reinstall app"));

        if(!isEditingSettingsFileFine)
            ErrorDialog.show(new FileNotFoundException("No settings file found. Try to reinstall app"));

        return isLanguagesFolderFine & isDatabaseFolderFine & isEditingSettingsFileFine;
    }
}
