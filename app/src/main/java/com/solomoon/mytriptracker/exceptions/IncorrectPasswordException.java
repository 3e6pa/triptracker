package com.solomoon.mytriptracker.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message){
        super(message);
    }
}
