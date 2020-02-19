package org.openjfx.mvc.view.jfxControllers;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.openjfx.App;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.constants.UIConsts;
import org.openjfx.enums.ModalType;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.TaskException;
import org.openjfx.exceptions.UserException;
import org.openjfx.interfaces.ITask;
import org.openjfx.constants.UIControllers;
import org.openjfx.messages.UI.NotificationMsg;
import org.openjfx.messages.UI.WarningMsg;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Project;
import org.openjfx.mvc.models.Task;
import org.openjfx.mvc.models.User;
import org.openjfx.mvc.view.ModalWindow;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuController {

    private static final String TASK_LABEL_STYLE = "taskLabel";
    private static final String TASK_HEADER_STYLE = "taskHeader";
    private static final String TASK_LINK_STYLE = "taskLink";
    private static final String ADD_BTN_STYLE = "addBtn";
    private static final String DELETE_BTN_STYLE = "deleteBtn";
    private static final String PROJECT_CONTAINER_STYLE = "deleteBtn";
    private static final String SEPARATOR_STYLE = "separator";
    private static final String LIST_LABEL_STYLE = "listLabel";

    private static final String TASK_LABEL = "Tasks";
    private static final String PROJECT_LABEL = "Projects";

    private static final String ADD_TASK_WINDOW_LABEL = "Add Task";
    private static final String SET_HEADER = "Set ";
    private static final String ADD_HEADER = "Add ";

    private static UserController userController = new UserController();
    private static TaskController taskController = new TaskController();

    @FXML
    private ScrollPane scrollView;
    @FXML
    private TextField searchField;
    @FXML
    private Text userName;

    public MenuController() {
        if (userController.isEmpty()) {
            try {
                userController.actionReadXml();
            } catch (IOException | SAXException | ParserConfigurationException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSelectedUserId() {
        return userController.getSelectedUserId();
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserId(int id) throws SAXException, ParserConfigurationException, ParseException, IOException {
        //userController.actionReadXml();
        userController.selectUser(id);
        try {
            System.out.println("ID in Menu: " + userController.getSelectedUserId());
            this.userName.setText(userController.actionGetUser(userController.getSelectedUserId()).getName());
        } catch (UserException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String name) throws SAXException, ParserConfigurationException, ParseException, IOException {
        //userController.actionReadXml();
        userController.actionPushNewUser(name);
        userController.selectUser(userController.popUser().getId());
        try {
            this.userName.setText(userController.actionGetUser(userController.getSelectedUserId()).getName());
        } catch (UserException e) {
            throw new RuntimeException(e);
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

    private void addHeader(String header, VBox container) {
        Label containerHeader = new Label(header);
        containerHeader.getStyleClass().add(TASK_HEADER_STYLE);
        container.getChildren().add(containerHeader);
    }

    private void addOnMouseClickedListener(
            Hyperlink hyperlink, String windowTitle, String windowField, ITask task
    ) {
        hyperlink.setOnMouseClicked((event1 -> {
            ModalWindow window = new ModalWindow(windowTitle);
            Object res = window.editTaskFieldWindow(windowField, userController);
            if (res != null) {
                hyperlink.setText(res.toString());
                hyperlink.getStyleClass().remove(ADD_BTN_STYLE);
                int taskIndex = taskController.getTaskIndex(task);
                try {
                    userController.actionSetTaskField(userController.getSelectedUserId(), taskIndex, windowField, res);
                } catch (UserException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }

    private Pair<String, Hyperlink> createLinkAndTitle(
            ITask task, String modalTitle
    ) {
        Hyperlink link;
        String title;
        String field;
        switch (modalTitle) {
            case TaskFieldNames.NAME:
                field = task.getName();
                break;
            case TaskFieldNames.DESCRIPTION:
                field = task.getDescription();
                break;
            case TaskFieldNames.START_DATE:
                field = task.getStartDate() != null ? task.getStartDate().toString() : null;
                break;
            case TaskFieldNames.FINISH_DATE:
                field = task.getFinishDate() != null ? task.getFinishDate().toString() : null;
                break;
            case TaskFieldNames.TASK_STATE:
                field = task.getState().toString();
                break;
            case TaskFieldNames.TASK_TYPE:
                field = task.getType().toString();
                break;
            case TaskFieldNames.TAG:
                field = task.getTag();
                break;
            default:
                throw new IllegalStateException(modalTitle);
        }
        if (field != null) {
            link = new Hyperlink(field);
            link.getStyleClass().add(TASK_LINK_STYLE);
            title = SET_HEADER.concat(modalTitle);
        } else {
            link = new Hyperlink(ADD_HEADER);
            link.getStyleClass().add(ADD_BTN_STYLE);
            title = ADD_HEADER.concat(modalTitle);
        }
        return new Pair<>(title, link);
    }

    private void addInfoFields(GridPane taskInfoContainer, ITask task) {
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


    private void drawDeleteButton(GridPane taskInfoContainer, int taskId, VBox verticalContainer, Hyperlink hyperlink) {
        if (!taskInfoContainer.getChildren().isEmpty()) {
            Button deleteTaskBtn = new Button(UIConsts.DELETE_BUTTON);
            deleteTaskBtn.getStyleClass().add(DELETE_BTN_STYLE);
            taskInfoContainer.add(deleteTaskBtn, 0, 8);
            deleteTaskBtn.setOnMouseClicked((event1 -> {
                try {
                    userController.actionDeleteTask(userController.getSelectedUserId(), taskId);
                    verticalContainer.getChildren().remove(hyperlink);
                    clearInfoContainer(verticalContainer, taskInfoContainer);
                } catch (UserException | TaskException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    private void drawLabels(VBox verticalContainer, ITask task) {
        Label label = new Label();
        label.getStyleClass().add(LIST_LABEL_STYLE);
        if (task instanceof Project) {
            label.setText(PROJECT_LABEL);
        } else {
            label.setText(TASK_LABEL);
        }
        verticalContainer.getChildren().add(label);
    }

    private void drawTaskList(ArrayList<ITask> tasks, VBox verticalContainer, GridPane taskInfoContainer) {
        Hyperlink[] editTaskField = new Hyperlink[tasks.size()];
        drawLabels(verticalContainer, tasks.get(0));
        for (int i = 0; i < editTaskField.length; i++) {
            ITask task = tasks.get(i);
            editTaskField[i] = createLink(task, i);
            editTaskField[i].getStyleClass().add(TASK_LINK_STYLE);
            int finalI = i;
            editTaskField[i].setOnMouseClicked((event) -> {
                        clearInfoContainer(verticalContainer, taskInfoContainer);
                        drawSeparator(taskInfoContainer);
                        addInfoFields(taskInfoContainer, tasks.get(finalI));
                        drawDeleteButton(taskInfoContainer, tasks.get(finalI).getId(), verticalContainer, editTaskField[finalI]);
                        verticalContainer.getChildren().add(taskInfoContainer);
                    }
            );
            verticalContainer.getChildren().add(editTaskField[i]);
            if (tasks.get(finalI) instanceof Project) {
                Project project = (Project) tasks.get(finalI);
                try {
                    for (ITask p : userController.actionGetProjects()) {
                        if (p.getName().equals(project.getName())) {
                            project = (Project) p;
                        }
                    }
                    drawTaskList(project.getTasks(), verticalContainer, taskInfoContainer);
                } catch (UserException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Hyperlink createLink(ITask task, int i) {
        return new Hyperlink((i + 1) + ". " + task.getName());
    }

    private void addEmptyTasksListMessage(FlowPane container) {
        Label emptyMessage = new Label(WarningMsg.EMPTY_TASK_LIST_MSG);
        VBox verticalContainer = new VBox();
        verticalContainer.getChildren().add(emptyMessage);
        container.getChildren().add(verticalContainer);
    }

    private <T> void drawContainer(String header, T filter) throws UserException {
        FlowPane container = new FlowPane(Orientation.VERTICAL, 10, 10);
        VBox verticalContainer = new VBox();
        container.getChildren().add(verticalContainer);
        addHeader(header, verticalContainer);
        GridPane taskInfoContainer = new GridPane();

        ArrayList<ITask> tasks;
        ArrayList<ITask> projects;
        if (filter instanceof TaskState) {
            tasks = filterTasksByState(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userController.getSelectedUserId()),
                    (TaskState) filter
            );
            projects = filterTasksByState(userController.actionGetProjects(), (TaskState) filter);
        } else if (filter instanceof TaskType) {
            tasks = filterTasksByType(
                    (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userController.getSelectedUserId()),
                    (TaskType) filter
            );
            projects = filterTasksByType(userController.actionGetProjects(), (TaskType) filter);
        } else {
            tasks = (ArrayList<ITask>) userController.actionGetTasksOutOfProjects(userController.getSelectedUserId());
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
        addHeader(UIConsts.HEADER_SEARCH, verticalContainer);
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
        drawContainer(UIConsts.HEADER_IN_PROCESS, TaskState.IN_PROCESS);
    }

    @FXML
    private void drawPausedTasks() throws UserException {
        drawContainer(UIConsts.HEADER_PAUSED, TaskState.PAUSED);
    }

    @FXML
    private void drawFinishedTasks() throws UserException {
        drawContainer(UIConsts.HEADER_FINISHED, TaskState.FINISHED);
    }

    @FXML
    private void drawDelayedTasks() throws UserException {
        drawContainer(UIConsts.HEADER_DELAYED, TaskState.DELAYED);
    }

    @FXML
    private void drawPreparationTasks() throws UserException {
        drawContainer(UIConsts.HEADER_IN_PREPARATION, TaskState.PREPARATION);
    }

    @FXML
    private void drawWaitingTasks() throws UserException {
        drawContainer(UIConsts.HEADER_WAITING, TaskState.WAITING);
    }

    @FXML
    private void drawTodayTasks() throws UserException {
        drawContainer(UIConsts.HEADER_TODAY, TaskType.TODAY);
    }

    @FXML
    private void drawGeneralTasks() throws UserException {
        drawContainer(UIConsts.HEADER_GENERAL, TaskType.GENERAL);
    }

    @FXML
    private void drawLessImportantTasks() throws UserException {
        drawContainer(UIConsts.HEADER_LESS_IMPORTANT, TaskType.LESS_IMPORTANT);
    }

    @FXML
    private void drawAnyTimeTasks() throws UserException {
        drawContainer(UIConsts.HEADER_ANY_TIME, TaskType.ANY_TIME);
    }

    @FXML
    private void drawAllTasks() throws UserException {
        drawContainer(UIConsts.HEADER_ALL_TASKS, "filter");
    }

    public void search(MouseEvent event) throws UserException {
        String text = searchField.getText();
        if ((text != null && !text.isEmpty())) {
            List<ITask> res = new ArrayList<>(
                    userController.actionGetTasksByTagSubstring(userController.getSelectedUserId(), text)
            );
            drawSearchResContainer(res);
        }
    }

    public void logWithAnotherAccount(MouseEvent mouseEvent) throws IOException {
        System.out.println("selected user MENU: "  + userController.getSelectedUserId());
        App.setRoot(UIControllers.LOGIN);
    }

    public void addTask(MouseEvent mouseEvent) throws UserException {
        ModalWindow window = new ModalWindow(ADD_TASK_WINDOW_LABEL);
        window.createTaskWindow(userController, new Task());
    }

    public void saveChanges(MouseEvent mouseEvent) throws TransformerException, ParserConfigurationException {
        userController.actionWriteXML();
        ModalWindow.alertWindow(NotificationMsg.SAVED_SUCCESSFULLY, ModalType.DEFAULT);
    }
}