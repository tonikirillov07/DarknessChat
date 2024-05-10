package com.ds.darknesschat.user;

import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserSettings {
    public static float getUserAlphaLevel(@NotNull User user){
        return Float.parseFloat(Objects.requireNonNull(DatabaseService.getValue(DatabaseConstants.OPACITY_LEVEL_ROW, user.getId())));
    }
}
