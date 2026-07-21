package com.worktrack.exceptions;

class InvalidAttributeUserException extends RuntimeException {

    public InvalidAttributeUserException() {
    }

    public InvalidAttributeUserException(String message) {
        super(message);
    }

    public InvalidAttributeUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
