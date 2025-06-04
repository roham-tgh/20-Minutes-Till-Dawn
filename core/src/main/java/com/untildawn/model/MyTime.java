package com.untildawn.model;

public class MyTime {
    private static MyTime instance;
    private float timePassed = 0;

    public static MyTime getInstance() {
        if (instance == null) {
            instance = new MyTime();
        }
        return instance;
    }

    public void update(float delta) {
        timePassed += delta;
    }

    public boolean timeUp() {
        return timePassed >= GameSettings.getInstance().getGameTime().getTime();
    }

    public int getTimePassed() {
        return (int) timePassed;
    }

    public void skipTime() {
        timePassed += 15f;
    }

    public String getTimeUi() {
        int minutesPassed = (int) (timePassed / 60);
        int secondsPassed = (int) (timePassed % 60);
        return String.format("%02d:%02d", minutesPassed, secondsPassed);
    }
}
