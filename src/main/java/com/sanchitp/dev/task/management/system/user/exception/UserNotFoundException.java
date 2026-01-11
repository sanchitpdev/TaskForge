package com.sanchitp.dev.task.management.system.user.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User not found with I'd: "+ id);
    }
}
