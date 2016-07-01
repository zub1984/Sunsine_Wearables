package com.example.jubair.wear.util;


public class ColorPreset {

    private int bgColor;
    private int textColor;

    public ColorPreset(int bgColor, int textColor) {
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
