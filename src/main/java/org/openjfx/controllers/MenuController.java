package org.openjfx.controllers;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.TaskIndexNotFound;
import org.openjfx.exceptions.UserNotFoundException;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Task;
import org.openjfx.mvc.view.ModalWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MenuController {

    private UserController userController;
    private TaskController taskController;
    private int userId;
    @FXML
    private ScrollPane scrollView;

    public MenuController() {
        this.userController = new UserController();
        this.taskController = new TaskController();
        this.userId = userController.actionPushUser("Kirill");
        try {
            userController.actionAddTasks(
                    userId,
                    taskController.actionCreateTask(
                            "Kill the Bill",
                            "Be careful, don't fall into the river",
                            new Date(),
                            new Date(2020, Calendar.MARCH, 3),
                            TaskType.ANY_TIME,
                            TaskState.WAITING,
                            "home"
                    ),
                    taskController.actionCreateTask(
                            "Smack the button",
                            "This button belongs to this app",
                            new Date(),
                            new Date(2021, Calendar.APRIL, 20),
                            TaskType.LESS_IMPORTANT,
                            TaskState.IN_PROCESS,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Say hello to your daddy",
                            "Some description",
                            new Date(),
                            new Date(2109, Calendar.FEBRUARY, 21),
                            TaskType.LESS_IMPORTANT,
                            TaskState.IN_PROCESS,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Do to school",
                            "Some description",
                            new Date(),
                            new Date(2021, Calendar.APRIL, 20),
                            TaskType.TODAY,
                            TaskState.FINISHED,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Go to the Uni",
                            "Some description",
                            null,
                            null,
                            TaskType.GENERAL,
                            TaskState.PREPARATION,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Eat something for breakfast",
                            "Some description",
                            null,
                            null,
                            TaskType.TODAY,
                            TaskState.DELAYED,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Smack the button",
                            "This button belongs to this app",
                            new Date(),
                            new Date(2021, Calendar.APRIL, 20),
                            TaskType.LESS_IMPORTANT,
                            TaskState.IN_PROCESS,
                            null
                    ),
                    taskController.actionCreateTask(
                            "Deploy the App"
                    )
            );
        } catch (UserNotFoundException e) {
            System.out.println("EXCEPTION in MenuController constructor: " + e.getMessage());
        }
    }

    private ArrayList<ITask> filterTasksByState(ArrayList<ITask> tasks, TaskState state) {
        ArrayList<ITask> result = new ArrayList<>();
        for (ITask task : tasks) {
            if (task.getState() == state) {
                result.add(task);
            }
        }
        return result;
    }

    private ArrayList<ITask> filterTasksByType(ArrayList<ITask> tasks, TaskType type) {
        ArrayList<ITask> result = new ArrayList<>();
        for (ITask task : tasks) {
            if (task.getType() == type) {
                result.add(task);
            }
        }
        return result;
    }

    private void addHeader(String header, FlowPane container) {
        Label containerHeader = new Label(header);
        containerHeader.getStyleClass().add("taskHeader");
        container.getChildren().add(containerHeader);
    }

    private void addLinks(ArrayList<ITask> tasks, FlowPane container) {
        Hyperlink[] taskLinks = new Hyperlink[tasks.size()];
        VBox verticalContainer = new VBox();
        GridPane taskInfoContainer = new GridPane();
        for (int i = 0; i < taskLinks.length; i++) {
            taskLinks[i] = new Hyperlink((i + 1) + ". " + tasks.get(i).getName());
            taskLinks[i].getStyleClass().add("taskLink");
            int finalI = i;
            taskLinks[i].setOnMouseClicked((event) -> {
                        if (!taskInfoContainer.getChildren().isEmpty()) {
                            taskInfoContainer.getChildren().clear();
                            verticalContainer.getChildren().remove(taskInfoContainer);
                        }
                        Separator separator = new Separator(Orientation.HORIZONTAL);
                        separator.setStyle("-fx-pref-height: 40;");
                        taskInfoContainer.add(separator, 0, 0);
                        Label l = new Label("Name: ");
                        l.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(l, 0, 1);
                        Hyperlink h = new Hyperlink(tasks.get(finalI).getName());
                        h.getStyleClass().add("taskLink");
                        taskInfoContainer.add(h, 1, 1);
                        if (tasks.get(finalI).getDescription() != null) {
                            Label label = new Label("Description: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(label, 0, 2);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getDescription());
                            hyperlink.getStyleClass().add("taskLink");
                            hyperlink.setOnMouseClicked((event1 -> {
                                String str = (String) ModalWindow.newWindow("Set Description", "description");
                                hyperlink.setText(str);
                                int taskIndex = taskController.getTaskIndex((Task) tasks.get(finalI));
                                try {
                                    userController.actionSetTaskField(this.userId, taskIndex, "description", str);
                                } catch (UserNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }));
                            taskInfoContainer.add(hyperlink, 1, 2);
                        }
                        if (tasks.get(finalI).getStartDate() != null) {
                            Label label = new Label("Start date: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(label, 0, 3);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getStartDate().toString());
                            hyperlink.getStyleClass().add("taskLink");
                            taskInfoContainer.add(hyperlink, 1, 3);
                        }
                        if (tasks.get(finalI).getFinishDate() != null) {
                            Label label = new Label("Finish date: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(new Label("Finish date: "), 0, 4);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getFinishDate().toString());
                            hyperlink.getStyleClass().add("taskLink");
                            taskInfoContainer.add(hyperlink, 1, 4);
                        }
                        if (tasks.get(finalI).getType() != null) {
                            Label label = new Label("Type: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(new Label("Type: "), 0, 5);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getType().toString());
                            hyperlink.getStyleClass().add("taskLink");
                            taskInfoContainer.add(hyperlink, 1, 5);
                        }
                        if (tasks.get(finalI).getState() != null) {
                            Label label = new Label("State: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(new Label("State: "), 0, 6);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getState().toString());
                            hyperlink.getStyleClass().add("taskLink");
                            taskInfoContainer.add(hyperlink, 1, 6);
                        }
                        if (tasks.get(finalI).getTag() != null) {
                            Label label = new Label("Tag: ");
                            label.getStyleClass().add("taskLabel");
                            taskInfoContainer.add(new Label("Tag: "), 0, 7);
                            Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getTag());
                            hyperlink.getStyleClass().add("taskLink");
                            taskInfoContainer.add(hyperlink, 1, 7);
                        }
                        if (!taskInfoContainer.getChildren().isEmpty()) {
                            Button deleteTaskBtn = new Button("Delete");
                            taskInfoContainer.add(deleteTaskBtn, 0, 8);
                            deleteTaskBtn.setOnMouseClicked((event1 -> {

                            }));
                            verticalContainer.getChildren().add(taskInfoContainer);
                        }
                    }
            );
            verticalContainer.getChildren().add(taskLinks[i]);
        }
        container.getChildren().add(verticalContainer);
    }

    private void addEmptyTasksListMessage(FlowPane container) {
        Label emptyMessage = new Label("No tasks here, try to add some");
        VBox verticalContainer = new VBox();
        verticalContainer.getChildren().add(emptyMessage);
        container.getChildren().add(verticalContainer);
    }

    @FXML
    private void switchToInProcessTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Tasks in process", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.IN_PROCESS
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToPausedTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Paused tasks", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.PAUSED
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToFinishedTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Finished tasks", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.FINISHED
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToDelayedTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Delayed tasks", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.IN_PROCESS
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToPreparationTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Tasks in preparation", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.PREPARATION
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToWaitingTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Waiting tasks", container);
        ArrayList<ITask> tasks = filterTasksByState(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskState.WAITING
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToTodayTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Today tasks", container);
        ArrayList<ITask> tasks = filterTasksByType(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskType.TODAY
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToGeneralTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("General Tasks", container);
        ArrayList<ITask> tasks = filterTasksByType(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskType.GENERAL
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToLessImportantTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Less important Tasks", container);
        ArrayList<ITask> tasks = filterTasksByType(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskType.LESS_IMPORTANT
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToAnyTimeTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("Any time Tasks", container);
        ArrayList<ITask> tasks = filterTasksByType(
                (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId),
                TaskType.ANY_TIME
        );
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToTasks() throws UserNotFoundException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader("All Tasks", container);
        ArrayList<ITask> tasks = (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId);
        if (!tasks.isEmpty()) {
            addLinks(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void switchToProjects() {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        Label containerHeader = new Label("Projects");
        containerHeader.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-padding: 0,0,0,10");
        container.getChildren().add(containerHeader);
        scrollView.setContent(container);
    }
}
