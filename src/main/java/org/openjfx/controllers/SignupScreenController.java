package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.openjfx.App;
import org.openjfx.mvc.view.ModalWindows;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public class SignupScreenController {

    private static final String ALERT_MSG = "User name must contain not less then 2 letters";

    @FXML
    private TextField input;

    public void signup(MouseEvent mouseEvent) throws IOException {
        if (input.getText().length() > 1) {
            try {
                App.setRoot("menu", input.getText());
            } catch (ParseException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            ModalWindows.alertWindow(ALERT_MSG);
        }
    }

    public void switchToLogin(MouseEvent mouseEvent) throws IOException {
        App.setRoot("login");
    }
}
