package com.untildawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.Main;
import com.untildawn.model.Player;
import com.untildawn.model.enums.Hero;

public class PlayerController {
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> idleAnimation;
    TextureAtlas runAtlas;
    TextureAtlas idleAtlas;
    private TextureRegion currentFrame;
    private float stateTime;
    private boolean isFacingRight = true;
    private boolean isRunning = false;
    private final Player player;

    public PlayerController(Hero hero) {
        player = new Player(hero);
        runAtlas = new TextureAtlas(hero.getAssetFolder().concat("run.atlas"));
        idleAtlas = new TextureAtlas(hero.getAssetFolder().concat("idle.atlas"));
        runAnimation = new Animation<>(0.1f, runAtlas.getRegions(), Animation.PlayMode.LOOP);
        idleAnimation = new Animation<>(0.09f, idleAtlas.getRegions(), Animation.PlayMode.LOOP);
        stateTime = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void update(float delta) {
        stateTime += delta;

        Animation<TextureRegion> currentAnimation = (!isRunning) ? idleAnimation : runAnimation;

        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        if ((isFacingRight && currentFrame.isFlipX()) || (!isFacingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }
        player.update(delta);
        //the width is 24 and the height is 30
        Main.getBatch().draw(currentFrame, player.position.x - (float) currentFrame.getRegionWidth() / 2, player.position.y - (float) currentFrame.getRegionHeight() / 2); // Draw the current frame at the player's position
    }

    public void setFacingRight(boolean facingRight) {
        if (isFacingRight != facingRight) {
            isFacingRight = facingRight;
        }
    }

    public void setRunning(boolean running) {
        if (isRunning != running) {
            isRunning = running;
            stateTime = 0; // Reset animation state time when changing animation
        }
    }

    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector2 movement = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x -= 1;
            setRunning(true);
            setFacingRight(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x += 1;
            setRunning(true);
            setFacingRight(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y += 1;
            setRunning(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y -= 1;
            setRunning(true);
        }

        if (movement.len2() > 0) {
            movement.nor();
            player.position.x += movement.x * player.getSpeed() * 30 * deltaTime;
            player.position.y += movement.y * player.getSpeed() * 30 * deltaTime;
        } else {
            setRunning(false);
        }
    }
}
