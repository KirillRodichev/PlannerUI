package org.openjfx.mvc.models;

import org.openjfx.constants.TaskFieldConsts;
import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Project extends Task implements Cloneable, ITask, Serializable, Selectable {
    private ArrayList<ITask> tasks;

    public Project(String name, ITask... tasks) {
        super(name);
        this.tasks = new ArrayList<>(Arrays.asList(tasks));
    }

    public Project(String name, ArrayList<ITask> tasks) {
        super(name);
        this.tasks = tasks;
    }

    public Project(
            String name,
            String description,
            Date startDate,
            Date finishDate,
            TaskType taskType,
            TaskState taskState,
            String tag,
            ArrayList<ITask> tasks) {
        super(name, description, startDate, finishDate, taskType, taskState, tag);
        this.tasks = tasks;
    }

    // SET

    public void setTaskNameByIndex(int index, String name) throws TaskException {
        ITask task = tasks.get(index);
        task.setName(name);
        tasks.set(index, task);
    }

    public void setTaskDescriptionByIndex(int index, String description) throws TaskException {
        ITask task = tasks.get(index);
        task.setDescription(description);
        tasks.set(index, task);
    }

    public void setTaskStartDateByIndex(int index, Date date) throws TaskException {
        ITask task = tasks.get(index);
        task.setStartDate(date);
        tasks.set(index, task);
    }

    public void setTaskFinishDateByIndex(int index, Date date) throws TaskException {
        ITask task = tasks.get(index);
        task.setFinishDate(date);
        tasks.set(index, task);
    }

    public void setTaskTypeByIndex(int index, TaskType taskType) throws TaskException {
        ITask task = tasks.get(index);
        task.setType(taskType);
        tasks.set(index, task);
    }

    public void setTaskStateByIndex(int index, TaskState taskState) throws TaskException {
        ITask task = tasks.get(index);
        task.setState(taskState);
        tasks.set(index, task);
    }

    public void setTaskTagByIndex(int index, String tag) throws TaskException {
        ITask task = tasks.get(index);
        task.setTag(tag);
        tasks.set(index, task);
    }

    public void setTaskByIndex(int index, ITask task) throws TaskException {
        tasks.set(index, task);
    }

    // END SET

    /*GET*/

    public ITask getTaskByIndex(int index) throws UserException {
        return tasks.get(index);
    }

    public Collection<ITask> getTasksByType(TaskType type) {
        Collection<ITask> tasks = new ArrayList<ITask>();
        for (ITask task : this.tasks)
            if (task.getType().equals(type))
                tasks.add(task);
        return tasks;
    }

    public Collection<ITask> getTasksByState(TaskState state) {
        Collection<ITask> tasks = new ArrayList<ITask>();
        for (ITask task : this.tasks)
            if (task.getState().equals(state))
                tasks.add(task);
        return tasks;
    }

    public Collection<ITask> getTasksByTag(String tag) {
        Collection<ITask> tasks = new ArrayList<ITask>();
        for (ITask task : this.tasks)
            if (task.getTag().equals(tag))
                tasks.add(task);
        return tasks;
    }

    public Collection<ITask> getTasksByStartDate(Date date) {
        Collection<ITask> tasks = new ArrayList<ITask>();
        for (ITask task : this.tasks)
            if (task.getStartDate().equals(date))
                tasks.add(task);
        return tasks;
    }

    public Collection<ITask> getTasksByFinishDate(Date date) {
        Collection<ITask> tasks = new ArrayList<ITask>();
        for (ITask task : this.tasks)
            if (task.getStartDate().equals(date))
                tasks.add(task);
        return tasks;
    }

    public int size() {
        return this.tasks.size();
    }

    public ArrayList<ITask> getTasks() {
        return this.tasks;
    }

    // END GET

    public void add(ITask task) {
        this.tasks.add(task);
    }

    public void removeTaskByIndex(int index) throws TaskException {
        this.tasks.remove(index);
    }

    public void finishTaskByIndex(int index) {
        ITask task = this.tasks.get(index);
        task.setState(TaskState.FINISHED);
        tasks.set(index, task);
    }

    public void writeFormat (PrintWriter out) {
        out.printf(
            "\nprojectId = %d" +
            "\nprojectName = %s" +
            "\nprojectDescription = %s" +
            "\nprojectStartDate = %s" +
            "\nprojectFinishDate = %s" +
            "\nprojectType = %s" +
            "\nprojectState = %s" +
            "\nprojectTag = %s",
            this.getId(),
            this.getName(),
            this.getDescription(),
            this.getStartDate(),
            this.getFinishDate(),
            this.getType(),
            this.getState(),
            this.getTag()
        );
        int i = 0;
        for (ITask task : tasks) {
            out.printf("\n\nTask #" + i++);
            task.writeFormat(out);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String info = "( "
                +"id: "+ this.getId() + ", "
                + this.getName() + ", "
                + this.getDescription() + ", "
                + this.getStartDate() + ", "
                + this.getFinishDate() + ", "
                + this.getState() + ", "
                + this.getType() + ", "
                + this.getTag() + " ";
        buf.append(info);
        buf.append("( ").append(this.size());
        for (ITask task : tasks)
            buf.append(", ").append(task.toString());
        buf.append("))");
        return buf.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        Project cloned = (Project) super.clone();
        cloned.setName(cloned.getName());
        cloned.setDescription(cloned.getDescription());
        cloned.setStartDate(cloned.getStartDate());
        cloned.setFinishDate(cloned.getFinishDate());
        cloned.setType(cloned.getType());
        cloned.setState(cloned.getState());
        cloned.setTag(cloned.getTag());
        for (int i = 0; i < cloned.size(); i++) {
            try {
                cloned.setTaskByIndex(i, (ITask) cloned.getTaskByIndex(i).clone());
            } catch (TaskException | UserException e) {
                e.printStackTrace();
            }
        }
        return cloned;
    }

    public void writeXML(Document document, Element root, Transformer transformer, DOMSource domSource, StreamResult streamResult) throws TransformerException {

        Element project = document.createElement("project");
        root.appendChild(project);
        project.setAttribute("id", String.valueOf(this.getId()));

        Element name = document.createElement(TaskFieldConsts.NAME);
        name.appendChild(document.createTextNode(this.getName()));
        project.appendChild(name);
        Element description = document.createElement(TaskFieldConsts.DESCRIPTION);
        description.appendChild(document.createTextNode(this.getDescription()));
        project.appendChild(description);
        Element startDate = document.createElement(TaskFieldConsts.START_DATE);
        startDate.appendChild(document.createTextNode(String.valueOf(this.getStartDate())));
        project.appendChild(startDate);
        Element finishDate = document.createElement(TaskFieldConsts.FINISH_DATE);
        finishDate.appendChild(document.createTextNode(String.valueOf(this.getFinishDate())));
        project.appendChild(finishDate);
        Element taskType = document.createElement(TaskFieldConsts.TASK_TYPE);
        taskType.appendChild(document.createTextNode(String.valueOf(this.getType())));
        taskType.setAttribute("levelCode", String.valueOf(this.getType().getLevelCode()));
        project.appendChild(taskType);
        Element taskState = document.createElement(TaskFieldConsts.TASK_STATE);
        taskState.appendChild(document.createTextNode(String.valueOf(this.getState())));
        taskState.setAttribute("levelCode", String.valueOf(this.getState().getLevelCode()));
        project.appendChild(taskState);
        Element tag = document.createElement(TaskFieldConsts.TAG);
        tag.appendChild(document.createTextNode(String.valueOf(this.getTag())));
        project.appendChild(tag);

        Element tasks = document.createElement("tasks");
        project.appendChild(tasks);
        for (ITask task : this.tasks) {
            task.writeXML(document, tasks, transformer, domSource, streamResult);
        }

        transformer.transform(domSource, streamResult);
    }

    public void readXML(Document document) throws ParseException {
        document.getDocumentElement().normalize();

        NodeList projectNodeList = document.getElementsByTagName("project");
        NodeList tasksNodeList = document.getElementsByTagName("tasks");
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        for (int i = 0; i < projectNodeList.getLength(); i++) {
            Node projectNode = projectNodeList.item(i);
            System.out.println("\nNode Name :" + projectNode.getNodeName());
            if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) projectNode;
                this.setId(Integer.parseInt(eElement.getAttribute("id")));
                this.setName(eElement.getElementsByTagName(TaskFieldConsts.NAME).item(0).getTextContent());
                this.setDescription(eElement.getElementsByTagName(TaskFieldConsts.DESCRIPTION).item(0).getTextContent());
                this.setStartDate(formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.START_DATE).item(0).getTextContent()));
                this.setFinishDate(formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.FINISH_DATE).item(0).getTextContent()));
                this.setType(TaskType.TASK_TYPES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_TYPE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]);
                this.setState(TaskState.TASK_STATES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_STATE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]);
                this.setTag(eElement.getElementsByTagName(TaskFieldConsts.TAG).item(0).getTextContent());

                Node taskNode = tasksNodeList.item(i);
                for (int j = 0; j < tasksNodeList.getLength(); j++) {
                    if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element tElement = (Element) taskNode;
                        ITask t = new Task();
                        t.setId(Integer.parseInt(eElement.getAttribute("id")));
                        t.setName(eElement.getElementsByTagName(TaskFieldConsts.NAME).item(0).getTextContent());
                        t.setDescription(eElement.getElementsByTagName(TaskFieldConsts.DESCRIPTION).item(0).getTextContent());
                        t.setStartDate(formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.START_DATE).item(0).getTextContent()));
                        t.setFinishDate(formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.FINISH_DATE).item(0).getTextContent()));
                        t.setType(TaskType.TASK_TYPES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_TYPE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]);
                        t.setState(TaskState.TASK_STATES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_STATE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]);
                        t.setTag(eElement.getElementsByTagName(TaskFieldConsts.TAG).item(0).getTextContent());
                    }
                }
            }
        }
    }
}