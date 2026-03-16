package com.sanchitp.dev.task.management.system.task.dto;

import com.sanchitp.dev.task.management.system.common.enums.TaskStatus;

public class TaskResponse {

    private final Long       id;
    private final String     title;
    private final String     description;
    private final TaskStatus taskStatus;
    private final Long       assignedUserId;

    public TaskResponse(Long id, String title, String description,
                        TaskStatus taskStatus, Long assignedUserId) {
        this.id             = id;
        this.title          = title;
        this.description    = description;
        this.taskStatus     = taskStatus;
        this.assignedUserId = assignedUserId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }
}
