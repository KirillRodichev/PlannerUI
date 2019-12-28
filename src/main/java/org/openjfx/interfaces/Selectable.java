package org.openjfx.interfaces;

import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;

import java.util.Collection;
import java.util.Date;

public interface Selectable {

    public Collection<ITask> getTasksByType(TaskType type);

    public Collection<ITask> getTasksByState(TaskState state);

    public Collection<ITask> getTasksByTag(String tag);

    public Collection<ITask> getTasksByStartDate(Date date);

    public Collection<ITask> getTasksByFinishDate(Date date);
}
