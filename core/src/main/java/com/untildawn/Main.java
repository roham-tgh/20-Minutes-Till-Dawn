package com.untildawn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untildawn.controller.GameController;
import com.untildawn.controller.MonsterController;
import com.untildawn.controller.PlayerController;
import com.untildawn.controller.WeaponController;
import com.untildawn.model.CursorManager;
import com.untildawn.model.SoundSystem;
import com.untildawn.model.User;
import com.untildawn.model.enums.Gun;
import com.untildawn.model.enums.Hero;
import com.untildawn.view.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;

    public SoundSystem getSoundSystem() {
        return SoundSystem.getInstance();
    }


    public static SpriteBatch getBatch() {
        return batch;
    }


    public static Main getMain() {
        return main;
    }


    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        Gdx.graphics.setWindowedMode(1920, 1080);
        main.setScreen(new MainMenu(null));
        CursorManager.getInstance().showCursor();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
