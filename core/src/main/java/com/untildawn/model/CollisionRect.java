package com.untildawn.model;

import com.badlogic.gdx.math.Vector2;

public class CollisionRect {

    float x;
    float y;
    float width;
    float height;

    public CollisionRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean overlaps(CollisionRect other) {
        float left = x - width/2;
        float right = x + width/2;
        float bottom = y - height/2;
        float top = y + height/2;

        // Check for overlap on both axes
        return left < other.getX() + other.getWidth() &&
           right > other.getX() &&
           bottom < other.getY() + other.getHeight() &&
           top > other.getY();
    }

    public void setPosition(Vector2 position) {
        this.x = position.x;
        this.y = position.y;
    }

    public void setPosition(float v, float v1) {
        this.x = v;
        this.y = v1;
    }

    public void setSize(float v, float v1) {
        this.width = v;
        this.height = v1;
    }
}
