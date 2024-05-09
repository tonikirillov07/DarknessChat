package com.ds.darknesschat;

import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;

import java.util.List;
import java.util.Objects;

public final class Constants {
    public static final String BUILT_IN_SETTINGS_PATH = "settings/settings.properties";
    public static final String OUTSIDE_SETTINGS_PATH = "editingAppSettings/settings.properties";
    public static final String LOGS_PATH = "logs/logs.txt";
    public static final String FONT_BOLD_PATH = "fonts/Inter-ExtraBold.ttf";
    public static final String FONT_BOLD_ITALIC_PATH = "fonts/Inter-BoldItalic.otf";
    public static final List<String> ON_OFF_OPTIONS_LIST = List.of(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.ON)), Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.OFF)));
}
