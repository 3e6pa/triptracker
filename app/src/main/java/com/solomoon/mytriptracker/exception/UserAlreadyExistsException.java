package com.solomoon.mytriptracker.exception;

public class UserAlreadyExistsException extends RuntimeException  {
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
