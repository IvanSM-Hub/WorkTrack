package com.worktrack.exceptions;

public class CategoryDisabledException extends RuntimeException {

    public CategoryDisabledException() {
    }

    public CategoryDisabledException(String message) {
        super(message);
    }

    public CategoryDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

}
