package com.ds.darknesschat.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Color{
    private final int red;
    private final int green;
    private final int blue;
    private float alpha = 1f;

    public Color(float alpha, int blue, int green, int red) {
        this.alpha = checkAlpha(alpha);
        this.red = checkColorComponent(red);
        this.green = checkColorComponent(green);
        this.blue = checkColorComponent(blue);
    }

    public Color(int red, int green, int blue) {
        this.red = checkColorComponent(red);
        this.green = checkColorComponent(green);
        this.blue = checkColorComponent(blue);
    }

    private int checkColorComponent(int component){
        if(component < 0)
            component = 0;

        if(component > 255)
            component = 255;

        return component;
    }

    private float checkAlpha(float alpha){
        if(alpha < 0f)
            alpha = 0f;

        if(alpha > 1f)
            alpha = 1f;

        return alpha;
    }

    public String generateCssStyle(){
        return "rgba(" + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + getAlpha() +");";
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
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
