package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.model.enums.Hero;

public class Player {
    private static Player player;
    private final static float INVINCIBILITY_INTERVAL = 1f;
    public Vector2 position;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> idleAnimation;
    private int maxHealth;
    private int health;
    private float invincibilityTimePassed;
    //    private float invincibilityDuration;
    private final CollisionRect collisionRect;
    private float speedyInterval = 0f;
    private float stateTime;
    private float speed;

    private int xp = 0;
    private int kills;
    private int level = 1;

    public Player(Hero hero) {
        player = this;
        this.maxHealth = hero.getMaxHealth();
        this.health = maxHealth;
        this.invincibilityTimePassed = 0;
        this.speed = hero.getSpeed();
        this.position = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        collisionRect = new CollisionRect(
        position.x - 12f,  // half of width
        position.y - 15f,  // half of height
        24f,  // width
        30f   // height
    );

        stateTime = 0;
    }

    public static Player getPlayer() {
        return player;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public float getWidth() {
        return 24f; // Adjust based on your sprite size
    }

    public float getHeight() {
        return 30f; // Adjust based on your sprite size
    }

    public void update(float delta) {
        stateTime += delta;
        if (speedyInterval > 0f) {
            speedyInterval -= delta;
            if (speedyInterval <= 0) {
                speed /= 2;
                speedyInterval = 0f;
            }
        }
        if (invincibilityTimePassed > 0f) {
            invincibilityTimePassed -= delta;
            if (invincibilityTimePassed <= 0) {
                invincibilityTimePassed = 0;
            }
        }

        updateCollisionRect();
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public void takeDamage() {
        System.out.println("YEOUCH");

        if (invincibilityTimePassed > 0) {
            return; // Player is invincible, ignore damage
        }

        --health;
        SoundSystem.getInstance().playSound("playerHit");
        invincibilityTimePassed = INVINCIBILITY_INTERVAL;
        if (health <= 0) {

        }
    }

    public void updateCollisionRect() {
    collisionRect.setPosition(
        position.x - getWidth()/2,
        position.y - getHeight()/2
    );
}

    public void activateSpeedy() {
        if (speedyInterval == 0f) {
            speed *= 2;
        }
        speedyInterval += 10f;
    }

    public void activateVitality() {
        health += 1;
        maxHealth += 1;
    }

    public void gainXP(int amount) {
        xp += amount;
        if (xp >= 10 * level * (level + 1)) { // Example level-up condition
            levelUp();
        }
    }

    private void levelUp() {
    }

    public int getLevel() {
        return level;
    }

    public float getXp() {
        return xp;
    }
}
