package org.openjfx.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;

public class ModalWindow {
    public static Object newWindow(String title, String field) {
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
        textField.getStyleClass().add("modal__text-field");
        HBox btnContainer = new HBox();
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setStyle("-fx-min-width: 70");
        switch (field) {
            case "startDate":
            case "finishDate":
                container.getChildren().add(new Label("Format: YY:MM:DD"));
            case "name":
            case "description":
            case "tag":
                container.getChildren().add(textField);
                container.getChildren().add(confirmBtn);
                confirmBtn.setOnMouseClicked(event -> {
                    result[0] = textField.getText();
                });
                break;
            case "type":
                MenuItem item = new MenuItem("today");
                item.setOnAction(actionEvent -> {
                    result[0] = TaskType.TODAY;
                });
                MenuItem item1 = new MenuItem("general");
                item1.setOnAction(actionEvent -> {
                    result[0] = TaskType.GENERAL;
                });
                MenuItem item2 = new MenuItem("any time");
                item2.setOnAction(actionEvent -> {
                    result[0] = TaskType.ANY_TIME;
                });
                MenuItem item3 = new MenuItem("less important");
                item3.setOnAction(actionEvent -> {
                    result[0] = TaskType.LESS_IMPORTANT;
                });
                MenuButton menuButton = new MenuButton("Select type", null, item, item1, item2, item3);
                container.getChildren().add(menuButton);
                break;
            case "state":
                MenuItem mItem = new MenuItem("in process");
                mItem.setOnAction(actionEvent -> {
                    result[0] = TaskState.IN_PROCESS;
                });
                MenuItem mItem1 = new MenuItem("paused");
                mItem1.setOnAction(actionEvent -> {
                    result[0] = TaskState.PAUSED;
                });
                MenuItem mItem2 = new MenuItem("finished");
                mItem2.setOnAction(actionEvent -> {
                    result[0] = TaskState.FINISHED;
                });
                MenuItem mItem3 = new MenuItem("delayed");
                mItem3.setOnAction(actionEvent -> {
                    result[0] = TaskState.DELAYED;
                });
                MenuItem mItem4 = new MenuItem("preparation");
                mItem4.setOnAction(actionEvent -> {
                    result[0] = TaskState.PREPARATION;
                });
                MenuItem mItem5 = new MenuItem("waiting");
                mItem5.setOnAction(actionEvent -> {
                    result[0] = TaskState.WAITING;
                });
                MenuButton mButton = new MenuButton("Select state", null, mItem, mItem1, mItem2, mItem3, mItem4, mItem5);
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
}
