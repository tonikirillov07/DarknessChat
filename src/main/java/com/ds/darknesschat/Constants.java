package com.ds.darknesschat;

import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;

import java.util.List;
import java.util.Objects;

public final class Constants {
    public static final String BUILT_IN_SETTINGS_PATH = "settings/settings.properties";
    public static final String OUTSIDE_SETTINGS_PATH = "editingAppSettings/settings.properties";
    public static final String DEFAULT_BACKGROUND_PATH = "bitmaps/background/background.png";
    public static final String LOGS_PATH = "logs/logs.txt";
    public static final String FONT_BOLD_PATH = "fonts/Inter-ExtraBold.ttf";
    public static final String FONT_BOLD_ITALIC_PATH = "fonts/Inter-BoldItalic.otf";
    public static final String ATTACHMENTS_FOLDER = "attachments";
    public static final String ATTACHMENT_PNG = "attachment.png";

    public static final String DEFAULT_BACKGROUND_VALUE = "DEFAULT";
    public static final String ANOTHER_BACKGROUND_VALUE = "ANOTHER";

    public static final Color WHITE_COLOR = new Color(255, 255, 255);
    public static final Color BLACK_COLOR = new Color(0, 0, 0);
    public static final Color TILE_COLOR = new Color(0.75f, 19, 19, 19);

    public static final long IGNORE_USER_AGREEMENT = -1L;

    public static final String NULL = "null";
    public static final String DISCONNECT_COMMAND = "/out";

    public static final int MAX_PORT_VALUE = 65535;
    public static final int MIN_PORT_VALUE = 1;
    public static final int MESSAGE_SIZE_LIMIT_IN_BYTES = 65535;
    public static final long CLIENT_AND_SERVER_UPDATE_DELAY_IN_MILLIS = 100L;

    public static final String[] SERVER_ADDRESS_REGEXES = {"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{3,5}", "localhost:\\d{3,5}"};

    public static final List<String> ON_OFF_OPTIONS_LIST = List.of(Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.ON)), Objects.requireNonNull(StringGetterWithCurrentLanguage.getString(StringsConstants.OFF)));
}
