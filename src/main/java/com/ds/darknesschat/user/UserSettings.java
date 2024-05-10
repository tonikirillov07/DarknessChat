package com.ds.darknesschat.user;

import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.ds.darknesschat.Constants.IGNORE_USER_AGREEMENT;

public class UserSettings {
    public static float getUserAlphaLevel(@NotNull User user){
        return Float.parseFloat(Objects.requireNonNull(DatabaseService.getValue(DatabaseConstants.OPACITY_LEVEL_ROW, user.getId())));
    }

    public static boolean getUserAnimationsUsing(long userId){
        return userId == IGNORE_USER_AGREEMENT || DatabaseService.getBoolean(Objects.requireNonNull(DatabaseService.getValue(DatabaseConstants.ANIMATIONS_USING_ROW, userId)));
    }
}
