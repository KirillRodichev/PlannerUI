package org.openjfx.mvc.controllers;

import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.openjfx.mvc.models.*;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

public class UserController {
    UserList userListModel = new UserList();

    public void actionDeleteUser(int id) {
        this.userListModel.removeUser(id);
    }

    // ADD ACTIONS

    public void actionAddUser(String name) {
        this.userListModel.addUser(new User(name));
    }

    public void actionAddProject(int userId, String projectName, Task...tasks) throws UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        user.addProject(projectName, tasks);
        this.userListModel.setUserById(userId, user);
    }

    public void actionAddProjectTask(int userId, int projectIndex, Task task)
            throws ProjectIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.add(task);
        user.setProjectByIndex(projectIndex, project);
        this.userListModel.setUserById(userId, user);
    }

    // SET ACTIONS

    public void actionSet(int userId, int index, ITask task)
            throws ProjectIndexOutOfBoundsException, TaskIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        if (task instanceof Project) {
            user.setProjectByIndex(index, (Project) task);
        } else if (task instanceof Task) {
            user.setTaskByIndex(index, (Task) task);
        }
    }

    public void actionSetProjectTask(int userId, int projectIndex, int taskIndex, Task task)
            throws ProjectIndexOutOfBoundsException, TaskIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        this.userListModel.setUserById(userId, user);
    }

    private <T> void fieldSetter(String fieldName, T field, ITask task) {
        switch (fieldName) {
            case "name":
                task.setName((String) field);
                break;
            case "description":
                task.setDescription((String) field);
                break;
            case "startDate":
                task.setStartDate((Date) field);
                break;
            case "finishDate":
                task.setFinishDate((Date) field);
                break;
            case "taskType":
                task.setType((TaskType) field);
                break;
            case "taskState":
                task.setState((TaskState) field);
                break;
            case "tag":
                task.setTag((String) field);
                break;
            default:
                break;
        }
    }

    public <T> void actionSetProjectTaskField(int userId, int projectIndex, int taskIndex, String fieldName, T field)
            throws ProjectIndexOutOfBoundsException, TaskIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        Task task = (Task) project.getTaskByIndex(taskIndex);
        fieldSetter(fieldName, field, task);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        this.userListModel.setUserById(userId, user);
    }

    public <T> void actionSetProjectField(int userId, int projectIndex, String fieldName, T field)
            throws ProjectIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        fieldSetter(fieldName, field, project);
        user.setProjectByIndex(projectIndex, project);
        this.userListModel.setUserById(userId, user);
    }

    public <T> void actionSetTaskField(int userId, int taskIndex, String fieldName, T field)
            throws TaskIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Task task = (Task) user.getTaskByIndex(taskIndex);
        fieldSetter(fieldName, field, task);
        user.setTaskByIndex(taskIndex, task);
        this.userListModel.setUserById(userId, user);
    }

    private <T> Collection<ITask> collectionSwitcher(String fieldName, T field, Selectable obj) {
        switch (fieldName) {
            case "startDate":
                return obj.getTasksByStartDate((Date) field);
            case "finishDate":
                return obj.getTasksByFinishDate((Date) field);
            case "taskType":
                return obj.getTasksByType((TaskType) field);
            case "taskState":
                return obj.getTasksByState((TaskState) field);
            case "tag":
                return obj.getTasksByTag((String) field);
        }
        return null;
    }

    // GETTERS

    public User actionGetUser(int id) throws UserNotFoundException {
        return this.userListModel.getUserByID(id);
    }

    public <T> Collection<ITask> actionGetProjectTasksByField (int userId, int projectIndex, String fieldName, T field)
            throws ProjectIndexOutOfBoundsException, UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        return collectionSwitcher(fieldName, field, project);
    }

    public <T> Collection<ITask> actionGetUserTasksByField (int userId, String fieldName, T field)
            throws UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        return collectionSwitcher(fieldName, field, user);
    }

    public Collection<ITask> actionGetTasksOutOfProjects(int userId) throws UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        return user.getTasksOutOfProjects();
    }

    // WRITE FORMAT ACTIONS

    public void actionWriteFormatTask(int userId, int taskIndex, PrintWriter out) throws UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Task task = (Task) user.getTaskByIndex(taskIndex);
        task.writeFormat(out);
    }

    public void actionWriteFormatProject(int userId, int projectIndex, PrintWriter out) throws UserNotFoundException {
        User user = this.userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.writeFormat(out);
    }

    public void actionWriteFormatUser(int userId, PrintWriter out) throws UserNotFoundException {
        this.userListModel.getUserByID(userId).writeFormat(out);
    }
}
