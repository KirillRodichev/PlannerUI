package org.openjfx.mvc.models;

import org.openjfx.constants.TaskFieldConsts;
import org.openjfx.enums.*;
import org.openjfx.interfaces.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.*;
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
    }

    public Task(String name) {
        this.name = name;
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
        boolean result = false;
        try {
            result = ((Task) o).getName().equals(this.name)
                    && ((Task) o).getDescription().equals(this.description)
                    && ((Task) o).getTag().equals(this.tag);
        } catch (NullPointerException e) {
            System.out.println("Some field was null");
            return false;
        }
        return result;
    }

    public void writeXML(
            Document document, Element root, Transformer transformer, DOMSource domSource, StreamResult streamResult
    ) throws TransformerException {

        Element task = document.createElement("task");
        root.appendChild(task);
        task.setAttribute("id", String.valueOf(this.id));

        Element name = document.createElement(TaskFieldConsts.NAME);
        name.appendChild(document.createTextNode(this.name));
        task.appendChild(name);
        Element description = document.createElement(TaskFieldConsts.DESCRIPTION);
        description.appendChild(document.createTextNode(this.description));
        task.appendChild(description);
        Element startDate = document.createElement(TaskFieldConsts.START_DATE);
        startDate.appendChild(document.createTextNode(String.valueOf(this.startDate)));
        task.appendChild(startDate);
        Element finishDate = document.createElement(TaskFieldConsts.FINISH_DATE);
        finishDate.appendChild(document.createTextNode(String.valueOf(this.finishDate)));
        task.appendChild(finishDate);
        Element taskType = document.createElement(TaskFieldConsts.TASK_TYPE);
        taskType.appendChild(document.createTextNode(String.valueOf(this.taskType)));
        taskType.setAttribute("levelCode", String.valueOf(this.taskType.getLevelCode()));
        task.appendChild(taskType);
        Element taskState = document.createElement(TaskFieldConsts.TASK_STATE);
        taskState.appendChild(document.createTextNode(String.valueOf(this.taskState)));
        taskState.setAttribute("levelCode", String.valueOf(this.taskState.getLevelCode()));
        task.appendChild(taskState);
        Element tag = document.createElement(TaskFieldConsts.TAG);
        tag.appendChild(document.createTextNode(String.valueOf(this.tag)));
        task.appendChild(tag);

        transformer.transform(domSource, streamResult);
    }

    public void readXML(Document document) throws ParseException {
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("task");
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            System.out.println("\nNode Name :" + node.getNodeName());
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                id = Integer.parseInt(eElement.getAttribute("id"));
                name = eElement.getElementsByTagName(TaskFieldConsts.NAME).item(0).getTextContent();
                description = eElement.getElementsByTagName(TaskFieldConsts.DESCRIPTION).item(0).getTextContent();
                startDate = !eElement.getElementsByTagName(TaskFieldConsts.START_DATE).item(0).getTextContent().equals("null")
                        ? formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.START_DATE).item(0).getTextContent())
                        : null;
                finishDate = !eElement.getElementsByTagName(TaskFieldConsts.FINISH_DATE).item(0).getTextContent().equals("null")
                        ? formatter.parse(eElement.getElementsByTagName(TaskFieldConsts.FINISH_DATE).item(0).getTextContent())
                        : null;
                taskType = TaskType.TASK_TYPES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_TYPE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())];
                taskState = TaskState.TASK_STATES[Integer.parseInt(eElement.getElementsByTagName(TaskFieldConsts.TASK_STATE).item(0).getAttributes().getNamedItem("levelCode").getNodeValue())];
                tag = eElement.getElementsByTagName(TaskFieldConsts.TAG).item(0).getTextContent();
            }
        }
    }

}
