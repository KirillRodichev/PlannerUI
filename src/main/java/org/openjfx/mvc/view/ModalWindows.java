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
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.constants.UIConsts;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.UserException;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Project;
import org.openjfx.mvc.models.Task;

import java.util.Date;

public class ModalWindows {
    private static String selectedName, selectedDescription, selectedStartDate, selectedFinishDate, selectedTag;
    private static TaskType selectedType;
    private static TaskState selectedState;

    private static final String MODAL_CONTAINER_STYLE = "modal__container";
    private static final String MODAL_HEADER_STYLE = "modal__header";
    private static final String MODAL_MANIPULATE_BTNS_STYLE = "modal__manipulateBtns";
    private static final String MODAL_BTNS_CONTAINER_STYLE = "modal__btnContainer";

    private static final String TASK_TYPE_DROPDOWN = "Select type";
    private static final String TASK_STATE_DROPDOWN = "Select state";

    private static Button addManipulateBtns(HBox btnContainer, Stage window) {
        Button confirmBtn = new Button("Confirm");
        Button closeBtn = new Button("Close");
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

    private static void addHeader(VBox container, String title) {
        Label header = new Label(title);
        header.getStyleClass().add(MODAL_HEADER_STYLE);
        container.getChildren().add(header);
    }

    public static Object editTaskFieldWindow(String title, String field) {
        final Object[] result = {null};
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        Scene scene = new Scene(container, 350, 200);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());

        container.getStyleClass().add(MODAL_CONTAINER_STYLE);
        addHeader(container, title);

        HBox btnContainer = new HBox();
        Button confirmBtn = addManipulateBtns(btnContainer, window);

        final Object[] resultBuffer = new Object[1];

        switch (field) {
            case TaskFieldNames.START_DATE:
            case TaskFieldNames.FINISH_DATE:
                container.getChildren().add(new Label("Format: YY:MM:DD"));
            case TaskFieldNames.NAME:
            case TaskFieldNames.DESCRIPTION:
            case TaskFieldNames.TAG:
                TextField textField = new TextField();
                textField.getStyleClass().add("modal__textField");
                container.getChildren().add(textField);
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
                        resultBuffer[0] = TaskType.TASK_TYPES[finalI];
                    });
                }
                MenuButton menuButton = new MenuButton("Select type", null, typeItems);
                container.getChildren().add(menuButton);
                confirmBtn.setOnMouseClicked(event -> {
                    result[0] = resultBuffer[0];
                });
                break;
            case TaskFieldNames.TASK_STATE:
                MenuItem[] stateItems = new MenuItem[TaskState.TASK_STATES_STR.length];
                for (int i = 0; i < TaskState.TASK_STATES_STR.length; i++) {
                    stateItems[i] = new MenuItem(TaskState.TASK_STATES_STR[i]);
                    int finalI = i;
                    stateItems[i].setOnAction(actionEvent -> {
                        resultBuffer[0] = TaskState.TASK_STATES_STR[finalI];
                    });
                }
                MenuButton mButton = new MenuButton("Select state", null, stateItems);
                container.getChildren().add(mButton);
                confirmBtn.setOnMouseClicked(event -> {
                    result[0] = resultBuffer[0];
                });
                break;
        }
        container.getChildren().add(btnContainer);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
        return result[0];
    }

    private static void addDropdown(GridPane taskInfoContainer, int gridRow, Object enumSelector) {
        MenuItem[] items = enumSelector instanceof TaskType
                ? new MenuItem[TaskType.TASK_TYPES_STR.length]
                : new MenuItem[TaskState.TASK_STATES_STR.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = enumSelector instanceof TaskType
                    ? new MenuItem(TaskType.TASK_TYPES_STR[i])
                    : new MenuItem(TaskState.TASK_STATES_STR[i]);
            int finalI = i;
            items[i].setOnAction(actionEvent -> {
                if (enumSelector instanceof TaskType) {
                    selectedType = TaskType.TASK_TYPES[finalI];
                } else {
                    selectedState = TaskState.TASK_STATES[finalI];
                }
            });
        }
        MenuButton menuButton = enumSelector instanceof TaskType
                ? new MenuButton(TASK_TYPE_DROPDOWN, null, items)
                : new MenuButton(TASK_STATE_DROPDOWN, null, items);
        taskInfoContainer.add(menuButton, 1, gridRow);
    }

    private static void addField(GridPane taskInfoContainer, String fieldName, int gridRow) {
        switch (fieldName) {
            case TaskFieldNames.START_DATE:
            case TaskFieldNames.FINISH_DATE:
                TextField dateField = new TextField();
                dateField.setPromptText("Format: YY:MM:DD");
                dateField.getStyleClass().add("modal__textField");
                taskInfoContainer.add(dateField, 1, gridRow);
                break;
            case TaskFieldNames.NAME:
            case TaskFieldNames.DESCRIPTION:
            case TaskFieldNames.TAG:
                TextField textField = new TextField();
                textField.getStyleClass().add("modal__textField");
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

    private static void addInfoFields(GridPane taskInfoContainer) {
        for (int i = 0; i < TaskFieldNames.FIELDS.length; i++) {
            Label label = new Label(UIConsts.FIELDS[i]);
            //label.getStyleClass().add(TASK_LABEL_STYLE);
            taskInfoContainer.add(label, 0, i + 1);
            addField(taskInfoContainer, TaskFieldNames.FIELDS[i], i + 1);
        }
    }

    private static Object addConfirmAction(Button confirm, GridPane taskInfoContainer, Object o) {
        confirm.setOnAction(actionEvent -> {
            int i = 0;
            for (Node node : taskInfoContainer.getChildren()) {
                if (taskInfoContainer.getColumnIndex(node) == 1) {
                    if (node instanceof TextField) {
                        switch (i) {
                            case 0:
                                selectedName = ((TextField) node).getText();
                                break;
                            case 1:
                                selectedDescription = ((TextField) node).getText();
                                break;
                            case 2:
                                selectedStartDate = ((TextField) node).getText();
                                break;
                            case 3:
                                selectedFinishDate = ((TextField) node).getText();
                                break;
                            case 6:
                                selectedTag = ((TextField) node).getText();
                                break;
                        }
                    }
                }
                i++;
            }

        });
        if (o instanceof Task) {
            return new Task(
                    selectedName,
                    selectedDescription,
                    new Date(selectedStartDate),
                    new Date(selectedFinishDate),
                    selectedType,
                    selectedState,
                    selectedTag
            );
        } else {
            return new Project(
                    selectedName,
                    selectedDescription,
                    new Date(selectedStartDate),
                    new Date(selectedFinishDate),
                    selectedType,
                    selectedState,
                    selectedTag,
                    null
            );
        }
    }

    public static void createTaskWindow(String title, UserController userController, Object task) throws UserException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox container = new VBox();
        Scene scene = new Scene(container, 450, 300);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());

        container.getStyleClass().add(MODAL_CONTAINER_STYLE);
        addHeader(container, title);

        HBox btnContainer = new HBox();
        Button confirmBtn = addManipulateBtns(btnContainer, window);

        GridPane taskInfoContainer = new GridPane();
        addInfoFields(taskInfoContainer);
        container.getChildren().add(taskInfoContainer);

        Object o = addConfirmAction(confirmBtn, taskInfoContainer, task);
        if (o instanceof Project) {
            userController.actionAddProject((Project) o);
        } else {
            userController.actionAddTask(userController.getSelectedUserId(), (Task) o);
        }

        container.getChildren().add(btnContainer);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
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