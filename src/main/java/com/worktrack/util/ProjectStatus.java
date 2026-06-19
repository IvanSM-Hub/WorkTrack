package com.worktrack.util;

public enum ProjectStatus {

    ACTIVE("active"),
    ARCHIVED("archived");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
