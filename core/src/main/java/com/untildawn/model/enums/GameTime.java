package com.untildawn.model.enums;

public enum GameTime {
    TWO("2 Minutes", 2 * 60f),
    FIVE("5 Minutes", 5 * 60f),
    TEN("10 Minutes", 10 * 60f),
    TWENTY("20 Minutes", 20 * 60f);

    public String getName() {
        return name;
    }

    public float getTime() {
        return time;
    }

    String name;
    float time;
    GameTime(String s, float v) {
        this.name = s;
        this.time = v;
    }

}
