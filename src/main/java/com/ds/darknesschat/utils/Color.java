package com.ds.darknesschat.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Color(int red, int green, int blue) {
    public Color {
        red = checkColorComponent(red);
        green = checkColorComponent(green);
        blue = checkColorComponent(blue);
    }

    private int checkColorComponent(int component){
        if(component < 0)
            component = 0;

        if(component > 255)
            component = 255;

        return component;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
