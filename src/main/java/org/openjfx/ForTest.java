package org.openjfx;

import org.openjfx.enums.*;
import org.openjfx.exceptions.*;
import org.openjfx.interfaces.*;
import org.openjfx.mvc.models.*;

import java.io.*;
import java.util.*;

public class ForTest {
    public static void main(String[] args)
            throws IOException, ProjectException, UserException {
        UserList model = new UserList();

        User user = new User("KupuJIJI");

        user.addTask(new Task(
                "Kill the Bill", "using knife",
                new Date(1990, 2, 20),
                new Date(1990, 2, 22),
                TaskType.ANY_TIME,
                TaskState.PREPARATION,
                "death"
        ));

        user.addTask(new Task(
                "7th lab", "don't forget to export smthg",
                new Date(),
                new Date(2019, 11, 23),
                TaskType.TODAY,
                TaskState.IN_PROCESS,
                "netCracker"
        ));

        ITask task = new Task(
                "walk with dog", null,
                null,
                null,
                TaskType.TODAY,
                TaskState.WAITING,
                "home"
        );

        ITask task1 = new Task(
                "wash the dishes", null,
                null,
                null,
                TaskType.TODAY,
                TaskState.WAITING,
                "home"
        );

        ITask task2 = new Task(
                "feed the cat", "don't forget water",
                null,
                null,
                TaskType.TODAY,
                TaskState.WAITING,
                "home"
        );

        user.addProject("Home tasks", task, task1, task2);
        ITask project = user.getProjectByIndex(0);
        project.setState(TaskState.IN_PROCESS);
        project.setType(TaskType.GENERAL);

        ITask task3 = new Task(
                "by new tires", "not costly",
                new Date(),
                null,
                TaskType.TODAY,
                TaskState.WAITING,
                "car"
        );

        ITask task4 = new Task(
                "wash the car", "",
                new Date(),
                null,
                TaskType.TODAY,
                TaskState.DELAYED,
                "car"
        );

        user.addProject("Car project", task3, task4);
        ITask project1 = user.getProjectByIndex(1);
        project1.setType(TaskType.LESS_IMPORTANT);
        project1.setDescription("wait u just dissed me, I'm perplexed");

        model.push(user);

        /*try (PrintWriter writer = new PrintWriter(new FileWriter("usersInfo.txt"))) {
            //user.writeFormat(writer);

            *//*user.getTaskByIndex(3).writeFormat(writer);
            user.setTaskDescriptionByIndex(3, "NEW DESCRIPTION");
            user.setTaskStartDateByIndex(3, new Date());
            user.setTaskFinishDateByIndex(3, new Date(2020, 2,2));
            user.setTaskStateByIndex(3, TaskState.FINISHED);
            user.setTaskTagByIndex(3, "NEW TAG");
            user.getTaskByIndex(3).writeFormat(writer);*//*

            Project project2 = user.getProjectByIndex(0);
            project2.writeFormat(writer);
            project2.removeTaskByIndex(1);
            project2.writeFormat(writer);
            project2.add(task3);
            ITask task5 = project2.getTaskByIndex(0);
            task5.setState(TaskState.FINISHED);
            project2.setTaskByIndex(0, task5);
            project2.setTaskDescriptionByIndex(0, "who runs the US?");

            user.removeTaskById(0);
        }*/

        User user1 = new User("Alla");
        user1.addTask(new Task(
            "go to the gym", "take the shaker",
            new Date(),
            null,
            TaskType.TODAY,
            TaskState.WAITING,
            "gym"
        ));
        ITask task5 = new Task(
            "buy some drugs", "near the supermarket",
            null,
            null,
            TaskType.TODAY,
            TaskState.WAITING,
            "health"
        );
        user1.addProject("Health care", task5);
        model.push(user1);

        /*try (PrintWriter writer = new PrintWriter(new FileWriter("usersInfo.txt"))) {
            model.writeFormat(writer);
        }*/

        ITask tempProject = new Project("tmp", (ArrayList<ITask>) user.getTasksByTag("home"));
        ITask tempProject1 = new Project("tmp1", (ArrayList<ITask>) user.getTasksByState(TaskState.PREPARATION));

        /*try (PrintWriter writer = new PrintWriter(new FileWriter("usersInfo.txt"))) {
            tempProject.writeFormat(writer);
            tempProject1.writeFormat(writer);
        }*/

        model.remove(1);

        model.push(new User("Lera"));
        User Lera = null;
        try {
            Lera = model.getUserByID(2);
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }
        Lera.addTask(new Task(
                "Lera's task", "desc",
                null, null,
                TaskType.LESS_IMPORTANT,
                TaskState.WAITING,
                "super tag"
        ));
        model.setUserById(2, Lera);

        /*try (PrintWriter writer = new PrintWriter(new FileWriter("usersInfo.txt"))) {
            model.writeFormat(writer);
        }*/

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("usersInfo.bin"))) {
            out.writeObject(model);
        }

        System.out.println(user.toString());
        /*
        *  SERIALIZATION
        *
        * Model m = new Model();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("usersInfo.bin"))) {
            m = (Model) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("usersInfo.txt"))) {
            m.writeFormat(writer);
        }*/

        /*user.getTaskByIndex(3).writeFormat(new PrintWriter(System.out));
        user.setTaskDescriptionByIndex(3, "NEW DESCRIPTION");
        user.getTaskByIndex(3).writeFormat(new PrintWriter(System.out));*/

        //model.writeFormat("usersInfo.txt");

    }
}