package org.openjfx.mvc.controllers;

import org.openjfx.constants.TaskFieldConsts;
import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.openjfx.mvc.models.*;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

public class UserController {
    private UserList userListModel;

    public UserController() {
        userListModel = new UserList();
    }

    public void actionDeleteUser(int id) throws UserException {
        userListModel.remove(id);
    }

    // ADD ACTIONS

    public int actionPushUser(String name) {
        return userListModel.push(new User(name));
    }

    public void actionAddProject(int userId, String projectName, ITask...tasks) throws UserException {
        User user = userListModel.getUserByID(userId);
        user.addProject(projectName, tasks);
        userListModel.setUserById(userId, user);
    }

    public void actionAddProjectTask(int userId, int projectIndex, ITask task)
            throws ProjectException, UserException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.add(task);
        user.setProjectByIndex(projectIndex, project);
        userListModel.setUserById(userId, user);
    }

    public void actionAddTask(int userId, ITask task) throws UserException {
        User user = userListModel.getUserByID(userId);
        user.addTask(task);
    }

    public void actionAddTasks(int userId, ITask ... tasks) throws UserException {
        User user = userListModel.getUserByID(userId);
        user.addTasks(tasks);
    }

    // SET ACTIONS

    public void actionSet(int userId, int index, ITask task)
            throws ProjectException, TaskException, UserException {
        User user = userListModel.getUserByID(userId);
        if (task instanceof Project) {
            user.setProjectByIndex(index, (Project) task);
        } else if (task instanceof Task) {
            user.setTaskByIndex(index, (Task) task);
        }
    }

    public void actionSetProjectTask(int userId, int projectIndex, int taskIndex, ITask task)
            throws ProjectException, TaskException, UserException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        userListModel.setUserById(userId, user);
    }

    private <T> void fieldSetter(String fieldName, T field, ITask task) {
        switch (fieldName) {
            case TaskFieldConsts.NAME:
                task.setName((String) field);
                break;
            case TaskFieldConsts.DESCRIPTION:
                task.setDescription((String) field);
                break;
            case TaskFieldConsts.START_DATE:
                task.setStartDate((Date) field);
                break;
            case TaskFieldConsts.FINISH_DATE:
                task.setFinishDate((Date) field);
                break;
            case TaskFieldConsts.TASK_TYPE:
                task.setType((TaskType) field);
                break;
            case TaskFieldConsts.TASK_STATE:
                task.setState((TaskState) field);
                break;
            case TaskFieldConsts.TAG:
                task.setTag((String) field);
                break;
            default:
                break;
        }
    }

    public <T> void actionSetProjectTaskField(int userId, int projectIndex, int taskIndex, String fieldName, T field)
            throws ProjectException, TaskException, UserException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        Task task = (Task) project.getTaskByIndex(taskIndex);
        fieldSetter(fieldName, field, task);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        userListModel.setUserById(userId, user);
    }

    public <T> void actionSetProjectField(int userId, int projectIndex, String fieldName, T field)
            throws ProjectException, UserException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        fieldSetter(fieldName, field, project);
        user.setProjectByIndex(projectIndex, project);
        userListModel.setUserById(userId, user);
    }

    public <T> void actionSetTaskField(int userId, int taskIndex, String fieldName, T field)
            throws TaskException, UserException {
        User user = userListModel.getUserByID(userId);
        Task task = (Task) user.getTaskByIndex(taskIndex);
        fieldSetter(fieldName, field, task);
        user.setTaskByIndex(taskIndex, task);
        userListModel.setUserById(userId, user);
    }

    private <T> Collection<ITask> collectionSwitcher(String fieldName, T field, Selectable obj) {
        switch (fieldName) {
            case TaskFieldConsts.START_DATE:
                return obj.getTasksByStartDate((Date) field);
            case TaskFieldConsts.FINISH_DATE:
                return obj.getTasksByFinishDate((Date) field);
            case TaskFieldConsts.TASK_TYPE:
                return obj.getTasksByType((TaskType) field);
            case TaskFieldConsts.TASK_STATE:
                return obj.getTasksByState((TaskState) field);
            case TaskFieldConsts.TAG:
                return obj.getTasksByTag((String) field);
        }
        return null;
    }

    // GETTERS

    public User actionGetUser(int id) throws UserException {
        return userListModel.getUserByID(id);
    }

    public <T> Collection<ITask> actionGetProjectTasksByField(int userId, int projectIndex, String fieldName, T field)
            throws ProjectException, UserException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        return collectionSwitcher(fieldName, field, project);
    }

    public <T> Collection<ITask> actionGetUserTasksByField(int userId, String fieldName, T field)
            throws UserException {
        User user = userListModel.getUserByID(userId);
        return collectionSwitcher(fieldName, field, user);
    }

    public Collection<ITask> actionGetTasksOutOfProjects(int userId) throws UserException {
        User user = userListModel.getUserByID(userId);
        return user.getTasksOutOfProjects();
    }

    public Collection<ITask> actionGetTasksByTagSubstring(int userId, String sub) throws UserException {
        User user = userListModel.getUserByID(userId);
        return user.findBySubstringInTag(sub);
    }

    // WRITE FORMAT ACTIONS

    public void actionWriteFormatTask(int userId, int taskIndex, PrintWriter out) throws UserException, TaskException {
        User user = userListModel.getUserByID(userId);
        Task task = (Task) user.getTaskByIndex(taskIndex);
        task.writeFormat(out);
    }

    public void actionWriteFormatProject(int userId, int projectIndex, PrintWriter out) throws UserException, ProjectException {
        User user = userListModel.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.writeFormat(out);
    }

    public void actionWriteFormatUser(int userId, PrintWriter out) throws UserException {
        userListModel.getUserByID(userId).writeFormat(out);
    }
}
