package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.openjfx.exceptions.UserNotFoundException;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MenuController {

    private UserController userController;
    private TaskController taskController;
    @FXML
    private Label header;
    @FXML
    private ScrollPane scrollView;

    public MenuController() {
        this.userController = new UserController();
        this.taskController = new TaskController();
    }

    @FXML
    private void switchToInProcessTasks() throws UserNotFoundException {
        header.setText("In Process Tasks");
        Pane pane = new Pane();
        int userId = userController.actionPushUser("Kirill");
        userController.actionAddTasks(
            userId,
            taskController.actionCreateTask("Kill the Bill"),
            taskController.actionCreateTask("Smack the button"),
            taskController.actionCreateTask("Deploy the App")
        );
        ArrayList<ITask> tasksCollection = (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userId);
        Label[] labels = new Label[tasksCollection.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(tasksCollection.get(i).getName());
        }
        pane.getChildren().addAll(labels);
        int y = 50;
        for (Node label : pane.getChildren()) {
            label.setLayoutY(y);
            y+=50;
        }
        scrollView.setContent(pane);
    }

    @FXML
    private void switchToPausedTasks() {
        header.setText("Paused Tasks");
    }

    @FXML
    private void switchToFinishedTasks() {
        header.setText("Finished Tasks");
    }

    @FXML
    private void switchToDelayedTasks() {
        header.setText("Delayed Tasks");
    }

    @FXML
    private void switchToPreparationTasks() {
        header.setText("In Preparation Tasks");
    }

    @FXML
    private void switchToWaitingTasks() {
        header.setText("Waiting Tasks");
    }

    @FXML
    private void switchToTodayTasks() {
        header.setText("Today Tasks");
    }

    @FXML
    private void switchToGeneralTasks() {
        header.setText("General Tasks");
    }

    @FXML
    private void switchToLessImportantTasks() {
        header.setText("Less Important Tasks");
    }

    @FXML
    private void switchToAnyTimeTasks() {
        header.setText("Any Time Tasks");
    }

    @FXML
    private void switchToTasks() {
        header.setText("All Tasks");
    }

    @FXML
    private void switchToProjects() {
        header.setText("Projects");
    }
}
