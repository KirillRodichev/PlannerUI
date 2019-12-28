package org.openjfx.mvc.models;

import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;

import java.io.*;
import java.util.*;

public class User implements Serializable, Selectable {
    private int id;
    private String name;
    private static int numberOfUsers = 0;
    private ArrayList<ITask> tasks;
    private ArrayList<Project> projects;

    public User(String name) {
        User.numberOfUsers++;
        this.id = User.numberOfUsers - 1;
        this.name = name;
        this.tasks = new ArrayList<ITask>();
        this.projects = new ArrayList<Project>();
    }

    public void addProject(String name, Task... tasks) {
        Project project = new Project(name, tasks);
        this.projects.add(project);
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public void setProjectByIndex(int projectIndex, Map<Integer, ITask> indexTaskMap)
            throws ProjectIndexOutOfBoundsException {
        Project project = this.projects.get(projectIndex);
        for (int i = 0; i < project.size(); i++) {
            if (indexTaskMap.containsKey(i)) {
                project.setTaskByIndex(i, indexTaskMap.get(i));
            }
        }
        this.projects.set(projectIndex, project);
    }

    public void setProjectByIndex(int index, Project project) throws ProjectIndexOutOfBoundsException {
        try {
            this.projects.set(index, project);
        } catch (IndexOutOfBoundsException e) {
            throw new ProjectIndexOutOfBoundsException(e.getMessage());
        }
    }

    public Project getProjectByIndex(int index) throws ProjectIndexOutOfBoundsException {
        try {
            return this.projects.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new ProjectIndexOutOfBoundsException(e.getMessage());
        }
    }

    // SET

    public void setTaskByIndex(int index, Task task) throws TaskIndexOutOfBoundsException {
        try {
            this.tasks.set(index, task);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
    }

    public void setTaskNameByIndex(int index, String name) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setName(name);
        tasks.set(index, task);
    }

    public void setTaskDescriptionByIndex(int index, String description) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setDescription(description);
        tasks.set(index, task);
    }

    public void setTaskStartDateByIndex(int index, Date date) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setStartDate(date);
        tasks.set(index, task);
    }

    public void setTaskFinishDateByIndex(int index, Date date) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setFinishDate(date);
        tasks.set(index, task);
    }

    public void setTaskTypeByIndex(int index, TaskType taskType) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setType(taskType);
        tasks.set(index, task);
    }

    public void setTaskStateByIndex(int index, TaskState taskState) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setState(taskState);
        tasks.set(index, task);
    }

    public void setTaskTagByIndex(int index, String tag) throws TaskIndexOutOfBoundsException {
        ITask task;
        try {
            task = tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
        task.setTag(tag);
        tasks.set(index, task);
    }

    // END SET

    /*GET*/

    public ITask getTaskByIndex(int index) throws TaskIndexOutOfBoundsException {
        try {
            return tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskIndexOutOfBoundsException(e.getMessage());
        }
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

    public int getId() {
        return this.id;
    }

    public int getNumberOfTasks() {
        return this.tasks.size();
    }

    public Collection<ITask> getTasksOutOfProjects() {
        Collection<ITask> uniqueTasks = tasks;
        for (ITask task : this.tasks) {
            for (Project project : this.projects) {
                Collection<ITask> tasksCollection = project.getTasks();
                if (tasksCollection.contains(task)) {
                    uniqueTasks.remove(task);
                }
            }
        }
        return uniqueTasks;
    }

    public int getNumberOfProjects() {
        return this.projects.size();
    }

    // END GET

    public void addTask(ITask task) {
        this.tasks.add(task);
    }

    public void addTasks(ITask ... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public void removeTaskById(int id) throws TaskIndexOutOfBoundsException {
        int removeIndex = 0;
        boolean removed = false;
        for (; removeIndex < this.tasks.size(); removeIndex++)
            if (this.tasks.get(removeIndex).getId() == id) {
                this.tasks.remove(removeIndex);
                removed = true;
            }
        if (!removed) throw new TaskIndexOutOfBoundsException("Can't find the task with such id");
    }

    public void finishTaskById(int id) throws TaskIndexOutOfBoundsException {
        int finishIndex = 0;
        boolean finished = false;
        for (; finishIndex < this.tasks.size(); finishIndex++)
            if (this.tasks.get(finishIndex).getId() == id) {
                ITask task = this.tasks.get(finishIndex);
                task.setState(TaskState.FINISHED);
                tasks.set(finishIndex, task);
                finished = true;
            }
        if (!finished) throw new TaskIndexOutOfBoundsException("Can't find the task with such id");
    }

    public void writeFormat (PrintWriter out) {
        out.printf("\nuserId = %d\nuserName = %s", this.id, this.name);
        ArrayList<ITask> tasksNotInProjects = new ArrayList<>(tasks);
        for (Project iProject : this.projects) {
            for (ITask iTask : iProject.getTasks()) {
                tasksNotInProjects.remove(iTask);
            }
        }
        int i = 0;
        out.printf("\n\nSingle Tasks:");
        for (ITask iTask : tasksNotInProjects) {
            out.printf("\n\nTask #" + i++);
            iTask.writeFormat(out);
        }
        i = 0;
        out.printf("\n\nProjects:");
        for (Project iProject : this.projects) {
            out.printf("\n\nProject #" + i++);
            iProject.writeFormat(out);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String info = "( "
                + this.id + ", "
                + this.name + " ";
        buf.append(info);
        ArrayList<ITask> tasksNotInProjects = new ArrayList<>(tasks);
        for (Project iProject : this.projects)
            for (ITask iTask : iProject.getTasks())
                tasksNotInProjects.remove(iTask);
        buf.append("( ").append(tasksNotInProjects.size()).append(" ");
        for (ITask iTask : tasksNotInProjects)
            buf.append(iTask.toString());
        buf.append("), ( ").append(this.projects.size()).append(" ");
        for (Project iProject : this.projects)
            buf.append(iProject.toString());
        buf.append("))");
        return buf.toString();
    }
}
