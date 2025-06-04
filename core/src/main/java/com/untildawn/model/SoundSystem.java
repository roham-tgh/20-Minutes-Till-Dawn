package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundSystem {
    private static SoundSystem instance;
    private float soundVolume = 1.0f;
    private float musicVolume = 1.0f;
    private Music currentMusic;
    private final Map<String, Sound> soundEffects;

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    private boolean soundEnabled = true;

    private SoundSystem() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    public static SoundSystem getInstance() {
        if (instance == null) {
            instance = new SoundSystem();
        }
        return instance;
    }

    private void loadSounds() {
        // Load all sound effects
        soundEffects.put("shoot", Gdx.audio.newSound(Gdx.files.internal("Sound/single_shot.wav")));
        soundEffects.put("enemyHit", Gdx.audio.newSound(Gdx.files.internal("Sound/enemyHit.wav")));
//        soundEffects.put("death", Gdx.audio.newSound(Gdx.files.internal("Sound/death.wav")));
        soundEffects.put("playerHit", Gdx.audio.newSound(Gdx.files.internal("Sound/playerDamage.wav")));
        soundEffects.put("reloading", Gdx.audio.newSound(Gdx.files.internal("Sound/weaponReloading.wav")));
        soundEffects.put("weaponReady", Gdx.audio.newSound(Gdx.files.internal("Sound/weaponReady.wav")));
        soundEffects.put("walk1", Gdx.audio.newSound(Gdx.files.internal("Sound/walk1.wav")));
        soundEffects.put("walk2", Gdx.audio.newSound(Gdx.files.internal("Sound/walk2.wav")));
        soundEffects.put("walk3", Gdx.audio.newSound(Gdx.files.internal("Sound/walk3.wav")));
    }

    public void playMusic(String path, boolean looping) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setVolume(musicVolume);
        currentMusic.setLooping(looping);
        currentMusic.play();
    }

    public void setMusic(String trackName) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/" + trackName));
        currentMusic.setVolume(musicVolume);
        currentMusic.setLooping(true);
        currentMusic.play();
    }

    public void playSound(String soundName) {
        if (!soundEnabled) return;
        Sound sound = soundEffects.get(soundName);
        if (sound != null) {
            sound.play(soundVolume);
        }
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = Math.max(0, Math.min(1, volume));
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        soundEffects.clear();
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }
}
