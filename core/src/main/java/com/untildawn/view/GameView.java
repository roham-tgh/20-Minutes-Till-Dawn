// In GameView.java
package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untildawn.Main;
import com.untildawn.controller.GameController;
import com.untildawn.model.enums.*;
import com.untildawn.model.*;
import com.untildawn.controller.*;

public class GameView implements Screen, InputProcessor {

    private static float ZOOM = 0.3f;
    private final BlackAndWhiteShader blackAndWhiteShader;
    private final SpotlightShader spotlightShader;
    private final GameController controller;
    private final Texture background;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private boolean paused = false;
    private boolean isAbilityPaused = false;
    private final BitmapFont pauseFont = new BitmapFont();
    private final GlyphLayout pauseLayout = new GlyphLayout();
    private final Texture cursorTexture;
    {
    Texture originalTexture = new Texture("5.png");
    TextureRegion region = new TextureRegion(originalTexture);
    Pixmap originalPixmap = new Pixmap(Gdx.files.internal("5.png"));
    Pixmap scaledPixmap = new Pixmap(64, 64, originalPixmap.getFormat());
    scaledPixmap.drawPixmap(originalPixmap,
        0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
        0, 0, 64, 64);
    cursorTexture = new Texture(scaledPixmap);
    originalPixmap.dispose();
    scaledPixmap.dispose();
    originalTexture.dispose();
    }
    private final Stage pauseStage;
    private final Skin pauseSkin;
    private final Table pauseTable;

    private final Stage abilityStage;
    private final Table abilityTable;

    private final Label abilityNameLabel;
    private final Label abilityDescriptionLabel;
    private final ButtonGroup<ImageButton> abilityButtonGroup;
    private Ability selectedAbility = Ability.VITALITY; // Default selection
    private final GameUI gameUI;

    private boolean autoShoot = false;

    private final MapController mapController;
    private final Texture[] tileTextures;

    public GameView(GameController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("GameController cannot be null");
        }
        this.controller = controller;
        this.controller.setView(this);
        gameUI = new GameUI(controller);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = ZOOM;
        camera.update();

        spotlightShader = new SpotlightShader();

        spotlightShader.setRadius(200f);
        spotlightShader.setSoftness(0.2f);
        spotlightShader.setDarkness(0.3f);

        background = new Texture("background.png");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Setup pause menu UI
        pauseStage = new Stage(new ScreenViewport());
        pauseSkin = AssetManager.getAssetManager().getSkin(); // Make sure you have uiskin.json and its atlas in assets

        pauseTable = new Table(AssetManager.getAssetManager().getSkin());
        pauseTable.setFillParent(true);
        pauseTable.center();

        TextButton resumeBtn = new TextButton("Resume", pauseSkin);
        CheckBox blackWhiteToggle = new CheckBox("Noir Style", pauseSkin);
        TextButton quitBtn = new TextButton("Give Up", pauseSkin);

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        blackWhiteToggle.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                blackAndWhiteShader.setEnabled(!blackAndWhiteShader.isEnabled());
            }
        });

        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        pauseTable.add("Paused").padBottom(40).row();
        pauseTable.add(resumeBtn).pad(10).row();
        pauseTable.add(blackWhiteToggle).pad(10).row();
        pauseTable.add(quitBtn).pad(10).row();

        pauseStage.addActor(pauseTable);

        // Initialize ability selection UI
        abilityStage = new Stage(new ScreenViewport());
        abilityTable = new Table();
        abilityTable.setFillParent(true);
        abilityTable.center();

        // Create ability buttons
        ImageButton vitalityBtn = createAbilityButton(Ability.VITALITY);
        ImageButton damagerBtn = createAbilityButton(Ability.DAMAGER);
        ImageButton procreaseBtn = createAbilityButton(Ability.PROCREASE);
        ImageButton amocreaseBtn = createAbilityButton(Ability.AMOCREASE);
        ImageButton speedyBtn = createAbilityButton(Ability.SPEEDY);

        // Setup button group
        abilityButtonGroup = new ButtonGroup<>(vitalityBtn, damagerBtn, procreaseBtn, amocreaseBtn, speedyBtn);
        abilityButtonGroup.setMinCheckCount(1);
        abilityButtonGroup.setMaxCheckCount(1);
        abilityButtonGroup.setUncheckLast(true);

        // Set default selection
        vitalityBtn.setChecked(true);

        // Create labels
        abilityNameLabel = new Label(selectedAbility.getName(), AssetManager.getAssetManager().getSkin());
        abilityDescriptionLabel = new Label(selectedAbility.getDescription(), AssetManager.getAssetManager().getSkin());
        abilityDescriptionLabel.setWrap(true);

        // Create accept button
        TextButton acceptBtn = new TextButton("Accept", AssetManager.getAssetManager().getSkin());
        acceptBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Handle ability selection
                isAbilityPaused = false;
                Gdx.input.setInputProcessor(GameView.this);
                // TODO: Handle selected ability
                controller.handleAbility(selectedAbility);
            }
        });

        // Add everything to the table
        Table buttonsTable = new Table();
        buttonsTable.add(vitalityBtn).pad(5);
        buttonsTable.add(damagerBtn).pad(5);
        buttonsTable.add(procreaseBtn).pad(5);
        buttonsTable.add(amocreaseBtn).pad(5);
        buttonsTable.add(speedyBtn).pad(5);

        abilityTable.add(buttonsTable).row();
        abilityTable.add(abilityNameLabel).pad(10).row();
        abilityTable.add(abilityDescriptionLabel).width(300).pad(10).row();
        abilityTable.add(acceptBtn).pad(10);

        abilityStage.addActor(abilityTable);


        blackAndWhiteShader = new BlackAndWhiteShader();
        blackAndWhiteShader.setEnabled(false);


        this.mapController = new MapController(System.currentTimeMillis());

        //TODO Load tile textures
        this.tileTextures = new Texture[9];
        for (int i = 0; i < tileTextures.length; i++) {
            tileTextures[i] = new Texture("tiles/" + i + ".png"); // Placeholder texture
        }
//        tileTextures[0] = new Texture("tiles/grass.png");    // base tile
//        tileTextures[1] = new Texture("tiles/dirt.png");     // variation 1
//        tileTextures[2] = new Texture("tiles/stone.png");    // variation 2
//        tileTextures[3] = new Texture("tiles/sand.png");     // variation 3
    }

    // Add this method to create ability buttons
    private ImageButton createAbilityButton(Ability ability) {
        Texture texture = new Texture(ability.getPath());
        TextureRegion region = new TextureRegion(texture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        Drawable checkedDrawable = new TextureRegionDrawable(region).tint(Color.GRAY);

        drawable.setMinWidth(64);
        drawable.setMinHeight(64);
        checkedDrawable.setMinWidth(64);
        checkedDrawable.setMinHeight(64);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawable;
        style.imageChecked = checkedDrawable;

        ImageButton button = new ImageButton(style);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedAbility = ability;
                updateAbilityInfo();
            }
        });

        return button;
    }

    // Add this method to update labels
    private void updateAbilityInfo() {
        abilityNameLabel.setText(selectedAbility.getName());
        abilityDescriptionLabel.setText(selectedAbility.getDescription());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        try {
            ScreenUtils.clear(0, 0, 0, 1);

            if (paused) {
                pauseStage.act(delta);
                pauseStage.draw();
                return;
            }
            if (isAbilityPaused) {
                abilityStage.act(delta);
                abilityStage.draw();
                return;
            }

            MyTime.getInstance().update(delta);
            if (MyTime.getInstance().timeUp()) {
                //TODO victory
                return;
            }
            if (controller != null && controller.getPlayerController() != null) {
                controller.getPlayerController().handleInput();
                controller.getWeaponController().handleInput();

                Player player = controller.getPlayerController().getPlayer();
                if (player != null && player.position != null) {
                    mapController.update(player.position);
                    camera.position.set(player.position.x, player.position.y, 0);
                    camera.update();

                    Vector3 screenPos = camera.project(new Vector3(player.position.x, player.position.y, 0));
                    spotlightShader.setPlayerPosition(screenPos.x, screenPos.y);
                }
            }


            Main.getBatch().setProjectionMatrix(camera.combined);
            spotlightShader.begin(Main.getBatch());
            blackAndWhiteShader.begin(Main.getBatch());
            Main.getBatch().begin();
//            Main.getBatch().draw(background, 0, 0);
            renderMap();
            controller.update(delta);
            if (autoShoot) {
                Vector2 monsterPos = controller.getMonsterController().getNearestMonsterPosition();
                if (monsterPos != null) {
                   Vector3 screenPos = camera.project(new Vector3(monsterPos.x, monsterPos.y, 0));
                   controller.getWeaponController().handleRotation((int) screenPos.x, Gdx.graphics.getHeight() - (int) screenPos.y);
                   Main.getBatch().draw(cursorTexture, monsterPos.x - cursorTexture.getWidth()/2, monsterPos.y - cursorTexture.getHeight()/2);
                }
            }
            Main.getBatch().end();
            blackAndWhiteShader.end(Main.getBatch());
            spotlightShader.end(Main.getBatch());
            gameUI.draw();

        } catch (Exception e) {
            Gdx.app.error("GameView", "Render error: " + e.getMessage());
            if (Main.getBatch() != null && Main.getBatch().isDrawing()) {
                Main.getBatch().end();
            }
        }
    }
    private void renderMap() {
    // Calculate visible area in world coordinates
    float startX = camera.position.x - camera.viewportWidth * camera.zoom / 2;
    float startY = camera.position.y - camera.viewportHeight * camera.zoom / 2;
    float endX = startX + camera.viewportWidth * camera.zoom;
    float endY = startY + camera.viewportHeight * camera.zoom;

    // Convert to tile coordinates and add padding
    int startTileX = (int) Math.floor(startX / MapController.TILE_SIZE) - 1;
    int startTileY = (int) Math.floor(startY / MapController.TILE_SIZE) - 1;
    int endTileX = (int) Math.ceil(endX / MapController.TILE_SIZE) + 1;
    int endTileY = (int) Math.ceil(endY / MapController.TILE_SIZE) + 1;

    // Render visible tiles
    for (int x = startTileX; x <= endTileX; x++) {
        for (int y = startTileY; y <= endTileY; y++) {
            int tileType = mapController.getTileAt(x * MapController.TILE_SIZE, y * MapController.TILE_SIZE);
            if (tileType >= 0 && tileType < tileTextures.length) {
                Main.getBatch().draw(tileTextures[tileType],
                    x * MapController.TILE_SIZE,
                    y * MapController.TILE_SIZE,
                    MapController.TILE_SIZE,
                    MapController.TILE_SIZE);
            }
        }
    }
}
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            paused = !paused;
            if (paused) {
                Gdx.input.setInputProcessor(pauseStage);
                CursorManager.getInstance().showCursor();
            } else {
                if (autoShoot) CursorManager.getInstance().hideCursor();
                Gdx.input.setInputProcessor(this);
            }
            return true;
        }
        if (keycode == Input.Keys.A) {
            isAbilityPaused = !isAbilityPaused;
            if (isAbilityPaused) {
                Gdx.input.setInputProcessor(abilityStage);
                CursorManager.getInstance().showCursor();
//                updateAbilityInfo(); // Update ability info when paused
            } else {
                Gdx.input.setInputProcessor(this);
                if (autoShoot) CursorManager.getInstance().hideCursor();
            }
            return true;
        }
        if (keycode == Input.Keys.T) {
            MyTime.getInstance().skipTime();
        }
        if (keycode == Input.Keys.SPACE) {
            autoShoot = !autoShoot;
            if (autoShoot) CursorManager.getInstance().hideCursor();
            else CursorManager.getInstance().showCursor();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controller.getWeaponController().handleShoot(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!autoShoot) controller.getWeaponController().handleRotation(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        ZOOM = (amountY > 0) ? Math.min(ZOOM + amountY * 0.05f, 2f) : Math.max(ZOOM + amountY * 0.05f, 0.3f);

        camera.zoom = ZOOM;
        camera.update();
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spotlightShader.resize(width, height);
        camera.zoom = ZOOM;
        camera.update();
        pauseStage.getViewport().update(width, height, true);
        abilityStage.getViewport().update(width, height, true);
//        blackAndWhiteShader.resize(width, height);
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
        if (blackAndWhiteShader != null) blackAndWhiteShader.dispose();
        if (background != null) background.dispose();
        if (spotlightShader != null) spotlightShader.dispose();
        if (pauseFont != null) pauseFont.dispose();
        if (pauseStage != null) pauseStage.dispose();
        if (pauseSkin != null) pauseSkin.dispose();
        if (abilityStage != null) abilityStage.dispose();
        for (Texture texture : tileTextures) {
        if (texture != null) texture.dispose();
    }
    }
}
