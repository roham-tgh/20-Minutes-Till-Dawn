package com.untildawn.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.untildawn.Main;
import com.untildawn.model.Bullet;
import com.untildawn.model.Weapon;

import java.util.ArrayList;

public class BulletController {
    private ArrayList<Bullet> bullets;
    private Texture bulletTexture;
    private Sprite sprite;
//    private Weapon weapon;
    public BulletController() {
//        this.weapon = weapon;
        bullets = new ArrayList<>();
        bulletTexture = new Texture("Guns/Bullet/still.png"); // Add your bullet texture
        sprite = new Sprite(bulletTexture);
    }

    public void spawnBullet(Weapon weapon, float angle) {
    // Calculate direction using the angle
         Vector2 spawnPosition = weapon.position;
    Vector2 direction = new Vector2(
        (float) Math.cos(angle),
        (float) Math.sin(-angle)
    ).nor();

    Bullet bullet = new Bullet(
        spawnPosition.x,
        spawnPosition.y,
        direction.x,
        direction.y,
        300f,
        weapon.getRange(),
        (int) (weapon.getDamage()), new Texture("Guns/Bullet/still.png")
    );
    bullets.add(bullet);
}

    public void update(float delta) {
        // Use iterator pattern to safely remove bullets
        bullets.removeIf(bullet -> {
            bullet.update(delta);
            if (!bullet.isActive()) {
                return true; // Remove this bullet
            }
            Main.getBatch().draw(bulletTexture,
                bullet.getPosition().x - (float) bulletTexture.getWidth() /2,
                bullet.getPosition().y - (float) bulletTexture.getHeight() /2);
            return false; // Keep this bullet
        });
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void dispose() {
        bulletTexture.dispose();
    }
}
