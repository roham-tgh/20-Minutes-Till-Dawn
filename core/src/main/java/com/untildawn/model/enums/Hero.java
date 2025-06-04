package com.untildawn.model.enums;

public enum Hero {
    SHANA("Shana", 4, 4.0f, "Heroes/Shana/"),
    DIAMOND("Diamond", 7, 1.0f, "Heroes/Diamond/"),
    SCARLET("Scarlet", 3, 5.0f, "Heroes/Scarlet/"),
    LILITH("Lilith", 5, 3.0f, "Heroes/Lilith/"),
    DASHER("Dasher", 2, 10.0f, "Heroes/Dasher/"),;

    String name;

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public float getSpeed() {
        return speed;
    }

    public String getAssetFolder() {
        return assetFolder;
    }

    int maxHealth;
    float speed;
    String assetFolder;

    Hero(String name, int maxHealth, float speed, String assetFolder) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.assetFolder = assetFolder;
    }
}
