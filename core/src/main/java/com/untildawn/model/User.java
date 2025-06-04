package com.untildawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class User {
    String username;
    String password;
    String question;
    int score;
    int kills;
    long playTime;
    Texture avatar;
    String avatarPath;
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User(String username, String password, String question) {
        this.username = username;
        this.password = password;
        this.question = question;
        score = 0;
        kills = 0;
        playTime = 0;
        avatarPath = "Avatars/2000c.png";
        avatar = new Texture(avatarPath);
    }

    public User(String username, String password, String question, int score, int kills, long playTime, String avatarPath) {
        this.username = username;
        this.password = password;
        this.question = question;
        this.score = score;
        this.kills = kills;
        this.playTime = playTime;
        this.avatarPath = avatarPath;
        avatar = new Texture(avatarPath);
    }

    public Texture getAvatar() {
        return avatar;
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void updateAvatar(String avatarPath) {
        this.avatarPath = avatarPath;
        avatar = new Texture(avatarPath);
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void updateUserPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
