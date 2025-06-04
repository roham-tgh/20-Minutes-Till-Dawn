package com.untildawn.controller;

import com.untildawn.model.GameSettings;
import com.untildawn.model.Player;
import com.untildawn.model.Weapon;
import com.untildawn.model.enums.Ability;
import com.untildawn.view.GameUI;
import com.untildawn.view.GameView;

public class GameController {
    private GameView view;
    private GameUI gameUI;
    private final MonsterController monsterController;
    private final PlayerController playerController;
    private final WeaponController weaponController;

    public GameController() {
       playerController = new PlayerController(GameSettings.getInstance().getHero());
       weaponController = new WeaponController(GameSettings.getInstance().getGun(), 2);
       monsterController = new MonsterController(GameSettings.getInstance().getGameTime().getTime());
    }

    public MonsterController getMonsterController() {
        return monsterController;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WeaponController getWeaponController() {
        return weaponController;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public Player getPlayer() {
        return playerController.getPlayer();
    }


    // In GameController.java

    public void update(float delta) {
        playerController.update(delta);
        weaponController.update(playerController.getPlayer().position.x, playerController.getPlayer().position.y, delta);
        monsterController.update(delta, playerController.getPlayer(), weaponController.getBulletController().getBullets());

    }

    public void handleInput() {
        playerController.handleInput();
        weaponController.handleInput();
    }

    public void handleAbility(Ability selectedAbility) {
        switch (selectedAbility) {
            case SPEEDY:
                getPlayer().activateSpeedy();
                break;
            case VITALITY:
                getPlayer().activateVitality();
                break;
            case DAMAGER:
                weaponController.activateDamager();
                break;
            case AMOCREASE:
                weaponController.activateAmocrease();
                break;
            case PROCREASE:
                weaponController.activateProcrease();
                break;
        }
    }
}

