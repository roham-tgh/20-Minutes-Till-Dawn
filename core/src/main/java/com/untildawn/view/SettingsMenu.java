package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untildawn.model.AssetManager;
import com.untildawn.model.SoundSystem;

import static com.badlogic.gdx.Gdx.gl;

public class SettingsMenu implements Screen {

       private Label volumeLabel = new Label("Volume: ".concat(String.valueOf((SoundSystem.getInstance().getMusicVolume() * 100) % 100)).concat("%"), AssetManager.getAssetManager().getSkin());
    private Slider soundVolume = new Slider(0f, 1.0f, 0.01f, false, AssetManager.getAssetManager().getSkin()){{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Handle the slider change here
                float volume = getValue();
                SoundSystem.getInstance().setMusicVolume(volume);
                volumeLabel.setText("Volume: ".concat(String.valueOf(SoundSystem.getInstance().getMusicVolume() * 100)).concat("%"));
//                setValue(SoundSystem.getInstance().getMusicVolume());
            }
        });

    }};
    private SelectBox<String> music = new SelectBox<String>(AssetManager.getAssetManager().getSkin()) {{
        setItems("20mins.wav", "DOOM.mp3");
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Handle the selection change here
                String selectedMusic = getSelected();
                SoundSystem.getInstance().setMusic(selectedMusic);
            }
        });

    }};
    private CheckBox sfxOn = new CheckBox("Enable Sound Effects", AssetManager.getAssetManager().getSkin()){{
        setChecked(SoundSystem.getInstance().isSoundEnabled());
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Handle the checkbox change here
                boolean isChecked = isChecked();
                SoundSystem.getInstance().setSoundEnabled(isChecked);
            }
        });
    }};
    private Stage stage;
    private Table table;
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(volumeLabel).pad(5.0f);
        table.add(soundVolume).width(400f).pad(5.0f);
        table.row();
        table.add(music).pad(5.0f);
        table.row();
        table.add(sfxOn).pad(5.0f);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0, 0, 0, 0);
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
