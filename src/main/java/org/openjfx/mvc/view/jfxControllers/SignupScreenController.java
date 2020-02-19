package org.openjfx.mvc.view.jfxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.openjfx.App;
import org.openjfx.constants.UIControllers;
import org.openjfx.messages.UI.WarningMsg;
import org.openjfx.mvc.view.ModalWindow;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public class SignupScreenController {

    @FXML
    private TextField input;

    public void signup(MouseEvent mouseEvent) throws IOException {
        if (input.getText().length() > 1) {
            try {
                App.setRoot(UIControllers.MENU, input.getText());
            } catch (ParseException | SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            ModalWindow.alertWindow(WarningMsg.ILLEGAL_USER_NAME);
        }
    }

    public void switchToLogin(MouseEvent mouseEvent) throws IOException {
        App.setRoot(UIControllers.LOGIN);
    }
}
