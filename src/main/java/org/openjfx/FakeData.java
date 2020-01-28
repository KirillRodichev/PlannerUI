package org.openjfx;

import org.openjfx.enums.TaskState;
import org.openjfx.enums.TaskType;
import org.openjfx.exceptions.UserException;
import org.openjfx.interfaces.ITask;
import org.openjfx.mvc.controllers.TaskController;
import org.openjfx.mvc.controllers.UserController;
import org.openjfx.mvc.models.Task;

import java.util.Calendar;
import java.util.Date;

public class FakeData {
    public static final ITask[] TASKS =  {
            new Task(
                    "Kill the Bill",
                    "Be careful, don't fall into the river",
                    new Date(),
                    new Date(2020, Calendar.MARCH, 3),
                    TaskType.ANY_TIME,
                    TaskState.WAITING,
                    "home"
            ),
            new Task(
                    "Smack the button",
                    null,
                    new Date(),
                    new Date(2021, Calendar.APRIL, 20),
                    TaskType.LESS_IMPORTANT,
                    TaskState.IN_PROCESS,
                    null
            ),
            new Task(
                    "Say hello to your daddy",
                    "Some description",
                    new Date(),
                    new Date(2109, Calendar.FEBRUARY, 21),
                    TaskType.LESS_IMPORTANT,
                    TaskState.IN_PROCESS,
                    null
            ),
            new Task(
                    "Go to school",
                    "Some description",
                    new Date(),
                    new Date(2021, Calendar.APRIL, 20),
                    TaskType.TODAY,
                    TaskState.FINISHED,
                    null
            ),
            new Task(
                    "Go to the Uni",
                    "Some description",
                    null,
                    null,
                    TaskType.GENERAL,
                    TaskState.PREPARATION,
                    null
            ),
            new Task(
                    "Eat something for breakfast",
                    "Some description",
                    null,
                    null,
                    TaskType.TODAY,
                    TaskState.DELAYED,
                    null
            ),
            new Task(
                    "Smack the button",
                    "This button belongs to this app",
                    new Date(),
                    new Date(2021, Calendar.APRIL, 20),
                    TaskType.LESS_IMPORTANT,
                    TaskState.IN_PROCESS,
                    null
            ),
            new Task(
                    "Deploy the App"
            )
    };
    public static ITask[] get() {
        return TASKS;
    }
}
