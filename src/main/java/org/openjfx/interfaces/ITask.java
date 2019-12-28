package org.openjfx.interfaces;

;

import org.openjfx.enums.*;

import java.io.PrintWriter;
import java.util.Date;

public interface ITask{
    public void finish();

    public void setName(String name);

    public void setDescription(String description);

    public void setStartDate(Date startDate);

    public void setFinishDate(Date finishDate);

    public void setType(TaskType taskType);

    public void setState(TaskState taskState);

    public void setTag(String tag);

    public String getName();

    public String getDescription();

    public TaskState getState();

    public Date getFinishDate();

    public Date getStartDate();

    public String getTag();

    public TaskType getType();

    public int getId();

    public void writeFormat(PrintWriter writer);

    public String toString();

    public Object clone() throws CloneNotSupportedException;
}
