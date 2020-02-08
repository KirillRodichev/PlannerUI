package org.openjfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.openjfx.App;
import org.openjfx.FakeData;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.constants.UIConsts;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.TaskException;
import org.openjfx.exceptions.UserException;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Project;
import org.openjfx.mvc.models.Task;
import org.openjfx.mvc.view.ModalWindows;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class MenuController {

    private static final String TASK_LABEL_STYLE = "taskLabel";
    private static final String TASK_HEADER_STYLE = "taskHeader";
    private static final String TASK_LINK_STYLE = "taskLink";
    private static final String ADD_BTN_STYLE = "addBtn";
    private static final String DELETE_BTN_STYLE = "deleteBtn";
    private static final String PROJ_CONTAINER_STYLE = "deleteBtn";
    private static final String SEPARATOR_STYLE = "separator";
    private static final String LIST_LABEL_STYLE = "listLabel";

    private UserController userController;
    private TaskController taskController;

    @FXML
    private ScrollPane scrollView;
    @FXML
    private TextField searchField;
    @FXML
    private Text userName;

    public void setUserId(int id) throws SAXException, ParserConfigurationException, ParseException, IOException {
        this.userController.actionReadXml();
        this.userController.selectUser(id);
        try {
            this.userName.setText(this.userController.actionGetUser(this.userController.getSelectedUserId()).getName());
        } catch (UserException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String name) {
        this.userController.actionPushNewUser(name);
        this.userController.selectUser(this.userController.popUser().getId());
        try {
            this.userName.setText(this.userController.actionGetUser(this.userController.getSelectedUserId()).getName());
        } catch (UserException e) {
            e.printStackTrace();
        }
    }

    public MenuController() {
        userController = new UserController();
        taskController = new TaskController();
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

    private ArrayList<Project> filterProjectsByState(ArrayList<Project> projects, TaskState state) {
        ArrayList<Project> result = new ArrayList<>();
        for (Project project : projects) {
            if (project.getState() == state) {
                result.add(project);
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

    private ArrayList<Project> filterProjectsByType(ArrayList<Project> projects, TaskType type) {
        ArrayList<Project> result = new ArrayList<>();
        for (Project project : projects) {
            if (project.getType() == type) {
                result.add(project);
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
            Hyperlink hyperlink, String windowTitle, String windowField, Object task
    ) {
        hyperlink.setOnMouseClicked((event1 -> {
            Object res = ModalWindows.editTaskFieldWindow(windowTitle, windowField);
            hyperlink.setText(res.toString());
            hyperlink.getStyleClass().remove(ADD_BTN_STYLE);
            int taskIndex = task instanceof Project
                    ? taskController.getTaskIndex((Project) task) : taskController.getTaskIndex((Task) task);
            try {
                userController.actionSetTaskField(this.userController.getSelectedUserId(), taskIndex, windowField, res);
            } catch (UserException | TaskException e) {
                e.printStackTrace();
            }
        }));
    }

    private Pair<String, Hyperlink> createLinkAndTitle(
            Object task, String modalTitle
    ) {
        Hyperlink link;
        String title;
        String field;
        switch (modalTitle) {
            case TaskFieldNames.NAME:
                field = task instanceof Project ? ((Project) task).getName() : ((Task) task).getName();
                break;
            case TaskFieldNames.DESCRIPTION:
                field = task instanceof Project ? ((Project) task).getDescription() : ((Task) task).getDescription();
                break;
            case TaskFieldNames.START_DATE:
                Date startDate = task instanceof Project
                        ? ((Project) task).getStartDate() : ((Task) task).getStartDate();
                field = startDate != null ? startDate.toString() : null;
                break;
            case TaskFieldNames.FINISH_DATE:
                Date finishDate = task instanceof Project
                        ? ((Project) task).getFinishDate() : ((Task) task).getFinishDate();
                field = finishDate != null ? finishDate.toString() : null;
                break;
            case TaskFieldNames.TASK_STATE:
                field = task instanceof Project
                        ? ((Project) task).getState().toString() : ((Task) task).getState().toString();
                break;
            case TaskFieldNames.TASK_TYPE:
                field = task instanceof Project
                        ? ((Project) task).getType().toString() : ((Task) task).getType().toString();
                break;
            case TaskFieldNames.TAG:
                field = task instanceof Project ? ((Project) task).getTag() : ((Task) task).getTag();
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

    private void addInfoFields(GridPane taskInfoContainer, Object task) {
        for (int i = 0; i < TaskFieldNames.FIELDS.length; i++) {
            Label label = new Label(UIConsts.FIELDS[i]);
            label.getStyleClass().add(TASK_LABEL_STYLE);
            taskInfoContainer.add(label, 0, i + 1);
            Pair<String, Hyperlink> titleAndLink;
            titleAndLink = createLinkAndTitle(task, TaskFieldNames.FIELDS[i]);
            String title = titleAndLink.getKey();
            Hyperlink link = titleAndLink.getValue();
            addOnMouseClickedListener(link, title, TaskFieldNames.FIELDS[i], task);
            taskInfoContainer.add(link, 1, i + 1);
        }
    }

    private void clearInfoContainer(VBox verticalContainer, GridPane taskInfoContainer) {
        taskInfoContainer.getChildren().clear();
        verticalContainer.getChildren().remove(taskInfoContainer);
    }

    private void drawSeparator(GridPane taskInfoContainer) {
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.getStyleClass().add(SEPARATOR_STYLE);
        taskInfoContainer.add(separator, 0, 0);
    }

    /*
    если я указываю, что метод пробрасывает TaskEx и UserEx, то среда все равно предлагает обернуть вызов удаления 
    в try catch. Почему здесь нельзя указать, что метод пробрасывает исключения?
     */
    private void drawDeleteButton(GridPane taskInfoContainer, int taskId) {
        if (!taskInfoContainer.getChildren().isEmpty()) {
            Button deleteTaskBtn = new Button("Delete");
            deleteTaskBtn.getStyleClass().add(DELETE_BTN_STYLE);
            taskInfoContainer.add(deleteTaskBtn, 0, 8);
            deleteTaskBtn.setOnMouseClicked((event1 -> {
                try {
                    this.userController.actionDeleteTask(this.userController.getSelectedUserId(), taskId);
                } catch (UserException | TaskException e) {
                    System.out.println("Delete exception: " + e.getLocalizedMessage());
                }
            }));
        }
    }

    private void drawLabels(VBox verticalContainer, Object task) {
        Label label = new Label();
        label.getStyleClass().add(LIST_LABEL_STYLE);
        if (task instanceof Project) {
            label.setText("Projects");
        } else {
            label.setText("Tasks");
        }
        verticalContainer.getChildren().add(label);
    }

    private void drawTaskList(ArrayList<?> tasks, VBox verticalContainer, GridPane taskInfoContainer) {
        Hyperlink[] editTaskField = new Hyperlink[tasks.size()];
        drawLabels(verticalContainer, tasks.get(0));
        for (int i = 0; i < editTaskField.length; i++) {
            if (tasks.get(i) instanceof Task) {
                editTaskField[i] = new Hyperlink((i + 1) + ". " + ((Task) tasks.get(i)).getName());
            } else {
                editTaskField[i] = new Hyperlink((i + 1) + ". " + ((Project) tasks.get(i)).getName());
            }
            editTaskField[i].getStyleClass().add(TASK_LINK_STYLE);
            int finalI = i;
            editTaskField[i].setOnMouseClicked((event) -> {
                        clearInfoContainer(verticalContainer, taskInfoContainer);
                        drawSeparator(taskInfoContainer);
                        addInfoFields(taskInfoContainer, tasks.get(finalI));
                        if (tasks.get(finalI) instanceof Task) {
                            drawDeleteButton(taskInfoContainer, ((Task) tasks.get(finalI)).getId());
                        } else {
                            drawDeleteButton(taskInfoContainer, ((Project) tasks.get(finalI)).getId());
                        }
                        verticalContainer.getChildren().add(taskInfoContainer);
                    }
            );
            verticalContainer.getChildren().add(editTaskField[i]);
            if (tasks.get(finalI) instanceof Project) {
                try {
                    drawTaskList(this.userController.actionGetProjects().get(finalI).getTasks(), verticalContainer, taskInfoContainer);
                } catch (UserException e) {
                    e.printStackTrace();
                }
            }
        }
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
        VBox verticalContainer = new VBox();
        container.getChildren().add(verticalContainer);
        GridPane taskInfoContainer = new GridPane();

        ArrayList<ITask> tasks;
        ArrayList<Project> projects;
        if (filter instanceof TaskState) {
            tasks = filterTasksByState(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userController.getSelectedUserId()),
                    (TaskState) filter
            );
            projects = filterProjectsByState(userController.actionGetProjects(), (TaskState) filter);
        } else if (filter instanceof TaskType) {
            tasks = filterTasksByType(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userController.getSelectedUserId()),
                    (TaskType) filter
            );
            projects = filterProjectsByType(userController.actionGetProjects(), (TaskType) filter);
        } else {
            tasks = (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(this.userController.getSelectedUserId());
            projects = userController.actionGetProjects();
        }
        if (!tasks.isEmpty()) {
            drawTaskList(tasks, verticalContainer, taskInfoContainer);
        }
        if (!projects.isEmpty()) {
            drawTaskList(projects, verticalContainer, taskInfoContainer);
        }
        if (tasks.isEmpty() && projects.isEmpty()) {
            addEmptyTasksListMessage(container);
        }
        scrollView.setPannable(true);
        scrollView.setContent(container);
    }

    private void drawSearchResContainer(Collection<ITask> tasks) {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        VBox verticalContainer = new VBox();
        container.getChildren().add(verticalContainer);
        GridPane taskInfoContainer = new GridPane();
        addHeader("Search result", container);
        if (!tasks.isEmpty()) {
            drawTaskList((ArrayList<ITask>) tasks, verticalContainer, taskInfoContainer);
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
            List<ITask> res = new ArrayList<>(
                    this.userController.actionGetTasksByTagSubstring(this.userController.getSelectedUserId(), text)
            );
            drawSearchResContainer(res);
        } else {
            System.out.println("NOTHING ENTERED");
        }
    }

    public void logWithAnotherAccount(MouseEvent mouseEvent) throws IOException {
        this.userController.selectUser(-1);
        App.setRoot("login");
    }

    public void addTask(MouseEvent mouseEvent) throws UserException {
        this.userController = ModalWindows.createTaskWindow("title", this.userController, new Task());
    }
}