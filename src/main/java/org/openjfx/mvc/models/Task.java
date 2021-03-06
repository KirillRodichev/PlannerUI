package org.openjfx.mvc.models;

import org.openjfx.constants.Global;
import org.openjfx.constants.TaskFieldNames;
import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.interfaces.ITask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements ITask, Cloneable, Serializable {
    private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date finishDate;
    private TaskType taskType;
    private TaskState taskState;
    private String tag;
    private static int numberOfTasks;

    public Task() {
        this.id = Task.numberOfTasks;
        Task.numberOfTasks++;
    }

    public Task(String name) {
        this.name = name;
        this.id = Task.numberOfTasks;
        Task.numberOfTasks++;
    }

    public Task(
            String name,
            String description,
            Date startDate,
            Date finishDate,
            TaskType taskType,
            TaskState taskState,
            String tag) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.taskType = taskType;
        this.taskState = taskState;
        this.tag = tag;
        Task.numberOfTasks++;
        this.id = Task.numberOfTasks - 1;
    }

    public void finish() {
        this.taskState = TaskState.FINISHED;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setState(TaskState state) {
        this.taskState = state;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public TaskState getState() {
        return this.taskState;
    }

    public Date getFinishDate() {
        return this.finishDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public String getTag() {
        return this.tag;
    }

    public TaskType getType() {
        return this.taskType;
    }

    public int getId() {
        return this.id;
    }

    public void writeFormat(PrintWriter out) {
        out.printf(
                "\ntaskId: %d" +
                        "\ntaskName = %s" +
                        "\ntaskDescription = %s" +
                        "\ntaskStartDate = %s" +
                        "\ntaskFinishDate = %s" +
                        "\ntaskType = %s" +
                        "\ntaskState = %s" +
                        "\ntaskTag = %s",
                this.id,
                this.name,
                this.description,
                this.startDate,
                this.finishDate,
                this.taskType,
                this.taskState,
                this.tag
        );
    }

    public String toString() {
        return "( "
                + this.id + ", "
                + this.name + ", "
                + this.description + ", "
                + this.startDate + ", "
                + this.finishDate + ", "
                + this.taskState + ", "
                + this.taskType + ", "
                + this.tag +
                ")";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Task) {
            Task aTask = (Task) o;
            return (aTask).getName().equals(this.name)
                    && (aTask).getDescription().equals(this.description)
                    && (aTask).getTag().equals(this.tag);
        }
        return false;
    }

    private void fillTaskFields(Element root, Document document) {
        String[] fieldValues = new String[]{
                this.name,
                this.description,
                String.valueOf(this.startDate),
                String.valueOf(this.finishDate),
                String.valueOf(this.taskType),
                String.valueOf(this.taskState),
                this.tag
        };
        for (int i = 0; i < TaskFieldNames.FIELDS.length; i++) {
            Element element = document.createElement(TaskFieldNames.FIELDS[i]);
            element.appendChild(document.createTextNode(fieldValues[i]));
            if (TaskFieldNames.FIELDS[i].equals(TaskFieldNames.TASK_TYPE)) {
                String attrVal = this.getType() == null
                        ? Global.NOT_SELECTED_STR
                        : this.getType().getStrVal();
                element.setAttribute(Global.ATTRIBUTE_NAME, attrVal);
            } else if (TaskFieldNames.FIELDS[i].equals(TaskFieldNames.TASK_STATE)) {
                String attrVal = this.getState() == null
                        ? Global.NOT_SELECTED_STR
                        : this.getState().getStrVal();
                element.setAttribute(Global.ATTRIBUTE_NAME, attrVal);
            }
            root.appendChild(element);
        }
    }

    public void writeXML(
            Document document, Element root, Transformer transformer, DOMSource domSource, StreamResult streamResult
    ) throws TransformerException {

        Element task = document.createElement(Global.TASK);
        root.appendChild(task);
        task.setAttribute(Global.ID, String.valueOf(this.id));

        fillTaskFields(task, document);

        transformer.transform(domSource, streamResult);
    }

    private String textContent(NodeList list) {
        return list.item(0).getTextContent();
    }

    private String getItemValue(NodeList list) {
        return list.item(0).getAttributes().getNamedItem(Global.ATTRIBUTE_NAME).getNodeValue();
    }

    public void readXML(Element eElement) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        id = Integer.parseInt(eElement.getAttribute(Global.ID));
        name = textContent(eElement.getElementsByTagName(TaskFieldNames.NAME));
        description = textContent(eElement.getElementsByTagName(TaskFieldNames.DESCRIPTION));
        startDate = !textContent(eElement.getElementsByTagName(TaskFieldNames.START_DATE)).equals(Global.NULL)
                ? formatter.parse(textContent(eElement.getElementsByTagName(TaskFieldNames.START_DATE)))
                : null;
        finishDate = !textContent(eElement.getElementsByTagName(TaskFieldNames.FINISH_DATE)).equals(Global.NULL)
                ? formatter.parse(textContent(eElement.getElementsByTagName(TaskFieldNames.FINISH_DATE)))
                : null;
        taskType = !getItemValue(eElement.getElementsByTagName(TaskFieldNames.TASK_TYPE)).equals(Global.NOT_SELECTED_STR)
                ? TaskType.values()[TaskType.getIndex(getItemValue(eElement.getElementsByTagName(TaskFieldNames.TASK_TYPE)))]
                : null;
        taskState = !getItemValue(eElement.getElementsByTagName(TaskFieldNames.TASK_STATE)).equals(Global.NOT_SELECTED_STR)
                ? TaskState.values()[TaskState.getIndex(getItemValue(eElement.getElementsByTagName(TaskFieldNames.TASK_STATE)))]
                : null;
        tag = textContent(eElement.getElementsByTagName(TaskFieldNames.TAG));
    }
}
