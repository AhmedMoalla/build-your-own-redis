package com.amoalla.redis.exception;

public class CommandParsingException extends RuntimeException {
    public CommandParsingException(String cause) {
        super(cause);
    }
}
