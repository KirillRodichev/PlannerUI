package org.openjfx.mvc.controllers;

import org.openjfx.mvc.models.Project;
import org.openjfx.mvc.models.Task;

public class TaskController {
    public Task actionCreateTask(String name) {
        return new Task(name);
    }

    public Project actionCreateProject(String name) {
        return new Project(name);
    }
}
