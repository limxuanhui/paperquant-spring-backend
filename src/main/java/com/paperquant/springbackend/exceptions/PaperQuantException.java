package com.paperquant.springbackend.exceptions;

public class PaperQuantException extends RuntimeException {
    public PaperQuantException(String message, Exception exception) {
        super(message, exception);
    }

    public PaperQuantException(String message) {
        super(message);
    }
}
