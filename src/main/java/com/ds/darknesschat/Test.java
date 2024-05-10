package com.ds.darknesschat;

import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.FilesChecker;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        OutsideSettingsManager.changeValue(OutsideSettingsKeys.CHANGING_APP_FIRST_TIME, "true");
    }
}
