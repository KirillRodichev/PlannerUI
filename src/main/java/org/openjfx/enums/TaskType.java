package org.openjfx.enums;

import org.openjfx.constants.Global;

public enum TaskType {

    TODAY ("Today"),
    GENERAL ("General"),
    ANY_TIME ("Any time"),
    LESS_IMPORTANT ("Less important");

    private final String strVal;

    TaskType(String strVal) {
        this.strVal = strVal;
    }

    public String getStrVal() {
        return this.strVal;
    }

    private static final String[] STR_VALUES = new String[] {
            "Today",
            "General",
            "Any time",
            "Less important"
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
