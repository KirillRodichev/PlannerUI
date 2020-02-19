package org.openjfx.mvc.view.jfxControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import org.openjfx.App;
import org.openjfx.constants.UIControllers;
import org.openjfx.messages.UI.WarningMsg;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.User;
import org.openjfx.mvc.view.ModalWindow;
import org.w3c.dom.ls.LSOutput;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    private static UserController userController = new UserController();
    @FXML
    private MenuButton dropdown;

    private void addItemsToDropdown() {
        Pair<String, Integer>[] namesAndIds = userController.getUsersNamesAndIds();
        List<MenuItem> items = new ArrayList<>();
        for (Pair<String, Integer> nameAndId: namesAndIds) {
            MenuItem item = new MenuItem(nameAndId.getKey());
            item.setOnAction(actionEvent -> {
                userController.selectUser(nameAndId.getValue());
                dropdown.setText(nameAndId.getKey());
            });
            items.add(item);
        }
        dropdown.getItems().clear();
        dropdown.getItems().addAll(items);
    }

    public LoginScreenController() {
        if (userController.getSelectedUserId() == -1) {
            try {
                userController.actionReadXml();
            } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        if (userController.getSelectedUserId() != -1) {
            try {
                System.out.println("Selected Id LOGIN: " + userController.getSelectedUserId());
                App.setRoot(UIControllers.MENU, userController.getSelectedUserId());
            } catch (ParseException | SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            ModalWindow.alertWindow(WarningMsg.ALERT_MSG);
        }
    }

    public void setUserController(UserController controller) {
        userController = controller;
        addItemsToDropdown();
    }

    public void switchToSignup(MouseEvent mouseEvent) throws IOException {
        App.setRoot(UIControllers.SIGNUP);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addItemsToDropdown();
    }
}
