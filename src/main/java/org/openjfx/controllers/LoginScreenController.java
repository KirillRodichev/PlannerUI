package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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
        MenuItem[] items = new MenuItem[TaskType.TASK_TYPES_STR.length];
        for (int i = 0; i < namesAndIds.length; i++) {
            items[i] = new MenuItem(namesAndIds[i].getKey());
            int finalI = i;
            items[i].setOnAction(actionEvent -> {
                userController.selectUser(namesAndIds[finalI].getValue());
            });
        }
        /*StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            System.out.println(s);
        }*/
        dropdown.getItems().addAll(items); // почему-то падает
    }

    public LoginScreenController() {
        this.userController = new UserController();
        try {
            this.userController.actionReadXml();
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            e.printStackTrace();
        }
        //addItemsToDropdown();
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        /*if (this.userController.getSelectedUserId() != -1) {
            App.setRoot("menu");
        } else {
            ModalWindows.alertWindow(ALERT_MSG);
        }*/


        /*
        HARD CODE - userID
         */
        try {
            App.setRoot("menu", 0);
        } catch (ParseException | ParserConfigurationException | SAXException e) {
            System.out.println("LOL EX: " + e.getLocalizedMessage());
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
