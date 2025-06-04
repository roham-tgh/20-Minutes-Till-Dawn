package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class CursorManager {
    private static CursorManager instance;

    private CursorManager() {
        // Private constructor to prevent instantiation
        setCustomCursor("5.png"); // Replace with your cursor texture path
    }

    public static CursorManager getInstance() {
        if (instance == null) {
            instance = new CursorManager();
        }
        return instance;
    }

    private void setCustomCursor(String texturePath) {
    // Load your cursor texture
    Pixmap originalPixmap = new Pixmap(Gdx.files.internal(texturePath));

    // Create a new scaled pixmap (e.g., 32x32 pixels)
    int targetSize = 128;
    Pixmap scaledPixmap = new Pixmap(targetSize, targetSize, originalPixmap.getFormat());
    scaledPixmap.drawPixmap(originalPixmap,
            0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
            0, 0, targetSize, targetSize);

    // Create cursor with scaled pixmap
    Cursor cursor = Gdx.graphics.newCursor(scaledPixmap, scaledPixmap.getWidth() / 2, scaledPixmap.getHeight() / 2);
    Gdx.graphics.setCursor(cursor);

    // Dispose both pixmaps
    originalPixmap.dispose();
    scaledPixmap.dispose();
    }

    public void showCursor() {
        Gdx.input.setCursorCatched(false);
    }

    public void hideCursor() {
        Gdx.input.setCursorCatched(true);
    }
}
