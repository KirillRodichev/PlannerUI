package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.openjfx.constants.UIConsts;
import org.openjfx.constants.UIControllers;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.User;
import org.openjfx.mvc.view.jfxControllers.LoginScreenController;
import org.openjfx.mvc.view.jfxControllers.MenuController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public class App extends Application {

    private static Scene scene;
    private static UserController userController;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(UIControllers.LOGIN));
        scene.getStylesheets().add(App.class.getResource(UIConsts.STYLESHEET).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // root to
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // root to menu from Login
    public static void setRoot(String fxml, int userId)
            throws IOException, ParseException, SAXException, ParserConfigurationException {
        scene.setRoot(loadFXML(fxml, userId));
    }

    // root to menu form Signup
    public static void setRoot(String fxml, String name)
            throws IOException, ParseException, SAXException, ParserConfigurationException {
        scene.setRoot(loadFXML(fxml, name));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        if (fxml.equals(UIControllers.LOGIN) && userController != null) {
            Parent parent = fxmlLoader.load();

            LoginScreenController loginController = fxmlLoader.getController();
            loginController.setUserController(userController);

            return parent;
        } else {
            return fxmlLoader.load();
        }
    }

    private static Parent loadFXML(String fxml, int userId)
            throws IOException, ParseException, SAXException, ParserConfigurationException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        Parent parent = fxmlLoader.load();

        MenuController menuController = fxmlLoader.getController();
        menuController.setUserId(userId);

        return parent;
    }

    private static Parent loadFXML(String fxml, String name)
            throws IOException, ParseException, SAXException, ParserConfigurationException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        Parent parent = fxmlLoader.load();

        MenuController menuController = fxmlLoader.getController();
        menuController.createUser(name);

        userController = menuController.getUserController();

        return parent;
    }

    public static void main(String[] args) {
        launch();
    }
}