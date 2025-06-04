package com.untildawn.model;

import com.untildawn.model.enums.GameTime;
import com.untildawn.model.enums.Gun;
import com.untildawn.model.enums.Hero;

public class GameSettings {
    private static GameSettings gameSettings;
    private final boolean autoReload = false;
    private final boolean blackAndWhite = false;
    private Gun gun = Gun.DUAL_SMG;
    private Hero hero = Hero.SHANA;
    private GameTime gameTime = GameTime.FIVE;

    public static GameSettings getInstance() {
        if (gameSettings == null) {
            gameSettings = new GameSettings();
        }
        return gameSettings;
    }

    public Gun getGun() {
        return gun;
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(GameTime gameTime) {
        this.gameTime = gameTime;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }
}
