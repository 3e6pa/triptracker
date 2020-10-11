package com.solomoon.mytriptracker.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message){
        super(message);
    }
}
