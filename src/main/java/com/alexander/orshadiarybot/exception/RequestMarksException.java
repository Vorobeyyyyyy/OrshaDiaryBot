package com.alexander.orshadiarybot.exception;

public class RequestMarksException extends RuntimeException {
    public RequestMarksException() {
        super();
    }

    public RequestMarksException(String message) {
        super(message);
    }

    public RequestMarksException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestMarksException(Throwable cause) {
        super(cause);
    }
}
