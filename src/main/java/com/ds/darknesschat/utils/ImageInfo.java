package com.ds.darknesschat.utils;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record ImageInfo(String path, Image image, byte[] imageBytes) {
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "ImageInfo{" +
                "path='" + path + '\'' +
                ", image=" + image +
                ", imageBytes=" + Arrays.toString(imageBytes) +
                '}';
    }
}
