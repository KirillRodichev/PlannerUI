package org.openjfx.mvc.controllers;

import javafx.util.Pair;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.openjfx.mvc.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserController {
    private UserList userList;
    public static final String XML_PATH = "userInfo.xml";
    private int selectedUserId;

    public UserController() {
        this.userList = new UserList();
        this.selectedUserId = -1;
    }

    public boolean isEmpty() {
        return this.userList.size() == 0;
    }

    public void actionDeleteUser(int userId) throws UserException {
        userList.remove(userId);
    }

    public void actionDeleteTask(int userId, int taskId) throws UserException, TaskException {
        User user = userList.getUserByID(userId);
        user.removeTaskById(taskId);
        userList.setUserById(userId, user);
    }

    public void selectUser(int Id) {
        this.selectedUserId = Id;
    }

    public User popUser() {
        return this.userList.getUsers().get(this.userList.size() - 1);
    }

    // ADD ACTIONS

    public int actionPushNewUser(String name) {
        return userList.push(new User(name));
    }

    public void actionPushUser(User user) {
        userList.push(user);
    }

    public void actionAddProject(int userId, String projectName, ITask...tasks) throws UserException {
        User user = userList.getUserByID(userId);
        user.addProject(projectName, tasks);
        userList.setUserById(userId, user);
    }

    public void actionAddProjectTask(int userId, int projectIndex, ITask task)
            throws ProjectException, UserException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.add(task);
        user.setProjectByIndex(projectIndex, project);
        userList.setUserById(userId, user);
    }

    public void actionAddProject(Project project) throws UserException {
        User user = userList.getUserByID(this.selectedUserId);
        user.addProject(project);
    }

    public void actionAddTask(int userId, ITask task) throws UserException {
        User user = userList.getUserByID(userId);
        user.addTask(task);
    }

    public void actionAddTasks(int userId, ITask ... tasks) throws UserException {
        User user = userList.getUserByID(userId);
        user.addTasks(tasks);
    }

    // SET ACTIONS

    public void actionSet(int userId, int index, ITask task)
            throws ProjectException, TaskException, UserException {
        User user = userList.getUserByID(userId);
        if (task instanceof Project) {
            user.setProjectByIndex(index, (Project) task);
        } else if (task instanceof Task) {
            user.setTaskByIndex(index, (Task) task);
        }
    }

    public void actionSetProjectTask(int userId, int projectIndex, int taskIndex, ITask task)
            throws ProjectException, TaskException, UserException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        userList.setUserById(userId, user);
    }

    private <T> void fieldSetter(String fieldName, T field, ITask task) {
        switch (fieldName) {
            case TaskFieldNames.NAME:
                task.setName((String) field);
                break;
            case TaskFieldNames.DESCRIPTION:
                task.setDescription((String) field);
                break;
            case TaskFieldNames.START_DATE:
                task.setStartDate((Date) field);
                break;
            case TaskFieldNames.FINISH_DATE:
                task.setFinishDate((Date) field);
                break;
            case TaskFieldNames.TASK_TYPE:
                task.setType((TaskType) field);
                break;
            case TaskFieldNames.TASK_STATE:
                task.setState((TaskState) field);
                break;
            case TaskFieldNames.TAG:
                task.setTag((String) field);
                break;
            default:
                break;
        }
    }

    public <T> void actionSetProjectTaskField(int userId, int projectIndex, int taskIndex, String fieldName, T field)
            throws ProjectException, TaskException, UserException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        Task task = (Task) project.getTaskByIndex(taskIndex);
        fieldSetter(fieldName, field, task);
        project.setTaskByIndex(taskIndex, task);
        user.setProjectByIndex(projectIndex, project);
        userList.setUserById(userId, user);
    }

    public <T> void actionSetProjectField(int userId, int projectIndex, String fieldName, T field)
            throws ProjectException, UserException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        fieldSetter(fieldName, field, project);
        user.setProjectByIndex(projectIndex, project);
        userList.setUserById(userId, user);
    }

    public <T> void actionSetTaskField(int userId, int taskId, String fieldName, T field) throws UserException {
        User user = userList.getUserByID(userId);
        Task task = (Task) user.getTaskById(taskId);
        fieldSetter(fieldName, field, task);
        user.setTaskById(taskId, task);
        userList.setUserById(userId, user);
    }

    private <T> Collection<ITask> collectionSwitcher(String fieldName, T field, Selectable obj) {
        switch (fieldName) {
            case TaskFieldNames.START_DATE:
                return obj.getTasksByStartDate((Date) field);
            case TaskFieldNames.FINISH_DATE:
                return obj.getTasksByFinishDate((Date) field);
            case TaskFieldNames.TASK_TYPE:
                return obj.getTasksByType((TaskType) field);
            case TaskFieldNames.TASK_STATE:
                return obj.getTasksByState((TaskState) field);
            case TaskFieldNames.TAG:
                return obj.getTasksByTag((String) field);
        }
        return null;
    }

    // GETTERS

    public int getSelectedUserId() {
        return this.selectedUserId;
    }

    public ArrayList<ITask> actionGetProjects() throws UserException {
        return this.userList.getUserByID(this.selectedUserId).getProjects();
    }

    public Pair<String, Integer>[] getUsersNamesAndIds() {
        Pair<String, Integer>[] namesAndIds = new Pair[this.userList.size()];
        Collection<User> users = this.userList.getUsers();
        int i = 0;
        for (User user : users) {
            namesAndIds[i] = new Pair<>(user.getName(), user.getId());
            i++;
        }
        return namesAndIds;
    }

    public User actionGetUser(int id) throws UserException {
        return userList.getUserByID(id);
    }

    public <T> Collection<ITask> actionGetProjectTasksByField(int userId, int projectIndex, String fieldName, T field)
            throws ProjectException, UserException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        return collectionSwitcher(fieldName, field, project);
    }

    public <T> Collection<ITask> actionGetUserTasksByField(int userId, String fieldName, T field)
            throws UserException {
        User user = userList.getUserByID(userId);
        return collectionSwitcher(fieldName, field, user);
    }

    public Collection<ITask> actionGetTasksOutOfProjects(int userId) throws UserException {
        User user = userList.getUserByID(userId);
        return user.getTasksOutOfProjects();
    }

    public Collection<ITask> actionGetTasksByTagSubstring(int userId, String sub) throws UserException {
        User user = userList.getUserByID(userId);
        return user.findBySubstringInTag(sub);
    }

    public Collection<ITask> actionGetTasks() throws UserException {
        User user = userList.getUserByID(this.selectedUserId);
        return user.getTasks();
    }

    // WRITE FORMAT ACTIONS

    public void actionWriteFormatTask(int userId, int taskIndex, PrintWriter out) throws UserException, TaskException {
        User user = userList.getUserByID(userId);
        Task task = (Task) user.getTaskByIndex(taskIndex);
        task.writeFormat(out);
    }

    public void actionWriteFormatProject(int userId, int projectIndex, PrintWriter out) throws UserException, ProjectException {
        User user = userList.getUserByID(userId);
        Project project = user.getProjectByIndex(projectIndex);
        project.writeFormat(out);
    }

    public void actionWriteFormatUser(int userId, PrintWriter out) throws UserException {
        userList.getUserByID(userId).writeFormat(out);
    }

    public void actionWriteXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("users");
        document.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(XML_PATH));

        Collection<User> users = userList.getUsers();
        for (User user : users) {
            user.writeXML(document, root, transformer, domSource, streamResult);
        }
    }
    
    public void actionReadXml() throws IOException, SAXException, ParserConfigurationException, ParseException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilderOut = documentBuilderFactory.newDocumentBuilder();
        Document documentOut = documentBuilderOut.parse(new File(XML_PATH));

        NodeList nodeList = documentOut.getElementsByTagName("user");
        userList.clear();
        User tmpUser;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                tmpUser = new User();
                tmpUser.readXML((Element) node);
                userList.push(tmpUser);
            }
        }
    }
}
