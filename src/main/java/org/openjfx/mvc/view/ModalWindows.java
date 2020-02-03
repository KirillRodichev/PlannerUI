package org.openjfx.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.interfaces.ITask;

public class ModalWindows {
    public static Object editTaskFieldWindow(String title, String field) {
        final Object[] result = {null};
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        container.setStyle("-fx-padding: 30");
        Label header = new Label(title);
        header.setStyle("-fx-font-size: 16; -fx-font-family: 'Roboto Medium'");
        header.setPadding(new Insets(0, 0, 20, 0));
        container.getChildren().add(header);
        TextField textField = new TextField();
        HBox btnContainer = new HBox();
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setStyle("-fx-min-width: 70");
        switch (field) {
            case TaskFieldNames.START_DATE:
            case TaskFieldNames.FINISH_DATE:
                container.getChildren().add(new Label("Format: YY:MM:DD"));
            case TaskFieldNames.NAME:
            case TaskFieldNames.DESCRIPTION:
            case TaskFieldNames.TAG:
                container.getChildren().add(textField);
                container.getChildren().add(confirmBtn);
                confirmBtn.setOnMouseClicked(event -> {
                    result[0] = textField.getText();
                });
                break;
            case TaskFieldNames.TASK_TYPE:
                MenuItem[] typeItems = new MenuItem[TaskType.TASK_TYPES_STR.length];
                for (int i = 0; i < TaskType.TASK_TYPES_STR.length; i++) {
                    typeItems[i] = new MenuItem(TaskType.TASK_TYPES_STR[i]);
                    int finalI = i;
                    typeItems[i].setOnAction(actionEvent -> {
                        result[0] = TaskType.TASK_TYPES[finalI];
                    });
                }
                MenuButton menuButton = new MenuButton("Select type", null, typeItems);
                container.getChildren().add(menuButton);
                break;
            case TaskFieldNames.TASK_STATE:
                MenuItem[] stateItems = new MenuItem[TaskState.TASK_STATES_STR.length];
                for (int i = 0; i < TaskState.TASK_STATES_STR.length; i++) {
                    stateItems[i] = new MenuItem(TaskState.TASK_STATES_STR[i]);
                    int finalI = i;
                    stateItems[i].setOnAction(actionEvent -> {
                        result[0] = TaskState.TASK_STATES_STR[finalI];
                    });
                }
                MenuButton mButton = new MenuButton("Select state", null, stateItems);
                container.getChildren().add(mButton);
                break;
        }
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-min-width: 70");
        btnContainer.setPadding(new Insets( 15, 0, 15, 0));
        btnContainer.getChildren().addAll(confirmBtn, closeBtn);
        closeBtn.setOnMouseClicked(event -> {
            window.close();
        });
        container.getChildren().add(btnContainer);
        container.getChildren().add(closeBtn);
        Scene scene = new Scene(container, 350, 200);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
        return result[0];
    }

    public static ITask createTaskWindow(String title) {
        ITask task = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        container.setStyle("-fx-padding: 30");
        Label header = new Label(title);
        header.setStyle("-fx-font-size: 16; -fx-font-family: 'Roboto Medium'");
        header.setPadding(new Insets(0, 0, 20, 0));
        container.getChildren().add(header);


        return task;
    }

    public static void alertWindow(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Pane container = new Pane();
        container.setStyle("-fx-padding: 30");
        Label text = new Label(message);
        text.setStyle("-fx-padding: 30; -fx-background-color: #c6c6c6");
        container.getChildren().add(text);
        Scene scene = new Scene(container, 350, 200);
        window.setScene(scene);
        window.setTitle("Modal");
        window.showAndWait();
    }
}
