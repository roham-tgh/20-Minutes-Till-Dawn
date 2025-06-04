package com.untildawn.controller;

import com.badlogic.gdx.Gdx;
import com.untildawn.Main;
import com.untildawn.view.LeaderBoardMenu;
import com.untildawn.view.MainMenu;
import com.untildawn.view.PreGameMenu;
import com.untildawn.view.SettingsMenu;

public class MainMenuController {
    private MainMenu view;

    public void setView (MainMenu view) {
        this.view = view;
    }

    public void handleInput() {
        if (view.getPlay().isChecked()) {
            Main.getMain().dispose();
            Main.getMain().setScreen(new PreGameMenu());
        }
        else if (view.getLeaderBoard().isChecked()) {
            Main.getMain().dispose();
            Main.getMain().setScreen(new LeaderBoardMenu());
        }
        else if (view.getSettings().isChecked()) {
            Main.getMain().dispose();
            Main.getMain().setScreen(new SettingsMenu());
        }
        else if (view.getExit().isChecked()) {
            Gdx.app.exit();
        }
    }
}
