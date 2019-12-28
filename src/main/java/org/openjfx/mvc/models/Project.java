package org.openjfx.mvc.models;

import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class Project extends Task implements Cloneable, ITask, Serializable, Selectable {
    private ArrayList<ITask> tasks;

    public Project(String name, Task... tasks) {
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

    public void setTaskNameByIndex(int index, String name) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setName(name);
        tasks.set(index, task);
    }

    public void setTaskDescriptionByIndex(int index, String description) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setDescription(description);
        tasks.set(index, task);
    }

    public void setTaskStartDateByIndex(int index, Date date) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setStartDate(date);
        tasks.set(index, task);
    }

    public void setTaskFinishDateByIndex(int index, Date date) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setFinishDate(date);
        tasks.set(index, task);
    }

    public void setTaskTypeByIndex(int index, TaskType taskType) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setType(taskType);
        tasks.set(index, task);
    }

    public void setTaskStateByIndex(int index, TaskState taskState) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setState(taskState);
        tasks.set(index, task);
    }

    public void setTaskTagByIndex(int index, String tag) throws TaskIndexOutOfBoundsException {
        ITask task = tasks.get(index);
        task.setTag(tag);
        tasks.set(index, task);
    }

    public void setTaskByIndex(int index, ITask task) throws TaskIndexOutOfBoundsException {
        tasks.set(index, task);
    }

    // END SET

    /*GET*/

    public ITask getTaskByIndex(int index) throws UserIndexOutOfBoundsException {
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

    public void removeTaskByIndex(int index) throws TaskIndexOutOfBoundsException {
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
            cloned.setTaskByIndex(i, (ITask) cloned.getTaskByIndex(i).clone());
        }
        return cloned;
    }
}