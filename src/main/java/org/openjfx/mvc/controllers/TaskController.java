package org.openjfx.mvc.controllers;

import org.openjfx.enums.*;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.models.*;

import java.util.ArrayList;
import java.util.Date;

public class TaskController {
    public Task actionCreateTask(String name) {
        return new Task(name);
    }

    public Task actionCreateTask(
        String name,
        String description,
        Date startDate,
        Date finishDate,
        TaskType taskType,
        TaskState taskState,
        String tag
    ) {
        return new Task(name, description, startDate, finishDate, taskType, taskState, tag);
    }

    public Project actionCreateProject(String name) {
        return new Project(name);
    }

    public Project actionCreateProject(
            String name,
            String description,
            Date startDate,
            Date finishDate,
            TaskType taskType,
            TaskState taskState,
            String tag,
            ArrayList<ITask> tasks
    ) {
        return new Project(name, description, startDate, finishDate, taskType, taskState, tag, tasks);
    }

    public int getTaskIndex(ITask task) {
        return task.getId();
    }
}
