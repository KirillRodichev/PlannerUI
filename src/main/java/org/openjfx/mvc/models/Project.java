package org.openjfx.mvc.models;

import org.openjfx.constants.TaskFieldNames;
import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

    public Project() {
        super();
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

    private void fillTaskFields(Element root, Document document) {
        String[] fieldValues = new String[] {
                this.getName(),
                this.getDescription(),
                String.valueOf(this.getStartDate()),
                String.valueOf(this.getFinishDate()),
                String.valueOf(this.getType()),
                String.valueOf(this.getState()),
                this.getTag()
        };
        for (int i = 0; i < TaskFieldNames.FIELDS.length; i++) {
            Element element = document.createElement(TaskFieldNames.FIELDS[i]);
            element.appendChild(document.createTextNode(fieldValues[i]));
            if (TaskFieldNames.FIELDS[i].equals(TaskFieldNames.TASK_TYPE)) {
                if (this.getType() == null) {
                    element.setAttribute("levelCode", "-1");
                } else {
                    element.setAttribute("levelCode", String.valueOf(this.getType().getLevelCode()));
                }
            } else if (TaskFieldNames.FIELDS[i].equals(TaskFieldNames.TASK_STATE)) {
                if (this.getState() == null) {
                    element.setAttribute("levelCode", "-1");
                } else {
                    element.setAttribute("levelCode", String.valueOf(this.getState().getLevelCode()));
                }
            }
            root.appendChild(element);
        }
    }

    public void writeXML(Document document, Element root, Transformer transformer, DOMSource domSource, StreamResult streamResult) throws TransformerException {

        Element project = document.createElement("project");
        root.appendChild(project);
        project.setAttribute("id", String.valueOf(this.getId()));

        fillTaskFields(project, document);

        Element tasks = document.createElement("tasks");
        project.appendChild(tasks);
        for (ITask task : this.tasks) {
            task.writeXML(document, tasks, transformer, domSource, streamResult);
        }

        transformer.transform(domSource, streamResult);
    }

    public void readXML(Element eElement) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        this.setId(Integer.parseInt(eElement.getAttribute("id")));
        this.setName(eElement.getElementsByTagName(TaskFieldNames.NAME).item(0).getTextContent());
        this.setDescription(eElement.getElementsByTagName(TaskFieldNames.DESCRIPTION).item(0).getTextContent());
        this.setStartDate(!eElement.getElementsByTagName(TaskFieldNames.START_DATE).item(0).getTextContent().equals("null")
                ? formatter.parse(eElement.getElementsByTagName(TaskFieldNames.START_DATE).item(0).getTextContent())
                : null);
        this.setFinishDate(!eElement.getElementsByTagName(TaskFieldNames.FINISH_DATE).item(0).getTextContent().equals("null")
                ? formatter.parse(eElement.getElementsByTagName(TaskFieldNames.FINISH_DATE).item(0).getTextContent())
                : null);
        TaskType type = Integer.parseInt(eElement.getElementsByTagName(TaskFieldNames.TASK_TYPE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue()) >= 0
                ? TaskType.TASK_TYPES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldNames.TASK_TYPE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]
                : null;
        this.setType(type);
        TaskState state = Integer.parseInt(eElement.getElementsByTagName(TaskFieldNames.TASK_STATE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue()) >= 0
                ? TaskState.TASK_STATES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldNames.TASK_STATE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())]
                : null;
        this.setState(state);
        this.setTag(eElement.getElementsByTagName(TaskFieldNames.TAG).item(0).getTextContent());

        NodeList taskList = eElement.getElementsByTagName("task");
        tasks = new ArrayList<>();
        for (int i = 0; i < taskList.getLength(); i++) {
            Node node = taskList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ITask tmpTask = new Task();
                tmpTask.readXML((Element) node);
                tasks.add(tmpTask);
            }
        }
    }
}