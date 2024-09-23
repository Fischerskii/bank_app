package ru.trofimov.app.exceptions;

public class UserAlreadyContainsException extends RuntimeException {
    public UserAlreadyContainsException(String message) {
        super(message);
    }
}
