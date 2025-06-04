package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetManager {
    private static AssetManager assetManager;
    private Skin skin = new Skin(Gdx.files.internal("E:\\Roham\\Code\\AP\\3_tamrin\\untildawn\\assets\\pixthulhuui\\pixthulhu-ui.json"));

    public Animation<TextureRegion> getBulletHitEffect() {
        return bulletHitEffect;
    }

    private Animation<TextureRegion> bulletHitEffect = new Animation<>(0.15f, new TextureAtlas("Effects/bulletHit.atlas").getRegions(), Animation.PlayMode.NORMAL);
    public static AssetManager getAssetManager() {
        if (assetManager == null) {
            assetManager = new AssetManager();
        }
        return assetManager;

    }

    public Skin getSkin() {
        return skin;
    }


    public Texture getTexture(String image) {
        return new Texture(Gdx.files.internal("Avatars/20004.png"));
    }
}
