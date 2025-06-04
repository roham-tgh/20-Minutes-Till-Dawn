package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpotlightShader {
    private ShaderProgram spotlightShader;
    private FrameBuffer frameBuffer;
    private TextureRegion frameRegion;
    private SpriteBatch batch;
    private float playerX, playerY;
    private float radius = 150f;
    private float softness = 0.2f;
    private float darkness = 0.8f;

    public SpotlightShader() {
        // Make sure we get shader compilation errors
        ShaderProgram.pedantic = true;

        // Create the spotlight shader
        spotlightShader = new ShaderProgram(
                Gdx.files.internal("spotlight.vert"),
                Gdx.files.internal("darknessShader.frag"));

        if (!spotlightShader.isCompiled()) {
            Gdx.app.error("SpotlightShader", "Shader compilation failed:\n" + spotlightShader.getLog());
        }

        // Create a framebuffer to render the scene to
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create a SpriteBatch for the final rendering step
        batch = new SpriteBatch();
        batch.setShader(spotlightShader);
    }

    /**
     * Call this when your screen size changes
     */
    public void resize(int width, int height) {
        // Dispose of old framebuffer if it exists
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }

        // Create new framebuffer with current screen size
        frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);
        frameRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        frameRegion.flip(false, true); // Required because FBO's y-axis is flipped
    }

    /**
     * Set the player position for the spotlight
     */
    public void setPlayerPosition(float x, float y) {
        this.playerX = x;
        this.playerY = y;
    }

    /**
     * Set the spotlight radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Set the spotlight edge softness (0-1)
     */
    public void setSoftness(float softness) {
        this.softness = Math.max(0f, Math.min(1f, softness));
    }

    /**
     * Set how dark the areas outside the spotlight are (0-1)
     */
    public void setDarkness(float darkness) {
        this.darkness = Math.max(0f, Math.min(1f, darkness));
    }

    /**
     * Begin rendering with the spotlight effect.
     * Everything drawn between begin() and end() will be affected by the spotlight.
     */
    public void begin(SpriteBatch gameBatch) {
        // Make sure the game's SpriteBatch isn't drawing
        if (gameBatch.isDrawing()) {
            gameBatch.end();
        }

        // Start rendering to our framebuffer
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * End rendering with the spotlight effect.
     * This will apply the spotlight shader to everything drawn since begin().
     */
    public void end(SpriteBatch gameBatch) {
        // End rendering to framebuffer
        frameBuffer.end();

        // Set shader uniforms
        spotlightShader.bind();
        spotlightShader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spotlightShader.setUniformf("playerPosition", playerX, playerY);
        spotlightShader.setUniformf("radius", radius);
        spotlightShader.setUniformf("softness", softness);
        spotlightShader.setUniformf("darkness", darkness);

        // Draw the framebuffer texture with the spotlight shader applied
        batch.begin();
        batch.draw(frameRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Return to normal rendering (without shader)
        if (gameBatch.getShader() != null) {
            gameBatch.setShader(null);
        }
    }

    /**
     * Dispose of resources when done
     */
    public void dispose() {
        if (spotlightShader != null) spotlightShader.dispose();
        if (frameBuffer != null) frameBuffer.dispose();
        if (batch != null) batch.dispose();
    }
}
