package com.amazon.ata.music.playlist.service.exceptions;

public class InvalidAttributeException extends RuntimeException {
    private static final long serialVersionUID = -5502360041282016885L;

    public InvalidAttributeException() {
        super();
    }

    public InvalidAttributeException(String message) {
        super(message);
    }

    public InvalidAttributeException(Throwable cause) {
        super(cause);
    }

    public InvalidAttributeException(String message, Throwable cause) {
        super(message, cause);
    }
}
