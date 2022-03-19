package com.alexander.orshadiarybot.exception;

public class FetchDataException extends RuntimeException {
    public FetchDataException() {
        super();
    }

    public FetchDataException(String message) {
        super(message);
    }

    public FetchDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchDataException(Throwable cause) {
        super(cause);
    }
}
