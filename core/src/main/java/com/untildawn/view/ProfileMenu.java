package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.untildawn.Main;
import com.untildawn.model.AssetManager;
import com.untildawn.model.User;
import com.untildawn.model.enums.Regex;
import com.untildawn.model.loadAndSave.JsonReader;

import static com.badlogic.gdx.Gdx.gl;

public class ProfileMenu implements Screen {
    private Image profileImage;
    // 1. Load your image file as a Texture
    Texture texture1 = new Texture(Gdx.files.internal("Avatars/2000a.png")); // replace with your image path
    TextureRegion region1 = new TextureRegion(texture1);
    Drawable drawable1 = new TextureRegionDrawable(region1);
ImageButton.ImageButtonStyle style1 = new ImageButton.ImageButtonStyle(){{
    imageUp = drawable1;
}};
Texture texture2 = new Texture(Gdx.files.internal("Avatars/2000c.png")); // replace with your image path
    TextureRegion region2 = new TextureRegion(texture2);
    Drawable drawable2 = new TextureRegionDrawable(region2);
ImageButton.ImageButtonStyle style2 = new ImageButton.ImageButtonStyle(){{
    imageUp = drawable2;
}};
Texture texture3 = new Texture(Gdx.files.internal("Avatars/20004.png")); // replace with your image path
    TextureRegion region3 = new TextureRegion(texture3);
    Drawable drawable3 = new TextureRegionDrawable(region3);
ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle(){{
    imageUp = drawable3;

}};
Texture texture4 = new Texture(Gdx.files.internal("Avatars/20009.png")); // replace with your image path
    TextureRegion region4 = new TextureRegion(texture4);
    Drawable drawable4 = new TextureRegionDrawable(region4);
ImageButton.ImageButtonStyle style4 = new ImageButton.ImageButtonStyle(){{
    imageUp = drawable4;
}};


    private ImageButton avatar1 = new ImageButton(style1){{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentUser.updateAvatar("Avatars/2000a.png");
profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(currentUser.getAvatarPath())))));            }
        });
    }};
    private ImageButton avatar2 = new ImageButton(style2){{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentUser.updateAvatar("Avatars/2000c.png");
profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(currentUser.getAvatarPath())))));            }
        });
    }};
    private ImageButton avatar3 = new ImageButton(style3){{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentUser.updateAvatar("Avatars/20004.png");
                profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(currentUser.getAvatarPath())))));
            }
        });
    }};
    private ImageButton avatar4 = new ImageButton(style4){{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentUser.updateAvatar("Avatars/20009.png");
profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(currentUser.getAvatarPath())))));            }
        });
    }};


//    private ButtonGroup<ImageButton> profileImages = new ButtonGroup<ImageButton>(){{
//        add(avatar1, avatar2, avatar3, avatar4);
//        setMaxCheckCount(1);
//        setMinCheckCount(0);
//        setUncheckLast(true);
//        profileImages.getChecked().setChecked(true);
//    }};

    private TextField usernameField;
    private TextField passwordField;
    private Label errorMessage = new Label("", AssetManager.getAssetManager().getSkin()) {{
        setColor(Color.RED);
    }};
    private TextButton updateCredentials = new TextButton("Update\nCredentials", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the click event for updating credentials
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();
                if (newUsername.isEmpty()) {
                errorMessage.setText("Username cannot be empty");
            } else if (newPassword.isEmpty()) {
                errorMessage.setText("Password cannot be empty");
            } else if (newUsername.length() < 3) {
                    errorMessage.setText("Username too short");
                } else if (newPassword.length() < 8) {
                    errorMessage.setText("Password too short");
                } else if (!Regex.PASSWORD.matches(newPassword)) {
                    errorMessage.setText("Password too weak");
                } else if (JsonReader.getUser(newUsername) != null && !newUsername.equals(currentUser.getUsername())) {
                    errorMessage.setText("Username already in use");
                } else {
                    String oldUsername = currentUser.getUsername();
                    currentUser.updateUserPassword(newUsername, newPassword);

                    JsonReader.updateUser(oldUsername, currentUser);
                    errorMessage.setText("Credentials updated successfully");
                }
            }
        });
    }};
    private TextButton deleteAccount = new TextButton("Delete Account", AssetManager.getAssetManager().getSkin()) {{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the click event for deleting account
                JsonReader.deleteUser(currentUser.getUsername());
                Main.getMain().setScreen(new LoginMenu());
            }
        });
    }};
    private final User currentUser;
    private Table table;
    private Stage stage;
    public ProfileMenu() {
        this.currentUser = MainMenu.getInstance().getCurrentUser();
//        currentUser = new User("Roham", "Abcd", "!@#");
        usernameField = new TextField(currentUser.getUsername(), AssetManager.getAssetManager().getSkin());
        passwordField = new TextField(currentUser.getPassword(), AssetManager.getAssetManager().getSkin());
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table = new Table();
        profileImage = new Image(currentUser.getAvatar());
    }
    @Override
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        table.center();
        table.add(usernameField).width(400f).pad(5.0f);
        table.row();
        table.add(passwordField).width(400f).pad(5.0f);
        table.row();
        table.add(errorMessage).pad(5.0f);
        table.row();
        table.add(updateCredentials).width(400f).pad(5.0f);
        table.row();
        table.add(deleteAccount).width(400f).pad(5.0f);
        table.row();
        table.add(profileImage).width(100f).height(100f).pad(5.0f);
        Table table1 = new Table(){{
            setFillParent(true);
            moveBy(300, 0);
            add(avatar1).width(100).height(100).pad(5.0f);
            row();
            add(avatar2).width(100).height(100).pad(5.0f);
            row();
            add(avatar3).width(100).height(100).pad(5.0f);
            row();
            add(avatar4).width(100).height(100).pad(5.0f);
        }};
        stage.addActor(table);
        stage.addActor(table1);

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
