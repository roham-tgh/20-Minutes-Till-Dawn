package com.untildawn.model.enums;

public enum Gun {
    REVOLVER("Revolver", 20, 1, 0.5f, 1.0f, 6, 300f, "Guns/Revolver/"),
    SHOTGUN("ShotGun", 10, 4, 0.1f, 1.0f, 2, 100f, "Guns/Shotgun/"),
    DUAL_SMG("Dual SMGs", 8, 1, 0.2f, 2.0f, 12, 200f, "Guns/DualSMG/");

    String name;
    int damage;
    int projectileCount;
    float shootingTime;

    public float getRange() {
        return range;
    }

    float range;
    public float getReloadTime() {
        return reloadTime;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public float getShootingTime() {
        return shootingTime;
    }

    public int getMagCapacity() {
        return magCapacity;
    }

    public String getAssetFolder() {
        return assetFolder;
    }

    float reloadTime;
    int magCapacity;
    String assetFolder;

     Gun(String name, int damage, int projectileCount, float shootingTime, float reloadTime, int magCapacity, float range, String assetFolder) {
        this.name = name;
        this.damage = damage;
        this.projectileCount = projectileCount;
        this.shootingTime = shootingTime;
        this.reloadTime = reloadTime;
        this.magCapacity = magCapacity;
        this.range = range;
        this.assetFolder = assetFolder;
    }
}
