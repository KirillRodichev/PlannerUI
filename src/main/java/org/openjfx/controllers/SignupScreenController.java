package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.openjfx.App;

import java.io.IOException;

public class SignupScreenController {

    @FXML
    private Button signupBtn;
    @FXML
    private Button switchToLoginBtn;

    public void signup(MouseEvent mouseEvent) throws IOException {
        App.setRoot("menu");
    }

    public void switchToLogin(MouseEvent mouseEvent) throws IOException {
        App.setRoot("login");
    }
}
