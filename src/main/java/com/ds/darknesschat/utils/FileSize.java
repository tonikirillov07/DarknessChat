package com.ds.darknesschat.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record FileSize(String format, double size) {
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "FileSize{" +
                "format='" + format + '\'' +
                ", size=" + size +
                '}';
    }
}
