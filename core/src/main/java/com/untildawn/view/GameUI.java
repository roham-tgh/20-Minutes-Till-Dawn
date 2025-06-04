package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.untildawn.controller.GameController;
import com.untildawn.model.AssetManager;
import com.untildawn.model.MyTime;

public class GameUI {
    private GameController gameController;
    private Stage stage;
    private HorizontalGroup healthGroup;
    private Texture fullHeartTexture;
    private Texture emptyHeartTexture;
    private static final float HEART_PADDING = 5f;
    private static final float HEART_SIZE = 32f;
    private static final float UI_MARGIN = 20f;
    private HorizontalGroup ammoGroup;
    private Texture ammoTexture;
    private Label time;
    private ProgressBar levelBar;
    public GameUI(GameController gameController) {
        this.gameController = gameController;
        stage = new Stage();
        healthGroup = new HorizontalGroup();
        healthGroup.pad(HEART_PADDING);

        // Load heart textures
        fullHeartTexture = new Texture(Gdx.files.internal("full_heart.png"));
        emptyHeartTexture = new Texture(Gdx.files.internal("empty_heart.png"));

        // Position the health group in top-left corner
        healthGroup.setPosition(UI_MARGIN, Gdx.graphics.getHeight() - UI_MARGIN - HEART_SIZE);
        stage.addActor(healthGroup);
        updateHealthUI();

        ammoGroup = new HorizontalGroup();
        ammoGroup.pad(HEART_PADDING);

        ammoTexture = new Texture(Gdx.files.internal("ammo.png"));
        ammoGroup.setPosition(UI_MARGIN, Gdx.graphics.getHeight() - UI_MARGIN - HEART_SIZE - 100f);
        stage.addActor(ammoGroup);
        updateAmmoUI();

        time = new Label(MyTime.getInstance().getTimeUi(), AssetManager.getAssetManager().getSkin());
        time.setPosition(UI_MARGIN, Gdx.graphics.getHeight() - UI_MARGIN - HEART_SIZE - 200f);
        stage.addActor(time);
        int level = gameController.getPlayer().getLevel();
        levelBar = new ProgressBar(10 * level * (level - 1), 10 * level * (level + 1), 1, false, AssetManager.getAssetManager().getSkin());
        levelBar.setValue(gameController.getPlayer().getXp());
        levelBar.setPosition(0,  Gdx.graphics.getHeight() - levelBar.getHeight());
        levelBar.setWidth(Gdx.graphics.getWidth());
        levelBar.setColor(Color.WHITE );
        stage.addActor(levelBar);
    }

    private void updateAmmoUI() {
        ammoGroup.clear();
        int ammoCount = gameController.getWeaponController().getAmmo();
        int maxAmmo = gameController.getWeaponController().getMaxAmmo();

        Label ammoLabel = new Label( ammoCount + "/" + maxAmmo, AssetManager.getAssetManager().getSkin());
        // Add ammo icons based on current ammo count
        ammoGroup.addActor(ammoLabel);
        ammoGroup.addActor(new Image(ammoTexture));

        // Position the ammo group in the top-right corner
//        ammoGroup.setPosition(Gdx.graphics.getWidth() - UI_MARGIN - (ammoCount * (HEART_SIZE + HEART_PADDING)), Gdx.graphics.getHeight() - UI_MARGIN - HEART_SIZE);
    }

    public void updateHealthUI() {
        healthGroup.clear();
        int maxHealth = gameController.getPlayer().getMaxHealth();
        int currentHealth = gameController.getPlayer().getHealth();

        // Add filled hearts for current health
        for (int i = 0; i < currentHealth; i++) {
            Image heartImage = new Image(new TextureRegionDrawable(new TextureRegion(fullHeartTexture)));
            heartImage.setSize(HEART_SIZE, HEART_SIZE);
            healthGroup.addActor(heartImage);
        }

        // Add empty hearts for lost health
        for (int i = currentHealth; i < maxHealth; i++) {
            Image heartImage = new Image(new TextureRegionDrawable(new TextureRegion(emptyHeartTexture)));
            heartImage.setSize(HEART_SIZE, HEART_SIZE);
            healthGroup.addActor(heartImage);
        }
    }

    public void updateTime() {
        time.setText(MyTime.getInstance().getTimeUi());
    }

    public void updateLevel() {
        int level = gameController.getPlayer().getLevel();
        levelBar.setRange(10 * level * (level - 1), 10 * level * (level + 1));
        levelBar.setValue(gameController.getPlayer().getXp());
        levelBar.setName("Level " + level);
        levelBar.updateVisualValue();
    }


    public void draw() {
        updateLevel();
        updateTime();
        updateAmmoUI();
        updateHealthUI();
        stage.act();
        stage.draw();

    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Update health group position after resize
        healthGroup.setPosition(UI_MARGIN, height - UI_MARGIN - HEART_SIZE);
        ammoGroup.setPosition(UI_MARGIN, height - UI_MARGIN - HEART_SIZE);
    }

    public void dispose() {
        stage.dispose();
        fullHeartTexture.dispose();
        emptyHeartTexture.dispose();
    }
}
