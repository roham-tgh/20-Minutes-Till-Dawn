package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BlackAndWhiteShader {
    private ShaderProgram shader;
    private boolean shaderEnabled = false;

    public BlackAndWhiteShader() {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(
            Gdx.files.internal("blackandwhite.vert"),
            Gdx.files.internal("blackandwhite.frag")
        );

        if (!shader.isCompiled()) {
            Gdx.app.error("BlackAndWhiteShader", "Shader compilation failed:\n" + shader.getLog());
        }
    }

    public void begin(SpriteBatch batch) {
        if (shaderEnabled) {
            batch.setShader(shader);
        }
    }

    public void end(SpriteBatch batch) {
        if (shaderEnabled) {
            batch.setShader(null);
        }
    }

    public void setEnabled(boolean enabled) {
        this.shaderEnabled = enabled;
    }

    public boolean isEnabled() {
        return shaderEnabled;
    }

    public void dispose() {
        if (shader != null) {
            shader.dispose();
        }
    }
}
