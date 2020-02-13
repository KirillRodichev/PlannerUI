package org.openjfx.mvc.view;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.App;
import org.openjfx.constants.DatePattern;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.constants.UIConsts;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.UserException;
import org.openjfx.interfaces.ITask;
import org.openjfx.messages.UI.ErrorMsg;
import org.openjfx.messages.UI.WarningMsg;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Project;
import org.openjfx.mvc.models.Task;
import org.openjfx.mvc.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ModalWindow {
    private String selectedName, selectedDescription, selectedTag;
    private Date selectedStartDate, selectedFinishDate;
    private TaskType selectedType;
    private TaskState selectedState;

    private static final String MODAL_CONTAINER_STYLE = "modal__container";
    private static final String MODAL_HEADER_STYLE = "modal__header";
    private static final String MODAL_MANIPULATE_BTNS_STYLE = "modal__manipulateBtns";
    private static final String MODAL_BTNS_CONTAINER_STYLE = "modal__btnContainer";
    private static final String MODAL_TEXT_FIELD = "modal__textField";
    private static final String TASK_LABEL_STYLE = "taskLabelModal";

    private static final String TASK_TYPE_DROPDOWN = "Select type";
    private static final String TASK_STATE_DROPDOWN = "Select state";

    private String title;

    public ModalWindow(String title) {
        this.title = title;
        this.selectedName = null;
        this.selectedDescription = null;
        this.selectedStartDate = null;
        this.selectedFinishDate = null;
        this.selectedType = null;
        this.selectedState = null;
        this.selectedTag = null;
    }

    private Button addManipulateButtons(HBox btnContainer, Stage window) {
        Button confirmBtn = new Button(UIConsts.CONFIRM_BUTTON);
        Button closeBtn = new Button(UIConsts.CLOSE_BUTTON);
        Separator separator = new Separator(Orientation.VERTICAL);
        confirmBtn.getStyleClass().add(MODAL_MANIPULATE_BTNS_STYLE);
        closeBtn.getStyleClass().add(MODAL_MANIPULATE_BTNS_STYLE);
        btnContainer.getStyleClass().add(MODAL_BTNS_CONTAINER_STYLE);
        btnContainer.getChildren().addAll(confirmBtn, separator, closeBtn);
        closeBtn.setOnMouseClicked(event -> {
            window.close();
        });
        return confirmBtn;
    }

    private void addHeader(VBox container) {
        Label header = new Label(this.title);
        header.getStyleClass().add(MODAL_HEADER_STYLE);
        container.getChildren().add(header);
    }

    private void drawContent(VBox container, String field) {
        switch (field) {
            case TaskFieldNames.START_DATE:
            case TaskFieldNames.FINISH_DATE:
                container.getChildren().add(new Label(DatePattern.FORMAT));
            case TaskFieldNames.NAME:
            case TaskFieldNames.DESCRIPTION:
            case TaskFieldNames.TAG:
                TextField textField = new TextField();
                textField.getStyleClass().add(MODAL_TEXT_FIELD);
                container.getChildren().add(textField);
                break;
            case TaskFieldNames.TASK_TYPE:
                MenuItem[] typeItems = new MenuItem[TaskType.TASK_TYPES_STR.length];
                MenuButton typeDropdown = new MenuButton();
                for (int i = 0; i < TaskType.TASK_TYPES_STR.length; i++) {
                    typeItems[i] = new MenuItem(TaskType.TASK_TYPES_STR[i]);
                    int finalI = i;
                    MenuButton finalTypeDropdown = typeDropdown;
                    typeItems[i].setOnAction(actionEvent -> {
                        selectedType = TaskType.TASK_TYPES[finalI];
                        finalTypeDropdown.setText(TaskType.TASK_TYPES_STR[finalI]);
                    });
                }
                typeDropdown = new MenuButton(TASK_TYPE_DROPDOWN, null, typeItems);
                container.getChildren().add(typeDropdown);
                break;
            case TaskFieldNames.TASK_STATE:
                MenuItem[] stateItems = new MenuItem[TaskState.TASK_STATES_STR.length];
                MenuButton stateDropdown = new MenuButton();
                for (int i = 0; i < TaskState.TASK_STATES_STR.length; i++) {
                    stateItems[i] = new MenuItem(TaskState.TASK_STATES_STR[i]);
                    int finalI = i;
                    MenuButton finalStateDropdown = stateDropdown;
                    stateItems[i].setOnAction(actionEvent -> {
                        selectedState = TaskState.TASK_STATES[finalI];
                        finalStateDropdown.setText(TaskState.TASK_STATES_STR[finalI]);
                    });
                }
                stateDropdown = new MenuButton(TASK_STATE_DROPDOWN, null, stateItems);
                container.getChildren().add(stateDropdown);
                break;
        }
    }

    private TextField findTextFieldNode(VBox container) {
        TextField textField = null;
        for (Node node : container.getChildren()) {
            if (node instanceof TextField) {
                textField = (TextField) node;
            }
        }
        return textField;
    }

    private boolean taskAlreadyExists(ArrayList<ITask> tasks, String name) {
        for (ITask t : tasks) {
            if (t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void addConfirmAction(String field, Button confirmBtn, VBox container, UserController userController) {
        confirmBtn.setOnMouseClicked(mouseEvent -> {
            TextField textField = findTextFieldNode(container);
            String text = textField == null ? null : textField.getText();
            switch (field) {
                case TaskFieldNames.NAME:
                    if (stringValidation(text, true)) {
                        try {
                            if (!taskAlreadyExists((ArrayList<ITask>) userController.actionGetTasks(), text)) {
                                this.selectedName = text;
                            } else {
                                alertWindow(ErrorMsg.TASK_ALREADY_EXISTS);
                            }
                        } catch (UserException e) {
                            throw new RuntimeException();
                        }
                    } else {
                        alertWindow(TaskFieldNames.NAME + " " + ErrorMsg.INVALID_STR);
                    }
                    break;
                case TaskFieldNames.DESCRIPTION:
                    if (stringValidation(text, false)) {
                        this.selectedDescription = text;
                    } else {
                        alertWindow(TaskFieldNames.DESCRIPTION + " " + ErrorMsg.INVALID_STR);
                    }
                    break;
                case TaskFieldNames.START_DATE:
                    try {
                        this.selectedStartDate = startDateValidation(text);
                    } catch (RuntimeException e) {
                        alertWindow(TaskFieldNames.START_DATE + " " + e.getMessage());
                    }
                    break;
                case TaskFieldNames.FINISH_DATE:
                    try {
                        this.selectedFinishDate = finishDateValidation(text);
                    } catch (RuntimeException e) {
                        alertWindow(TaskFieldNames.START_DATE + " " + e.getMessage());
                    }
                    break;
                case TaskFieldNames.TASK_TYPE:
                case TaskFieldNames.TASK_STATE:
                    break;
                case TaskFieldNames.TAG:
                    if (stringValidation(text, true)) {
                        this.selectedTag = text;
                    } else {
                        alertWindow(TaskFieldNames.TAG + " " + ErrorMsg.INVALID_STR);
                    }
                    break;
            }
        });
    }

    private Object switchResult(String field) {
        switch (field) {
            case TaskFieldNames.START_DATE:
                return this.selectedStartDate;
            case TaskFieldNames.FINISH_DATE:
                return this.selectedFinishDate;
            case TaskFieldNames.NAME:
                return this.selectedName;
            case TaskFieldNames.DESCRIPTION:
                return this.selectedDescription;
            case TaskFieldNames.TAG:
                return this.selectedTag;
            case TaskFieldNames.TASK_TYPE:
                return this.selectedType;
            case TaskFieldNames.TASK_STATE:
                return this.selectedState;
            default:
                return null;
        }
    }

    public Object editTaskFieldWindow(String field, UserController userController) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        Scene scene = new Scene(container, 350, 200);
        scene.getStylesheets().add(App.class.getResource(UIConsts.STYLESHEET).toExternalForm());

        container.getStyleClass().add(MODAL_CONTAINER_STYLE);
        addHeader(container);

        HBox btnContainer = new HBox();
        Button confirmBtn = addManipulateButtons(btnContainer, window);

        drawContent(container, field);
        addConfirmAction(field, confirmBtn, container, userController);

        container.getChildren().add(btnContainer);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();

        return switchResult(field);
    }

    private void addDropdown(GridPane taskInfoContainer, int gridRow, Object enumSelector) {
        MenuItem[] items = enumSelector instanceof TaskType
                ? new MenuItem[TaskType.TASK_TYPES_STR.length]
                : new MenuItem[TaskState.TASK_STATES_STR.length];
        MenuButton menuButton = new MenuButton();
        for (int i = 0; i < items.length; i++) {
            items[i] = enumSelector instanceof TaskType
                    ? new MenuItem(TaskType.TASK_TYPES_STR[i])
                    : new MenuItem(TaskState.TASK_STATES_STR[i]);
            int finalI = i;
            MenuButton finalMenuButton = menuButton;
            items[i].setOnAction(actionEvent -> {
                if (enumSelector instanceof TaskType) {
                    this.selectedType = TaskType.TASK_TYPES[finalI];
                    finalMenuButton.setText(selectedType.toString());
                } else {
                    this.selectedState = TaskState.TASK_STATES[finalI];
                    finalMenuButton.setText(selectedState.toString());
                }
            });
        }
        menuButton = enumSelector instanceof TaskType
                ? new MenuButton(TASK_TYPE_DROPDOWN, null, items)
                : new MenuButton(TASK_STATE_DROPDOWN, null, items);
        taskInfoContainer.add(menuButton, 1, gridRow);
    }

    private void addField(GridPane taskInfoContainer, String fieldName, int gridRow) {
        switch (fieldName) {
            case TaskFieldNames.START_DATE:
            case TaskFieldNames.FINISH_DATE:
                TextField dateField = new TextField();
                dateField.setPromptText(DatePattern.FORMAT);
                dateField.getStyleClass().add(MODAL_TEXT_FIELD);
                taskInfoContainer.add(dateField, 1, gridRow);
                break;
            case TaskFieldNames.NAME:
            case TaskFieldNames.DESCRIPTION:
            case TaskFieldNames.TAG:
                TextField textField = new TextField();
                textField.getStyleClass().add(MODAL_TEXT_FIELD);
                taskInfoContainer.add(textField, 1, gridRow);
                break;
            case TaskFieldNames.TASK_TYPE:
                addDropdown(taskInfoContainer, gridRow, TaskType.TODAY);
                break;
            case TaskFieldNames.TASK_STATE:
                addDropdown(taskInfoContainer, gridRow, TaskState.WAITING);
                break;
        }
    }

    private void addInfoFields(GridPane taskInfoContainer) {
        for (int i = 0; i < TaskFieldNames.FIELDS.length; i++) {
            Label label = new Label(UIConsts.FIELDS[i]);
            label.getStyleClass().add(TASK_LABEL_STYLE);
            taskInfoContainer.add(label, 0, i + 1);
            addField(taskInfoContainer, TaskFieldNames.FIELDS[i], i + 1);
        }
    }

    private boolean stringValidation(String str, boolean mustBeFilled) {
        if (mustBeFilled) {
            return str.length() > 2;
        } else {
            return str.equals("") | str.length() > 2;
        }
    }

    private String datePartsValidation(String[] dateParts) {
        StringBuffer feedback = new StringBuffer();
        if (dateParts[0].equals("00")) {
            feedback.append(WarningMsg.NULL_DAY);
        }
        if (dateParts[1].equals("00")) {
            feedback.append(WarningMsg.NULL_MONTH);
        }
        if (Integer.parseInt(dateParts[0]) > 31) {
            feedback.append(WarningMsg.ILLEGAL_DAY_NUMBER);
        }
        if (Integer.parseInt(dateParts[1]) > 12) {
            feedback.append(WarningMsg.ILLEGAL_MONTH_NUMBER);
        }
        if (Integer.parseInt(dateParts[2]) > 2100) {
            feedback.append(WarningMsg.NOT_REALISTIC_YEAR);
        }
        return String.valueOf(feedback);
    }

    private Date startDateValidation(String arg) {
        if (arg.length() == 0) return null;
        Date date = null;
        StringBuffer errMsg = new StringBuffer();
        if (DatePattern.PATTERN.matcher(arg).matches()) {
            try {
                date = new SimpleDateFormat(DatePattern.FORMAT).parse(arg);
            } catch (ParseException e) {
                errMsg.append(ErrorMsg.INVALID_DATE);
            }
        } else if (errMsg.length() == 0) {
            errMsg.append(ErrorMsg.INVALID_DATE);
        }

        String[] dateParts = arg.split("-");

        errMsg.append(datePartsValidation(dateParts));

        if (date != null) {
            if (date.before(new Date())) {
                errMsg.append(WarningMsg.ILLEGAL_START_DATE);
            }
        }
        if (errMsg.length() != 0) {
            throw new RuntimeException(String.valueOf(errMsg));
        }
        return date;
    }

    private Date finishDateValidation(String arg) {
        if (arg.length() == 0) return null;
        Date date = null;
        StringBuffer errMsg = new StringBuffer();
        if (DatePattern.PATTERN.matcher(arg).matches()) {
            try {
                date = new SimpleDateFormat(DatePattern.FORMAT).parse(arg);
            } catch (ParseException e) {
                errMsg.append(ErrorMsg.INVALID_DATE);
            }
        } else if (errMsg.length() == 0) {
            errMsg.append(ErrorMsg.INVALID_DATE);
        }

        String[] dateParts = arg.split("-");

        errMsg.append(datePartsValidation(dateParts));

        if (date != null) {
            if (this.selectedStartDate != null) {
                if (date.before(this.selectedStartDate)) {
                    errMsg.append(WarningMsg.ILLEGAL_FINISH_DATE);
                }
            } else if (date.before(new Date())) {
                errMsg.append(WarningMsg.ILLEGAL_FINISH_DATE);
            }
        }
        if (errMsg.length() != 0) {
            throw new RuntimeException(String.valueOf(errMsg));
        }
        return date;
    }

    private void addConfirmAction(Button confirm, GridPane taskInfoContainer, UserController userController) {
        confirm.setOnAction(actionEvent -> {
            int i = 0;
            for (Node node : taskInfoContainer.getChildren()) {
                if (taskInfoContainer.getColumnIndex(node) == 1) {
                    if (node instanceof TextField) {
                        String text = ((TextField) node).getText();
                        switch (TaskFieldNames.FIELDS[i]) {
                            case TaskFieldNames.NAME:
                                if (stringValidation(text, true)) {
                                    try {
                                        if (!taskAlreadyExists((ArrayList<ITask>) userController.actionGetTasks(), text)) {
                                            selectedName = text;
                                        } else {
                                            alertWindow(ErrorMsg.TASK_ALREADY_EXISTS);
                                        }
                                    } catch (UserException e) {
                                        throw new RuntimeException();
                                    }
                                } else {
                                    alertWindow(TaskFieldNames.NAME + " " + ErrorMsg.INVALID_STR);
                                }
                                break;
                            case TaskFieldNames.DESCRIPTION:
                                if (stringValidation(text, false)) {
                                    selectedDescription = text;
                                } else {
                                    alertWindow(TaskFieldNames.DESCRIPTION + " " + ErrorMsg.INVALID_STR);
                                }
                                break;
                            case TaskFieldNames.START_DATE:
                                try {
                                    selectedStartDate = startDateValidation(text);
                                } catch (RuntimeException e) {
                                    alertWindow(TaskFieldNames.START_DATE + " " + e.getMessage());
                                }
                                break;
                            case TaskFieldNames.FINISH_DATE:
                                try {
                                    selectedFinishDate = finishDateValidation(text);
                                } catch (RuntimeException e) {
                                    alertWindow(TaskFieldNames.FINISH_DATE + " " + e.getMessage());
                                }
                                break;
                            case TaskFieldNames.TAG:
                                if (stringValidation(text, false)) {
                                    selectedTag = text;
                                } else {
                                    alertWindow(TaskFieldNames.TAG + " " + ErrorMsg.INVALID_STR);
                                }
                                break;
                        }
                    }
                    i++;
                }
            }
        });
    }

    public UserController createTaskWindow(UserController userController, Object task) throws UserException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        Scene scene = new Scene(container, 350, 300);
        scene.getStylesheets().add(App.class.getResource(UIConsts.STYLESHEET).toExternalForm());

        container.getStyleClass().add(MODAL_CONTAINER_STYLE);
        addHeader(container);

        HBox btnContainer = new HBox();
        Button confirmBtn = addManipulateButtons(btnContainer, window);

        GridPane taskInfoContainer = new GridPane();
        addInfoFields(taskInfoContainer);
        container.getChildren().add(taskInfoContainer);

        addConfirmAction(confirmBtn, taskInfoContainer, userController);

        container.getChildren().add(btnContainer);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();

        if (task instanceof Project) {
            userController.actionAddProject(new Project(
                    selectedName,
                    selectedDescription,
                    selectedStartDate,
                    selectedFinishDate,
                    selectedType,
                    selectedState,
                    selectedTag,
                    null
            ));
        } else {
            userController.actionAddTask(userController.getSelectedUserId(), new Task(
                    selectedName,
                    selectedDescription,
                    selectedStartDate,
                    selectedFinishDate,
                    selectedType,
                    selectedState,
                    selectedTag
            ));
        }
        return userController;
    }

    public static void alertWindow(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Pane container = new Pane();
        container.getStyleClass().add(MODAL_CONTAINER_STYLE);
        Label text = new Label(message);
        text.setStyle("-fx-padding: 30; -fx-background-color: #c6c6c6");
        container.getChildren().add(text);
        Scene scene = new Scene(container, 250, 100);
        window.setScene(scene);
        window.setTitle("Modal");
        window.showAndWait();
    }
}