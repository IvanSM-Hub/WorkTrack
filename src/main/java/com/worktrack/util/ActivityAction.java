package com.worktrack.util;

public enum ActivityAction {

    PROJECT_CREATED(""),
    PROJECT_UPDATED(""),
    PROJECT_ARCHIVED(""),
    TASK_CREATED(""),
    TASK_ASSIGNED(""),
    TASK_REASSIGNED(""),
    TASK_STATUS_CHANGED(""),
    COMMENT_ADDED("");

    private final String displayName;

    ActivityAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
