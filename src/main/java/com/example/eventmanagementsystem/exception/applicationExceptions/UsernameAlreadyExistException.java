package com.example.eventmanagementsystem.exception.applicationExceptions;

public class UsernameAlreadyExistException extends Exception{
    public UsernameAlreadyExistException() {
        super();
    }

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
