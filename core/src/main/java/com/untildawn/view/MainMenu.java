package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untildawn.Main;
import com.untildawn.controller.MainMenuController;
import com.untildawn.model.AssetManager;
import com.untildawn.model.User;

import static com.badlogic.gdx.Gdx.gl;

public class MainMenu implements Screen {
    private static MainMenu mainMenu;
    private Stage stage;
    private final MainMenuController controller;
    private final TextButton play = new TextButton("Play", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new PreGameMenu());
            }
        });
    }};
    private final TextButton leaderBoard = new TextButton("LeaderBoard", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new LeaderBoardMenu());
            }
        });
    }};
    private final TextButton hint = new TextButton("Hint", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new HintMenu());
            }
        });
    }};
    private final TextButton settings = new TextButton("Settings", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new SettingsMenu());
            }
        });
    }};
    private final TextButton exit = new TextButton("Exit", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }};
    private final Table table;
    private final Label username;
    private final Label score;
    private final Image avatar;
    private final User currentUser;
    private final TextButton profile = new TextButton("Profile", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!currentUser.getUsername().equals("Guest")) Main.getMain().setScreen(new ProfileMenu());

            }
        });
    }};

    public MainMenu(User user) {
        mainMenu = this;
        controller = new MainMenuController();
        controller.setView(this);

        table = new Table();
        if (user == null) {
            this.currentUser = new User("Guest", "", "");
        } else {
            this.currentUser = user;
        }

        username = new Label("Username: ".concat(currentUser.getUsername()), AssetManager.getAssetManager().getSkin());
        username.setColor(Color.CORAL);
        score = new Label("Score: ".concat(currentUser.getScore()), AssetManager.getAssetManager().getSkin());
        score.setColor(Color.CORAL);
        avatar = new Image(currentUser.getAvatar());
        avatar.setSize(192, 192);
        avatar.setPosition(5, 880);
        username.setPosition(5, 830);
        score.setPosition(5, 800);
    }

    public static MainMenu getInstance() {
        return mainMenu;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        table.setDebug(true);
        table.center();
        table.row();
        table.add(play).pad(5.0f);
        table.row();
        table.add(leaderBoard).pad(5.0f);
        table.row();
        table.add(profile).pad(5.0f);
        table.row();
        table.add(hint).pad(5.0f);
        table.row();
        table.add(settings).pad(5.0f);
        table.row();
        table.add(exit).pad(5.0f);
        stage.addActor(table);
        stage.addActor(avatar);
        stage.addActor(username);
        stage.addActor(score);
    }

    @Override
    public void render(float v) {
        //ScreenUtils.clear(0, 0, 0, 0);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0, 0, 0, 0);
//        Main.getBatch().begin();
//        Main.getBatch().end();
        stage.getViewport().apply();
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
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

    public TextButton getPlay() {
        return play;
    }

    public TextButton getLeaderBoard() {
        return leaderBoard;
    }

    public TextButton getSettings() {
        return settings;
    }

    public TextButton getExit() {
        return exit;
    }
}
