package com.untildawn.model.enums;

import com.badlogic.gdx.graphics.Pixmap;

public enum Ability {
    VITALITY(
            "Vitality",
            "Increases your health by 1.",
            "Ability/Vitality.png"),
    DAMAGER(
            "Damager",
            "Double your damage for 10 seconds.",
            "Ability/Damager.png"),
    PROCREASE(
            "Procrease",
            "Increases your weapon projectiles by 1.",
            "Ability/Procrease.png"),

    AMOCREASE(
            "Ammocrease",
            "Increases your ammo by 5.",
            "Ability/Amocrease.png"),
    SPEEDY(
            "Speedy",
            "Double your speed for 10 seconds.",
            "Ability/Speedy.png");

    private final String name;
    private final String description;
    private final String path;
    Ability(String name, String description, String path) {
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
