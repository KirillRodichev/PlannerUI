package org.openjfx.enums;

public enum TaskState {
    IN_PROCESS (0),
    PAUSED (1),
    FINISHED (2),
    DELAYED (3),
    PREPARATION (4),
    WAITING (5);

    private final int levelCode;

    TaskState(int levelCode) {
        this.levelCode = levelCode;
    }

    public int getLevelCode() {
        return levelCode;
    }

    public static final TaskState[] TASK_STATES = new TaskState[] {
            IN_PROCESS,
            PAUSED,
            FINISHED,
            DELAYED,
            PREPARATION,
            WAITING
    };
}
