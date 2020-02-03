package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.openjfx.App;
import org.openjfx.enums.TaskType;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.view.ModalWindows;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    private static final String ALERT_MSG = "User wasn't selected";

    private UserController userController;
    @FXML
    private MenuButton dropdown;
    @FXML
    private Pane loginPane;

    private void addItemsToDropdown() {



        Pair<String, Integer>[] namesAndIds = this.userController.getUsersNamesAndIds();
        List<MenuItem> items = new ArrayList<>();
        for (Pair<String, Integer> nameAndId: namesAndIds) {
            MenuItem menu = new MenuItem(nameAndId.getKey());
            menu.setOnAction(actionEvent -> {
                userController.selectUser(nameAndId.getValue());
            });
            items.add(menu);
        }
        dropdown.getItems().addAll(items);
    }

    public LoginScreenController() {
        this.userController = new UserController();
        try {
            this.userController.actionReadXml();
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        if (this.userController.getSelectedUserId() != -1) {
            try {
                App.setRoot("menu", this.userController.getSelectedUserId());
            } catch (ParseException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            ModalWindows.alertWindow(ALERT_MSG);
        }
    }

    public void switchToSignup(MouseEvent mouseEvent) throws IOException {
        App.setRoot("signup");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addItemsToDropdown();
    }
}
