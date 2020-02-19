package org.openjfx.enums;

public enum ModalType {
    ERROR("error"),
    WARNING("warning"),
    DEFAULT("default");

    private final String type;

    ModalType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
