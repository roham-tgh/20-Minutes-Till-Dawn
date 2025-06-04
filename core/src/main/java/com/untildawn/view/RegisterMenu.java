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
import com.untildawn.controller.RegisterMenuController;
import com.untildawn.model.AssetManager;

import static com.badlogic.gdx.Gdx.gl;


public class RegisterMenu implements Screen {
    private Stage stage;
    private RegisterMenuController controller;

    private Label usernameLabel;
    private Label passwordLabel;
    private Label questionLabel;
    private Label errorMessage;
    private TextField usernameField;
    private TextField passwordField;
    private TextField questionField;
    private TextButton registerButton;
    private TextButton enterAsGuestButton;
    private Table table;

    public TextButton getRegisterButton() {
        return registerButton;
    }

    public TextButton getEnterAsGuestButton() {
        return enterAsGuestButton;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getQuestionField() {
        return questionField;
    }


    public RegisterMenu() {
        controller = new RegisterMenuController();
        controller.setView(this);
        usernameLabel = new Label("Enter your username: ", AssetManager.getAssetManager().getSkin());
        passwordLabel = new Label("Enter your password: ", AssetManager.getAssetManager().getSkin());
        questionLabel = new Label("Your favorite movie: ", AssetManager.getAssetManager().getSkin());
        usernameField = new TextField("", AssetManager.getAssetManager().getSkin());
        passwordField = new TextField("", AssetManager.getAssetManager().getSkin());
        questionField = new TextField("", AssetManager.getAssetManager().getSkin());
        usernameField.setMaxLength(15);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMaxLength(15);
        registerButton = new TextButton("Register", AssetManager.getAssetManager().getSkin());
        enterAsGuestButton = new TextButton("Guest Mode", AssetManager.getAssetManager().getSkin());
        questionField.setMaxLength(15);
        errorMessage = new Label("", AssetManager.getAssetManager().getSkin());
        errorMessage.setColor(Color.RED);
        table = new Table();
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);

        table.center();
        table.row();
        table.add(usernameLabel).pad(5.0f);
        table.add(usernameField).width(300.0f).pad(5.0f);
        table.row();
        table.add(passwordLabel).pad(5.0f);
        table.add(passwordField).width(300.0f).pad(5.0f);
        table.row();
        table.add(questionLabel).pad(5.0f);
        table.add(questionField).width(300.0f).pad(5.0f);
        table.row();
        table.add(errorMessage).colspan(2).spaceTop(20.0f).pad(15.0f);
        table.row();
        table.add(registerButton).colspan(2).spaceTop(30.0f).pad(5.0f);
        table.row();
        table.add(enterAsGuestButton).colspan(2).spaceTop(20.0f).pad(5.0f);


        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleInput(registerButton);
            }
        });

        enterAsGuestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleInput(enterAsGuestButton);
            }
        });



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
        stage.getViewport().update(width, height, true);
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

    public void updateErrorMessage() {
        errorMessage.setText(controller.getErrorMessage());
    }
}
