package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untildawn.controller.LoginMenuController;
import com.untildawn.model.AssetManager;

import static com.badlogic.gdx.Gdx.gl;


public class LoginMenu implements Screen {
    private Stage stage;
    private LoginMenuController controller;

    private Label username;
    private Label password;
    private TextField usernameField;
    private TextField passwordField;
    private Label errorMessage;



    private TextButton login;
    private TextButton forgotPassword;
    private TextButton goToRegister;
    private Table table;

    public LoginMenu() {
        controller = new LoginMenuController();
        controller.setView(this);
        username = new Label("Enter your username: ", AssetManager.getAssetManager().getSkin());
        password = new Label("Enter your password: ", AssetManager.getAssetManager().getSkin());
        usernameField = new TextField("", AssetManager.getAssetManager().getSkin());
        usernameField.setMaxLength(15);
        passwordField = new TextField("", AssetManager.getAssetManager().getSkin());
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMaxLength(15);
        login = new TextButton("Login", AssetManager.getAssetManager().getSkin());
        forgotPassword = new TextButton("Forgot\nPassword?", AssetManager.getAssetManager().getSkin());
        goToRegister = new TextButton("Register", AssetManager.getAssetManager().getSkin());
        errorMessage = new Label("", AssetManager.getAssetManager().getSkin());
        errorMessage.setColor(Color.RED);
        table = new Table();
    }



    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
//        table.setDebug(true);
        table.center();
        table.row();
        table.add(username).pad(5.0f);
        table.add(usernameField).width(300.0f).pad(5.0f);
        table.row();
        table.add(password).pad(5.0f);
        table.add(passwordField).width(300.0f).pad(5.0f);
        table.row();
        errorMessage.setText(controller.getErrorMessage());
        table.add(errorMessage).colspan(2).spaceTop(20.0f).pad(15.0f);
        table.row();
        table.add(login).colspan(2).spaceTop(30.0f).pad(5.0f);
        table.row();
        table.add(forgotPassword).colspan(2).spaceTop(50.0f).pad(5.0f);
        table.row();
        table.add(goToRegister).colspan(2).spaceTop(20.0f).pad(5.0f);

        login.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleInput(login);
            }
        });

        forgotPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleInput(forgotPassword);
            }
        });

        goToRegister.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleInput(goToRegister);
            }
        });

        stage.addActor(table);
    }

     public void updateErrorMessage() {
        errorMessage.setText(controller.getErrorMessage());
    }

    @Override
    public void render(float v) {
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0, 0, 0, 0);
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

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextButton getForgotPassword() {
        return forgotPassword;
    }

    public TextButton getGoToRegister() {
        return goToRegister;
    }

    public TextButton getLogin() {
        return login;
    }
}
