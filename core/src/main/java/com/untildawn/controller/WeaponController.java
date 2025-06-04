package com.untildawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.untildawn.Main;
import com.untildawn.model.GameSettings;
import com.untildawn.model.SoundSystem;
import com.untildawn.model.Weapon;
import com.untildawn.model.enums.Gun;

import java.util.ArrayList;

public class WeaponController {
    private static final float WEAPON_RADIUS = 15f;
    private static final float SPREAD_ANGLE = 10f;
    private static final float WEAPON_SPREAD_ANGLE = (30f);
    private final ArrayList<Weapon> weapons;
    private final ArrayList<Float> weaponAngles;
    private final Sprite currentSprite;
    private boolean isFacingLeft;
    private final TextureAtlas reloadAtlas;
    private float currentAngle = 0f;
    private State state;
    private float currentActionTimePassed = 0f;
    private final Texture stillTexture;
    private final Animation<TextureRegion> reloadAnimation;
    private final float stateTime = 0;
    private final BulletController bulletController;
    private int ammo;
    private int maxAmmo;

    private int projectileCount;
    private int damage;

    public int getAmmo() {
        return ammo;
    }
    public int getMaxAmmo() {
        return maxAmmo;
    }

    public WeaponController(Gun gun, int weaponCount) {
        weapons = new ArrayList<>();
        weaponAngles = new ArrayList<>();
        float totalSpread = WEAPON_SPREAD_ANGLE * (weaponCount - 1);
        float startAngle = -(totalSpread / 2);
        for (int i = 0; i < weaponCount; i++) {
            weapons.add(new Weapon(gun));
            // Calculate and store fixed angle offset for each weapon
            float weaponAngle = (float) Math.toRadians(startAngle + (WEAPON_SPREAD_ANGLE * i));
            weaponAngles.add(weaponAngle);
        }
        reloadAtlas = new TextureAtlas(gun.getAssetFolder().concat("reload.atlas"));
        stillTexture = new Texture(gun.getAssetFolder().concat("still.png"));
        reloadAnimation = new Animation<>(0.1f, reloadAtlas.getRegions(), Animation.PlayMode.LOOP);
        currentSprite = new Sprite(stillTexture);
        currentSprite.setOriginCenter();
        state = State.IDLE;
        isFacingLeft = false;
        bulletController = new BulletController();
        maxAmmo = weapons.get(0).getAmmo() * weaponCount;
        ammo = maxAmmo;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public void handleRotation(int x, int y) {
        float centerX = (float) Gdx.graphics.getWidth() / 2;
        float centerY = (float) Gdx.graphics.getHeight() / 2;
        float dy = y - centerY;
        float dx = x - centerX;
        float angle = (float) (Math.atan2(dy, dx));
        if (Math.cos(angle) < 0 && !isFacingLeft) {
            isFacingLeft = true;
//            currentSprite.flip(false, true);
        } else if (Math.cos(angle) > 0 && isFacingLeft) {
            isFacingLeft = false;
//            currentSprite.flip(false, true);
        }
        this.currentAngle = angle;
        currentSprite.setRotation((float) (Math.PI - angle * MathUtils.radiansToDegrees)); // -90 to adjust for sprite's default orientation
    }

    public void update(float playerX, float playerY, float delta) {
        switch (state) {
            case IDLE:
                currentSprite.setRegion(stillTexture);
                break;
            case RELOADING:
                currentActionTimePassed += delta;
                currentSprite.setRegion(reloadAnimation.getKeyFrame(currentActionTimePassed, true));
                if (currentActionTimePassed >= getReloadTime()) {
                    currentActionTimePassed = 0;
                    ammo = maxAmmo;
                    state = State.IDLE;
                    SoundSystem.getInstance().playSound("weaponReady");
                }
                break;
            case SHOOTING:
                currentSprite.setRegion(stillTexture); //TODO make shooting animation
                currentActionTimePassed += delta;
                if (currentActionTimePassed >= getShootingTime()) {
                    currentActionTimePassed = 0;
                    state = State.IDLE;
                    if (ammo == 0) {
                        if (GameSettings.getInstance().isAutoReload()) {
                            handleReload(); // Automatically reload if out of ammo
                        }
                    }
                }
                break;
        }
        if (isFacingLeft) {
            currentSprite.flip(false, true);
        }
        // Calculate angle between player and mouse
        // Calculate weapon position around player
        for (int i = 0; i < weapons.size(); i++) {
            weapons.get(i).update(delta);
            weapons.get(i).position.x = playerX + WEAPON_RADIUS * (float) Math.cos(-currentAngle + weaponAngles.get(i));
            weapons.get(i).position.y = playerY + WEAPON_RADIUS * (float) Math.sin(-currentAngle + weaponAngles.get(i));
            currentSprite.setPosition(weapons.get(i).position.x - currentSprite.getWidth() / 2, weapons.get(i).position.y - currentSprite.getHeight() / 2);
            currentSprite.draw(Main.getBatch());
        }
//        weapon.position.x = playerX + WEAPON_RADIUS * (float) Math.cos(-currentAngle);
//        weapon.position.y = playerY + WEAPON_RADIUS * (float) Math.sin(-currentAngle);

        // Update sprite position and draw

        bulletController.update(delta);
    }

    private float getShootingTime() {
        return weapons.get(0).getShootingTime();
    }

    private float getReloadTime() {
        return weapons.get(0).getReloadTime();
    }

    public void handleShoot(int x, int y) {
        if (state == State.SHOOTING || state == State.RELOADING) {
            return; // Ignore input if already shooting or reloading
        }
        if (ammo == 0) {
            return;
        }
        int bulletCount = weapons.get(0).getProjectileCount();
        float angleInRadians = currentAngle;
        for (Weapon weapon : weapons) {
            if (bulletCount == 1) {
                // Single bullet - shoot straight
                bulletController.spawnBullet(weapon, angleInRadians);
            } else {
                // Multiple bullets - calculate spread
                float totalSpread = SPREAD_ANGLE * (bulletCount - 1);
                float startAngle = angleInRadians - (float) Math.toRadians(totalSpread / 2);

                for (int i = 0; i < bulletCount; i++) {
                    float bulletAngle = startAngle + (float) Math.toRadians(SPREAD_ANGLE * i);
                    bulletController.spawnBullet(weapon, bulletAngle);
                }
            }

        }
        SoundSystem.getInstance().playSound("shoot");
        state = State.SHOOTING;
        useAmmo();


    }

    private void useAmmo() {
        ammo -= weapons.size();
    }

    public void handleReload() {
        if (state == State.RELOADING) {
            return;
        }
        state = State.RELOADING;
        SoundSystem.getInstance().playSound("reloading");
        currentActionTimePassed = 0;

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            handleReload();
        }
    }


    enum State {
        IDLE, RELOADING, SHOOTING
    }

    public void activateDamager() {
        for (Weapon weapon : weapons) {
            weapon.activateDamager();
        }
    }
    public void activateProcrease() {
        for (Weapon weapon : weapons) {
            weapon.activateProcrease();
        }
    }
    public void activateAmocrease() {
        for (Weapon weapon : weapons) {
            weapon.activateAmocrease();
        }
    }
}
