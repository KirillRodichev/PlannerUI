package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class MenuController {

    @FXML
    private Label header;

    @FXML
    private void switchToInProcessTasks() {
        header.setText("In Process Tasks");
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
