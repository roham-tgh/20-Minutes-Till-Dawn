package com.untildawn.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class HitEffect {
    private Vector2 position;
    private float stateTime = 0f;
    private Animation<TextureRegion> animation;
    private boolean finished = false;

    public HitEffect(Vector2 position, Animation<TextureRegion> animation) {
        this.position = position;
        this.animation = animation;
    }

    public void update(float delta) {
        stateTime += delta;
        if (animation.isAnimationFinished(stateTime)) {
            finished = true;
        }
    }

    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch) {
        TextureRegion frame = animation.getKeyFrame(stateTime, false);
        batch.draw(frame, position.x, position.y);
    }

    public boolean isFinished() {
        return finished;
    }
}
