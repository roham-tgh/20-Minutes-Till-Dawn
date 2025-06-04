package com.untildawn.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.Main;

public class Bullet {
    private Texture texture;
    private final int damage;
    private final float speed;
    private final float range;
    private final Vector2 position;
    private final Vector2 direction;
    private float distanceTraveled;
    private boolean active;

    private CollisionRect collisionRect;

    public Bullet(float x, float y, float directionX, float directionY, float speed, float range, int damage, Texture texture) {
        this.position = new Vector2(x, y);
        this.direction = new Vector2(directionX, directionY).nor(); // Normalize for consistent speed
        this.speed = speed;
        this.range = range;
        this.damage = damage;
        this.distanceTraveled = 0;
        this.active = true;
        this.texture = texture;
    }

    public float getWidth() {
        return texture.getWidth();
    }
    public float getHeight() {
        return texture.getHeight();
    }

    public int getDamage() {
        return damage;
    }

    public CollisionRect getCollisionRect() {
        return new CollisionRect(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void update(float delta) {
        if (!active) return;

        // Update position
        float movement = speed * delta;
        position.x += direction.x * movement;
        position.y += direction.y * movement;

        // Track distance
        distanceTraveled += movement;

        // Deactivate if exceeded range
        if (distanceTraveled >= range) {
            active = false;
        }

        Main.getBatch().draw(texture, position.x - (float) texture.getWidth() / 2, position.y - (float) texture.getHeight() / 2);
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void deactivate() {
        active = false;
    }
}
