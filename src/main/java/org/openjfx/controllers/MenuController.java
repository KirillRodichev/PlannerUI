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
                            null,
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

    private void addOnMouseClickedListener(
            Hyperlink hyperlink, ArrayList<ITask> tasks, String windowTitle, String windowField, int index
    ) {
        hyperlink.setOnMouseClicked((event1 -> {
            Object res = ModalWindow.newWindow(windowTitle, windowField);
            hyperlink.setText(res.toString());
            hyperlink.getStyleClass().remove("addBtn");
            int taskIndex = taskController.getTaskIndex((Task) tasks.get(index));
            try {
                userController.actionSetTaskField(this.userId, taskIndex, windowField, res);
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }));
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

                        Label label = new Label("Name: ");
                        label.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label, 0, 1);
                        Hyperlink hyperlink = new Hyperlink(tasks.get(finalI).getName());
                        hyperlink.getStyleClass().add("taskLink");
                        addOnMouseClickedListener(
                                hyperlink,
                                tasks,
                                "Set name",
                                "name",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink, 1, 1);

                        Label label1 = new Label("Description: ");
                        label1.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label1, 0, 2);
                        Hyperlink hyperlink1;
                        String modalWindowTitle;
                        if (tasks.get(finalI).getDescription() != null) {
                            hyperlink1 = new Hyperlink(tasks.get(finalI).getDescription());
                            hyperlink1.getStyleClass().add("taskLink");
                            modalWindowTitle = "Set description";
                        } else {
                            hyperlink1 = new Hyperlink("add");
                            hyperlink1.getStyleClass().add("addBtn");
                            modalWindowTitle = "Add description";
                        }
                        addOnMouseClickedListener(
                                hyperlink1,
                                tasks,
                                modalWindowTitle,
                                "description",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink1, 1, 2);

                        Label label2 = new Label("Start date: ");
                        label2.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label2, 0, 3);
                        Hyperlink hyperlink2;
                        String modalWindowTitle1;
                        if (tasks.get(finalI).getStartDate() != null) {
                            modalWindowTitle1 = "Set start date";
                            hyperlink2 = new Hyperlink(tasks.get(finalI).getStartDate().toString());
                            hyperlink2.getStyleClass().add("taskLink");
                        } else {
                            modalWindowTitle1 = "Add start date";
                            hyperlink2 = new Hyperlink("add");
                            hyperlink2.getStyleClass().add("addBtn");
                        }
                        addOnMouseClickedListener(
                                hyperlink2,
                                tasks,
                                modalWindowTitle1,
                                "startDate",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink2, 1, 3);

                        Label label3 = new Label("Finish date: ");
                        label3.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label3, 0, 4);
                        Hyperlink hyperlink3;
                        String modalWindowTitle3;
                        if (tasks.get(finalI).getFinishDate() != null) {
                            modalWindowTitle3 = "Set finish date";
                            hyperlink3 = new Hyperlink(tasks.get(finalI).getFinishDate().toString());
                            hyperlink3.getStyleClass().add("taskLink");

                        } else {
                            modalWindowTitle3 = "Add finish date";
                            hyperlink3 = new Hyperlink("add");
                            hyperlink3.getStyleClass().add("btnAdd");
                        }
                        addOnMouseClickedListener(
                                hyperlink3,
                                tasks,
                                modalWindowTitle3,
                                "finishDate",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink3, 1, 4);


                        Label label4 = new Label("Type: ");
                        label4.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label4, 0, 5);
                        String modalWindowTitle4;
                        Hyperlink hyperlink4;
                        if (tasks.get(finalI).getType() != null) {
                            modalWindowTitle4 = "Set type";
                            hyperlink4 = new Hyperlink(tasks.get(finalI).getType().toString());
                            hyperlink4.getStyleClass().add("taskLink");

                        } else {
                            modalWindowTitle4 = "Add type";
                            hyperlink4 = new Hyperlink("add");
                            hyperlink4.getStyleClass().add("addBtn");
                        }
                        addOnMouseClickedListener(
                                hyperlink4,
                                tasks,
                                modalWindowTitle4,
                                "type",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink4, 1, 5);


                        Label label5 = new Label("State: ");
                        label5.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label5, 0, 6);
                        String modalWindowTitle5;
                        Hyperlink hyperlink5;
                        if (tasks.get(finalI).getState() != null) {
                            modalWindowTitle5 = "Set state";
                            hyperlink5 = new Hyperlink(tasks.get(finalI).getState().toString());
                            hyperlink5.getStyleClass().add("taskLink");

                        } else {
                            modalWindowTitle5 = "Add state";
                            hyperlink5 = new Hyperlink("add");
                            hyperlink5.getStyleClass().add("addBtn");
                        }
                        addOnMouseClickedListener(
                                hyperlink5,
                                tasks,
                                modalWindowTitle5,
                                "state",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink5, 1, 6);

                        Label label6 = new Label("Tag: ");
                        label6.getStyleClass().add("taskLabel");
                        taskInfoContainer.add(label6, 0, 7);
                        String modalWindowTitle6;
                        Hyperlink hyperlink6;
                        if (tasks.get(finalI).getTag() != null) {
                            modalWindowTitle6 = "Set tag";
                            hyperlink6 = new Hyperlink(tasks.get(finalI).getTag());
                            hyperlink6.getStyleClass().add("taskLink");
                        } else {
                            modalWindowTitle6 = "Add tag";
                            hyperlink6 = new Hyperlink("add");
                            hyperlink6.getStyleClass().add("addBtn");
                        }
                        addOnMouseClickedListener(
                                hyperlink6,
                                tasks,
                                modalWindowTitle6,
                                "tag",
                                finalI
                        );
                        taskInfoContainer.add(hyperlink6, 1, 7);

                        if (!taskInfoContainer.getChildren().isEmpty()) {
                            Button deleteTaskBtn = new Button("Delete");
                            deleteTaskBtn.getStyleClass().add("deleteBtn");
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
