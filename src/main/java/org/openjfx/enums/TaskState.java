package org.openjfx.enums;

import org.openjfx.constants.Global;

public enum TaskState {
    IN_PROCESS("In process"),
    PAUSED("Paused"),
    FINISHED("Finished"),
    DELAYED("Delayed"),
    PREPARATION("Preparation"),
    WAITING("Waiting");

    private final String strVal;

    TaskState(String strVal) {
        this.strVal = strVal;
    }

    public String getStrVal() {
        return this.strVal;
    }

    private static final String[] STR_VALUES = new String[]{
            "In process",
            "Paused",
            "Finished",
            "Delayed",
            "Preparation",
            "Waiting",
    };

    public static int getIndex(String strVal) {
        for (int i = 0; i < STR_VALUES.length; i++) {
            if (strVal.equals(STR_VALUES[i])) {
                return i;
            }
        }
        return Global.NOT_SELECTED;
    };
}
