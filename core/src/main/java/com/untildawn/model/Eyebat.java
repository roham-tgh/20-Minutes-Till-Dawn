package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.untildawn.Main;
import com.untildawn.model.enums.MonsterType;

import java.util.ArrayList;

public class Eyebat extends Monster {
    private final static float ATTACK_DISTANCE = 150f;
    private final static float PROJECTILE_INTERVAL = 3f;
    private final static float PROJECTILE_SPEED = 120f;
    private static final float PROJECTILE_RANGE = 360f;
    private boolean isShooting;
    private float lastProjectileTime = 0f;
    private Bullet projectile;
//    private static ArrayList<Bullet> projectiles = new ArrayList<>();
    public Eyebat(float x, float y) {
        super(x, y);
        health = 50;
        speed = 20f;
        this.animationAtlas = new TextureAtlas("Monsters/Eyebat/animation.atlas");
        this.animation = new Animation<>(0.2f, animationAtlas.getRegions(), Animation.PlayMode.LOOP);
        collisionRect = new CollisionRect(x, y, animationAtlas.getRegions().get(0).getRegionWidth(), animationAtlas.getRegions().get(0).getRegionHeight());
        isShooting = false;
        isActive = true;
        currentFrame = new Sprite(animation.getKeyFrame(0f, true));
        currentFrame.setPosition(position.x - currentFrame.getWidth() / 2, position.y - currentFrame.getHeight() / 2);
    }

    public boolean isShooting() {
        return isShooting;
    }

    @Override
    public void update(float delta, Player player) {
        Vector2 playerPosition = new Vector2(player.position).sub(player.getWidth() / 2, player.getHeight() / 2);
        stateTime += delta;
        currentFrame = new Sprite(animation.getKeyFrame(stateTime, true));
        currentFrame.setPosition(position.x - (float) width / 2, position.y - (float) height / 2);
        handleRotation(playerPosition);

        float distance = new Vector2(playerPosition).sub(position).len();

        if (distance >= DEACTIVATION_DISTANCE) {
            isActive = false;
        }

        lastProjectileTime += delta;

        if (distance <= ATTACK_DISTANCE) {
            // Stop moving, shoot at player
            isShooting = true;
            handleShoot(playerPosition);
            // Do not move towards player
        } else {
            // Chase player
            isShooting = false;
            Vector2 direction = new Vector2(playerPosition).sub(position).nor();
            position.add(direction.scl(speed * delta));
        }

        updateCollisionRect();
        if (getCollisionRect().overlaps(player.getCollisionRect())) {
            player.takeDamage();
        }


        if (projectile != null) {
            projectile.update(delta);
            if (projectile.getCollisionRect().overlaps(player.getCollisionRect())) {
                player.takeDamage();
                projectile.deactivate();
                projectile = null;
            }
        }
    }

    public void draw() {
        currentFrame.draw(Main.getBatch());
    }

    private void handleRotation(Vector2 playerPosition) {
        float dy = position.y - playerPosition.y;
        float dx = position.x - playerPosition.x;
        float angle = MathUtils.atan2(dy, dx);
        currentFrame.setRotation(180 + angle * MathUtils.radiansToDegrees);
    }

    private void handleShoot(Vector2 playerPosition) {
        if (lastProjectileTime >= PROJECTILE_INTERVAL) {
            Vector2 bulletPosition = new Vector2(position.x + currentFrame.getWidth()/2, position.y + currentFrame.getHeight()/2);
            projectile = new Bullet(bulletPosition.x, bulletPosition.y, playerPosition.x - bulletPosition.x, playerPosition.y - bulletPosition.y,
                                    PROJECTILE_SPEED, PROJECTILE_RANGE, 1, new Texture("Monsters/Eyebat/EyeMonsterProjecitle.png"));

            lastProjectileTime = 0f;
        }
    }

}
