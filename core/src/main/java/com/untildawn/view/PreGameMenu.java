package com.untildawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untildawn.Main;
import com.untildawn.controller.GameController;
import com.untildawn.model.AssetManager;
import com.untildawn.model.GameSettings;
import com.untildawn.model.enums.GameTime;
import com.untildawn.model.enums.Gun;
import com.untildawn.model.enums.Hero;

import static com.badlogic.gdx.Gdx.gl;

public class PreGameMenu implements Screen {
    private ImageButton shanaIcon = createHeroButton("Heroes/Shana/T_Shana_Portrait.png", Hero.SHANA);
private ImageButton diamondIcon = createHeroButton("Heroes/Diamond/T_Diamond_Portrait.png", Hero.DIAMOND);
private ImageButton scarletIcon = createHeroButton("Heroes/Scarlet/T_Scarlett_Portrait.png", Hero.SCARLET);
private ImageButton lilithIcon = createHeroButton("Heroes/Lilith/T_Lilith_Portrait.png", Hero.LILITH);
private ImageButton dasherIcon = createHeroButton("Heroes/Dasher/T_Dasher_Portrait.png", Hero.DASHER);
    private ImageButton createHeroButton(String path, Hero heroType) {
    Texture texture = new Texture(path);
    TextureRegion region = new TextureRegion(texture);
    TextureRegionDrawable drawable = new TextureRegionDrawable(region);
    Drawable checkedDrawable = new TextureRegionDrawable(region).tint(Color.GRAY);

    drawable.setMinWidth(100);
    drawable.setMinHeight(100);
    checkedDrawable.setMinWidth(100);
    checkedDrawable.setMinHeight(100);

    ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
    style.imageUp = drawable;
    style.imageChecked = checkedDrawable;

    ImageButton button = new ImageButton(style);
    button.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            GameSettings.getInstance().setHero(heroType);
        }
    });

    return button;
}

    private ImageButton dualSmgIcon = createWeaponButton("Guns/DualSMG/SMG-1.png", Gun.DUAL_SMG);
    private ImageButton revolverIcon = createWeaponButton("Guns/Revolver/Pistol-2.png", Gun.REVOLVER);
    private ImageButton shotgunIcon = createWeaponButton("Guns/Shotgun/Shotgun-3.png", Gun.SHOTGUN);


    private ImageButton createWeaponButton(String path, Gun gunType) {
    Texture texture = new Texture(path);
    TextureRegion region = new TextureRegion(texture);
    TextureRegionDrawable drawable = new TextureRegionDrawable(region);
    Drawable checkedDrawable =  new TextureRegionDrawable(region).tint(Color.GRAY); // Creates a gray tinted version for checked state

    drawable.setMinWidth(250);
    drawable.setMinHeight(250);
    checkedDrawable.setMinWidth(250);
    checkedDrawable.setMinHeight(250);

    ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
    style.imageUp = drawable;
    style.imageChecked = checkedDrawable; // The image to show when button is checked

    ImageButton button = new ImageButton(style);
    button.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            GameSettings.getInstance().setGun(gunType);
        }
    });

    return button;
}
    private TextButton startGame = new TextButton("Start Game", AssetManager.getAssetManager().getSkin()) {{;
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameView(new GameController()));
            }
        });
    }};
    private TextButton twoMins = new TextButton("2 Mins", AssetManager.getAssetManager().getSkin(), "toggle") {{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.getInstance().setGameTime(GameTime.TWO);
            }
        });
    }};
    private ButtonGroup<TextButton> timeButtonGroup;

    private TextButton fiveMins = new TextButton("5 Mins", AssetManager.getAssetManager().getSkin(), "toggle") {{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.getInstance().setGameTime(GameTime.FIVE);
            }
        });
    }};
    private TextButton tenMins = new TextButton("10 Mins", AssetManager.getAssetManager().getSkin(), "toggle") {{
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.getInstance().setGameTime(GameTime.TEN);
            }
        });
    }};
    private TextButton twentyMins = new TextButton("20 Mins", AssetManager.getAssetManager().getSkin(), "toggle") {{;
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.getInstance().setGameTime(GameTime.TWENTY);
            }
        });
    }};

    private Stage stage;
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        timeButtonGroup = new ButtonGroup<>(twoMins, fiveMins, tenMins, twentyMins);
    timeButtonGroup.setMinCheckCount(1);
    timeButtonGroup.setMaxCheckCount(1);
    timeButtonGroup.setUncheckLast(true);

    // Set initial selection (optional)
    twoMins.setChecked(true);
    ButtonGroup<ImageButton> weaponButtonGroup = new ButtonGroup<>(dualSmgIcon, revolverIcon, shotgunIcon);
    weaponButtonGroup.setMinCheckCount(1);
    weaponButtonGroup.setMaxCheckCount(1);
    weaponButtonGroup.setUncheckLast(true);
    dualSmgIcon.setChecked(true);

    ButtonGroup<ImageButton> heroButtonGroup = new ButtonGroup<>(shanaIcon, diamondIcon, scarletIcon, lilithIcon, dasherIcon);
    heroButtonGroup.setMinCheckCount(1);
    heroButtonGroup.setMaxCheckCount(1);
    heroButtonGroup.setUncheckLast(true);

    // Set initial selection
    shanaIcon.setChecked(true);

        Table table = new Table();
        table.setFillParent(true);
        table.add(shanaIcon).size(100, 100).pad(10);
        table.add(diamondIcon).size(100, 100).pad(10);
        table.add(scarletIcon).size(100, 100).pad(10);
        table.add(lilithIcon).size(100, 100).pad(10);
        table.add(dasherIcon).size(100, 100).pad(10);
        table.row();
        table.add(dualSmgIcon).pad(10);
        table.add(revolverIcon).pad(10);
        table.add(shotgunIcon).pad(10);
        table.row();
        table.add(twoMins).pad(10);
        table.add(fiveMins).pad(10);
        table.add(tenMins).pad(10);
        table.add(twentyMins).pad(10);
        table.row();
        table.add(startGame).colspan(4).pad(10);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0, 0, 0, 0);
        stage.getViewport().apply();
        stage.act(delta);
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

    }
}
