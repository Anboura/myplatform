package com.myplatform.media.exception;

public class MediaAlreadyExistException extends Exception {

    public MediaAlreadyExistException(String message) {
        super(message);
    }

    public MediaAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
