package com.untildawn.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.Main;

public abstract class Monster {
    public Vector2 position;
    public Vector2 velocity;
    protected float health;

    public float getSpeed() {
        return speed;
    }

    protected float speed;
    protected boolean isActive;
    protected Sprite currentFrame;
    protected TextureAtlas animationAtlas;
    protected Animation<TextureRegion> animation;
    protected CollisionRect collisionRect;
    protected float stateTime = 0;
    protected int width;
    protected int height;
    protected final static float DEACTIVATION_DISTANCE = Float.MAX_VALUE;
    protected boolean isGettingHit = false;

    public static float getDeactivationDistance() {
        return DEACTIVATION_DISTANCE;
    }

    public Monster(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.isActive = true;

    }

    public abstract void update(float delta, Player player);

    public boolean takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            Player.getPlayer().gainXP(3);
            isActive = false;
            return true;
        }
        return false;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        collisionRect.setPosition(position);
    }

    protected void updateCollisionRect() {
        if (currentFrame != null) {
            collisionRect.setPosition(
                position.x - currentFrame.getWidth()/2,
                position.y - currentFrame.getHeight()/2
            );
            collisionRect.setSize(
                currentFrame.getWidth() * 0.8f,  // slightly smaller than sprite
                currentFrame.getHeight() * 0.8f
            );
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void draw() {
        currentFrame.draw(Main.getBatch());
    }

    public CollisionRect getCollisionRect() {
        return new CollisionRect(position.x, position.y, currentFrame.getWidth(), currentFrame.getHeight());
    }


}
