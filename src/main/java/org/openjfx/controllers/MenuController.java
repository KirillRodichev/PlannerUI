package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.openjfx.FakeData;
import org.openjfx.constants.TaskFieldConsts;
import org.openjfx.constants.UIConsts;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.TaskException;
import org.openjfx.exceptions.UserException;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Task;
import org.openjfx.mvc.view.ModalWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController {

    private static final String TASK_LABEL_STYLE = "taskLabel";
    private static final String TASK_HEADER_STYLE = "taskHeader";
    private static final String TASK_LINK_STYLE = "taskLink";
    private static final String ADD_BTN_STYLE = "addBtn";
    private static final String DELETE_BTN_STYLE = "deleteBtn";
    private static final String PROJ_CONTAINER_STYLE = "deleteBtn";

    private UserController userController;
    private TaskController taskController;
    private int userId;

    @FXML
    private ScrollPane scrollView;
    public Button searchBtn;
    public TextField searchField;

    public MenuController() {
        userController = new UserController();
        taskController = new TaskController();
        userId = userController.actionPushUser("Kirill");
        ITask[] tasks = FakeData.get();
        try {
            userController.actionAddTasks(userId, tasks);
        } catch (UserException e) {
            System.out.println(e.getMessage());
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
        containerHeader.getStyleClass().add(TASK_HEADER_STYLE);
        container.getChildren().add(containerHeader);
    }

    private void addOnMouseClickedListener(
            Hyperlink hyperlink, ArrayList<ITask> tasks, String windowTitle, String windowField, int index
    ) {
        hyperlink.setOnMouseClicked((event1 -> {
            Object res = ModalWindow.newWindow(windowTitle, windowField);
            hyperlink.setText(res.toString());
            hyperlink.getStyleClass().remove(ADD_BTN_STYLE);
            int taskIndex = taskController.getTaskIndex((Task) tasks.get(index));
            try {
                userController.actionSetTaskField(this.userId, taskIndex, windowField, res);
            } catch (UserException | TaskException e) {
                e.printStackTrace();
            }
        }));
    }

    private Pair<String, Hyperlink> setLinkAndTitle(
            ArrayList<ITask> tasks, int index, String modalTitle
    ) {
        Hyperlink link;
        String title;
        String field;
        switch (modalTitle) {
            case TaskFieldConsts.NAME:
                field = tasks.get(index).getName();
                break;
            case TaskFieldConsts.DESCRIPTION:
                field = tasks.get(index).getDescription();
                break;
            case TaskFieldConsts.START_DATE:
                field = tasks.get(index).getStartDate().toString();
                break;
            case TaskFieldConsts.FINISH_DATE:
                field = tasks.get(index).getFinishDate().toString();
                break;
            case TaskFieldConsts.TASK_STATE:
                field = tasks.get(index).getState().toString();
                break;
            case TaskFieldConsts.TASK_TYPE:
                field = tasks.get(index).getType().toString();
                break;
            case TaskFieldConsts.TAG:
                field = tasks.get(index).getTag();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + modalTitle);
        }
        if (field != null) {
            link = new Hyperlink(field);
            link.getStyleClass().add(TASK_LINK_STYLE);
            title = "Set".concat(modalTitle);
        } else {
            link = new Hyperlink("add");
            link.getStyleClass().add(ADD_BTN_STYLE);
            title = "Add".concat(modalTitle);
        }
        return new Pair<>(title, link);
    }

    private void drawTaskList(ArrayList<ITask> tasks, FlowPane container) {
        Hyperlink[] taskLinks = new Hyperlink[tasks.size()];
        VBox verticalContainer = new VBox();
        GridPane taskInfoContainer = new GridPane();
        for (int i = 0; i < taskLinks.length; i++) {
            taskLinks[i] = new Hyperlink((i + 1) + ". " + tasks.get(i).getName());
            taskLinks[i].getStyleClass().add(TASK_LINK_STYLE);
            int finalI = i;
            taskLinks[i].setOnMouseClicked((event) -> {
                        if (!taskInfoContainer.getChildren().isEmpty()) {
                            taskInfoContainer.getChildren().clear();
                            verticalContainer.getChildren().remove(taskInfoContainer);
                        }
                        Separator separator = new Separator(Orientation.HORIZONTAL);
                        separator.setStyle("-fx-pref-height: 40;");
                        taskInfoContainer.add(separator, 0, 0);

                        Label name = new Label(UIConsts.NAME_LABEL);
                        name.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(name, 0, 1);
                        Pair<String, Hyperlink> nameTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.NAME);
                        String nameTitle = nameTitleAndLink.getKey();
                        Hyperlink nameLink = nameTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                nameLink,
                                tasks,
                                nameTitle,
                                TaskFieldConsts.NAME,
                                finalI
                        );
                        taskInfoContainer.add(nameLink, 1, 1);

                        Label description = new Label(UIConsts.DESCRIPTION_LABEL);
                        description.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(description, 0, 2);
                        Pair<String, Hyperlink> descriptionTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.DESCRIPTION);
                        String descriptionTitle = descriptionTitleAndLink.getKey();
                        Hyperlink descriptionLink = descriptionTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                descriptionLink,
                                tasks,
                                descriptionTitle,
                                TaskFieldConsts.DESCRIPTION,
                                finalI
                        );
                        taskInfoContainer.add(descriptionLink, 1, 2);

                        Label startDate = new Label(UIConsts.START_DATE_LABEL);
                        startDate.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(startDate, 0, 3);
                        Pair<String, Hyperlink> startDateTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.START_DATE);
                        String startDateTitle = startDateTitleAndLink.getKey();
                        Hyperlink startDateLink = startDateTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                startDateLink,
                                tasks,
                                startDateTitle,
                                TaskFieldConsts.START_DATE,
                                finalI
                        );
                        taskInfoContainer.add(startDateLink, 1, 3);

                        Label finishDate = new Label(UIConsts.FINISH_DATE_LABEL);
                        finishDate.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(finishDate, 0, 4);
                        Pair<String, Hyperlink> finishDateTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.FINISH_DATE);
                        String finishDateTitle = finishDateTitleAndLink.getKey();
                        Hyperlink finishDateLink = finishDateTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                finishDateLink,
                                tasks,
                                finishDateTitle,
                                TaskFieldConsts.FINISH_DATE,
                                finalI
                        );
                        taskInfoContainer.add(finishDateLink, 1, 4);

                        Label taskType = new Label(UIConsts.TYPE_LABEL);
                        taskType.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(taskType, 0, 5);
                        Pair<String, Hyperlink> typeTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.TASK_TYPE);
                        String typeTitle = typeTitleAndLink.getKey();
                        Hyperlink typeLink = typeTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                typeLink,
                                tasks,
                                typeTitle,
                                TaskFieldConsts.TASK_TYPE,
                                finalI
                        );
                        taskInfoContainer.add(typeLink, 1, 5);


                        Label taskState = new Label(UIConsts.STATE_LABEL);
                        taskState.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(taskState, 0, 6);
                        Pair<String, Hyperlink> stateTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.TASK_STATE);
                        String stateTitle = stateTitleAndLink.getKey();
                        Hyperlink stateLink = stateTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                stateLink,
                                tasks,
                                stateTitle,
                                TaskFieldConsts.TASK_STATE,
                                finalI
                        );
                        taskInfoContainer.add(stateLink, 1, 6);

                        Label taskTag = new Label(UIConsts.TAG_LABEL);
                        taskTag.getStyleClass().add(TASK_LABEL_STYLE);
                        taskInfoContainer.add(taskTag, 0, 7);
                        Pair<String, Hyperlink> tagTitleAndLink = setLinkAndTitle(tasks, finalI, TaskFieldConsts.TAG);
                        String tagTitle = tagTitleAndLink.getKey();
                        Hyperlink tagLink = tagTitleAndLink.getValue();
                        addOnMouseClickedListener(
                                tagLink,
                                tasks,
                                tagTitle,
                                TaskFieldConsts.TAG,
                                finalI
                        );
                        taskInfoContainer.add(tagLink, 1, 7);

                        if (!taskInfoContainer.getChildren().isEmpty()) {
                            Button deleteTaskBtn = new Button("Delete");
                            deleteTaskBtn.getStyleClass().add(DELETE_BTN_STYLE);
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

    private <T> void drawContainer(String header, T filter) throws UserException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader(header, container);

        ArrayList<ITask> tasks;
        if (filter instanceof TaskState) {
            tasks = filterTasksByState(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userId),
                    (TaskState) filter
            );
        } else if (filter instanceof TaskType) {
            tasks = filterTasksByType(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userId),
                    (TaskType) filter
            );
        } else {
            tasks = (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userId);
        }
        if (!tasks.isEmpty()) {
            drawTaskList(tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    private void drawContainer(String header, Collection<ITask> tasks) {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        addHeader(header, container);
        if (!tasks.isEmpty()) {
            drawTaskList((ArrayList<ITask>) tasks, container);
        } else {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    @FXML
    private void drawInProcessTasks() throws UserException {
        drawContainer("Tasks in process", TaskState.IN_PROCESS);
    }

    @FXML
    private void drawPausedTasks() throws UserException {
        drawContainer("Paused tasks", TaskState.PAUSED);
    }

    @FXML
    private void drawFinishedTasks() throws UserException {
        drawContainer("Finished tasks", TaskState.FINISHED);
    }

    @FXML
    private void drawDelayedTasks() throws UserException {
        drawContainer("Delayed tasks", TaskState.DELAYED);
    }

    @FXML
    private void drawPreparationTasks() throws UserException {
        drawContainer("Tasks in preparation", TaskState.PREPARATION);
    }

    @FXML
    private void drawWaitingTasks() throws UserException {
        drawContainer("Waiting tasks", TaskState.WAITING);
    }

    @FXML
    private void drawTodayTasks() throws UserException {
        drawContainer("Today tasks", TaskType.TODAY);
    }

    @FXML
    private void drawGeneralTasks() throws UserException {
        drawContainer("General tasks", TaskType.GENERAL);
    }

    @FXML
    private void drawLessImportantTasks() throws UserException {
        drawContainer("Less important tasks", TaskType.LESS_IMPORTANT);
    }

    @FXML
    private void drawAnyTimeTasks() throws UserException {
        drawContainer("Any time tasks", TaskType.ANY_TIME);
    }

    @FXML
    private void drawAllTasks() throws UserException {
        drawContainer("All tasks", "filter");
    }

    @FXML
    private void switchToProjects() {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        Label containerHeader = new Label("Projects");
        containerHeader.getStyleClass().add(PROJ_CONTAINER_STYLE);
        container.getChildren().add(containerHeader);
        scrollView.setContent(container);
    }

    public void search(MouseEvent event) throws UserException {
        String text = searchField.getText();
        if ((text != null && !text.isEmpty())) {
            System.out.println("ENTERED TEXT: " + searchField.getText());
            Collection<ITask> res = new ArrayList<>(userController.actionGetTasksByTagSubstring(userId, text));
            drawContainer("Search result", res);
        } else {
            System.out.println("NOTHING ENTERED");
        }
    }
}