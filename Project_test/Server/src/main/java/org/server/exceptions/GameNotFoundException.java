package org.server.exceptions;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(String message) {
        super(message);
    }
}