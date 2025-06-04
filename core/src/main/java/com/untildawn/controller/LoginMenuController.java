package com.untildawn.controller;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.untildawn.Main;
import com.untildawn.model.loadAndSave.JsonReader;
import com.untildawn.model.User;
import com.untildawn.view.ForgetPasswordMenu;
import com.untildawn.view.LoginMenu;
import com.untildawn.view.MainMenu;
import com.untildawn.view.RegisterMenu;

public class LoginMenuController {
    private LoginMenu view;
    private String errorMessage = "";

    public void setView(LoginMenu view) {
        this.view = view;
    }

    public void handleInput(TextButton clickedButton) {
        if (clickedButton == view.getLogin()) {
            if (view.getUsernameField().getText().isEmpty()) {
                errorMessage = "Username cannot be empty";
            } else if (view.getPasswordField().getText().isEmpty()) {
                errorMessage = "Password cannot be empty";
            } else {
                String username = view.getUsernameField().getText();
                String password = view.getPasswordField().getText();
                User user = JsonReader.getUser(username);
                if (user == null) {
                    errorMessage = "User does not exist";
                } else if (!user.getPassword().equals(password)) {
                    errorMessage = "Incorrect password";
                } else {
                    // Proceed to the main menu
//                    Main.getMain().dispose();
                    Main.getMain().setScreen(new MainMenu(user));
                }
            }
        } else if (clickedButton == view.getForgotPassword()) {
//            Main.getMain().dispose();
            Main.getMain().setScreen(new ForgetPasswordMenu());
        } else if (clickedButton == view.getGoToRegister()) {
//            Main.getMain().dispose();
            Main.getMain().setScreen(new RegisterMenu());
        }
        view.updateErrorMessage();  // Update the error message on the view
    }


    public String getErrorMessage() {
        return errorMessage;
    }
}
