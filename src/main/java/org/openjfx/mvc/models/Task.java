package org.openjfx.mvc.models;

import org.openjfx.enums.*;
import org.openjfx.interfaces.*;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;

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

    public void finish() { this.taskState = TaskState.FINISHED; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setState(TaskState state) { this.taskState = state; }

    public void setFinishDate(Date finishDate) { this.finishDate = finishDate; }

    public void setType(TaskType taskType) { this.taskType = taskType; }

    public void setTag(String tag) { this.tag = tag; }

    public String getName() { return this.name; }

    public String getDescription() { return this.description; }

    public TaskState getState() { return this.taskState; }

    public Date getFinishDate() { return this.finishDate; }

    public Date getStartDate() { return this.startDate; }

    public String getTag() { return this.tag; }

    public TaskType getType() { return this.taskType; }

    public int getId() { return this.id; }

    public void writeFormat (PrintWriter out) {
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
}
