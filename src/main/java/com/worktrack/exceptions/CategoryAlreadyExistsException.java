package com.worktrack.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException() {
    }

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    public CategoryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
