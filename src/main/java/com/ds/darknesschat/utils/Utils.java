package com.ds.darknesschat.utils;

import com.ds.darknesschat.Main;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Utils {
    @Contract("_ -> new")
    public static @NotNull Image getImage(String resourcePath){
        return new Image(Objects.requireNonNull(Main.class.getResourceAsStream(resourcePath)));
    }
}
