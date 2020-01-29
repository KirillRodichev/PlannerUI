package org.openjfx.enums;

public enum TaskType {
    TODAY (0),
    GENERAL (1),
    ANY_TIME (2),
    LESS_IMPORTANT (3);

    private final int levelCode;

    TaskType(int levelCode) {
        this.levelCode = levelCode;
    }

    public int getLevelCode() {
        return levelCode;
    }

    public static TaskType byLvl(int lvl) {
        for (TaskType type : TaskType.values()) {
            if (type.levelCode == lvl) {
                return type;
            }
        }
        return null;
    }

    public static final TaskType[] TASK_TYPES = new TaskType[] {
            TODAY,
            GENERAL,
            ANY_TIME,
            LESS_IMPORTANT
    };
}
