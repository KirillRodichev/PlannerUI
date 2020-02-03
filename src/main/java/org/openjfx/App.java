package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.controllers.LoginScreenController;
import org.openjfx.controllers.MenuController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /*private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }*/

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        /*LoginScreenController controller = fxmlLoader.getController();
        controller.initialize(null, null);
        fxmlLoader.setController(controller);
        parent = fxmlLoader.load();*/
        return parent;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRoot(String fxml, int userId) throws IOException, ParseException, SAXException, ParserConfigurationException {
        scene.setRoot(loadFXML(fxml, userId));
    }

    private static Parent loadFXML(String fxml, int userId)
            throws IOException, ParseException, SAXException, ParserConfigurationException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        Parent parent = fxmlLoader.load();

        MenuController menuController = fxmlLoader.getController();
        menuController.setUserId(userId);

        return parent;
    }

    public static void main(String[] args) {
        launch();
    }
}