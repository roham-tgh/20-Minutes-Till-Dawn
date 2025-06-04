package com.untildawn.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.Main;

public class BrainEater extends Monster {

    private boolean isFacingLeft = false;

    private static final TextureAtlas SHARED_ANIMATION_ATLAS = new TextureAtlas("Monsters/BrainEater/animation.atlas");

    public BrainEater(float x, float y) {
        super(x, y);
        health = 25;
        speed = 30f;
        animationAtlas = SHARED_ANIMATION_ATLAS;
        animation = new Animation<>(0.2f, animationAtlas.getRegions(), Animation.PlayMode.LOOP);
        collisionRect = new CollisionRect(x, y, animationAtlas.getRegions().get(0).getRegionWidth(), animationAtlas.getRegions().get(0).getRegionHeight());
        isActive = true;
        currentFrame = new Sprite(animation.getKeyFrame(0f, true));
        currentFrame.setPosition(position.x - currentFrame.getWidth() / 2, position.y - currentFrame.getHeight() / 2);
    }


   @Override
public void update(float delta, Player player) {
    Vector2 playerPosition = player.position;
    stateTime += delta;

    // Calculate direction and move towards player
    Vector2 direction = new Vector2(playerPosition).sub(position).nor();
    position.add(direction.x * speed * delta, direction.y * speed * delta);

    // Update sprite and collision rect position
    currentFrame = new Sprite(animation.getKeyFrame(stateTime, true));
    currentFrame.setPosition(position.x - (float) width / 2, position.y - (float) height / 2);
//    collisionRect.setPosition(position.x - (float) width / 2, position.y - (float) height / 2);

    handleRotation(playerPosition.x, playerPosition.y);

    float distance = new Vector2(playerPosition).sub(position).len2();
    if (distance >= DEACTIVATION_DISTANCE) {
        isActive = false;
        return;
    }
    updateCollisionRect();
    // Check collision after movement
    if (isActive && getCollisionRect().overlaps(player.getCollisionRect())) {
        player.takeDamage();
    }
}

    public void draw() {
        currentFrame.draw(Main.getBatch());
    }

    private void handleRotation(float x, float y) {
        float dy = y - position.y;
        float dx = x - position.x;
        float angle = (float) (Math.atan2(dy, dx));
        if (Math.cos(angle) < 0 && !isFacingLeft) {
            isFacingLeft = true;

        }
        else if (Math.cos(angle) > 0 && isFacingLeft){
            isFacingLeft = false;
        }
        if ((isFacingLeft && !currentFrame.isFlipX()) ||
            (!isFacingLeft && currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }

//        this.currentAngle = angle;
//        currentFrame.setRotation((float) (Math.PI - angle * MathUtils.radiansToDegrees));
    }
//    public void update(float delta, Vector2 playerPosition) {
//        // Calculate direction to player
//        Vector2 direction = new Vector2(playerPosition).sub(position).nor();
//        velocity.set(direction.scl(speed));
//        position.add(velocity.x * delta, velocity.y * delta);
//        collisionRect.setPosition(position);
//
//        stateTime += delta;
//        currentFrame = new Sprite(animation.getKeyFrame(delta, true));
//
//        Main.getBatch().draw(currentFrame, position.x, position.y);
//    }
}
