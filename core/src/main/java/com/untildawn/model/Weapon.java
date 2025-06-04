package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.model.enums.Ability;
import com.untildawn.model.enums.Gun;

public class Weapon {
    private final float reloadTime;
    public float currentActionTimePassed;
    public Vector2 position;

    private int projectileCount = 1;
    private int magCapacity;
    private int ammo;
    private float damage;
    private final float range;
    private final float shootingTime;
    private final boolean isReloading;
    private float damagerInterval = 0f;

    public Weapon(Gun gun) {
        this.damage = gun.getDamage();

        this.projectileCount = gun.getProjectileCount();
        this.magCapacity = gun.getMagCapacity();
        ammo = magCapacity;
        this.reloadTime = gun.getReloadTime();
        this.shootingTime = gun.getShootingTime();
        this.range = gun.getRange();
        this.isReloading = false;
        this.position = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
    }

    public void update(float delta) {
        if (damagerInterval > 0f) {
            damagerInterval -= delta;
            if (damagerInterval <= 0f) {
                damage /= 2; // Reset damage after interval
            }
        }

    }

    public float getDamage() {
        return damage;
    }

    public void reload() {
        ammo = magCapacity;
    }

    public float getRange() {
        return range;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public float getShootingTime() {
        return shootingTime;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public int getMagCapacity() {
        return magCapacity;
    }

    public int getProjectileCount() {
        return projectileCount;
    }


    public void useAmmo() {
        if (ammo > 0) {
            ammo--;
        }
    }
    public int getAmmo() {
        return ammo;
    }


    public void activateDamager() {
        if (damagerInterval == 0f) {
            damage *= 2;
        }
        damagerInterval += 10f;
    }
    public void activateProcrease() {
        projectileCount += 1;
    }
    public void activateAmocrease() {
        magCapacity += 5;
        ammo += 5; // Ensure ammo is increased when capacity is increased
    }
}
