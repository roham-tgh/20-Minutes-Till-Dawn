package com.untildawn.controller;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.untildawn.Main;
import com.untildawn.model.User;
import com.untildawn.model.enums.Regex;
import com.untildawn.model.loadAndSave.JsonReader;
import com.untildawn.view.MainMenu;
import com.untildawn.view.RegisterMenu;

public class RegisterMenuController {
    private RegisterMenu view;
    private String errorMessage = "";

    public void setView(RegisterMenu registerMenu) {
        this.view = registerMenu;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void handleInput(Button clickedButton) {
        if (clickedButton == view.getRegisterButton()) {
            if (view.getUsernameField().getText().isEmpty()) {
                errorMessage = "Username cannot be empty";
            } else if (view.getPasswordField().getText().isEmpty()) {
                errorMessage = "Password cannot be empty";
            } else if (view.getQuestionField().getText().isEmpty()) {
                errorMessage = "Question cannot be empty";
            } else {
                if (view.getUsernameField().getText().length() < 3) {
                    errorMessage = "Username too short";
                } else if (view.getPasswordField().getText().length() < 8) {
                    errorMessage = "Password too short";
                } else if (!Regex.PASSWORD.matches(view.getPasswordField().getText())) {
                    errorMessage = "Password too weak";
                } else if (JsonReader.getUser(view.getUsernameField().getText()) != null) {
                    errorMessage = "Username already in use";
                } else {
                    User user = new User(view.getUsernameField().getText(),
                                         view.getPasswordField().getText(),
                                         view.getQuestionField().getText());

                    JsonReader.appendUser(user);
                    Main.getMain().dispose();
                    Main.getMain().setScreen(new MainMenu(user));
            }
        }
    } else if (clickedButton == view.getEnterAsGuestButton()) {
//        Main.getMain().dispose();
        Main.getMain().setScreen(new MainMenu(null));
    }
        view.updateErrorMessage();  // Update the error message on the view
    }

}
