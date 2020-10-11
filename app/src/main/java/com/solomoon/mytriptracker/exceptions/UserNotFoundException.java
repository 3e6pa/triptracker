package com.solomoon.mytriptracker.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
