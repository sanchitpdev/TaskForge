package com.sanchitp.dev.task.management.system.common.enums.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(Long id){
        super("Task not found with id: "+ id);
    }
}
