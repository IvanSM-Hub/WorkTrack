package com.worktrack.util;

public enum TaskStatus {

    PENDING("pending"),
    WORKING("working"),
    FINISHED("finished");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
