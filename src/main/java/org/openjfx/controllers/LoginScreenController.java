package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.openjfx.App;

import java.io.IOException;

public class LoginScreenController {


    @FXML
    private Button loginBtn;
    @FXML
    private Button switchToSignupBtn;

    public void login(MouseEvent mouseEvent) throws IOException {
        App.setRoot("menu");
    }

    public void switchToSignup(MouseEvent mouseEvent) throws IOException {
        App.setRoot("signup");
    }
}
